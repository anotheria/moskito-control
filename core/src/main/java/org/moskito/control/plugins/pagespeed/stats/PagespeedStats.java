package org.moskito.control.plugins.pagespeed.stats;

import net.anotheria.moskito.core.decorators.DecoratorRegistryFactory;
import net.anotheria.moskito.core.predefined.Constants;
import net.anotheria.moskito.core.producers.AbstractStats;
import net.anotheria.moskito.core.stats.StatValue;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.core.stats.impl.StatValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.13 13:01
 */
public class PagespeedStats extends AbstractStats {

	private static Logger log = LoggerFactory.getLogger(PagespeedStats.class);

	public static enum StatDef {
		VALUE("Value");

		private String statName;

		private StatDef(final String aStatName) {
			statName = aStatName;
		}

		public String getStatName() {
			return statName;
		}

		public static List<String> getStatNames() {
			List<String> ret = new ArrayList<String>(StatDef.values().length);
			for (StatDef value : StatDef.values()) {
				ret.add(value.getStatName());
			}
			return ret;
		}

		public static StatDef getValueByName(String statName) {
			for (StatDef value : StatDef.values()) {
				if (value.getStatName().equals(statName)) {
					return value;
				}
			}
			throw new IllegalArgumentException("No such value with name: " + statName);
		}
	}

	static{
		DecoratorRegistryFactory.getDecoratorRegistry().addDecorator(PagespeedStats.class, new PagespeedStatsDecorator());
	}

	/**
	 * The value of the value (nice naming, isn't). Basically hold the last measured value.
	 */
	private StatValue value;

	public PagespeedStats(String name) {
		super(name);

		value = StatValueFactory.createStatValue(Double.valueOf(0), StatDef.VALUE.getStatName(), Constants.getDefaultIntervals());
	}

	public void setValue(String valueAsString){
		if (valueAsString == null || valueAsString.length()==0)
			return;
		try {
			value.setValueAsDouble(Double.parseDouble(valueAsString));
		}catch(Exception e){
			log.debug("Silently ignore non-parseable value \""+valueAsString+"\"", e);
		}
	}

	@Override
	public String toStatsString(String s, TimeUnit timeUnit) {
		return null;
	}

	@Override
	public String getValueByNameAsString(String valueName, String intervalName, TimeUnit timeUnit) {
		StatDef statDef = StatDef.getValueByName(valueName);
		switch (statDef) {
			case VALUE:
				return value.getValueAsString(intervalName);
			default:
				return super.getValueByNameAsString(valueName, intervalName, timeUnit);
		}
	}

	@Override
	public List<String> getAvailableValueNames() {
		return StatDef.getStatNames();
	}

	public double getValue(){
		return value.getValueAsDouble();
	}

}