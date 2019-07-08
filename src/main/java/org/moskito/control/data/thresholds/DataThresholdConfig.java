package org.moskito.control.data.thresholds;

import net.anotheria.util.StringUtils;
import org.moskito.control.core.HealthColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * A configuration class for a single threshold.
 */

public class DataThresholdConfig {
	/**
	 * Name of the guarded variable.
	 */
	private String guardedVariableName;

	/**
	 * Configuration guards list.
	 */
	private List<GuardConfig> guards = new LinkedList<>();

	/**
	 * Optional name.
	 */
	private String targetVariableName;

	private static Logger log = LoggerFactory.getLogger(DataThresholdConfig.class);

	public String getGuardedVariableName() {
		return guardedVariableName;
	}

	public void setGuardedVariableName(String guardedVariableName) {
		this.guardedVariableName = guardedVariableName;
	}

	public List<GuardConfig> getGuards() {
		return guards;
	}

	public void setGuards(List<GuardConfig> guards) {
		this.guards = guards;
	}

	public String getTargetVariableName() {
		return targetVariableName;
	}

	public void setTargetVariableName(String targetVariableName) {
		this.targetVariableName = targetVariableName;
	}

	public void configure(String guardedVariableName, List<String> thresholdConfigs) {

		if (guardedVariableName == null || guardedVariableName.isEmpty()) {
			log.error("value name must not be null or empty!");
			return;
		}

		this.guardedVariableName = guardedVariableName;

		for (String config : thresholdConfigs) {
			String tokens[] = StringUtils.tokenize(config, ' ');
//            "<guardedVariableName> <direction> <value> <color> <targetVariableName>"
			try {
				targetVariableName = (tokens.length > 4) ? tokens[4] : ((targetVariableName == null || targetVariableName.isEmpty()) ? guardedVariableName + ".THRESHOLD" : targetVariableName);

				GuardConfig guardConfig = new GuardConfig();
				guardConfig.setDirection(GuardDirection.forName(tokens[1]));
				guardConfig.setColor(HealthColor.forName(tokens[3].toLowerCase()));
				guardConfig.setValue(Double.valueOf(tokens[2]).toString()); //cast check
				this.guards.add(guardConfig);
			} catch (Exception e) {
				log.error("Wrong DataThreshold setting line: " + config);
			}
		}
	}

	@Override
	public String toString() {
		return "guardedVariableName: " + getGuardedVariableName() + ", targetVariableName: " + getTargetVariableName() + ", guards: " + getGuards();
	}
}
