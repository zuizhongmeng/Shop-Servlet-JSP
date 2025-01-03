package com.shop.service;

import java.sql.SQLException;
import java.util.List;

import com.shop.dao.UserDao;
import com.shop.pojo.User;
import com.shop.utils.PageBean;

import jakarta.servlet.http.HttpServletRequest;

public class UserService {
	UserDao userDao = new UserDao();

	public User login(String username, String password) {
		try {
			return userDao.login(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param user
	 * @return
	 */
	public boolean register(User user,HttpServletRequest request) {
		try {
			boolean success = userDao.register(user);
			
			// 删除发送邮箱
//			//邮件标题
//			String subject="来自商城的激活邮件！请点击以下链接激活账户！！";
//			//邮箱内容
//			String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
//			String text =url+"/activeServlet?code="+user.getCode();
//			//发送邮件
//			MailUtils.sendMail("2285016799@qq.com",user.getEmail(),subject, text);
			return success;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	/**
	 * 激活用户
	 * @param code
	 */
	public void activeUser(String code) {
		try {
			userDao.activeUser(code);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 查询总数
	 * @param user
	 * @return
	 */
	public int getTotal(User user) {
		try {
			return userDao.selectTotal(user);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 删除
	 * @param uid
	 * @return
	 */
	public int delete(String uid) {
		try {
			return userDao.delete(uid);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 查询用户列表
	 * @param user
	 * @param pageBean
	 * @return
	 */
	public List<User> getUserList(User user, PageBean pageBean) {
		try {
			return userDao.selectUserList(user,pageBean);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
