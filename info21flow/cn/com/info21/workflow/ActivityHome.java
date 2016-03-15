/*
 * 创建日期 2005-7-19
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class ActivityHome {
	/**
	 * 默认构造函数
	 */
	public ActivityHome() {
	}
	/**
	 * 创建活动
	 * @param name 活动名称
	 * @return 活动
	 */
	public static Activity create(String name) {
		Activity acti = null;
		try {
			acti = new Activity(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acti;
	}
	/**
	 * 根据ID查找活动
	 * @param id 活动ID
	 * @return 活动
	 */
	public static Activity findById(int id) {
		Activity acti = null;
		try {
			acti = new Activity(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acti;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return ActivityIterator
	 * @throws SystemException 扑获错误
	 */
	public static ActivityIterator findBySQL(String sql)
	throws SystemException {
		return ActivityIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return ActivityIterator
	 * @throws SystemException 捕获错误
	 */
	public static ActivityIterator findByCondition(String condition)
	throws SystemException {
		return new ActivityIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return ActivityIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static ActivityIterator findByCondition(String condition ,
			int start , int num) {
		return new ActivityIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Activity.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getActivityCount(String conditionStr) {
		return SysFunction.getCnt(Activity.TABLENAME, conditionStr);
	}

}
