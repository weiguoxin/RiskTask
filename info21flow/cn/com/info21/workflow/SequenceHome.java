/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class SequenceHome {
	/**
	 * 默认构造函数
	 */
	public SequenceHome() {
	}
	/**
	 * 创建活动
	 * @param flowid 序列所属流程的ID
	 * @return 过程
	 */
	public static Sequence create(String flowid) {
		Sequence flow = null;
		try {
			flow = new Sequence(flowid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flow;
   }

	/**
	 * 根据ID查找过程
	 * @param id 用户自定义序列ID
	 * @return 过程
	 */
	public static Sequence findById(int id) {
		Sequence flowid = null;
		try {
			flowid = new Sequence(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return SequenceIterator
	 * @throws SystemException 扑获错误
	 */
	public static SequenceIterator findBySQL(String sql)
	throws SystemException {
		return SequenceIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return SequenceIterator
	 * @throws SystemException 捕获错误
	 */
	public static SequenceIterator findByCondition(String condition)
	throws SystemException {
		return new SequenceIterator(condition);
	}
	/** 
	 * 查找符合条件的记录
	 * @return SequenceIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static SequenceIterator findByCondition(String condition ,
			int start , int num) {
		return new SequenceIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Sequence.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getSequenceCount(String conditionStr) {
		return SysFunction.getCnt(Sequence.TABLENAME, conditionStr);
	}
}
