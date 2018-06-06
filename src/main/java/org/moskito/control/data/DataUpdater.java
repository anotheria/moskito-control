package org.moskito.control.data;

import org.moskito.control.data.processors.DataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.06.18 14:10
 */
public class DataUpdater {

	private static Logger log = LoggerFactory.getLogger(DataUpdater.class);


	public DataUpdater(){
		ScheduledExecutorService mainStarter = Executors.newScheduledThreadPool(1);
		mainStarter.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				retrieveAndProcess(
						DataRepository.getInstance().getRetrievers(),
						DataRepository.getInstance().getProcessors());
			}
		}, 1000, 30*1000L, TimeUnit.MILLISECONDS);
	}

	public void retrieveAndProcess(List<DataRetriever> retrievers, List<DataProcessor> processors){
		//for now we retrieve all the data directly.
		log.info("Starting update run with retrievers: "+retrievers+", processors: "+processors);
		ConcurrentMap<String, String> data = new ConcurrentHashMap<>();

		for (DataRetriever r : retrievers){
			try{
				Map<String,String> dataMap =  r.retrieveData();
				data.putAll(dataMap);
			}catch(Exception any){
				log.warn("DataRetriever "+r+" exception caught ", any);
			}
		}

		for (DataProcessor p : processors){
			try {
				Map<String, String> dataMap = p.process(data);
				data.putAll(dataMap);
			}catch(Exception any){
				log.error("Processor "+p+" failed, ", any);
			}
		}

		log.info("Updating data repository with "+data);
		DataRepository.getInstance().update(data);
	}
}
