package org.moskito.control.plugins.pagespeed;

import net.anotheria.moskito.aop.annotation.DontMonitor;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.accumulation.AccumulatorDefinition;
import net.anotheria.moskito.core.accumulation.AccumulatorRepository;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistry;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.threshold.Threshold;
import net.anotheria.moskito.core.threshold.ThresholdDefinition;
import net.anotheria.moskito.core.threshold.ThresholdRepository;
import net.anotheria.moskito.core.threshold.ThresholdStatus;
import net.anotheria.moskito.core.threshold.guard.DoubleBarrierPassGuard;
import net.anotheria.moskito.core.threshold.guard.GuardedDirection;
import net.anotheria.util.NumberUtils;
import org.moskito.control.config.datarepository.VariableMapping;
import org.moskito.control.core.Repository;
import org.moskito.control.data.DataRepository;
import org.moskito.control.data.retrievers.DataRetriever;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;
import org.moskito.control.plugins.pagespeed.stats.PagespeedStats;
import org.moskito.control.plugins.pagespeed.stats.PagespeedStatsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.08.20 17:08
 */
public class PagespeedPlugin extends AbstractMoskitoControlPlugin implements DataRetriever {

	private static Logger log = LoggerFactory.getLogger(PagespeedPlugin.class);

	/**
	 * Name of the configuration.
	 */
	private String configurationName;

	private ScheduledExecutorService scheduler;

	private ExecutorService taskExecutorService;
	/**
	 * My config. TODO later add re-load of config on @AfterConfiguration.
	 */
	PagespeedPluginConfig config = null;

	private Map<String, Map<String,String>> results = new HashMap<>();

	private AtomicLong executionNumberCounter = new AtomicLong();

	@Override
	public void initialize() {
		super.initialize();
		config = PagespeedPluginConfig.getByName(configurationName);
		Repository.getInstance().addCustomConfigurationProvider(new PagespeedConfigurationProvider(config));
		scheduler = Executors.newSingleThreadScheduledExecutor();
		taskExecutorService = Executors.newFixedThreadPool(10);
		//for now every 10 minutes, later 50 minutes.
		scheduler.scheduleAtFixedRate(new PagespeedPluginRunnable(),0, 15, TimeUnit.MINUTES);
		DataRepository.getInstance().addDataRetriever(this);

	}

	@Override
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	@Override
	public Map<String, String> retrieveData() {
		HashMap<String,String> ret = new HashMap<>();
		try {
			Map<String, Map<String, String>> currentResults = results;
			PagespeedPluginTargetConfig[] targets = config.getTargets();
			for (PagespeedPluginTargetConfig target : targets) {
				String prefix = "pagespeed." + target.getName() + ".";
				Map<String, String> resultsForTarget = currentResults.get(target.getName());
				if (resultsForTarget == null){
					log.warn("Couldn't obtain results for target "+target.getName());
					continue;
				}
				for (Map.Entry<String, String> entry : resultsForTarget.entrySet()) {
					ret.put(prefix + entry.getKey(), entry.getValue());
				}
			}
		}catch(Exception any){
			log.error("Can't retrieve data completely, aborted", any);
		}
		return ret;

	}

	@Override
	public void configure(String configurationParameter, List<VariableMapping> mappings) {

	}

	List<PagespeedTask> createTasks(){
		PagespeedPluginTargetConfig[] targets = config.getTargets();
		LinkedList<PagespeedTask> tasks = new LinkedList<>();
		for (PagespeedPluginTargetConfig target : targets){
			PagespeedTask task = new PagespeedTask(config.getApiKey(), target);
			tasks.add(task);
		}
		return tasks;

	}

	@Monitor(producerId = "Pagespeed.PluginExecutor", subsystem = "pagespeed")
	class PagespeedPluginTaskRunnable implements Runnable{

		private PagespeedTask task;
		private CountDownLatch notifyWhenFinished;
		private Map<String, Map<String,String>> results;

		public PagespeedPluginTaskRunnable(PagespeedTask task, CountDownLatch latch, Map<String, Map<String,String>>  globalResults) {
			this.task = task;
			this.notifyWhenFinished = latch;
			this.results = globalResults;
		}


