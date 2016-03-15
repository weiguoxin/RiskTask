/*
 * 创建日期 2005-7-20
 */
package cn.com.info21.workflow;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RuleChoosePersonHome {
	/**
	 * 默认构造函数
	 */
	public RuleChoosePersonHome() {
	}
	/**
	 * 创建活动
	 * @param rule 规则描述
	 * @return 规则
	 */
	public static RuleChoosePerson create(String rule) {
		RuleChoosePerson rcps = null;
		try {
			rcps = new RuleChoosePerson(rule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcps;
	}
	/**
	 * 根据ID查找选择人员规则
	 * @param id 选择人员规则ID
	 * @return 规则
	 */
	public static RuleChoosePerson findById(int id) {
		RuleChoosePerson rcps = null;
		try {
			rcps = new RuleChoosePerson(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcps;
	}
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return RuleChoosePersonIterator
	 * @throws SystemException 扑获错误
	 */
	public static RuleChoosePersonIterator findBySQL(String sql)
	throws SystemException {
		return RuleChoosePersonIterator.findBySQL(sql);
	}
	/**
	 * 构造函数
	 * @param condition 查询条件
	 * @return RuleChoosePersonIterator
	 * @throws SystemException 捕获错误
	 */
	public static RuleChoosePersonIterator findByCondition(String condition)
	throws SystemException {
		return new RuleChoosePersonIterator(condition);
	}
	/**
	 * 查找符合条件的记录
	 * @return RuleChoosePersonIterator
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	*/
	public static RuleChoosePersonIterator findByCondition(String condition ,
			int start , int num) {
		return new RuleChoosePersonIterator(condition, start, num);
	}
	/**
	 * 删除一条记录
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(RuleChoosePerson.TABLENAME, id);
	}
	/**
	 * 获取符合条件记录的数目
	 * @param conditionStr 条件
	 * @return int
	 */
	public static int getRcpsCount(String conditionStr) {
		return SysFunction.getCnt(RuleChoosePerson.TABLENAME, conditionStr);
	}
}
