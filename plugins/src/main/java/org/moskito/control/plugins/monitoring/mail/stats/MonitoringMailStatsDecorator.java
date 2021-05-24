package org.moskito.control.plugins.monitoring.mail.stats;

import net.anotheria.moskito.core.decorators.AbstractDecorator;
import net.anotheria.moskito.core.decorators.value.DoubleValueAO;
import net.anotheria.moskito.core.decorators.value.LongValueAO;
import net.anotheria.moskito.core.decorators.value.StatValueAO;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.stats.TimeUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ynikonchuk
 */
public class MonitoringMailStatsDecorator extends AbstractDecorator {
    /**
     * Captions.
     */
    static final String CAPTIONS[] = {
            "Value"
    };

    /**
     * Short explanations.
     */
    static final String SHORT_EXPLANATIONS[] = {
            "Last measured value"
    };

    /**
     * Explanations.
     */
    static final String EXPLANATIONS[] = {
            "Value returned by the monitoring insights api"
    };

    /**
     * Creates a new decorator object with given name.
     */
    protected MonitoringMailStatsDecorator() {
        super("Monitoring", CAPTIONS, SHORT_EXPLANATIONS, EXPLANATIONS);
    }

    @Override
    public List<StatValueAO> getValues(IStats statsObject, String interval, TimeUnit unit) {
        MonitoringMailStats stats = (MonitoringMailStats) statsObject;
        //this seems overcomplicated
        List<StatValueAO> ret = new ArrayList<StatValueAO>(CAPTIONS.length);
        int i = 0;
        ret.add(new LongValueAO(CAPTIONS[i++], stats.getValue()));
        return ret;
    }
}
