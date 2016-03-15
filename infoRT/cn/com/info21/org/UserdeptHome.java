/*
 * 创建日期 2006-1-16
 */

package cn.com.info21.org;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author songle
 */

public class UserdeptHome {
	/**
	 * 默认构造函数
	 */
	public UserdeptHome() {
	}
	/**
	 * 创建信息表
	 * @param userid
	 * @return 人员部门信息ID
	 */
	public static Userdept create(String userid) {
		Userdept obj = null;
		try {
			obj = new Userdept(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
   }

	/**
	 * 根据ID查找信息表
	 * @param pid 人员部门信息ID
	 * @return 信息表
	 */
	public static Userdept findById(int id) {
		Userdept ep = null;
		try {
			ep = new Userdept(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ep;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return UserdeptIterator
	 * @throws SystemException 扑获错误
	 */
	public static UserdeptIterator findBySQL(String sql)
	throws SystemException {
		return UserdeptIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return UserdeptIterator
	 * @throws SystemException 捕获错误
	 */
	public static UserdeptIterator findByCondition(String condition)
	throws SystemException {
		return new UserdeptIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return UserdeptIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static UserdeptIterator findByCondition(String condition ,
			int start , int num) {
		return new UserdeptIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Userdept.TABLENAME, id);
	}

	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(Userdept.TABLENAME, conditionStr);
	}
}