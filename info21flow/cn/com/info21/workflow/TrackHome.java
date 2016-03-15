/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class TrackHome {
	/**
	 * 默认构造函数
	 */
	public TrackHome() {
	}
	/**
	 * 创建活动
	 * @param flowid 流程实例ID
	 * @return 过程
	 */
	public static Track create(String flowid) {
		Track flow = null;
		try {
			flow = new Track(flowid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flow;
   }

	/**
	 * 根据ID查找过程
	 * @param id 流程跟踪序列号
	 * @return 过程
	 */
	public static Track findById(int id) {
		Track flowid = null;
		try {
			flowid = new Track(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return TrackIterator
	 * @throws SystemException 扑获错误
	 */
	public static TrackIterator findBySQL(String sql)
	throws SystemException {
		return TrackIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return TrackIterator
	 * @throws SystemException 捕获错误
	 */
	public static TrackIterator findByCondition(String condition)
	throws SystemException {
		return new TrackIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return TrackIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static TrackIterator findByCondition(String condition ,
			int start , int num) {
		return new TrackIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Track.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getTrackCount(String conditionStr) {
		return SysFunction.getCnt(Track.TABLENAME, conditionStr);
	}
}
