package org.moskito.control.ui.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This is a container bean for history items for one application.
 *
 * @author lrosenberg
 * @since 13.06.13 17:14
 */
@XmlRootElement
public class HistoryBean extends ControlReplyObject {
	/**
	 * History items for this application.
	 */
	@XmlElement()
	private List<HistoryItemBean> historyItems;

	public HistoryBean() {
	}

	public HistoryBean(List<HistoryItemBean> items){
		historyItems = items;
	}

	public List<HistoryItemBean> getHistoryItems() {
		return historyItems;
	}

	public void setHistoryItems(List<HistoryItemBean> historyItems) {
		this.historyItems = historyItems;
	}

	@Override public String toString(){
		return "History: "+getHistoryItems();
	}
}
