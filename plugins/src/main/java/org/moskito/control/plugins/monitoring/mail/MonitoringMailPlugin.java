package org.moskito.control.plugins.monitoring.mail;

import net.anotheria.moskito.aop.annotation.DontMonitor;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.accumulation.AccumulatorDefinition;
import net.anotheria.moskito.core.accumulation.AccumulatorRepository;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducerException;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistry;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.threshold.Threshold;
import net.anotheria.moskito.core.threshold.ThresholdDefinition;
import net.anotheria.moskito.core.threshold.ThresholdRepository;
import net.anotheria.moskito.core.threshold.ThresholdStatus;
import net.anotheria.moskito.core.threshold.guard.DoubleBarrierPassGuard;
import net.anotheria.moskito.core.threshold.guard.GuardedDirection;
import org.moskito.control.config.datarepository.VariableMapping;
import org.moskito.control.core.ComponentRepository;
import org.moskito.control.data.DataRepository;
import org.moskito.control.data.retrievers.DataRetriever;
import org.moskito.control.plugins.AbstractMoskitoControlPlugin;
import org.moskito.control.plugins.monitoring.mail.stats.MonitoringMailStats;
import org.moskito.control.plugins.monitoring.mail.stats.MonitoringMailStatsFactory;
import org.moskito.control.plugins.pagespeed.stats.PagespeedStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Plugin for monitoring emails.
 * Checks periodically if there new mails.
 * Mails to check is configured in {@link MonitoringMailPluginConfig}.
 * Plugin will change mail component status if there is no new mails in 15 minutes.
 *
 * It also can trigger external app endpoint to send mail via REST API.
 * Endpoint to trigger send mail can be configured in {@link MonitoringMailSendEndpointConfig}.
 *
 * @author ynikonchuk
 */
public class MonitoringMailPlugin extends AbstractMoskitoControlPlugin implements DataRetriever {

    private static final Logger log = LoggerFactory.getLogger(MonitoringMailPlugin.class);


    private static final int THRESHOLD_DEFAULT_UPDATE_MINUTES_INTERVAL = 15;
    private static final int[] THRESHOLD_UPDATE_MINUTES_VALUES = { 1, 5, 15 };
    /**
     * Max mail update interval. It should be less than max threshold update interval.
     * Otherwise threshold will not have data to check.
     */
    private static final int MAX_FETCH_MAIL_UPDATE_MINUTES_INTERVAL = 14;

    /**
     * Name of the configuration.
     */
    private String configurationName;

    private ScheduledExecutorService tasksScheduler;
    private ExecutorService taskExecutorService;

    private MonitoringMailPluginConfig pluginConfig;

    @Override
    public void initialize() {
        super.initialize();
        tasksScheduler = Executors.newSingleThreadScheduledExecutor();
        taskExecutorService = Executors.newFixedThreadPool(10);

        ComponentRepository.getInstance().addCustomConfigurationProvider(new MonitoringMailConfigurationProvider(pluginConfig));


        // fetch mails with interval
        long mailFetchInterval = getFetchMailMinutesInterval();
        tasksScheduler.scheduleAtFixedRate(new MonitoringMailPluginFetchRunnable(),0, mailFetchInterval, TimeUnit.MINUTES);
        // send mails with interval
        long mailSendInterval = getSendMailMinutesInterval();
        tasksScheduler.scheduleAtFixedRate(new MonitoringMailPluginSendRunnable(),0, mailSendInterval, TimeUnit.MINUTES);

        DataRepository.getInstance().addDataRetriever(this);
    }

    @Override
    public void setConfigurationName(String configurationName) {
        String oldConfigurationName = this.configurationName;
        this.configurationName = configurationName;

        if (oldConfigurationName == null || !oldConfigurationName.equals(configurationName)) {
            onConfigurationNameChanged();
        }
    }

    private void onConfigurationNameChanged() {
        // change plugin configuration if configuration name changed
        this.pluginConfig = MonitoringMailPluginConfig.getByName(configurationName);
    }

    @Override
    public Map<String, String> retrieveData() {
        return new HashMap<>();
    }

