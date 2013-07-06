package org.moskito.control.ui.action;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;

import javax.servlet.http.HttpSession;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.07.13 00:07
 */
public class HttpServletRequestMocking implements Mocking{
	private HttpSession session = MockFactory.createMock(HttpSession.class, new HttpSessionMock());

	public HttpSession getSession(){
		return session;
	}
}
