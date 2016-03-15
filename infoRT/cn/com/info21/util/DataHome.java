/*
 * Created on 2004-7-22
 */

package cn.com.info21.util;
import java.sql.Timestamp;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Administrator
 */

public class DataHome {

	/**
	 * 默认构造函数
	 */
	public DataHome() {
	}
	/**
	 * 创建对象
	 * @param pid PID
	 * @return 对象
	 */
	public static Data create(Timestamp recTime) {
		System.out.println("VIEW can't create!");
		return null;
	}
	public static Data create(String tablename) {
		Data rtn = null;
		try {
			rtn = new Data(tablename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	/**
	 * 根据ID查找对象
	 * @param id 对象ID
	 * @return 对象
	 */
	public static Data findById(String id, String tablename) {
		Data obj = null;
		try {
			obj = new Data(id, tablename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return Iterator
	 * @throws SystemException 扑获错误
	 */
	public static DataIterator findBySQL(String sql)
	throws SystemException {
		return DataIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return Iterator
	 * @throws SystemException 捕获错误
	 */
	public static DataIterator findByCondition(String condition, String tablename)
			throws SystemException {
		return new DataIterator(condition, tablename);
	}
	/**
	 * 查找符合条件的记录
	 * @return Iterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static DataIterator findByCondition(String condition, String tablename,
			int start , int num) {
		return new DataIterator(condition, tablename, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		System.out.println("VIEW can't remove!");
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getCount(String conditionStr, String tablename) {
		return SysFunction.getCnt(tablename, conditionStr);
	}
}
