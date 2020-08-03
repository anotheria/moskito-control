package org.moskito.controlagent.data.info;

/**
 * Abstraction that allows to replace UptimeProvider for testing.
 *
 * @author lrosenberg
 * @since 24.07.17 22:33
 */
public interface UptimeProvider {
	long getUptime();
}
