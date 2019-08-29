package org.moskito.control.ui;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter sends the user to the new url.
 *
 * @author lrosenberg
 * @since 21.02.18 09:42
 */
@WebFilter(urlPatterns = "/moskito-control/*")
public class OldVersionRedirectFilter implements Filter{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse)servletResponse;
		res.sendRedirect("/control/main");
	}

	@Override
	public void destroy() {

	}
}
