package com.shop.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shop.pojo.User;
import com.shop.utils.C3P0Utils;
import com.shop.utils.PageBean;

public class UserDao {
	
	private static final Logger logger = LogManager.getLogger(ProductDao.class);
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */ 
	public User login(String username,String password) throws SQLException{
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql="select * from user where username = ? and password = ?";
		logger.debug("Executing SQL: " + sql);
		User user = queryRunner.query(sql,new BeanHandler<User>(User.class),username,password);
		logger.debug("SQL Execution Completed: " + user.toString());
		return user;
	}

	/**
	 * 注册
	 * @param user
	 * @return
	 */
	public boolean register(User user) throws SQLException{
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql="insert into user values(?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug("Executing SQL: " + sql);
		int update = queryRunner.update(sql,user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),
				user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode(),user.getCreatetime());
		logger.debug("SQL Execution Completed: " + update);
		return update>0?true:false;
	}
	/**
	 * 激活用户
	 * @param code
	 * @throws SQLException
	 */
	public void activeUser(String code) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql="update user set state=1 where code=?";
		logger.debug("Executing SQL: " + sql);
		int result = queryRunner.update(sql,code);
		logger.debug("SQL Execution Completed: " + result);
	}

	/**
	 * 查询总数
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	public int selectTotal(User user) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from user where 1=1";
		logger.debug("Executing SQL: " + sql);
		// 用户名称
		String name = user.getName();
		
		if(name !=null && !"".equals(name)) {
			sql += " and name like '%"+name+"%'";
		}
		
		List<User> userList = queryRunner.query(sql,new BeanListHandler<User>(User.class));
		logger.debug("SQL Execution Completed: " + userList.size() + " records fetched.");
		
		// TODO Auto-generated method stub
		return userList.size();
	}

	/**
	 * 删除
	 * @param uid
	 * @return
	 * @throws SQLException 
	 */
	public int delete(String uid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "delete from user where uid='"+uid+"'";
		logger.debug("Executing SQL: " + sql);
		int result = runner.update(sql);
		logger.debug("SQL Execution Completed: " + result);
		
		return result;
	}

	/**
	 * 查询用户列表
	 * @param user
	 * @param pageBean
	 * @return
	 * @throws SQLException 
	 */
	public List<User> selectUserList(User user, PageBean pageBean) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from user where 1=1";
		logger.debug("Executing SQL: " + sql);
		// 用户名称
		String name = user.getName();
		
		if(name !=null && !"".equals(name)) {
			sql += " and name like '%"+name+"%'";
		}
		
		sql += " order by createtime desc limit ?,?";
		List<User> userList = queryRunner.query(sql,new BeanListHandler<User>(User.class),pageBean.getStart(),pageBean.getRows());
		logger.debug("SQL Execution Completed: " + userList.size() + " records fetched.");
		
		// TODO Auto-generated method stub
		return userList;
	}
}
