/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class FlowHome {
	/**
	 * 默认构造函数
	 */
	public FlowHome() {
	}
	/**
	 * 创建活动
	 * @param procid 过程名称
	 * @return 过程
	 */
	public static Flow create(String procid) {
		Flow flow = null;
		try {
			flow = new Flow(procid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flow;
   }

	/**
	 * 根据ID查找过程
	 * @param id 过程ID
	 * @return 过程
	 */
	public static Flow findById(int id) {
		Flow procid = null;
		try {
			procid = new Flow(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return procid;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return FlowIterator
	 * @throws SystemException 扑获错误
	 */
	public static FlowIterator findBySQL(String sql)
	throws SystemException {
		return FlowIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return FlowIterator
	 * @throws SystemException 捕获错误
	 */
	public static FlowIterator findByCondition(String condition)
	throws SystemException {
		return new FlowIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return FlowIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static FlowIterator findByCondition(String condition ,
			int start , int num) {
		return new FlowIterator(condition, start, num);
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
	public static int getFlowCount(String conditionStr) {
		return SysFunction.getCnt(Flow.TABLENAME, conditionStr);
	}
}
