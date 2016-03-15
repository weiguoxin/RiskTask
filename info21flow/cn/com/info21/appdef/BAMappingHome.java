/*
 * 创建日期 2005-7-19
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */
public class BAMappingHome {
	/**
	 * 默认构造函数
	 */
	public BAMappingHome () {
	}
	/**
	 * 创建操作与活动之间的映射
	 * @param appname 应用名称
	 * @return 应用
	 */
	public static BAMapping create(int buttonid, int actid) {
	    BAMapping bam = null;
		try {
			bam = new BAMapping(buttonid, actid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bam;
	}
	/**
	 * 根据ID查找应用
	 * @param id 应用ID
	 * @return 应用
	 */
	public static BAMapping findById(int id) {
	    BAMapping bam = null;
		try {
			bam = new BAMapping(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bam;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return AppIterator
	 * @throws SystemException 扑获错误
	 */
	public static BAMappingIterator findBySQL(String sql)
	throws SystemException {
		return BAMappingIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return AppIterator
	 * @throws SystemException 捕获错误
	 */
	public static BAMappingIterator findByCondition(String condition) throws SystemException {
		return new BAMappingIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return AppIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static BAMappingIterator findByCondition(String condition ,
			int start , int num) {
		return new BAMappingIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(BAMapping.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getBAMappingCount(String conditionStr) {
		return SysFunction.getCnt(BAMapping.TABLENAME, conditionStr);
	}
	public static void removeByXId(int xid) {
	    try {
	        BAMappingIterator bi = BAMappingHome.findByCondition(" buttonid = " + xid);
	        BAMapping bam = null;
	        while (bi.hasNext()) {
	            bam = (BAMapping) bi.next();
	            BAMappingHome.remove(bam.getId());
	        }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
