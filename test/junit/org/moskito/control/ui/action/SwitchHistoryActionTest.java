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
public class SwitchHistoryActionTest {
	@Test public void testHistoryOn(){
		SwitchHistoryAction a = new SwitchHistoryAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new GetParameterMocking("history", "on"), new HttpServletRequestMocking());
		assertTrue(a.isHistoryOn(request));

		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")), null, request, null);
		assertTrue(a.isHistoryOn(request));
	}

	@Test public void testHistoryOff(){
		SwitchHistoryAction a = new SwitchHistoryAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new GetParameterMocking("history", "off"), new HttpServletRequestMocking());
		assertTrue(a.isHistoryOn(request));

		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")), null, request, null);
		assertFalse(a.isHistoryOn(request));
	}
}
