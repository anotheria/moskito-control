package org.moskito.control.connectors.response;

import java.util.LinkedList;
import java.util.List;

/**
 * Retrieved accumulators names container.
 *
 * @author Vladyslav Bezuhlyi
 */
public class ConnectorAccumulatorsNamesResponse extends ConnectorResponse {

    private final List<String> names;


    public ConnectorAccumulatorsNamesResponse(List<String> names) {
        this.names = names;
    }

    public ConnectorAccumulatorsNamesResponse() {
        this.names = new LinkedList<String>();
    }


    public List<String> getNames() {
        return names;
    }

}
