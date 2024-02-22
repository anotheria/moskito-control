package org.moskito.control.ui;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
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
