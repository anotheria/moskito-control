package org.moskito.control.core.updater;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.08.13 08:31
 */
public class UpdaterStatus {
	private boolean updateInProgress;
	private ExecutorStatus updaterStatus;
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
