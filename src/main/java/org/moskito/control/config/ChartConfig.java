package org.moskito.control.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;

/**
 * Configuration object for a chart.
 *
 * @author lrosenberg
 * @since 18.06.13 13:50
 */
@ConfigureMe
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}, justification = "This is the way configureMe works, it provides beans for access")
public class ChartConfig {

	/**
	 * Name of the chart.
	 */
	@Configure
	private String name;

	/**
	 * Lines that are part of the chart.
	 */
	@Configure
	private ChartLineConfig[] lines;

	/**
	 * Max number of elements that should be used. Exceeding elements will be cut off.
	 */
	@Configure
	private int limit = -1;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChartLineConfig[] getLines() {
		return lines;
	}

	public void setLines(ChartLineConfig[] lines) {
		this.lines = lines;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override public String toString(){
		return getName()+" "+ Arrays.toString(lines)+" Limit: "+getLimit();
	}
}
