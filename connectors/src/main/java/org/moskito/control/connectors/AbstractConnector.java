package org.moskito.control.connectors;

public abstract class AbstractConnector implements Connector{

    public boolean supportsInfo(){
        return false;
    }

    public boolean supportsThresholds(){
        return false;
    }

    public boolean supportsAccumulators(){
        return false;
    }

}
