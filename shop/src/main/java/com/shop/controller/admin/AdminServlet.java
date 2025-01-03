package com.shop.controller.admin;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.alibaba.fastjson.JSONObject;
import com.shop.pojo.Admin;
import com.shop.utils.C3P0Utils;
import com.shop.utils.RequestBodyParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 管理员 登录相关 因为只有一个登录方法，为了简便，就没有使用service，dao等模块，直接在这里面写登录方法
 *
 */
public class AdminServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
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
		case "/login": // 登录
			login(request, response);
			break;
		case "/logout": // 退出登录
			logout(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 删除session
		request.getSession().invalidate();
		
		JSONObject result = new JSONObject();
		result.put("success",true);
		response.getWriter().write(result.toString());
	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 返回结果
		JSONObject result = new JSONObject();

		// 读取请求体中的JSON数据
		Admin a = null;
		try {
			a = RequestBodyParser.parseJsonFromRequestBody(request, Admin.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "未知数据，非法操作");
			response.getWriter().write(result.toString());
			return;
		}

		// 根据账号和密码查询数据
		String sql = "select * from admin where username=? and password=?";
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		Admin admin = null;
		try {
			admin = runner.query(sql, new BeanHandler<Admin>(Admin.class), a.getUsername(), a.getPassword());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("success", false);
			result.put("message", e.getMessage());
			response.getWriter().write(result.toString());
			return;
		}

		// 未查询到 说明用户名或者密码错误
		if (admin == null) {
			result.put("success", false);
			result.put("message", "用户名或者密码错误");
			response.getWriter().write(result.toString());
			return;
		}

		// 登录成功 设置session
		admin.setPassword(""); // 把密码设置为空 一般不透露
		request.getSession().setAttribute("loginAdmin", admin);
		result.put("success", true);
		result.put("data", admin);
		response.getWriter().write(result.toString());

	}

}
