package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
			return null;
		}
	}

	@Test public void testChartWidgetSwitcher(){
		TestAction action = new TestAction();
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new HttpServletRequestMocking());

		//on by default
		assertTrue(action.areChartsOn(req));

		action.setChartsOff(req);
		assertFalse(action.areChartsOn(req));

		action.setChartsOn(req);
		assertTrue(action.areChartsOn(req));


	}

	@Test public void testHistoryWidgetSwitcher(){
		TestAction action = new TestAction();
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new HttpServletRequestMocking());

		//on by default
		assertTrue(action.isHistoryOn(req));

		action.setHistoryOff(req);
		assertFalse(action.isHistoryOn(req));

		action.setHistoryOn(req);
		assertTrue(action.isHistoryOn(req));


	}

	@Test public void testStatusWidgetSwitcher(){
		TestAction action = new TestAction();
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new HttpServletRequestMocking());

		//on by default
		assertTrue(action.isStatusOn(req));

		action.setStatusOff(req);
		assertFalse(action.isStatusOn(req));

		action.setStatusOn(req);
		assertTrue(action.isStatusOn(req));
	}

	@Test public void testApplicationSelection(){
		TestAction action = new TestAction();
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new HttpServletRequestMocking());

		action.setCurrentApplicationName(req, "FOO");
		assertEquals("FOO", action.getCurrentApplicationName(req));
		assertEquals("", action.getCurrentCategoryName(req));

		action.setCurrentCategoryName(req, "TEST-CAT");
		assertEquals("TEST-CAT", action.getCurrentCategoryName(req));

		action.setCurrentApplicationName(req, "BAR");
		assertEquals("BAR", action.getCurrentApplicationName(req));


	}

}
