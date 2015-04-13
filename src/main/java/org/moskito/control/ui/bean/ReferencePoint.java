package org.moskito.control.ui.bean;

import net.anotheria.util.NumberUtils;

/**
 * This class is used as container for timestamp to create a reference line. A reference line is needed to align interval
 * graphs which are shifted in time but share the same distance. Example, if you have two charts with 5 min values, but
 * the second server was started two minutes after the first, the values will be shifted by 2 minutes.
 * For example first server would return values for:
 * 10:00, 10:05, 10:10, and second server would return 10:02, 10:07, 10:12. The resulting chart would contain values for
 * 10:00, 10:02, 10:05, 10:07, 10:10, 10:12 but only chart values for the half of the points.
 * To resolve this problem a reference line is built and second chart is aligned by the first chart.
 *
 *
 * @author lrosenberg
 * @since 01.08.13 22:34
 */
public class ReferencePoint {
	/**
	 * This reference points timestamp.
	 */
	private long timestamp;

	public ReferencePoint(long aTimestamp){
		timestamp = aTimestamp;
	}

	public boolean isInRange(long anotherTimestamp, long range){
		return Math.abs(timestamp-anotherTimestamp)<range;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override public String toString(){
		return getTimestamp()+" "+ NumberUtils.makeISO8601TimestampString(getTimestamp());
	}
}
