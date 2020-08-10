package org.moskito.control.plugins.pagespeed.stats;

import net.anotheria.moskito.core.decorators.AbstractDecorator;
import net.anotheria.moskito.core.decorators.value.DoubleValueAO;
import net.anotheria.moskito.core.decorators.value.StatValueAO;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.stats.TimeUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.13 23:24
 */
public class PagespeedStatsDecorator extends AbstractDecorator {
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
			"Value returned by the pagespeed insights api"
	};

	/**
	 * Creates a new decorator object with given name.
	 */
	protected PagespeedStatsDecorator(){
		super("Pagespeed", CAPTIONS, SHORT_EXPLANATIONS, EXPLANATIONS);
	}

	@Override public List<StatValueAO> getValues(IStats statsObject, String interval, TimeUnit unit) {
		PagespeedStats stats = (PagespeedStats)statsObject;
		//this seems overcomplicated with i-variable, but it allows to add more later.
		List<StatValueAO> ret = new ArrayList<StatValueAO>(CAPTIONS.length);
		int i = 0;
		ret.add(new DoubleValueAO(CAPTIONS[i++], stats.getValue()));
		return ret;
	}
}