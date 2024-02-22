package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.action.CommandRedirect;
import org.junit.Test;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.07.13 00:00
 */
public class SelectCategoryActionTest {
	@Test public void testSetCategory(){
		SelectCategoryAction a = new SelectCategoryAction();
		HttpServletRequest request = MockFactory.createMock(HttpServletRequest.class, new GetParameterMocking("category", "mycat"), new HttpServletRequestMocking());
		assertEquals("", a.getCurrentCategoryName(request));

		a.execute(new ActionMapping(null, null, new CommandRedirect("dummy", "dummy")), request, null);
		assertEquals("mycat", a.getCurrentCategoryName(request));
	}
}
