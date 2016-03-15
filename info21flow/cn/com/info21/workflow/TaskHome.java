/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class TaskHome {
	/**
	 * 默认构造函数
	 */
	public TaskHome() {
	}
	/**
	 * 创建活动
	 * @param procid 过程名称
	 * @return Task 过程
	 */
	public static Task create(String procid) {
		Task flow = null;
		try {
			flow = new Task(procid);
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
	public static Task findById(int id) {
		Task flowid = null;
		try {
			flowid = new Task(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return TaskIterator
	 * @throws SystemException 扑获错误
	 */
	public static TaskIterator findBySQL(String sql)
	throws SystemException {
		return TaskIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return TaskIterator
	 * @throws SystemException 捕获错误
	 */
	public static TaskIterator findByCondition(String condition)
	throws SystemException {
		return new TaskIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return TaskIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static TaskIterator findByCondition(String condition ,
			int start , int num) {
		return new TaskIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Task.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getTaskCount(String conditionStr) {
		return SysFunction.getCnt(Task.TABLENAME, conditionStr);
	}
}
