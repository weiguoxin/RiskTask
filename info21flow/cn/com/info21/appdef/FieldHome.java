/*
 * 创建日期 2005-8-1
 *
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 *
 */
public class FieldHome {
	/**
	 * 默认构造函数
	 */
	public FieldHome() {
	}
	/**
	 * 创建域
	 * @param fieldname 域名称
	 * @return 域
	 */
	public static Field create(String fieldname) {
		Field field = null;
		try {
			field = new Field(fieldname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}
	/**
	 * 根据ID查找域
	 * @param id 域ID
	 * @return 域
	 */
	public static Field findById(int id) {
		Field acti = null;
		try {
			acti = new Field(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acti;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return FieldIterator
	 * @throws SystemException 扑获错误
	 */
	public static FieldIterator findBySQL(String sql)
	throws SystemException {
		return FieldIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return FieldIterator
	 * @throws SystemException 捕获错误
	 */
	public static FieldIterator findByCondition(String condition)
	throws SystemException {
		return new FieldIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return FieldIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static FieldIterator findByCondition(String condition ,
			int start , int num) {
		return new FieldIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Field.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(Field.TABLENAME, conditionStr);
	}
}
