package org.moskito.control.ui.resource.accumulators;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Represents JSON parameters for accumulator charts request.
 * @author strel
 */
@XmlRootElement
public class AccumulatorChartsParameters {

    /**
     * Application name.
     */
    @XmlElement
    private String application;

    /**
     * Application component name.
     */
    @XmlElement
    private String component;

    /**
     * List of component accumulator names.
     */
    @XmlElement
    private ArrayList<String> accumulators;


    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public ArrayList<String> getAccumulators() {
        return accumulators;
    }

    public void setAccumulators(ArrayList<String> accumulators) {
        this.accumulators = accumulators;
    }


    @Override
    public String toString() {
        return "AccumulatorChartsParameters{" +
                "application='" + application + '\'' +
                ", component='" + component + '\'' +
                ", accumulators=" + accumulators +
                '}';
    }
}
