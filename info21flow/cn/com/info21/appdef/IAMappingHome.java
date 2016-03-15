/*
 * 创建日期 2005-7-19
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class IAMappingHome {
	/**
	 * 默认构造函数
	 */
	public IAMappingHome () {
	}
	/**
	 * 创建操作与活动之间的映射
	 * @param appname 应用名称
	 * @return 应用
	 */
	public static IAMapping create(int ideaid, int actid) {
	    IAMapping iam = null;
		try {
			iam = new IAMapping(ideaid, actid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iam;
	}
	/**
	 * 根据ID查找应用
	 * @param id 应用ID
	 * @return 应用
	 */
	public static IAMapping findById(int id) {
	    IAMapping iam = null;
		try {
			iam = new IAMapping(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iam;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return AppIterator
	 * @throws SystemException 扑获错误
	 */
	public static IAMappingIterator findBySQL(String sql)
	throws SystemException {
		return IAMappingIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return AppIterator
	 * @throws SystemException 捕获错误
	 */
	public static IAMappingIterator findByCondition(String condition) throws SystemException {
		return new IAMappingIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return AppIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static IAMappingIterator findByCondition(String condition ,
			int start , int num) {
		return new IAMappingIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(IAMapping.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getIAMappingCount(String conditionStr) {
		return SysFunction.getCnt(IAMapping.TABLENAME, conditionStr);
	}
}
