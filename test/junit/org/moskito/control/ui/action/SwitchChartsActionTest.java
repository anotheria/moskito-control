package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.action.CommandRedirect;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.07.13 00:00
 */
public class SwitchChartsActionTest {
	@Test public void testChartsOn(){
		SwitchChartsAction a = new SwitchChartsAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new GetParameterMocking("charts", "on"), new HttpServletRequestMocking());
		assertTrue(a.areChartsOn(request));

		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")), null, request, null);
		assertTrue(a.areChartsOn(request));
	}

	@Test public void testChartsOff(){
		SwitchChartsAction a = new SwitchChartsAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new GetParameterMocking("charts", "off"), new HttpServletRequestMocking());
		assertTrue(a.areChartsOn(request));

		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")), null, request, null);
		assertFalse(a.areChartsOn(request));
	}
}
