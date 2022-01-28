package com.topideal.order.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.topideal.common.tools.wrapper.DerpHttpServletRequestWrapper;

import java.io.IOException;

/**
 * 重复提交包装request过滤器
 * 
 * @author gy
 *
 */
@WebFilter(urlPatterns = "/webapi/*", filterName = "requestReplaced")
public class HttpServletRequestReplacedFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String contentType = request.getContentType();

		String type = httpRequest.getMethod();

		/**避免跨域时，content-type:application/json时会转换成 OPTIONS请求方式，试探服务端允许的get post提交*/
		if (type.toUpperCase().equals("OPTIONS") == true) {

			httpResponse.setHeader("Access-Control-Allow-Headers", "content-type, accept");
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET");
			httpResponse.setStatus(200);
			httpResponse.setContentType("text/plain;charset=utf-8");
			httpResponse.setCharacterEncoding("utf-8");

			chain.doFilter(request, response);

			return;
		}

		if (contentType == null 
				|| !contentType.contains("application/json")) {
			chain.doFilter(request, response);

			return;
		}

		ServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			requestWrapper = new DerpHttpServletRequestWrapper((HttpServletRequest) request);
		}
		if (requestWrapper == null) {
			chain.doFilter(request, response);
		} else {
			chain.doFilter(requestWrapper, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}