		@Override
		public void run() {
			try {
				Map<String, String> taskResult = task.execute();
				results.put(task.getName(), taskResult);
				OnDemandStatsProducer<PagespeedStats> producer = getProducerByName(task.getName());
				for (Map.Entry<String,String> entry : taskResult.entrySet()){
					String key = entry.getKey();
					String value = entry.getValue();
					producer.getStats(key).setValue(value);
				}
			}catch(Exception e){
				log.error("Can't execute task "+task, e);
			}finally {
				notifyWhenFinished.countDown();
			}
		}
		@DontMonitor
		private OnDemandStatsProducer<PagespeedStats> getProducerByName(String name){
			IProducerRegistry producerRegistry = ProducerRegistryFactory.getProducerRegistryInstance();
			String producerName = Constants.configName2ProducerName(name);
			IStatsProducer existingProducer = producerRegistry.getProducer(producerName);
			if (existingProducer!=null)
				return (OnDemandStatsProducer)existingProducer;

			//no producer exists, we have to create new (this is first run or reconfiguration).
			OnDemandStatsProducer<PagespeedStats> newProducer = new OnDemandStatsProducer<PagespeedStats>(
					producerName, "pagespeed", "pagespeed", new PagespeedStatsFactory()
			);
			producerRegistry.registerProducer(newProducer);

			//create accumulators.
			String[] accumulatorNames = config.getAutoAccumulators();
			log.info("Creating accumulators "+ Arrays.toString(accumulatorNames));
			if (accumulatorNames!=null && accumulatorNames.length>0){
				for (String an : accumulatorNames){
					AccumulatorDefinition def = new AccumulatorDefinition();
					def.setName(producerName+"."+Constants.getNameSubstitution(an));
					def.setProducerName(producerName);
					def.setIntervalName("15m");
					def.setStatName(an);
					def.setValueName(PagespeedStats.StatDef.VALUE.getStatName());
					AccumulatorRepository.getInstance().createAccumulator(def);
				}
			}

			//create thresholds
			String[] thresholdNames = config.getAutoThresholds();
			log.info("Creating thresholds "+Arrays.toString(thresholdNames));
			for (String tn : thresholdNames){
				ThresholdDefinition def = new ThresholdDefinition();
				def.setName(producerName+"."+Constants.getNameSubstitution(tn));
				def.setProducerName(producerName);
				def.setIntervalName("15m");
				def.setStatName(tn);
				def.setValueName(PagespeedStats.StatDef.VALUE.getStatName());
				Threshold t = ThresholdRepository.getInstance().createThreshold(def);
				setStandardGuards(t);

			}

			return newProducer;
		}

		@DontMonitor
		private void setStandardGuards(Threshold t){
			if (t.getDefinition().getStatName().equals("largestContentfulPaint")){
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.RED, 4000, GuardedDirection.UP));
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.ORANGE, 2500, GuardedDirection.UP));
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.GREEN, 2500, GuardedDirection.DOWN));

			}

			if (t.getDefinition().getStatName().equals("lighthouseResult.categories.performance.score")){
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.RED, 0.5, GuardedDirection.DOWN));
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.ORANGE, 0.5, GuardedDirection.UP));
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.YELLOW, 0.8, GuardedDirection.UP));
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.GREEN, 0.9, GuardedDirection.UP));

			}

			if (t.getDefinition().getStatName().equals("firstContentfulPaint")){
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.RED, 2500, GuardedDirection.UP));
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.ORANGE, 1000, GuardedDirection.UP));
				t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.GREEN, 1000, GuardedDirection.DOWN));

			}
		}


	}

	@Monitor(producerId = "Pagespeed.Plugin", subsystem = "pagespeed")
	class PagespeedPluginRunnable implements Runnable{
		@Override
		public void run() {
			HashMap<String, Map<String,String>> results = new HashMap<>();
			long executionNumber = executionNumberCounter.incrementAndGet();
			log.debug("Scheduled execution nr. "+executionNumber+" of pagespeed "+ NumberUtils.makeISO8601TimestampString(System.currentTimeMillis()));
			long startTime = System.currentTimeMillis();
			List<PagespeedTask> tasks = createTasks();
			CountDownLatch latch = new CountDownLatch(tasks.size());
			for (PagespeedTask task : tasks) {
				PagespeedPluginTaskRunnable taskRunnable = new PagespeedPluginTaskRunnable(task, latch, results);
				taskExecutorService.submit(taskRunnable);
			}
			try {
				latch.await();
			}catch(InterruptedException e){
				log.warn("Couldn't receive results, waiting interrupted", e);
			}
			log.debug("Execution nr. "+executionNumber+" finished in "+((System.currentTimeMillis()-startTime)/1000)+" seconds ");
			log.debug("Global results: "+results);
			setResults(results);
		}

	}

	void setResults(Map<String,Map<String,String>> someResults){
		results = someResults;

	}

	@Override
	public String toString() {
		return "PagespeedPlugin{" +
				"configurationName='" + configurationName + '\'' +
				'}';
	}
}
