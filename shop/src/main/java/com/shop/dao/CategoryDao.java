package com.shop.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shop.pojo.Category;
import com.shop.utils.C3P0Utils;
import com.shop.utils.PageBean;

public class CategoryDao {
	
	private static final Logger logger = LogManager.getLogger(ProductDao.class);
	
	/**
	 * 查询分类列表
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> getCategoryList() throws SQLException{
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category order by createtime desc";
		logger.debug("Executing SQL: " + sql);
		List<Category> list = queryRunner.query(sql,new BeanListHandler<Category>(Category.class));
		logger.debug("SQL Execution Completed: " + list.size() + " records fetched.");
		return list;
	}
	
	/**
	 * 查询分类列表 条件 分页
	 * @param category 条件
	 * @param pageBean 分页
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> selectProductList(Category category, PageBean pageBean) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category where 1=1";
		logger.debug("Executing SQL: " + sql);
		
		// 分类名称 条件
		String cname = category.getCname();
		if(cname != null && !"".equals(cname)) {
			sql += " and cname like '%"+cname+"%'";
		}
		
		// 分页
		sql += " order by createtime desc limit ?,?";
		
		List<Category> list = queryRunner.query(sql,new BeanListHandler<Category>(Category.class),pageBean.getStart(),pageBean.getRows());
		logger.debug("SQL Execution Completed: " + list.size() + " records fetched.");
				
		
		return list;
	}
	
	/**
	 * 查询总数
	 * @param category
	 * @return
	 * @throws SQLException 
	 */
	public int selectTotal(Category category) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category where 1=1";
		logger.debug("Executing SQL: " + sql);
		
		// 分类名称 条件
		String cname = category.getCname();
		if(cname != null && !"".equals(cname)) {
			sql += " and cname like '"+cname+"'";
		}
		
		
		List<Category> list = queryRunner.query(sql,new BeanListHandler<Category>(Category.class));
		logger.debug("SQL Execution Completed: " + list.size() + " records fetched.");
				
		
		return list.size();
	}

	/**
	 * 新增
	 * @param category
	 * @return
	 * @throws SQLException 
	 */
	public int insert(Category category) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into category values(?,?,?)";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql,category.getCid(),category.getCname(),category.getCreatetime());
		logger.debug("SQL Execution Completed: " + result);
		return result;
	}

	/**
	 * 根据id删除
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public int deleteByCid(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "delete from category where cid='"+cid+"'";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql);
		logger.debug("SQL Execution Completed: " + result);
		
		return result;
	}

	/**
	 * 修改
	 * @param category
	 * @return
	 * @throws SQLException 
	 */
	public int update(Category category) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update category set cname = ? where cid = ?";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql,category.getCname(),category.getCid());
		logger.debug("SQL Execution Completed: " + result);
		
		return result;
	}
	
	/**
	 * 查询详情
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public Category selectCategoryById(String cid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category where cid = ?"; 
		logger.debug("Executing SQL: " + sql);
		Category category = queryRunner.query(sql,new BeanHandler<Category>(Category.class),cid);
		logger.debug("SQL Execution Completed: " + category.toString());
		return category;
	}
}
