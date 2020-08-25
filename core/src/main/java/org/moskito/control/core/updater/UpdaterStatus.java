package org.moskito.control.core.updater;

/**
 * This class represents the current status of an updater.
 *
 * @author lrosenberg
 * @since 14.08.13 08:31
 */
public class UpdaterStatus {
	/**
	 * True if the update is triggered right now. However, this represents the update triggerer not the update process
	 * that follows the trigger.
	 */
	private boolean updateInProgress;
	/**
	 * Status of the updater thread pool.
	 */
	private ExecutorStatus updaterStatus;
	/**
	 * Status of the connector thread pool.
	 */
	private ExecutorStatus connectorStatus;

	public boolean isUpdateInProgress() {
		return updateInProgress;
	}

	public void setUpdateInProgress(boolean updateInProgress) {
		this.updateInProgress = updateInProgress;
	}

	public ExecutorStatus getUpdaterStatus() {
		return updaterStatus;
	}

	public void setUpdaterStatus(ExecutorStatus updaterStatus) {
		this.updaterStatus = updaterStatus;
	}

	public ExecutorStatus getConnectorStatus() {
		return connectorStatus;
	}

	public void setConnectorStatus(ExecutorStatus connectorStatus) {
		this.connectorStatus = connectorStatus;
	}

	@Override public String toString(){
		return "UiP: "+updateInProgress+" updaterStatus: "+getUpdaterStatus()+", connectorStatus: "+getConnectorStatus();
	}
}
