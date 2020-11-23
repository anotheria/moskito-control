package org.moskito.control.data.retrievers;

/**
 * Internal class used to map a value to variable in the moskito retriever.
 *
 * @author lrosenberg
 * @since 04.06.18 16:50
 */
class MoSKitoValueMapping {
	/**
	 * Name of the producer.
	 */
	private String producerName;
	private String statName;
	private String valueName;
	private String intervalName;
	private String timeUnitName;
	private String targetVariableName;

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public String getStatName() {
		return statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public String getIntervalName() {
		return intervalName;
	}

	public void setIntervalName(String intervalName) {
		this.intervalName = intervalName;
	}

	public String getTimeUnitName() {
		return timeUnitName;
	}

	public void setTimeUnitName(String timeUnitName) {
		this.timeUnitName = timeUnitName;
	}

	public String getTargetVariableName() {
		return targetVariableName;
	}

	public void setTargetVariableName(String targetVariableName) {
		this.targetVariableName = targetVariableName;
	}

	public String getMoskitoId(){
		return getMoskitoId(producerName, statName, valueName, intervalName, timeUnitName);
	}

	public static String getMoskitoId(String producerName, String statName, String valueName, String intervalName, String timeUnitName){
		return new StringBuilder(producerName).append('.').
				append(statName).append('.').
				append(valueName).append('.').
				append(intervalName).append('.')
				.append(timeUnitName).toString();
	}

	@Override
	public String toString() {
		return getMoskitoId()+" -> "+targetVariableName;
	}
}
