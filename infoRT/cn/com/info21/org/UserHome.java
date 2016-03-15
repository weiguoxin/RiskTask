/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class UserHome {
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return Iterator
	 * @throws SystemException 扑获错误
	 */
	public static UserIterator findBySQL(String sql)
	throws SystemException {
		return UserIterator.findBySQL(sql);
	}
	/**
	 * 根据条件获取用户列表
	 * @param conditionStr 查询条件。
	 * @return UserIterator
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static UserIterator findByCondition(String conditionStr)
	throws SystemException {
		UserIterator userIterator = null;
		userIterator = new UserIterator(conditionStr);
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
	public static UserIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		UserIterator userIterator = null;
		userIterator = new UserIterator(conditionStr, startIndex, num);
		return userIterator;
	}
	/**
	 * 根据用户编号 ，获得一个User对象
	 * @param id int 用户编号
	 * @return User 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static User findById(int id) throws SystemException {
		User user = new User(id);
		return user;
	}
	/**
	 * 根据用户代码 ，获得一个User对象
	 * @param code 用户编号
	 * @return 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static User findByCode(String code) throws SystemException {
		User user = null;
		UserIterator userIterator = null;
		userIterator = new UserIterator("UPPER(id) = '" + code.toUpperCase() + "'");
		try {
			user = (User) userIterator.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * 根据用户代码 ，获得一个User对象
	 * @param username 用户名
	 * @return 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static User findByUserName(String username) throws SystemException {
		User user = null;
		UserIterator userIterator = null;
		userIterator = new UserIterator("username = '" + username + "'");
		try {
			if (userIterator.hasNext()) {
				user = (User) userIterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * 创建一个新的User对象
	 * @param username 对象名
	 * @return User 对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static User create(String username) throws SystemException {
		User user = null;
		try {
			//新建用户
			user = new User(username);
		} catch (SystemException se) {
			System.out.println("user create error in UserHome");
			try {
				if (user != null) {
					UserHome.remove(user.getId());
				}
			} catch (Exception e2) {
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("user create error in UserHome");
			try {
				if (user != null) {
					UserHome.remove(user.getId());
				}
			} catch (Exception e2) {
				System.out.println();
			}
		}
		return  user;
	}
	/**
	 * 根据查询条件 ，获得符合条件的User对象的个数
	 * @param conditionStr 查询条件
	 * @return 用户对象个数
	 */
	public static int getUserCount(String conditionStr) {
		return SysFunction.getCnt(User.TABLENAME, conditionStr);
	}
	/**
	 * 删除一个User对象
	 * @param id 用户编号
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static void remove(int id) throws SystemException {
		SysFunction.delRecord(User.TABLENAME, id);
	}

}
