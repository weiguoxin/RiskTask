/*
 * 创建日期 2005-9-2
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class FlowTaskMappingHome {
	/**
	 * 默认构造函数
	 */
	public FlowTaskMappingHome() {
	}
	public static FlowTaskMapping findById(int id) {
	    FlowTaskMapping ftm = null;
		try {
			ftm = new FlowTaskMapping(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ftm;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return FlowTaskMappingIterator
	 * @throws SystemException 扑获错误
	 */
	public static FlowTaskMappingIterator findBySQL(String sql)
	throws SystemException {
		return FlowTaskMappingIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return FlowTaskMappingIterator
	 * @throws SystemException 捕获错误
	 */
	public static FlowTaskMappingIterator findByCondition(String condition)
	throws SystemException {
		return new FlowTaskMappingIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return FlowTaskMappingIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static FlowTaskMappingIterator findByCondition(String condition ,
			int start , int num) {
		return new FlowTaskMappingIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Flow.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getFlowTaskMappingCount(String conditionStr) {
		return SysFunction.getCnt(FlowTaskMapping.TABLENAME, conditionStr);
	}
}
