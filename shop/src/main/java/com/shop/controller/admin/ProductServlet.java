package com.shop.controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.shop.pojo.Product;
import com.shop.service.ProductService;
import com.shop.utils.PageBean;
import com.shop.utils.RequestBodyParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
/**
 * 
 * 商品相关
 *
 */
public class ProductServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		case "/list": // 查询列表
			productList(request, response);
			break; 
		case "/add": // 新增
			addProduct(request, response);
			break;
		case "/upload": // 上传图片
			uploadImage(request, response);
			break;
		case "/detail": // 详情
			detail(request, response);
			break;
		case "/update": // 修改
			updateProduct(request, response);
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
	 * 删除
	 * @throws IOException 
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pid = request.getParameter("pid");
		ProductService productService = new ProductService();
		
		int result = productService.delete(pid);
		
		JSONObject o = new JSONObject();
		
		o.put("code", result);
		
		response.getWriter().write(o.toString());
	}
	
	/**
	 * 查询详情
	 * @throws IOException 
	 */
	private void detail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pid = request.getParameter("pid");
		ProductService productService = new ProductService();
		Product product = productService.getProductById(pid);
		
		JSONObject result = new JSONObject();
		result.put("data", product);
		
		response.getWriter().write(result.toString());
		
	}
	/**
	 * 查询
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void productList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ProductService productService = new ProductService();
		// 读取请求体中的JSON数据
		Product product = null;
		try {
			product = RequestBodyParser.parseJsonFromRequestBody(request, Product.class);
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

		// 查询商品列表
		List<Product> productList = productService.getProductList(product, pageBean);

		// 查询总数
		int total = productService.getTotal(product);

		

		JSONObject object = new JSONObject();
		object.put("list", productList);
		object.put("total", total);
		// 封装后端地址
		// 获取请求的上下文路径
		String contextPath = request.getContextPath();

		// 获取请求的协议、服务器名称、端口号和上下文路径，构建目标URL
		String scheme = request.getScheme(); // http
		String serverName = request.getServerName(); // localhost
		int serverPort = request.getServerPort(); // 8080
		String baseURL = scheme + "://" + serverName + ":" + serverPort + contextPath + "/";

		object.put("baseURL", baseURL);

		response.getWriter().write(object.toString());

	}

	/**
	 * 新增
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 读取请求体中的JSON数据
		Product product = null;
		try {
			product = RequestBodyParser.parseJsonFromRequestBody(request, Product.class);
			// id
			product.setPid(UUID.randomUUID().toString()); 
			
			// 日期
			product.setPdate(new Date());
			
			// 创建时间
			product.setCreatetime(new Timestamp(System.currentTimeMillis()));
			
		} catch (Exception e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ProductService productService = new ProductService();
		
		
		int result = productService.addProduct(product);
		
		
		JSONObject object = new JSONObject();
		
		object.put("code", result);
		
		response.getWriter().write(object.toString());

	}
	
	
	/**
	 * 修改
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 读取请求体中的JSON数据
		Product product = null;
		try {
			product = RequestBodyParser.parseJsonFromRequestBody(request, Product.class);
			
			
		} catch (Exception e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ProductService productService = new ProductService();
		
		
		int result = productService.updateProduct(product);
		
		
		JSONObject object = new JSONObject();
		
		object.put("code", result);
		
		response.getWriter().write(object.toString());

	}
	
	/**
	 * 上传图片
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void uploadImage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 从web.xml中获取location路径
        String uploadDir = getServletContext().getInitParameter("uploadLocation");   
        // 创建保存图片的文件夹
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdir();
        }
        String imageUrl = "";
        for (Part part : request.getParts()) {
           
            
        	// 原始文件名
            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            // 扩展名
            String fileExtension = getFileExtension(fileName);
            // 随机生成新的文件名
            String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
            // 前端图片返回地址
//            imageUrl ="/images/"+newFileName;
            imageUrl ="/shop/products/1/"+newFileName;
            part.write(uploadDir + File.separator + newFileName);
        }
        
        // 获取协议和主机
        String scheme = request.getScheme(); // http
        String serverName = request.getServerName(); // localhost
        int serverPort = request.getServerPort(); // 8080

        // 组合成所需的URL
        String baseUrl = scheme + "://" + serverName + ":" + serverPort;
        
        // 返回结果 返回图片地址
        JSONObject result = new JSONObject();
        result.put("success",true);
        result.put("imageUrl", baseUrl+imageUrl);
        

        response.getWriter().println(result.toString());
		
	}
	
	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
	

}
