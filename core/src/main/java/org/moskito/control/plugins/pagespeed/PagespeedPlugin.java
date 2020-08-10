package org.moskito.control.plugins.pagespeed;

import net.anotheria.moskito.aop.annotation.DontMonitor;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistry;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import org.moskito.control.config.datarepository.VariableMapping;
import org.moskito.control.data.DataRepository;
import org.moskito.control.data.retrievers.DataRetriever;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;
import org.moskito.control.plugins.pagespeed.stats.PagespeedStats;
import org.moskito.control.plugins.pagespeed.stats.PagespeedStatsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

	private ScheduledExecutorService executorService;
	/**
	 * My config. TODO later add re-load of config on @AfterConfiguration.
	 */
	PagespeedPluginConfig config = null;

	private Map<String, Map<String,String>> results = new HashMap<>();

	@Override
	public void initialize() {
		super.initialize();
		config = PagespeedPluginConfig.getByName(configurationName);
		executorService = Executors.newSingleThreadScheduledExecutor();
		//for now every 10 minutes, later 50 minutes.
		executorService.scheduleAtFixedRate(new PagespeedPluginRunnable(),0, 10, TimeUnit.MINUTES);
		DataRepository.getInstance().addDataRetriever(this);

	}

	@Override
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	@Override
	public Map<String, String> retrieveData() {
		HashMap<String,String> ret = new HashMap<>();
		Map<String,Map<String,String>> currentResults = results;
		PagespeedPluginTargetConfig[] targets = config.getTargets();
		for (PagespeedPluginTargetConfig target : targets){
			String prefix = "pagespeed."+target.getName()+".";
			Map<String,String> resultsForTarget = currentResults.get(target.getName());
			for (Map.Entry<String,String> entry : resultsForTarget.entrySet()){
				ret.put(prefix+entry.getKey(), entry.getValue());
			}
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

	@Monitor(producerId = "Pagespeed.Plugin", subsystem = "pagespeed")
	class PagespeedPluginRunnable implements Runnable{
		@Override
		public void run() {
			HashMap<String, Map<String,String>> results = new HashMap<>();
			System.out.println("Scheduled execution of pagespeed");
			List<PagespeedTask> tasks = createTasks();
			for (PagespeedTask task : tasks){
				System.out.println("Executing task "+task);
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
				}
			}
			setResults(results);
		}

		@DontMonitor
		private OnDemandStatsProducer<PagespeedStats> getProducerByName(String name){
			IProducerRegistry producerRegistry = ProducerRegistryFactory.getProducerRegistryInstance();
			String producerName = "Pagespeed."+name;
			IStatsProducer existingProducer = producerRegistry.getProducer(producerName);
			if (existingProducer!=null)
				return (OnDemandStatsProducer)existingProducer;

			//no producer exists, we have to create new (this is first run or reconfiguration).
			OnDemandStatsProducer<PagespeedStats> newProducer = new OnDemandStatsProducer<PagespeedStats>(
					producerName, "pagespeed", "pagespeed", new PagespeedStatsFactory()
			);
			producerRegistry.registerProducer(newProducer);
			return newProducer;
		}
	}

	void setResults(Map<String,Map<String,String>> someResults){
		results = someResults;

	}
}
