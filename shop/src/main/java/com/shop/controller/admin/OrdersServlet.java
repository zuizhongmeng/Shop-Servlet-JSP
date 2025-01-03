package com.shop.controller.admin;

import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.shop.pojo.Order;
import com.shop.service.OrderService;
import com.shop.utils.PageBean;
import com.shop.utils.RequestBodyParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * 后台管理 订单相关模块
 *
 */

public class OrdersServlet extends HttpServlet{
 
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 返回响应
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String path = request.getPathInfo();
		switch (path) {
			case "/orderList": // 查询订单列表 分页 条件
				ordersList(request,response);
				break;
			case "/delete": // 删除
				delete(request,response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				break;
		}
		
		
		
	}
	
	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String oid = request.getParameter("oid");
		OrderService orderService = new OrderService();
		
		int result = orderService.delete(oid);
		
		JSONObject o = new JSONObject();
		
		o.put("code", result);
		
		response.getWriter().write(o.toString());
		
	}

	

	/**
	 * 订单列表
	 * @param request
	 * @param response
	 * @throws IOException 

	 */
	private void ordersList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		OrderService orderService = new OrderService();
		// 读取请求体中的JSON数据
		Order order = null;
		try {
			order = RequestBodyParser.parseJsonFromRequestBody(request, Order.class);
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
		
		// 查询订单列表
		List<Order> orderList = orderService.getOrderList(order,pageBean);

		// 查询总数
		int total = orderService.getTotal(order);

		JSONObject result = new JSONObject();
		result.put("list", orderList);
		result.put("total", total);
		
		
		response.getWriter().write(result.toString());
		
	}
	
	
	
	
	
	
	

}
