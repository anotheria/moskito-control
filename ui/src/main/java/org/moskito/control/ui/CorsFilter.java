package org.moskito.control.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);

    /**
     * Slash after protocol.
     * for example https://
     */
    private static final String PROTOCOL_SLASH = "//";
    private static final char SLASH = '/';

    private static final String DEFAULT_ALLOW_ORIGIN = "*";

    private static final String OPTIONS_METHOD = "OPTIONS";
    private static final String REFERER_HEADER = "Referer";
    private static final String ORIGIN_HEADER = "Origin";

    private static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS_HEADER = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_ALLOW_METHODS_HEADER = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER = "Access-Control-Allow-Credentials";
    private static final String ACCESS_CONTROL_EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";

    private static String accessControlAllowHeaders = "";
    private static String accessControlAllowMethods = "";
    private static String accessControlAllowCredentials = "";
    private static String accessControlExposeHeaders = "";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        accessControlAllowHeaders = "origin, content-type, content-disposition, accept, authorization, x-requested-with, UTC-timezone, journeyname";
        accessControlAllowHeaders = String.join(", ", accessControlAllowHeaders, "token");

        accessControlAllowMethods = "GET, POST, PUT, DELETE, OPTIONS";
        accessControlAllowCredentials = "true";

        accessControlExposeHeaders = "cache-control, content-language, content-length, content-type, expires, last-modified, pragma, content-disposition";
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest))
            return;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String allowOrigin = DEFAULT_ALLOW_ORIGIN;
        if (log.isDebugEnabled()) {
            log.debug("cors. allow origin: " + allowOrigin);
        }

        response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, allowOrigin);
        response.addHeader(ACCESS_CONTROL_ALLOW_HEADERS_HEADER, accessControlAllowHeaders);
        response.addHeader(ACCESS_CONTROL_ALLOW_METHODS_HEADER, accessControlAllowMethods);
        response.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, accessControlAllowCredentials);
        response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, accessControlExposeHeaders);


        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (req.getMethod().equals(OPTIONS_METHOD)) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }


}
