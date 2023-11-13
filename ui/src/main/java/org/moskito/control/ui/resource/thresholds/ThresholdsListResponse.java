package org.moskito.control.ui.resource.thresholds;

import org.moskito.control.ui.resource.ControlReplyObject;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This is a container bean for threshold beans.
 * @author strel
 */
@XmlRootElement
public class ThresholdsListResponse extends ControlReplyObject {

	/**
	 * Threshold beans list.
	 */
	@XmlElement()
	private List<ThresholdBean> thresholds;


	public List<ThresholdBean> getThresholds() {
		return thresholds;
	}

	public void setThresholds(List<ThresholdBean> thresholds) {
		this.thresholds = thresholds;
	}
}
