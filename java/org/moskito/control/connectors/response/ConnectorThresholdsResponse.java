package org.moskito.control.connectors.response;

import org.moskito.control.core.threshold.ThresholdDataItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Retrieved thresholds data container.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ConnectorThresholdsResponse extends ConnectorResponse {

	/**
	 * Contained thresholds information.
	 */
    private List<ThresholdDataItem> items;


    public ConnectorThresholdsResponse() {
        items = new LinkedList<ThresholdDataItem>();
    }

    public ConnectorThresholdsResponse(List<ThresholdDataItem> items) {
        this.items = items;
    }


    public List<ThresholdDataItem> getItems() {
        return items;
    }

    public void setItems(List<ThresholdDataItem> items) {
        this.items = items;
    }

}
