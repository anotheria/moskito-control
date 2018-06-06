package org.moskito.control.data;

import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.datarepository.DataRepositoryConfig;
import org.moskito.control.config.datarepository.ProcessingConfig;
import org.moskito.control.config.datarepository.ProcessorConfig;
import org.moskito.control.data.processors.DataProcessor;
import org.moskito.control.data.test.MoSKitoValueMapping;
import org.moskito.control.data.test.TestDataRetriever;
import org.moskito.control.data.test.TestMoSKitoRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 13:56
 */
public class DataRepository {

	private static Logger log = LoggerFactory.getLogger(DataRepository.class);

	private volatile Map<String, String> dataMap = Collections.unmodifiableMap(Collections.emptyMap());

	private ConcurrentMap<String, Class<DataProcessor>> processorClasses = new ConcurrentHashMap<>();

	private List<DataRetriever> retrievers = new LinkedList<>();
	private List<DataProcessor> processors = new LinkedList<>();

	public static final DataRepository getInstance(){
		return DataRepositoryInstanceHolder.instance;
	}

	public Map<String,String> getData(){
		return dataMap;
	}

	public void update(Map<String, String> newData){
		dataMap = Collections.unmodifiableMap(newData);
	}

	static class DataRepositoryInstanceHolder{
		private static DataRepository instance = new DataRepository();
		//start updater.
		private static DataUpdater updater = new DataUpdater();
		static {
			instance.configure();
			instance.testFilling();
		}
	}

	public List<DataRetriever> getRetrievers() {
		return retrievers;
	}

	public List<DataProcessor> getProcessors() {
		return processors;
	}

	public void addDataRetriever(DataRetriever aRetriever){
		retrievers.add(aRetriever);
	}

	public void addDataProcessor(DataProcessor dataProcessor){
		processors.add(dataProcessor);
	}

	private void configure(){
		DataRepositoryConfig config = MoskitoControlConfiguration.getConfiguration().getDataRepositoryConfig();
		processorClasses.clear();
		for (ProcessorConfig pc : config.getProcessors()){
			try{
				Class<DataProcessor> processorClass = (Class<DataProcessor>)Class.forName(pc.getClazz());
				processorClasses.put(pc.getName(), processorClass);
			}catch(ClassNotFoundException e){
				log.error("Class "+pc.getClazz()+" for processor "+pc.getName()+" not found", e);
			}
		}
		log.info("Configured processors: "+processorClasses);
		processors = new CopyOnWriteArrayList<>();
		for (ProcessingConfig processingConfig : config.getProcessing()){
			Class<DataProcessor> clazz = processorClasses.get(processingConfig.getProcessor());
			if (clazz==null){
				log.error("Can't setup processing "+processingConfig+" processor "+processingConfig.getProcessor()+" is not configured");
				continue;
			}

			try {
				DataProcessor processor = clazz.newInstance();
				processor.configure(processingConfig.getVariable(), processingConfig.getParameter());
				addDataProcessor(processor);
			} catch (InstantiationException |IllegalAccessException e) {
				log.error("Can't instantiate processor "+processingConfig.getProcessor()+" -> "+clazz+" -> ", e);
			}
		}
		log.info("Configured processing: "+processors);
	}

	private void testFilling(){
		addDataRetriever(new TestDataRetriever());
		addDataRetriever(createTestAddMosKitoMappings("hamburg"));
		addDataRetriever(createTestAddMosKitoMappings("munich"));
		addDataRetriever(createTestAddMosKitoMappings("bedcon"));
	}

	private TestMoSKitoRetriever createTestAddMosKitoMappings(String prefix){

		MoSKitoValueMapping mapping1 = new MoSKitoValueMapping();
		mapping1.setProducerName("ShopService");
		mapping1.setStatName("placeOrder");
		mapping1.setValueName("req");
		mapping1.setIntervalName("1m");
		mapping1.setTimeUnitName("MILLISECONDS");
		mapping1.setTargetVariableName(prefix+".orderCount");

		MoSKitoValueMapping mapping2 = new MoSKitoValueMapping();
		mapping2.setProducerName("sales");
		mapping2.setStatName("cumulated");
		mapping2.setValueName("Volume");
		mapping2.setIntervalName("default");
		mapping2.setTimeUnitName("MILLISECONDS");
		mapping2.setTargetVariableName(prefix+".earnings");

		TestMoSKitoRetriever r = new  TestMoSKitoRetriever("http://burgershop-"+prefix+".demo.moskito.org/burgershop/moskito-inspect-rest", mapping1, mapping2);
		return r;

	}
}
