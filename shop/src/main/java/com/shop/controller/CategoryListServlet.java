package com.shop.controller;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.shop.pojo.Category;
import com.shop.service.CategoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CategoryListServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryService service = new CategoryService();
		List<Category> categoryList = service.getCategoryList();
		Gson gson = new Gson();
		String categoryJson = gson.toJson(categoryList);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryJson);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
