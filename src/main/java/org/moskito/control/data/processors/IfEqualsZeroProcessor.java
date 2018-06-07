package org.moskito.control.data.processors;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.06.18 18:18
 */
public class IfEqualsZeroProcessor extends IfComparisonProcessor{
	@Override
	boolean conditionFullfilled(double value) {
		return value == 0;
	}
}
