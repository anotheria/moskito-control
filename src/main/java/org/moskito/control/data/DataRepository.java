package org.moskito.control.data;

import net.anotheria.util.StringUtils;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.datarepository.DataRepositoryConfig;
import org.moskito.control.config.datarepository.ProcessorConfig;
import org.moskito.control.data.preprocessors.DataPreprocessor;
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
	private ConcurrentMap<String, Class<DataPreprocessor>> preprocessorClasses = new ConcurrentHashMap<>();

	private List<DataRetriever> retrievers = new LinkedList<>();
	private List<DataProcessor> processors = new LinkedList<>();
	private List<DataPreprocessor> preprocessors = new LinkedList<>();

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

	public void addDataPreprocessor(DataPreprocessor dataPreprocessor){
		preprocessors.add(dataPreprocessor);
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
		for (String processingLine : config.getProcessing()){
			String tokens[] = StringUtils.tokenize(processingLine, ' ');
			//TODO if more then 3 tokens, sum all the following tokens back into 3rd
			String processorName = tokens[0];
			String variableName  = tokens[1];
			String parameter     = tokens[2];
			Class<DataProcessor> clazz = processorClasses.get(processorName);
			if (clazz==null){
				log.error("Can't setup processing "+processingLine+" processor "+processorName+" is not configured");
				continue;
			}

			try {
				DataProcessor processor = clazz.newInstance();
				processor.configure(variableName, parameter);
				addDataProcessor(processor);
			} catch (InstantiationException |IllegalAccessException e) {
				log.error("Can't instantiate processor "+processorName+" -> "+clazz+" -> ", e);
			}
		}
		log.info("Configured processing: "+processors);

		//preprocessing
		preprocessorClasses.clear();
		for (ProcessorConfig pc : config.getPreprocessors()){
			try{
				Class<DataPreprocessor> preprocessorClass = (Class<DataPreprocessor>)Class.forName(pc.getClazz());
				preprocessorClasses.put(pc.getName(), preprocessorClass);
			}catch(ClassNotFoundException e){
				log.error("Class "+pc.getClazz()+" for preprocessor "+pc.getName()+" not found", e);
			}
		}
		log.info("Configured preprocessors: "+preprocessorClasses);
		preprocessors = new CopyOnWriteArrayList<>();
		for (String preprocessingLine : config.getPreprocessing()){
			String tokens[] = StringUtils.tokenize(preprocessingLine, ' ');
			//TODO if more then 3 tokens, sum all the following tokens back into 3rd
			String preprocessorName = tokens[0];
			String variableName  = tokens[1];
			String parameter     = tokens[2];
			Class<DataPreprocessor> clazz = preprocessorClasses.get(preprocessorName);
			if (clazz==null){
				log.error("Can't setup processing "+preprocessingLine+" processor "+preprocessorName+" is not configured");
				continue;
			}

			try {
				DataPreprocessor preprocessor = clazz.newInstance();
				preprocessor.configure(variableName, parameter);
				addDataPreprocessor(preprocessor);
			} catch (InstantiationException |IllegalAccessException e) {
				log.error("Can't instantiate processor "+preprocessorName+" -> "+clazz+" -> ", e);
			}
		}
		log.info("Configured preprocessing: "+preprocessors);


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
		mapping2.setIntervalName("1h");
		mapping2.setTimeUnitName("MILLISECONDS");
		mapping2.setTargetVariableName(prefix+".earnings");

		MoSKitoValueMapping mapping3 = new MoSKitoValueMapping();
		mapping3.setProducerName("SessionCount");
		mapping3.setStatName("Sessions");
		mapping3.setValueName("Cur");
		mapping3.setIntervalName("default");
		mapping3.setTimeUnitName("MILLISECONDS");
		mapping3.setTargetVariableName(prefix+".sessions");

		MoSKitoValueMapping mapping4 = new MoSKitoValueMapping();
		mapping4.setProducerName("RequestURI");
		mapping4.setStatName("cumulated");
		mapping4.setValueName("Req");
		mapping4.setIntervalName("1h");
		mapping4.setTimeUnitName("MILLISECONDS");
		mapping4.setTargetVariableName(prefix+".requests");

		TestMoSKitoRetriever r = new  TestMoSKitoRetriever("http://burgershop-"+prefix+".demo.moskito.org/burgershop/moskito-inspect-rest", mapping1, mapping2, mapping3, mapping4);
		return r;

	}
}
