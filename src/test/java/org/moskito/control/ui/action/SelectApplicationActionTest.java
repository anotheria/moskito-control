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
public class SelectApplicationActionTest {
	@Test public void testSetApplication(){
		SelectApplicationAction a = new SelectApplicationAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new GetParameterMocking("view", "myapp"), new HttpServletRequestMocking());
		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")), null, request, null);
		assertEquals("myapp", a.getCurrentViewName(request));
	}
}
