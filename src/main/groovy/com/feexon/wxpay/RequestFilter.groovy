package com.feexon.wxpay

import javax.servlet.*

/**
 * @author Administrator
 * @version 1.0 2014/7/14,14:58
 */
class RequestFilter implements Filter{
    @Override
    void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        println("userAgent:${servletRequest.getHeader('user-agent')}")
        println("requestURL===>$servletRequest.requestURL")
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    void destroy() {

    }
}