    @Override
    public void configure(String configurationParameter, List<VariableMapping> mappings) {

    }

    @Monitor(producerId = "MonitoringMailFetch.Plugin", subsystem = "monitoring.mail")
    private class MonitoringMailPluginFetchRunnable implements Runnable {

        @Override
        public void run() {
            try {
                List<MonitoringFetchMailTask> tasks = createFetchMailTasks();

                List<Future<MonitoringFetchMailTask.Result>> taskFutures = new ArrayList<>(tasks.size());
                for (MonitoringFetchMailTask task : tasks) {
                    taskFutures.add(taskExecutorService.submit(task::execute));
                }

                // collecting async results
                for (Future<MonitoringFetchMailTask.Result> taskFuture : taskFutures) {
                    try {
                        MonitoringFetchMailTask.Result taskResult = taskFuture.get();

                        MonitoringMailConfig mailConfig = taskResult.getConfig();
                        Date lastMessageDate = taskResult.getLastMessageDate();

                        OnDemandStatsProducer<MonitoringMailStats> producer = getProducerByName(mailConfig.getName());

                        // last execution date
                        long lastExecutionDate = System.currentTimeMillis();
                        // set actual last seen mail date
                        long lastSeenMailDate = lastMessageDate == null ? new Date(0).getTime() : lastMessageDate.getTime();
                        // time passed from last seen mail date
                        long timePassed = lastExecutionDate - lastSeenMailDate;

                        producer.getStats(Constants.STAT_FETCH_LAST_EXECUTION_DATE).setValue(lastExecutionDate+"");
                        producer.getStats(Constants.STAT_FETCH_LAST_MSG_DATE).setValue(lastSeenMailDate+"");
                        producer.getStats(Constants.STAT_FETCH_TIME_PASSED).setValue(timePassed+"");
                    } catch (InterruptedException | ExecutionException | OnDemandStatsProducerException e) {
                        log.error("can't get task result. cause: {}", e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Monitor(producerId = "MonitoringMailSend.Plugin", subsystem = "monitoring.mail")
    private class MonitoringMailPluginSendRunnable implements Runnable {

        @Override
        public void run() {
            try {
                List<MonitoringSendMailTask> tasks = createSendMailTasks();

                List<Future<MonitoringSendMailTask.Result>> taskFutures = new ArrayList<>(tasks.size());
                for (MonitoringSendMailTask task : tasks) {
                    taskFutures.add(taskExecutorService.submit(task::execute));
                }

                // collecting async results
                for (Future<MonitoringSendMailTask.Result> taskFuture : taskFutures) {
                    try {
                        MonitoringSendMailTask.Result taskResult = taskFuture.get();

                        MonitoringMailConfig mailConfig = taskResult.getConfig();

                        OnDemandStatsProducer<MonitoringMailStats> producer = getProducerByName(mailConfig.getName());
                        if (taskResult.isSuccess()) {
                            long successReqAmount = producer.getStats(Constants.STAT_SEND_SUCCESS).getValue();
                            producer.getStats(Constants.STAT_SEND_SUCCESS).setValue(++successReqAmount + "");
                        } else {
                            long failedReqAmount = producer.getStats(Constants.STAT_SEND_FAILED).getValue();
                            producer.getStats(Constants.STAT_SEND_FAILED).setValue(++failedReqAmount + "");
                        }
                    } catch (InterruptedException | ExecutionException | OnDemandStatsProducerException e) {
                        log.error("can't get task result. cause: {}", e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private List<MonitoringFetchMailTask> createFetchMailTasks() {
        List<MonitoringFetchMailTask> result = new LinkedList<>();
        for (MonitoringMailConfig mailConfig : pluginConfig.getMailConfigs()) {
            MonitoringMailFetchConfig fetchConfig = mailConfig.getFetchMailConfig();
            if (fetchConfig != null) {
                result.add(new MonitoringFetchMailTask(mailConfig));
            }
        }
        return result;
    }

    private List<MonitoringSendMailTask> createSendMailTasks() {
        List<MonitoringSendMailTask> result = new LinkedList<>();
        for (MonitoringMailConfig mailConfig : pluginConfig.getMailConfigs()) {
            MonitoringMailSendEndpointConfig sendConfig = mailConfig.getSendMailConfig();
            if (sendConfig != null) {
                result.add(new MonitoringSendMailTask(mailConfig));
            }
        }
        return result;
    }

    @DontMonitor
    private OnDemandStatsProducer<MonitoringMailStats> getProducerByName(String producerName){
        IProducerRegistry producerRegistry = ProducerRegistryFactory.getProducerRegistryInstance();
        IStatsProducer existingProducer = producerRegistry.getProducer(producerName);
        if (existingProducer!=null)
            return (OnDemandStatsProducer)existingProducer;

        //no producer exists, we have to create new (this is first run or reconfiguration).
        OnDemandStatsProducer<MonitoringMailStats> newProducer = new OnDemandStatsProducer<>(
                producerName, pluginConfig.getCategoryName(), pluginConfig.getSubsystem(), new MonitoringMailStatsFactory()
        );
        producerRegistry.registerProducer(newProducer);

        //create thresholds
        createThresholds(producerName);
        createAccumulators(producerName);

        return newProducer;
    }

    @DontMonitor
    private void setStandardGuards(Threshold t){
        if (t.getDefinition().getStatName().equals(Constants.STAT_FETCH_TIME_PASSED)){
            // if less than 1ms means that no new messages
            t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.RED, TimeUnit.MINUTES.toMillis(15), GuardedDirection.UP));
            t.addGuard(new DoubleBarrierPassGuard(ThresholdStatus.GREEN, TimeUnit.MINUTES.toMillis(15), GuardedDirection.DOWN));
        }
    }

    @DontMonitor
    private void createThresholds(String producerName) {
        log.info("Creating thresholds.");
        createThreshold(producerName, Constants.STAT_FETCH_TIME_PASSED);
    }

    @DontMonitor
    private void createThreshold(String producerName, String thresholdName) {
        ThresholdDefinition def = new ThresholdDefinition();
        def.setName(producerName);
        def.setProducerName(producerName);
        def.setIntervalName(getThresholdUpdateInterval());
        def.setStatName(thresholdName);
        def.setValueName(MonitoringMailStats.StatDef.VALUE.getStatName());
        Threshold t = ThresholdRepository.getInstance().createThreshold(def);
        setStandardGuards(t);
    }

    @DontMonitor
    private void createAccumulators(String producerName) {
        log.info("Creating accumulators.");
        createAccumulator(producerName, Constants.STAT_SEND_SUCCESS);
        createAccumulator(producerName, Constants.STAT_SEND_FAILED);
    }


    @DontMonitor
    private void createAccumulator(String producerName, String accumulatorName) {
        AccumulatorDefinition def = new AccumulatorDefinition();
        def.setName(producerName);
        def.setProducerName(producerName);
        def.setIntervalName("15m");
        def.setStatName(accumulatorName);
        def.setValueName(PagespeedStats.StatDef.VALUE.getStatName());
        AccumulatorRepository.getInstance().createAccumulator(def);
    }

    private int getFetchMailMinutesInterval() {
        return Math.min(pluginConfig.getFetchIntervalMinutes(), MAX_FETCH_MAIL_UPDATE_MINUTES_INTERVAL);
    }

    private int getSendMailMinutesInterval() {
        return Math.min(pluginConfig.getSendIntervalMinutes(), MAX_FETCH_MAIL_UPDATE_MINUTES_INTERVAL);
    }

    private String getThresholdUpdateInterval() {
        return getThresholdUpdateMinutesInterval()+"m";
    }

    private int getThresholdUpdateMinutesInterval() {
        int mailUpdateMinutesInterval = getFetchMailMinutesInterval();
        // default value
        int result = THRESHOLD_DEFAULT_UPDATE_MINUTES_INTERVAL;
        for (int thresholdUpdateInterval : THRESHOLD_UPDATE_MINUTES_VALUES) {
            if (thresholdUpdateInterval > mailUpdateMinutesInterval) {
                // set first interval bigger than mail update interval
                result = thresholdUpdateInterval;
                break;
            }
        }
        return result;
    }
}
