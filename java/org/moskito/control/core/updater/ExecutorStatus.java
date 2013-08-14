package org.moskito.control.core.updater;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.08.13 08:35
 */
public class ExecutorStatus {
	private long taskCount;
	private long activeCount;
	private long completedTaskCount;
	private long poolSize;

	public long getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(long taskCount) {
		this.taskCount = taskCount;
	}

	public long getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(long activeCount) {
		this.activeCount = activeCount;
	}

	public long getCompletedTaskCount() {
		return completedTaskCount;
	}

	public void setCompletedTaskCount(long completedTaskCount) {
		this.completedTaskCount = completedTaskCount;
	}

	public long getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(long poolSize) {
		this.poolSize = poolSize;
	}

	@Override public String toString(){
		return "TaskCount: "+getTaskCount()+", ActiveCount: "+getActiveCount()+", Completed: "+getCompletedTaskCount()+", Pool: "+getPoolSize();
	}
}
