/*
 * 创建日期 2005-7-19
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class AppHome {
	/**
	 * 默认构造函数
	 */
	public AppHome() {
	}
	/**
	 * 创建活动
	 * @param appname 应用名称
	 * @return 应用
	 */
	public static App create(String appname) {
		App wa = null;
		try {
			wa = new App(appname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wa;
	}
	/**
	 * 根据ID查找应用
	 * @param id 应用ID
	 * @return 应用
	 */
	public static App findById(int id) {
		App wa = null;
		try {
			wa = new App(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wa;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return AppIterator
	 * @throws SystemException 扑获错误
	 */
	public static AppIterator findBySQL(String sql)
	throws SystemException {
		return AppIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return AppIterator
	 * @throws SystemException 捕获错误
	 */
	public static AppIterator findByCondition(String condition) throws SystemException {
		return new AppIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return AppIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static AppIterator findByCondition(String condition ,
			int start , int num) {
		return new AppIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(App.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(App.TABLENAME, conditionStr);
	}
}
