package org.moskito.control.data;

import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.datarepository.DataProcessingConfig;
import org.moskito.control.config.datarepository.DataRepositoryConfig;
import org.moskito.control.config.datarepository.ProcessorConfig;
import org.moskito.control.config.datarepository.RetrieverConfig;
import org.moskito.control.config.datarepository.RetrieverInstanceConfig;
import org.moskito.control.data.preprocessors.DataPreprocessor;
import org.moskito.control.data.processors.DataProcessor;
import org.moskito.control.data.retrievers.DataRetriever;
import org.moskito.control.data.thresholds.DataThreshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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

	/**
	 * Configured implementations of processors.
	 */
	private ConcurrentMap<String, Class<DataProcessor>> processorClasses = new ConcurrentHashMap<>();
	/**
	 * Configured implementations of preprocessors.
	 */
	private ConcurrentMap<String, Class<DataPreprocessor>> preprocessorClasses = new ConcurrentHashMap<>();
	/**
	 * Configured implementations of retrievers.
	 */
	private ConcurrentMap<String, Class<DataRetriever>> retrieverClasses = new ConcurrentHashMap<>();

	/**
	 * Configured retrievers through processing.
	 */
	private List<DataRetriever> retrievers = new LinkedList<>();
	/**
	 * Configured processors through processing.
	 */
	private List<DataProcessor> processors = new LinkedList<>();
	/**
	 * Configured preprocessors through processing.
	 */
	private List<DataPreprocessor> preprocessors = new LinkedList<>();

	private List<DataThreshold> thresholds = new LinkedList<>();

	public static final DataRepository getInstance(){
		return DataRepositoryInstanceHolder.instance;
	}

	public Map<String,String> getData(){
		return dataMap;
	}

	public void update(Map<String, String> newData){
		dataMap = Collections.unmodifiableMap(newData);
		updateThresholds(Collections.unmodifiableMap(newData));
	}

	private void updateThresholds(Map<String, String> newData) {
		for (DataThreshold threshold : thresholds) {
			String newValue = newData.get(threshold.getConfig().getGuardedVariableName());
			if (newValue != null && !newValue.isEmpty()) {
				threshold.update(newValue);
			}
		}
	}

	static class DataRepositoryInstanceHolder{
		private static DataRepository instance = new DataRepository();
		//start updater.
		private static DataUpdater updater = new DataUpdater();
		static {
			instance.configure();
		}
	}

	public List<DataRetriever> getRetrievers() {
		return retrievers;
	}

	public List<DataProcessor> getProcessors() {
		return processors;
	}

	public List<DataPreprocessor> getPreprocessors(){
		return preprocessors;
	}

	public List<DataThreshold> getThresholds() {
		return thresholds;
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

	public void addDataThreshold(DataThreshold dataThreshold) {
		thresholds.add(dataThreshold);
	}

	private void configure(){
		DataRepositoryConfig repositoryConfig = new DataRepositoryConfig();
		ConfigurationManager.INSTANCE.configure(repositoryConfig);

		//configure retriever classes / implementations.
		retrieverClasses.clear();
		if (repositoryConfig.getRetrievers()!=null && repositoryConfig.getRetrievers().length>0){
			for (RetrieverConfig rc : repositoryConfig.getRetrievers()){
				try {
					Class<DataRetriever> retrieverClass = (Class<DataRetriever>) Class.forName(rc.getClazz());
					retrieverClasses.put(rc.getName(), retrieverClass);
				} catch (ClassNotFoundException e) {
					log.error("Class " + rc.getClazz() + " for retriever " + rc.getName() + " not found", e);
				}
			}
			log.info("Configured processors: " + processorClasses);
		}

		DataProcessingConfig processingConfig = MoskitoControlConfiguration.getConfiguration().getDataprocessing();
		//configure processors - classes.
		processorClasses.clear();
		if (repositoryConfig.getProcessors()!=null && repositoryConfig.getProcessors().length>0) {
			for (ProcessorConfig pc : repositoryConfig.getProcessors()) {
				try {
					Class<DataProcessor> processorClass = (Class<DataProcessor>) Class.forName(pc.getClazz());
					processorClasses.put(pc.getName(), processorClass);
				} catch (ClassNotFoundException e) {
					log.error("Class " + pc.getClazz() + " for processor " + pc.getName() + " not found", e);
				}
			}
			log.info("Configured processors: " + processorClasses);
		}

		//configure processors - processing.
		processors = new CopyOnWriteArrayList<>();
		for (String processingLine : processingConfig.getProcessing()){
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
			} catch(Exception any){
				log.error("Can't configure processor "+processorName+", of class "+clazz+", with ("+variableName+", "+parameter+")", any);
			}
		}
		log.info("Configured processing: "+processors);

		//configure preprocessors - classes.
		preprocessorClasses.clear();
		if (repositoryConfig.getPreprocessors()!=null && repositoryConfig.getPreprocessors().length>0){
			for (ProcessorConfig pc : repositoryConfig.getPreprocessors()){
				try{
					Class<DataPreprocessor> preprocessorClass = (Class<DataPreprocessor>)Class.forName(pc.getClazz());
					preprocessorClasses.put(pc.getName(), preprocessorClass);
				}catch(ClassNotFoundException e){
					log.error("Class "+pc.getClazz()+" for preprocessor "+pc.getName()+" not found", e);
				}
			}
			log.info("Configured preprocessors: "+preprocessorClasses);
		}



		//Configure preprocessors - processing.
		preprocessors = new CopyOnWriteArrayList<>();
		for (String preprocessingLine : processingConfig.getPreprocessing()){
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


		//finally configure retrievers
		retrievers = new CopyOnWriteArrayList<>();
		for (RetrieverInstanceConfig retrieverConfig : processingConfig.getRetrievers()){
			Class<DataRetriever> clazz = retrieverClasses.get(retrieverConfig.getName());
			if (clazz==null){
				log.error("Can't setup retriever "+retrieverConfig+" - clazz is not configured");
				continue;
			}

			try {
				DataRetriever retriever = clazz.newInstance();
				retriever.configure(retrieverConfig.getConfiguration(), Arrays.asList(retrieverConfig.getMappings()));
				addDataRetriever(retriever);
			} catch (InstantiationException |IllegalAccessException e) {
				log.error("Can't instantiate retriever "+retrieverConfig.getName()+" -> "+clazz+" -> ", e);
			}
		}
		log.info("Configured retrieveres: "+retrievers);


		//configure thresholds
		thresholds = new CopyOnWriteArrayList<>();
		Map<String, List<String>> guardedVariableNameMap = new ConcurrentHashMap<>();

		for (String thresholdLine : processingConfig.getThresholds()) {
			String tokens[] = StringUtils.tokenize(thresholdLine, ' ');

			if (tokens.length > 1) {
				if (!guardedVariableNameMap.containsKey(tokens[0])) {
					guardedVariableNameMap.put(tokens[0], new LinkedList<>());
				}
				guardedVariableNameMap.get(tokens[0]).add(thresholdLine);
			}
		}
		
		for (String key : guardedVariableNameMap.keySet()) {
			try {
				DataThreshold dataThreshold = new DataThreshold();
				dataThreshold.configure(key, guardedVariableNameMap.get(key));
				addDataThreshold(dataThreshold);
			} catch (Exception e) {
				log.error("Can't create/configure threshold for " + key + ". " + e.getMessage(), e);
			}
		}

        log.info("Configured thresholds: " + thresholds);
    }

}
