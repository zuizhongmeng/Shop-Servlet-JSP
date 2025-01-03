package com.shop.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shop.pojo.Order;
import com.shop.pojo.OrderItem;
import com.shop.pojo.Product;
import com.shop.utils.C3P0Utils;
import com.shop.utils.PageBean;

public class OrderDao {
	
	private static final Logger logger = LogManager.getLogger(ProductDao.class);
	
	 static {
	        // 注册自定义的类型转换器
	        ConvertUtils.register(new Converter() {
	            public Object convert(Class type, Object value) {
	                if (value instanceof String && type.equals(Timestamp.class)) {
	                    String datetimeStr = (String) value;
	                    try {
	                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
	                        Date parsedDate = sdf.parse(datetimeStr);
	                        return new Timestamp(parsedDate.getTime());
	                    } catch (Exception e) {
	                        return null;
	                    }
	                }
	                return null;
	            }
	        }, Timestamp.class);
	    }
	
	
	public void saveOrder(Order order) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql,order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),
				order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
		logger.debug("SQL Execution Completed: " + result);
	}

	public void saveItem(OrderItem item) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into orderitem values(?,?,?,?,?)";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql,item.getItemid(),item.getCount(),item.getSubtotal(),item.getProduct().getPid(),item.getOrder().getOid());
		logger.debug("SQL Execution Completed: " + result);
	}

	public List<Order> getOrderList(String uid) throws SQLException, IllegalAccessException, InvocationTargetException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from orders where uid = ?";
		logger.debug("Executing SQL: " + sql);
		List<Order> orderList = runner.query(sql,new BeanListHandler<Order>(Order.class),uid);
		for (Order order : orderList) {
			sql = "select * from orderitem o ,product p where o.pid=p.pid and o.oid=?";
			List<Map<String, Object>> olist = runner.query(sql, new MapListHandler(),order.getOid());
			logger.debug("SQL Execution Completed: " + olist.size() + " records fetched.");
			for(Map<String,Object> map:olist) {
				OrderItem item = new OrderItem();
				BeanUtils.populate(item, map);
				Product product = new Product();
				BeanUtils.populate(product, map);
				item.setOrder(order);
				item.setProduct(product);
				order.getOrderItems().add(item);
			}
		}
		return orderList;
	}

	/**
	 * 查询订单列表
	 * @param order
	 * @param pageBean
	 * @return
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<Order> selectOrderList(Order order, PageBean pageBean) throws SQLException, IllegalAccessException, InvocationTargetException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from orders where 1=1 ";
		logger.debug("Executing SQL: " + sql);
		
		// 订单号
		String oid = order.getOid();
		if(oid !=null && !"".equals(oid)) {
			sql += " and oid = '"+oid+"'";
		}
		
		sql += " order by ordertime desc limit ?,?";
		
		List<Order> orderList = queryRunner.query(sql,new BeanListHandler<Order>(Order.class),pageBean.getStart(),pageBean.getRows());
		logger.debug("SQL Execution Completed: " + orderList.size() + " records fetched.");
		
		// 封装订单项
		for(Order o : orderList) {
			sql = "select * from orderitem o ,product p where o.pid=p.pid and o.oid=?";
			logger.debug("Executing SQL: " + sql);
			List<Map<String, Object>> olist = queryRunner.query(sql, new MapListHandler(),o.getOid());
			logger.debug("SQL Execution Completed: " + olist.size() + " records fetched.");
			for(Map<String,Object> map:olist) {
				OrderItem item = new OrderItem();
				BeanUtils.populate(item, map);
				Product product = new Product();
				BeanUtils.populate(product, map);
				item.setOrder(o);
				item.setProduct(product);
				o.getOrderItems().add(item);
			}
		}
		
		
		// TODO Auto-generated method stub
		return orderList;
	}
	
	/**
	 * 查询总数
	 * @param order
	 * @return
	 * @throws SQLException 
	 */
	public int selectTotal(Order order) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from orders where 1=1";
		logger.debug("Executing SQL: " + sql);
		
		// 订单号
		String oid = order.getOid();
		if(oid !=null && !"".equals(oid)) {
			sql += " and oid = '"+oid+"'";
		}
		
		List<Order> orderList = queryRunner.query(sql,new BeanListHandler<Order>(Order.class));
		logger.debug("SQL Execution Completed: " + orderList.size() + " records fetched.");
		
		// TODO Auto-generated method stub
		return orderList.size();
	}

	/**
	 * 删除订单
	 * @param oid
	 * @return
	 * @throws SQLException 
	 */
	public int delete(String oid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		// 主表订单sql
		String mainSql = "delete from orders where oid = ?";
		logger.debug("Executing SQL: " + mainSql);
		
		// 子表订单项sql
		String subSql = "delete from orderitem where oid = ?";
		logger.debug("Executing SQL: " + subSql);
		
		int result = runner.update(subSql,oid);
		logger.debug("SQL Execution Completed: " + result);
		
		result = runner.update(mainSql,oid);
		logger.debug("SQL Execution Completed: " + result);
		
		return result;
	}

}
