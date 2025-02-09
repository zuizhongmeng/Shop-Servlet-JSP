package com.shop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.shop.pojo.Product;
import com.shop.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IndexServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		//获得热门商品
		List<Product> hotProductList = service.getHotProduct();
		//获得最新商品
		List<Product> newProductList = service.getNewProduct();
		
		//历史记录
		List<Product> hisProductList = new ArrayList<Product>();
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
			for (Cookie cookie : cookies) {
				if("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for(String pid:split) {
						Product product = service.getProductById(pid);
						hisProductList.add(product);
					}
				}
			}
		} 
		
		request.setAttribute("hotProductList", hotProductList);
		request.setAttribute("newProductList", newProductList);
		request.setAttribute("hisProductList", hisProductList);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
