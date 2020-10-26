package org.moskito.control.ui.resource.accumulators;

import org.moskito.control.ui.resource.ControlReplyObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This is a container bean for accumulator names.
 * @author strel
 */
@XmlRootElement
public class AccumulatorsListBean extends ControlReplyObject {

	/**
	 * Accumulator names.
	 */
	@XmlElement()
	private List<String> names;

	/**
	 * Component name.
	 */
	@XmlElement
	private String componentName;


	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
}
