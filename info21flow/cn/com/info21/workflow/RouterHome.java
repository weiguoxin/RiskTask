/*
 * 创建日期 2005-7-20
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RouterHome {
	/**
	 * 默认构造函数
	 */
	public RouterHome() {
	}
	/**
	 * 创建活动
	 * @param actid 活动ID
	 * @return 过程
	 */
	public static Router create(String actid) {
		Router router = null;
		try {
			router = new Router(actid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return router;
	}
	/**
	 * 根据ID查找路由器
	 * @param id 路由器ID
	 * @return 路由器
	 */
	public static Router findById(int id) {
		Router router = null;
		try {
			router = new Router(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return router;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return RouterIterator
	 * @throws SystemException 扑获错误
	 */
	public static RouterIterator findBySQL(String sql)
	throws SystemException {
		return RouterIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return RouterIterator
	 * @throws SystemException 捕获错误
	 */
	public static RouterIterator findByCondition(String condition)
	throws SystemException {
		return new RouterIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return RouterIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static RouterIterator findByCondition(String condition ,
			int start , int num) {
		return new RouterIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Router.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getRouterCount(String conditionStr) {
		return SysFunction.getCnt(Router.TABLENAME, conditionStr);
	}
}
