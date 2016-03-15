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
public class ButtonHome {
	/**
	 * 默认构造函数
	 */
	public ButtonHome() {
	}
	/**
	 * 创建活动
	 * @param name 按钮名称
	 * @return 按钮
	 */
	public static Button create(String name) {
		Button button = null;
		try {
			button = new Button(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return button;
	}
	/**
	 * 根据ID查找按钮
	 * @param id 唯一性标记ID
	 * @return 按钮
	 */
	public static Button findById(int id) {
		Button button = null;
		try {
			button = new Button(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return button;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return ButtonIterator
	 * @throws SystemException 扑获错误
	 */
	public static ButtonIterator findBySQL(String sql)
	throws SystemException {
		return ButtonIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return ButtonIterator
	 * @throws SystemException 捕获错误
	 */
	public static ButtonIterator findByCondition(String condition)
	throws SystemException {
		return new ButtonIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return ButtonIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static ButtonIterator findByCondition(String condition ,
			int start , int num) {
		return new ButtonIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Button.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getButtonCount(String conditionStr) {
		return SysFunction.getCnt(Button.TABLENAME, conditionStr);
	}
}
