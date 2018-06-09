package org.moskito.control;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import net.anotheria.util.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 08.06.18 23:27
 */
public class JsonPathTest {
	//this test ensures that json path does what we need, so it is not quite a unit-test, but more of a contract test.

	@Test public void testPath() throws Exception{
		String content = IOUtils.readFileAtOnceAsString("src/test/resources/jsonpathtest.json");
		//System.out.println(content);

		ReadContext ctx = JsonPath.parse(content);

		//String yesterdayCount = ctx.read("$.results.payments[?(@.name=='yesterday')].all.count");
		Object yesterdayCount = ctx.read("$.results.payments[2].all.count");
		assertEquals("3", ""+yesterdayCount);
		//System.out.println(yesterdayCount+" -> "+yesterdayCount.getClass());
	}
}
