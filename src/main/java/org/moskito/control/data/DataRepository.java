package org.moskito.control.data;

import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.datarepository.DataProcessingConfig;
import org.moskito.control.config.datarepository.DataRepositoryConfig;
import org.moskito.control.config.datarepository.ProcessorConfig;
import org.moskito.control.config.datarepository.RetrieverConfig;
import org.moskito.control.config.datarepository.RetrieverInstanceConfig;
import org.moskito.control.config.datarepository.VariableMapping;
import org.moskito.control.data.preprocessors.DataPreprocessor;
import org.moskito.control.data.processors.DataProcessor;
import org.moskito.control.data.retrievers.DataRetriever;
import org.moskito.control.data.retrievers.MoSKitoRetriever;
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

	public List<DataPreprocessor> getPreprocessors(){
		return preprocessors;
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
				System.out.println("COnfiguring "+retrieverConfig);
				log.error("COnfiguring "+retrieverConfig);
				DataRetriever retriever = clazz.newInstance();
				retriever.configure(retrieverConfig.getConfiguration(), Arrays.asList(retrieverConfig.getMappings()));
				addDataRetriever(retriever);
			} catch (InstantiationException |IllegalAccessException e) {
				log.error("Can't instantiate retriever "+retrieverConfig.getName()+" -> "+clazz+" -> ", e);
			}
		}
		log.info("Configured retrieveres: "+retrievers);


	}

	private void testFilling(){
		//addDataRetriever(new TestDataRetriever());
		//addDataRetriever(createTestAddMosKitoMappings("hamburg"));
		//addDataRetriever(createTestAddMosKitoMappings("munich"));
		//addDataRetriever(createTestAddMosKitoMappings("bedcon"));

		//addDataRetriever(createJsonTestRetrieverPayment());
		//addDataRetriever(createJsonTestRetrieverRegs());
	}
/*
	private JSONRetriever createJsonTestRetrieverPayment(){
		JSONRetriever retriever = new JSONRetriever();
		retriever.setUrl("https://extapi.thecasuallounge.com/extapi/api/v1/data/paymentsPerDay");
		retriever.setMappings(Arrays.asList(new JSONValueMapping[]{
			new JSONValueMapping("$.results.payments[2].all.count", "payments.count.yesterday"),
				new JSONValueMapping("$.results.payments[0].all.count", "payments.count.today"),
				new JSONValueMapping("$.results.payments[1].all.count", "payments.count.sameYesterday"),
				new JSONValueMapping("$.results.payments[2].all.revenue", "payments.revenue.yesterday"),
				new JSONValueMapping("$.results.payments[0].all.revenue", "payments.revenue.today"),
				new JSONValueMapping("$.results.payments[1].all.revenue", "payments.revenue.sameYesterday")
		}));

		return retriever;
	}

	private JSONRetriever createJsonTestRetrieverRegs(){
		JSONRetriever retriever = new JSONRetriever();
		retriever.setUrl("https://extapi.thecasuallounge.com/extapi/api/v1/data/registrationsPerDay");
		retriever.setMappings(Arrays.asList(new JSONValueMapping[]{
				new JSONValueMapping("$.results.registrations[2].all.count", "reg.total.yesterday"),
				new JSONValueMapping("$.results.registrations[0].all.count", "reg.total.today"),
				new JSONValueMapping("$.results.registrations[1].all.count", "reg.total.sameYesterday"),
				new JSONValueMapping("$.results.registrations[2].all.male", "reg.male.yesterday"),
				new JSONValueMapping("$.results.registrations[0].all.male", "reg.male.today"),
				new JSONValueMapping("$.results.registrations[1].all.male", "reg.male.sameYesterday")
		}));



		return retriever;
	}
	*/

	private MoSKitoRetriever createTestAddMosKitoMappings(String prefix){

		VariableMapping mapping1 = new VariableMapping();
		mapping1.setVariableName(prefix+".orderCount");
		mapping1.setExpression("ShopService.placeOrder.req.1m.MILLISECONDS");

		VariableMapping mapping2 = new VariableMapping();
		mapping2.setExpression("sales.cumulated.Volume.1h.MILLISECONDS");
		mapping2.setVariableName(prefix+".earnings");

		VariableMapping mapping3 = new VariableMapping();
		mapping3.setExpression("SessionCount.Sessions.Cur.default.MILLISECONDS");
		mapping3.setVariableName(prefix+".sessions");

		VariableMapping mapping4 = new VariableMapping();
		mapping4.setExpression("RequestURI.cumulated.Req.1h.MILLISECONDS");
		mapping4.setVariableName(prefix+".requests");

		List<VariableMapping> list = new LinkedList<>();
		list.add(mapping1);list.add(mapping2);list.add(mapping3);list.add(mapping4);

		MoSKitoRetriever r = new MoSKitoRetriever();
		r.configure("http://burgershop-"+prefix+".demo.moskito.org/burgershop/moskito-inspect-rest", list);
		return r;

	}
}
