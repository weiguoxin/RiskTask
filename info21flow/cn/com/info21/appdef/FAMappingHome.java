/*
 * 创建日期 2005-7-19
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class FAMappingHome {
	/**
	 * 默认构造函数
	 */
	public FAMappingHome () {
	}
	/**
	 * 创建操作与活动之间的映射
	 * @param appname 应用名称
	 * @return 应用
	 */
	public static FAMapping create(int fieldid, int actid) {
	    FAMapping fam = null;
		try {
			fam = new FAMapping(fieldid, actid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fam;
	}
	/**
	 * 根据ID查找应用
	 * @param id 应用ID
	 * @return 应用
	 */
	public static FAMapping findById(int id) {
	    FAMapping fam = null;
		try {
			fam = new FAMapping(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fam;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return AppIterator
	 * @throws SystemException 扑获错误
	 */
	public static FAMappingIterator findBySQL(String sql)
	throws SystemException {
		return FAMappingIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return AppIterator
	 * @throws SystemException 捕获错误
	 */
	public static FAMappingIterator findByCondition(String condition) throws SystemException {
		return new FAMappingIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return AppIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static FAMappingIterator findByCondition(String condition ,
			int start , int num) {
		return new FAMappingIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(FAMapping.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getFAMappingCount(String conditionStr) {
		return SysFunction.getCnt(FAMapping.TABLENAME, conditionStr);
	}
	public static void removeByXId(int xid) {
	    try {
	        FAMappingIterator fi = FAMappingHome.findByCondition(" fieldid = " + xid);
	        FAMapping fam = null;
	        while (fi.hasNext()) {
	            fam = (FAMapping) fi.next();
	            FAMappingHome.remove(fam.getId());
	        }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
