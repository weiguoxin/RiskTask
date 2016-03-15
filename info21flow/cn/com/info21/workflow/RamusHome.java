/*
 * 创建日期 2005-7-20
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RamusHome {
	/**
	 * 默认构造函数
	 */
	public RamusHome() {
	}
	/**
	 * 创建活动
	 * @param nextprocid 目标过程ID
	 * @return 目标过程ID
	 */
	public static Ramus create(String nextprocid) {
		Ramus ramus = null;
		try {
			ramus = new Ramus(nextprocid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ramus;
	}
	/**
	 * 根据ID查找过程
	 * @param id 过程ID
	 * @return 过程
	 */
	public static Ramus findById(int id) {
		Ramus ramus = null;
		try {
			ramus = new Ramus(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ramus;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return RamusIterator
	 * @throws SystemException 扑获错误
	 */
	public static RamusIterator findBySQL(String sql)
	throws SystemException {
		return RamusIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return RamusIterator
	 * @throws SystemException 捕获错误
	 */
	public static RamusIterator findByCondition(String condition) throws SystemException {
		return new RamusIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return RamusIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static RamusIterator findByCondition(String condition ,
			int start , int num) {
		return new RamusIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Ramus.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getRamusCount(String conditionStr) {
		return SysFunction.getCnt(Ramus.TABLENAME, conditionStr);
	}
}
