/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author lkh
 */

public class ProcHome {
	/**
	 * 默认构造函数
	 */
	public ProcHome() {
	}
	/**
	 * 创建活动
	 * @param procname 过程名称
	 * @return 过程
	 */
	public static Proc create(String procname) {
		Proc proc = null;
		try {
			proc = new Proc(procname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proc;
	}
	/**
	 * 根据ID查找过程
	 * @param id 过程ID
	 * @return 过程
	 */
	public static Proc findById(int id) {
		Proc proc = null;
		try {
			proc = new Proc(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proc;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return ProcIterator
	 * @throws SystemException 扑获错误
	 */
	public static ProcIterator findBySQL(String sql)
	throws SystemException {
		return ProcIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return ProcIterator
	 * @throws SystemException 捕获错误
	 */
	public static ProcIterator findByCondition(String condition) throws SystemException {
		return new ProcIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return ProcIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static ProcIterator findByCondition(String condition ,
			int start , int num) {
		return new ProcIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Proc.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(Proc.TABLENAME, conditionStr);
	}
}
