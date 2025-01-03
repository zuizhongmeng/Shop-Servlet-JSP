package com.shop.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.shop.dao.ProductDao;
import com.shop.pojo.Product;
import com.shop.utils.C3P0Utils;
import com.shop.utils.PageBean;

public class ProductService {
	
	ProductDao productDao = new ProductDao();
	/**
	 * 获得热门商品
	 * @return
	 */
	public List<Product> getHotProduct(){
		try {
			return productDao.getHotProduct();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获得最新商品
	 * @return
	 */
	public List<Product> getNewProduct(){
		try {
			return productDao.getNewProduct();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 通过id查询商品
	 * @param id
	 * @return
	 */
	public Product getProductById(String id) {
		try {
			return productDao.getProductById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据分类id查询商品列表
	 * @param cid
	 * @return
	 */
	public List<Product> getProductListByCid(String cid,PageBean pageBean){
		try {
			return productDao.getProductListByCid(cid,pageBean);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public int getTotal(String cid) {
		try {
			return productDao.getTotal(cid);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	
// 以下 新增管理员端方法
	
	/**
	 * 查询商品列表 
	 * @param product 参数条件
	 * @param pageBean 分页条件
	 * @return
	 */
	public List<Product> getProductList(Product product,PageBean pageBean){
		try {
			return productDao.selectProductList(product,pageBean);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 查询总数
	 * @param product 参数
	 * @return
	 */
	public int getTotal(Product product) {
		try {
			return productDao.selectTotal(product);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 新增
	 * @param product
	 * @return
	 */
	public int addProduct(Product product) {
		try {
			return productDao.insert(product);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 修改
	 * @param product
	 * @return
	 */
	public int updateProduct(Product product) {
		try {
			return productDao.update(product);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 删除
	 * @param pid
	 * @return
	 */
	public int delete(String pid) {
		try {
			return productDao.delete(pid);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	
	
}
