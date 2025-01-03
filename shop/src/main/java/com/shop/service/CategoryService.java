package com.shop.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.shop.dao.CategoryDao;
import com.shop.pojo.Category;
import com.shop.utils.C3P0Utils;
import com.shop.utils.PageBean;

public class CategoryService {
	
	CategoryDao categoryDao = new CategoryDao();
	
	public List<Category> getCategoryList(){
		try {
			return categoryDao.getCategoryList();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询类别列表 条件分页
	 * @param category 条件
	 * @param pageBean 分页
	 * @return
	 */
	public List<Category> getCategoryList(Category category, PageBean pageBean) {
		try {
			return categoryDao.selectProductList(category,pageBean);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询总数
	 * @param category
	 * @return
	 */
	public int getTotal(Category category) {
		try {
			return categoryDao.selectTotal(category);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 新增
	 * @param category 参数
	 * @return
	 */
	public int addCategory(Category category) {
		try {
			return categoryDao.insert(category);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public int delete(String cid) {
		try {
			return categoryDao.deleteByCid(cid);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
		
	}

	/**
	 * 修改
	 * @param category
	 * @return
	 */
	public int update(Category category) {
		try {
			return categoryDao.update(category);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 查询详情
	 * @param cid
	 * @return
	 */
	public Category getCategoryById(String cid) {
		try {
			return categoryDao.selectCategoryById(cid);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
