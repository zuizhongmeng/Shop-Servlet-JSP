package com.shop.controller;

import java.io.IOException;
import java.util.List;

import com.shop.pojo.Product;
import com.shop.service.SearchService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String searchword = request.getParameter("searchword");
		SearchService service = new SearchService();
		List<Product> productList = service.search(searchword);
		request.setAttribute("productList", productList);
		request.getRequestDispatcher("searchList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
