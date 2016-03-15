/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class AclHome {
	/**
	 * 默认构造函数
	 */
	public AclHome() {
	}
	/**
	 * 创建活动
	 * @param flowid 过程名称
	 * @return 过程
	 */
	public static Acl create(String flowid) {
		Acl flow = null;
		try {
			flow = new Acl(flowid);
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
	public static Acl findById(int id) {
		Acl flowid = null;
		try {
			flowid = new Acl(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return AclIterator
	 * @throws SystemException 扑获错误
	 */
	public static AclIterator findBySQL(String sql)
	throws SystemException {
		return AclIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return AclIterator
	 * @throws SystemException 捕获错误
	 */
	public static AclIterator findByCondition(String condition)
	throws SystemException {
		return new AclIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return AclIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static AclIterator findByCondition(String condition ,
			int start , int num) {
		return new AclIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Acl.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(Acl.TABLENAME, conditionStr);
	}
}
