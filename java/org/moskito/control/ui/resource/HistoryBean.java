package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.06.13 17:14
 */
@XmlRootElement
public class HistoryBean extends ControlReplyObject {
	@XmlElement()
	private List<HistoryItemBean> historyItems;

	@XmlElement
	private String applicationName;

	public HistoryBean(String anApplicationName, List<HistoryItemBean> items){
		historyItems = items;
		applicationName = anApplicationName;
	}

	public List<HistoryItemBean> getHistoryItems() {
		return historyItems;
	}

	public void setHistoryItems(List<HistoryItemBean> historyItems) {
		this.historyItems = historyItems;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String toString(){
		return "AppName: "+applicationName+", History: "+getHistoryItems();
	}
}
