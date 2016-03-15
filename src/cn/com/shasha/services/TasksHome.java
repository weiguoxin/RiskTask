package cn.com.shasha.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;
import cn.com.shasha.sys.Article;
import cn.com.shasha.sys.ArticleHome;

/**
 * @author liukh
 */

public class TasksHome {
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return Iterator
	 * @throws SystemException 扑获错误
	 */
	public static TasksIterator findBySQL(String sql)
	throws SystemException {
		return TasksIterator.findBySQL(sql);
	}
	/**
	 * 根据条件获取用户列表
	 * @param conditionStr 查询条件。
	 * @return UserIterator
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static TasksIterator findByCondition(String conditionStr)
	throws SystemException {
		TasksIterator userIterator = null;
		userIterator = new TasksIterator(conditionStr);
		return userIterator;
	}
	/**
	 * 根据查询条件 ，从startIndex开始，获得num个User对象的列表
	 * @param conditionStr 查询条件
	 * @param startIndex 开始位置
	 * @param num 要求返回对象个数
	 * @return UserIterator 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static TasksIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		TasksIterator userIterator = null;
		userIterator = new TasksIterator(conditionStr, startIndex, num);
		return userIterator;
	}
	/**
	 * 根据用户编号 ，获得一个User对象
	 * @param id int 用户编号
	 * @return User 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static Tasks findById(int userid) throws SystemException {
		Tasks user = new Tasks(userid);
		return user;
	}
	/**
	 * 创建一个新的User对象
	 * @param username 对象名
	 * @return User 对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static Tasks create(String title) throws SystemException {
		Tasks grds = null;
		if ((title == null) || ("".equals(title))) {
			throw new SystemException("null or empty parameter 'title'");
		} else {
			grds = new Tasks(title);
		}
		return grds;
	}
	/**
	 * 根据查询条件 ，获得符合条件的User对象的个数
	 * @param conditionStr 查询条件
	 * @return 用户对象个数
	 */
	public static int getTasksCount(String conditionStr) {
		return SysFunction.getCnt(Tasks.TABLENAME, conditionStr);
	}
	/**
	 * 删除一个User对象
	 * @param id 用户编号
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static void remove(int id) throws SystemException {
		TasksHome.findById(id).remove();
	}

}
