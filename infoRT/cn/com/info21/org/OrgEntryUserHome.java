/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class OrgEntryUserHome {
	/**
	 * 默认构造函数
	 */
	public OrgEntryUserHome() {
	}
	/**
	 * 创建活动
	 * @param appname 应用名称
	 * @return 应用
	 */
	public static OrgEntryUser create(String username) {
		OrgEntryUser wa = null;
		try {
			wa = new OrgEntryUser(username);
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
	public static OrgEntryUser findById(int id) {
		OrgEntryUser wa = null;
		try {
			wa = new OrgEntryUser(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wa;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return OrgEntryUserIterator
	 * @throws SystemException 扑获错误
	 */
	public static OrgEntryUserIterator findBySQL(String sql)
	throws SystemException {
		return OrgEntryUserIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return OrgEntryUserIterator
	 * @throws SystemException 捕获错误
	 */
	public static OrgEntryUserIterator findByCondition(String condition) throws SystemException {
		return new OrgEntryUserIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return OrgEntryUserIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static OrgEntryUserIterator findByCondition(String condition ,
			int start , int num) {
		return new OrgEntryUserIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(OrgEntryUser.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getBasedataCount(String conditionStr) {
		return SysFunction.getCnt(OrgEntryUser.TABLENAME, conditionStr);
	}
}

