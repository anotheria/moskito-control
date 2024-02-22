package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import org.junit.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for widget switching functionality of  BaseMoSKitoControlAction.
 *
 * @author lrosenberg
 * @since 06.07.13 23:18
 */
public class BaseMoSKitoControlActionTest {
	class TestAction extends BaseMoSKitoControlAction{
		@Override
		public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
			return null;
		}
	}

	@Test public void testChartWidgetSwitcher(){
		TestAction action = new TestAction();
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new org.moskito.control.ui.action.HttpServletRequestMocking());

		//on by default
		assertTrue(action.areChartsOn(req));

		action.setChartsOff(req);
		assertFalse(action.areChartsOn(req));

		action.setChartsOn(req);
		assertTrue(action.areChartsOn(req));


	}

	@Test public void testHistoryWidgetSwitcher(){
		TestAction action = new TestAction();
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new org.moskito.control.ui.action.HttpServletRequestMocking());

		//on by default
		assertTrue(action.isHistoryOn(req));

		action.setHistoryOff(req);
		assertFalse(action.isHistoryOn(req));

		action.setHistoryOn(req);
		assertTrue(action.isHistoryOn(req));


	}

	@Test public void testStatusWidgetSwitcher(){
		TestAction action = new TestAction();
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new org.moskito.control.ui.action.HttpServletRequestMocking());

		//on by default
		assertTrue(action.isStatusOn(req));

		action.setStatusOff(req);
		assertFalse(action.isStatusOn(req));

		action.setStatusOn(req);
		assertTrue(action.isStatusOn(req));
	}

	@Test public void testViewSelection(){
		TestAction action = new TestAction();
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new org.moskito.control.ui.action.HttpServletRequestMocking());

		action.setCurrentViewName(req, "FOO");
		assertEquals("FOO", action.getCurrentViewName(req));
		assertEquals("", action.getCurrentCategoryName(req));

		action.setCurrentCategoryName(req, "TEST-CAT");
		assertEquals("TEST-CAT", action.getCurrentCategoryName(req));

		action.setCurrentViewName(req, "BAR");
		assertEquals("BAR", action.getCurrentViewName(req));


	}

}
