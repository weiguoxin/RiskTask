/*
 * 创建日期 2005-8-1
 *
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 *
 */
public class ProxyHome {
	/**
	 * 默认构造函数
	 */
	public ProxyHome() {
	}
	/**
	 * 创建代理服务器
	 * @param consignorid 交付人ID
	 * @return 代理服务器
	 */
	public static Proxy create(String consignorid) {
		Proxy proxy = null;
		try {
			proxy = new Proxy(consignorid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proxy;
	}
	/**
	 * 根据ID查找代理服务器
	 * @param id 代理服务器ID
	 * @return 代理服务器
	 */
	public static Proxy findById(int id) {
		Proxy proxy = null;
		try {
			proxy = new Proxy(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proxy;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return ProxyIterator
	 * @throws SystemException 扑获错误
	 */
	public static ProxyIterator findBySQL(String sql)
	throws SystemException {
		return ProxyIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return ProxyIterator
	 * @throws SystemException 捕获错误
	 */
	public static ProxyIterator findByCondition(String condition)
	throws SystemException {
		return new ProxyIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return ProxyIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static ProxyIterator findByCondition(String condition ,
			int start , int num) {
		return new ProxyIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Proxy.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getProxyCount(String conditionStr) {
		return SysFunction.getCnt(Proxy.TABLENAME, conditionStr);
	}

}
