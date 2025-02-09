package com.shop.controller;

import java.io.IOException;

import com.shop.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class ActiveServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService service = new UserService();
		request.getSession().removeAttribute("error");//删除session，用户在未激活登录时留下的session
		String code = request.getParameter("code");//获取激活码
		
		service.activeUser(code);//激活用户
		
		response.setContentType("text/html;charset=utf-8"); 
		request.getSession().setAttribute("activeMessage", "激活成功！！！");
		response.sendRedirect("login.jsp"); 
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
