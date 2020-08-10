package org.moskito.control.plugins.pagespeed;

import org.junit.Test;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.08.20 17:31
 */
public class PagespeedTaskTest {
	@Test public void test() throws Exception{
		PagespeedPluginConfig config = PagespeedPluginConfig.getByName("test2-pagespeedconfig");
		PagespeedPluginTargetConfig target = config.getTargets()[0];

		PagespeedTask task = new PagespeedTask(config.getApiKey(), target);
		task.execute();
	}
}
