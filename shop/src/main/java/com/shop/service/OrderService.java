package com.shop.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import com.shop.dao.OrderDao;
import com.shop.pojo.Order;
import com.shop.pojo.OrderItem;
import com.shop.utils.PageBean;

public class OrderService {
	
	OrderDao orderDao = new OrderDao();
	
	public void saveOrder(Order order) {
		try {
			orderDao.saveOrder(order);
			for(OrderItem item:order.getOrderItems()) {
				orderDao.saveItem(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public List<Order> getOrderList(String uid) {
		try {
			return orderDao.getOrderList(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询订单列表
	 * @param order 参数 条件
	 * @param pageBean 分页
	 * @return
	 */
	public List<Order> getOrderList(Order order, PageBean pageBean){
		try {
			return orderDao.selectOrderList(order,pageBean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getTotal(Order order) {
		try {
			return orderDao.selectTotal(order);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 删除订单
	 * @param oid
	 * @return
	 */
	public int delete(String oid) {
		try {
			return orderDao.delete(oid);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
