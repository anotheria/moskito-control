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
public class SwitchStatusActionTest {
	@Test public void testStatusOn(){
		SwitchStatusAction a = new SwitchStatusAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new GetParameterMocking("status", "on"), new HttpServletRequestMocking());
		assertTrue(a.isStatusOn(request));

		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")), request, null);
		assertTrue(a.isStatusOn(request));
	}

	@Test public void testStatusOff(){
		SwitchStatusAction a = new SwitchStatusAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new GetParameterMocking("status", "off"), new HttpServletRequestMocking());
		assertTrue(a.isStatusOn(request));

		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")),  request, null);
		assertFalse(a.isStatusOn(request));
	}
}
