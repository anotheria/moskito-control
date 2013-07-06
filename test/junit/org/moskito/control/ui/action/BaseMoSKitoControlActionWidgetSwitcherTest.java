package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for widget switching functionality of  BaseMoSKitoControlAction.
 *
 * @author lrosenberg
 * @since 06.07.13 23:18
 */
public class BaseMoSKitoControlActionWidgetSwitcherTest {
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

	public static class HttpServletRequestMocking implements Mocking{
		private HttpSession session = MockFactory.createMock(HttpSession.class, new HttpSessionMock());

		public HttpSession getSession(){
			return session;
		}
	}

	public static class HttpSessionMock implements Mocking{

		private Map<String,Object> map = new HashMap<String,Object>();

		public Object getAttribute(String name){
			return map.get(name);
		}

		public void setAttribute(String name, Object o){
			map.put(name, o);
		}

	}
}
