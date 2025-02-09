package com.shop.controller;

import java.io.IOException;
import java.util.List;

import com.shop.pojo.Product;
import com.shop.service.ProductService;
import com.shop.utils.PageBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductListByCidServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		//获取当前页码
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr == null) {
			currentPageStr = "1";
		}
		int currentPage = Integer.parseInt(currentPageStr);
		//获取分类id  cid
		String cid = request.getParameter("cid");
		//获取该类别下的总数
		int total = service.getTotal(cid);
		//封装分页
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(currentPage);
		pageBean.setRows(12);//每页显示12个商品
		pageBean.setTotal(total);
		//调用服务
		List<Product> productList = service.getProductListByCid(cid, pageBean);
		request.setAttribute("productList", productList);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);
		request.setAttribute("pages",pageBean.getPages());
		request.getRequestDispatcher("productList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
