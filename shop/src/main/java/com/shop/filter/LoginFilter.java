package com.shop.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// 获取当前请求的路径
		String requestURI = request.getRequestURI();

		// 排除不需要过滤的路径（比如登录页面、注册页面、静态资源等）
		if (requestURI.endsWith("login.html") || requestURI.endsWith("admin/login") || requestURI.contains("/css/")
				|| requestURI.contains("/js/") || requestURI.contains("/images/") || requestURI.contains("/layui/")) {
			chain.doFilter(request, response); // 放行
			return;
		}

		// 其他请求需要验证登录状态 如果 session 不存在则返回 null
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginAdmin") == null) {
			// 用户未登录，重定向到登录页面
			// response.sendRedirect(request.getContextPath() + "/admin/login.html");

			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().println("<script type=\"text/javascript\">");
			response.getWriter().println("top.location.href = '" + request.getContextPath() + "/admin/login.html';");
			response.getWriter().println("</script>");

		} else {
			// 用户已登录，继续请求
			chain.doFilter(request, response);
		}

	}

}
