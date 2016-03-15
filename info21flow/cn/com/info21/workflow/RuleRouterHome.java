/*
 * 创建日期 2005-7-20
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RuleRouterHome {
	/**
	 * 默认构造函数
	 */
	public RuleRouterHome() {
	}
	/**
	 * 创建活动
	 * @param condition 规则条件
	 * @return 规则条件
	 */
	public static RuleRouter create(String condition) {
		RuleRouter rurt = null;
		try {
			rurt = new RuleRouter(condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rurt;
	}
	/**
	 * 根据ID查找路由规则
	 * @param id 路由规则ID
	 * @return 路由
	 */
	public static RuleRouter findById(int id) {
		RuleRouter rurt = null;
		try {
			rurt = new RuleRouter(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rurt;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return RuleRouterIterator
	 * @throws SystemException 扑获错误
	 */
	public static RuleRouterIterator findBySQL(String sql)
	throws SystemException {
		return RuleRouterIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return RuleRouterIterator
	 * @throws SystemException 捕获错误
	 */
	public static RuleRouterIterator findByCondition
	(String condition)
	throws SystemException {
		return new RuleRouterIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return RuleRouterIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static RuleRouterIterator findByCondition(String condition ,
			int start , int num) {
		return new RuleRouterIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(RuleRouter.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(RuleRouter.TABLENAME, conditionStr);
	}
}
