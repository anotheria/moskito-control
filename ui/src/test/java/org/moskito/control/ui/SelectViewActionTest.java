package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.action.CommandRedirect;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.07.13 00:00
 */
public class SelectViewActionTest {
	@Test public void testSetView(){
		SelectViewAction a = new SelectViewAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new org.moskito.control.ui.action.GetParameterMocking("view", "myview"), new org.moskito.control.ui.action.HttpServletRequestMocking());
		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")), null, request, null);
		assertEquals("myview", a.getCurrentViewName(request));
	}
}
