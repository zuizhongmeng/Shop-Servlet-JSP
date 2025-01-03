package com.shop.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shop.pojo.Product;
import com.shop.utils.C3P0Utils;
import com.shop.utils.PageBean;

public class ProductDao {
	
	private static final Logger logger = LogManager.getLogger(ProductDao.class);
	
	/**
	 * 获取热门商品
	 * @return
	 * @throws SQLException 
	 */
	public List<Product> getHotProduct() throws SQLException{
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where is_hot = ? limit ?,?";
		logger.debug("Executing SQL: " + sql);
		List<Product> productList = queryRunner.query(sql,new BeanListHandler<Product>(Product.class),1,1,3);
		logger.debug("SQL Execution Completed: " + productList.size() + " records fetched.");
		return productList;
	}
	/**
	 * 获得最新商品
	 * @return
	 * @throws SQLException
	 */
	public List<Product> getNewProduct() throws SQLException{
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,?"; 
		logger.debug("Executing SQL: " + sql);
		List<Product> productList = queryRunner.query(sql,new BeanListHandler<Product>(Product.class),1,12);
		logger.debug("SQL Execution Completed: " + productList.size() + " records fetched.");
		return productList;
	}
	/**
	 * 通过商品id查询商品详细信息
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public Product getProductById(String id) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select a.*,b.cname from product a,category b where a.cid = b.cid and a.pid = ?"; 
		logger.debug("Executing SQL: " + sql);
		Product product = queryRunner.query(sql,new BeanHandler<Product>(Product.class),id);
		logger.debug("SQL Execution Completed: " + product.toString());
		return product;
	}
	/**
	 * 根据分类id查询商品列表
	 * @param cid
	 * @return
	 */
	public List<Product> getProductListByCid(String cid,PageBean pageBean) throws SQLException{
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where cid = ? limit ?,?";
		logger.debug("Executing SQL: " + sql);
		List<Product> productList = queryRunner.query(sql,new BeanListHandler<Product>(Product.class),cid,pageBean.getStart(),pageBean.getRows());
		logger.debug("SQL Execution Completed: " + productList.size() + " records fetched.");
		return productList;
	}
	/**
	 * 
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public int getTotal(String cid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where cid = ?";
		logger.debug("Executing SQL: " + sql);
		List<Product> productList = queryRunner.query(sql,new BeanListHandler<Product>(Product.class),cid);
		logger.debug("SQL Execution Completed: " + productList.size() + " records fetched.");
		return productList.size();
	}
	
	
	/**
	 * 查询商品列表
	 * @param product 参数
	 * @param pageBean 分页
	 * @return
	 * @throws SQLException 
	 */
	public List<Product> selectProductList(Product product, PageBean pageBean) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select a.*,b.cname from product a,category b where a.cid = b.cid";
		logger.debug("Executing SQL: " + sql);
		
		// 商品类别
		String cid = product.getCid();
		if(cid !=null && !"".equals(cid)) {
			sql += " and a.cid = "+cid;
		}
		
		// 商品名称
		String pname = product.getPname();
		
		if(pname !=null && !"".equals(pname)) {
			sql += " and a.pname like '%"+pname+"%'";
		}
		
		// 分页
		sql += " order by createtime desc limit ?,?";
		
		List<Product> productList = queryRunner.query(sql,new BeanListHandler<Product>(Product.class),pageBean.getStart(),pageBean.getRows());
		logger.debug("SQL Execution Completed: " + productList.size() + " records fetched.");
		
		
		// TODO Auto-generated method stub
		return productList;
	}
	
	/**
	 * 查询总数
	 * @param product
	 * @return
	 * @throws SQLException 
	 */
	public int selectTotal(Product product) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where 1=1";
		logger.debug("Executing SQL: " + sql);
		
 
		// 商品类别
		String cid = product.getCid();
		if(cid !=null && !"".equals(cid)) {
			sql += " and cid = "+cid;
		}
		
		// 商品名称
		String pname = product.getPname();
		System.out.println(pname);
		if(pname !=null && !"".equals(pname)) {
			sql += " and pname like '%"+pname+"%'";
		}
		
		List<Product> productList = queryRunner.query(sql,new BeanListHandler<Product>(Product.class));
		logger.debug("SQL Execution Completed: " + productList.size() + " records fetched.");
		
		// TODO Auto-generated method stub
		return productList.size();
	}
	
	/**
	 * 新增
	 * @param product
	 * @return
	 * @throws SQLException 
	 */
	public int insert(Product product) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql,product.getPid(),product.getPname(),product.getMarket_price(),product.getShop_price()
				,product.getPimage(),product.getPdate(),product.getIs_hot(),product.getPdesc(),0,product.getCid(),product.getCreatetime());
		logger.debug("SQL Execution Completed: " + result);
		
		return result;
	}
	/**
	 * 修改
	 * @param product
	 * @return
	 * @throws SQLException 
	 */
	public int update(Product product) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update product set pname=?,market_price=?,shop_price=?,pimage=?,is_hot=?,pdesc=?,cid=? where pid = ?";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql,product.getPname(),product.getMarket_price(),product.getShop_price()
				,product.getPimage(),product.getIs_hot(),product.getPdesc(),product.getCid(),product.getPid());
		logger.debug("SQL Execution Completed: " + result);
		
		return result;
	}
	
	/**
	 * 删除
	 * @param product
	 * @return
	 * @throws SQLException 
	 */
	public int delete(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "delete from product where pid='"+pid+"'";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql);
		logger.debug("SQL Execution Completed: " + result);
		
		return result;
	}
	
}
