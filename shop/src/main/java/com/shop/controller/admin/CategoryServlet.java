package com.shop.controller.admin;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.shop.pojo.Category;
import com.shop.service.CategoryService;
import com.shop.utils.PageBean;
import com.shop.utils.RequestBodyParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * 商品类别相关
 *
 */
public class CategoryServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		// 返回响应
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String path = request.getPathInfo();
		switch (path) {
		case "/list": // 查询所有类别
			getAllList(request,response);
			break;
		case "/categoryList": // 查询类别列表 分页 条件
			categoryList(request,response);
			break;
		case "/update": // 修改
			update(request,response);
			break;
		case "/delete": // 删除
			delete(request,response);
			break;
		case "/add": // 新增
			add(request,response);
			break;
		case "/detail": // 详情
			detail(request,response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
		 
	}

	
	



	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		doGet(request, response); 
	}
	
	/**
	 * 查询详情
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void detail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cid = request.getParameter("cid");
		CategoryService categoryService = new CategoryService();
		Category category = categoryService.getCategoryById(cid);
		
		JSONObject result = new JSONObject();
		result.put("data", category);
		
		response.getWriter().write(result.toString());
		
	}
	
	/**
	 * 查询所有类别
	 */
	private void getAllList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CategoryService categoryService = new CategoryService();
		List<Category> list  = categoryService.getCategoryList();
		
		JSONObject result = new JSONObject();
		
		result.put("list",list);
		
		response.getWriter().write(result.toString());
		
	}
	
	/**
	 * 查询 分页 条件
	 * @throws IOException 
	 */
	private void categoryList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CategoryService categoryService = new CategoryService();
		
		// 读取请求体中的JSON数据
		Category category = null;
		try {
			category = RequestBodyParser.parseJsonFromRequestBody(request, Category.class);
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
		List<Category> categoryList = categoryService.getCategoryList(category,pageBean);
		
		// 查询总数
		int total = categoryService.getTotal(category);
		
		// 封装返回结果
		JSONObject result = new JSONObject();
		result.put("list", categoryList);
		result.put("total", total);
		
		response.getWriter().write(result.toString());
	}
	
	/**
	 * 新增
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CategoryService categoryService = new CategoryService();
		// 读取请求体中的JSON数据
		Category category = null;
		try {
			category = RequestBodyParser.parseJsonFromRequestBody(request, Category.class);
			
			// 设置主键
			category.setCid(UUID.randomUUID().toString());
			
			// 创建时间
			category.setCreatetime(new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 执行结果 1成功 0失败
		int result = categoryService.addCategory(category);
		
		// 返回结果
		JSONObject object = new JSONObject();
		
		object.put("code", result);
		
		response.getWriter().write(object.toString());
	}

	/**
	 * 删除
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CategoryService categoryService = new CategoryService();
		String cid = request.getParameter("cid");
		
		// 执行结果 1成功 0失败
		int result = categoryService.delete(cid);
		// 返回结果
		JSONObject object = new JSONObject();
		
		object.put("code", result);
		
		response.getWriter().write(object.toString());
	}

	/**
	 * 修改
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CategoryService categoryService = new CategoryService();
		// 读取请求体中的JSON数据
		Category category = null;
		try {
			category = RequestBodyParser.parseJsonFromRequestBody(request, Category.class);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 执行结果 1成功 0失败
		int result = categoryService.update(category);
		
		// 返回结果
		JSONObject object = new JSONObject();
		
		object.put("code", result);
		
		response.getWriter().write(object.toString());
		
	}
	
	
	

}
