package com.shop.controller.admin;

import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.shop.pojo.User;
import com.shop.service.UserService;
import com.shop.utils.PageBean;
import com.shop.utils.RequestBodyParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		// 返回响应
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String path = request.getPathInfo();
		switch (path) {
		case "/userList": // 查询用户列表
			userList(request, response);
			break;
		case "/delete": // 删除
			delete(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}

	}
	
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserService userService = new UserService();
		String uid = request.getParameter("uid");
		
		// 执行结果 1成功 0失败
		int result = userService.delete(uid);
		// 返回结果
		JSONObject object = new JSONObject();
		
		object.put("code", result);
		
		response.getWriter().write(object.toString());
		
	}

	/**
	 * 查询用户列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void userList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserService userService = new UserService();
		
		// 读取请求体中的JSON数据
		User user = null;
		try {
			user = RequestBodyParser.parseJsonFromRequestBody(request, User.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 分页
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(Integer.parseInt(page));
		pageBean.setRows(Integer.parseInt(size));
		
		
		// 查询类别列表
		List<User> userList = userService.getUserList(user,pageBean);
		
		// 查询总数
		int total = userService.getTotal(user);
		
		// 封装返回结果
		JSONObject result = new JSONObject();
		result.put("list", userList);
		result.put("total", total);
		
		response.getWriter().write(result.toString());
		
	}

}
