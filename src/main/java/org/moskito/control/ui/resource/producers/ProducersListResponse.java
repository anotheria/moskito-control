package org.moskito.control.ui.resource.producers;

import org.moskito.control.ui.resource.ControlReplyObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * This is a container bean for producer names.
 * @author strel
 */
@XmlRootElement
public class ProducersListResponse extends ControlReplyObject {

	/**
	 * Producer names list.
	 */
	@XmlElement()
	private Set<ProducerBean> producers;


	public Set<ProducerBean> getProducers() {
		return producers;
	}

	public void setProducers(Set<ProducerBean> producers) {
		this.producers = producers;
	}
}
