package org.moskito.control.ui.resource.producers;

import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * REST resource used to retrieve list of possible producers.
 * @author strel
 */
@Path("/producers")
@Produces(MediaType.APPLICATION_JSON)
public class ProducersResource {

	@GET
	@Path("/get/all")
	public ProducersListResponse getProducers(){

		IProducerRegistryAPI producerRegistryAPI = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		List<IStatsProducer> producers = producerRegistryAPI.getAllProducers();

		Set<ProducerBean> producerBeans = new TreeSet<>();
		for (IStatsProducer producer : producers) {

			Set<StatisticsBean> statisticsBeans = new TreeSet<>();
			List<IStats> stats = (List<IStats>) producer.getStats();

			for (IStats stat : stats) {
				StatisticsBean statisticsBean = new StatisticsBean();
				statisticsBean.setName(stat.getName());
				statisticsBean.setValues(new TreeSet<>(stat.getAvailableValueNames()));
				statisticsBeans.add(statisticsBean);
			}

			ProducerBean producerBean = new ProducerBean();
			producerBean.setName(producer.getProducerId());
			producerBean.setStats(statisticsBeans);
			producerBeans.add(producerBean);
		}

		ProducersListResponse response = new ProducersListResponse();
		response.setProducers(producerBeans);

		return response;
	}
}
