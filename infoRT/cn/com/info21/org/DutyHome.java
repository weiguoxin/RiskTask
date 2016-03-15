/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;
//import cn.com.info21.system.*;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */
public class DutyHome {
	/**
	 * 构造函数
	 * DutyHome()
	 */
	public DutyHome() {
	}
	/**
	 * 根据条件获取职务列表
	 * @param conditionStr 查询条件。
	 * @return DutyIterator
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static DutyIterator findByCondition(String conditionStr)
	throws SystemException {
		DutyIterator dutyIterator = null;
		dutyIterator = new DutyIterator(conditionStr);
		return dutyIterator;
	}
	/**
	 * 根据查询条件 ，从startIndex开始，获得num个Duty对象的列表
	 * @param conditionStr 查询条件
	 * @param startIndex 开始位置
	 * @param num 要求返回对象个数
	 * @return DutyIterator 职务对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static DutyIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		DutyIterator dutyIterator = null;
		dutyIterator = new DutyIterator(conditionStr, startIndex, num);
		return dutyIterator;
	}
	/**
	 * 根据职务编号 ，获得一个Duty对象
	 * @param id int 职务编号
	 * @return Duty 职务对象
	 */
	public static Duty findById(int id) {
		Duty duty = null;
		try {
			duty = new Duty(id);
		} catch (SystemException e) {
			System.out.println("duty find error in DutyHome");
		} catch (Exception e) {
			System.out.println("duty find error in DutyHome");
		}
		return duty;
	}
	/**
	 * 创建一个新的Duty对象
	 * @param dutyname 字符串
	 * @return Duty 对象
	 */
	public static Duty create(String dutyname) {
		Duty duty = null;
		try {
			duty = new Duty(dutyname);
		} catch (SystemException e) {
			System.out.println("duty create error in DutyHome");
		} catch (Exception e) {
			System.out.println("duty create error in DutyHome");
		}
		return duty;
	}
	/**
	 * 根据职务代码 ，获得一个Duty对象
	 * @param code 职务编号
	 * @return 职务对象
	 */
	public static Duty findByCode(String code) {
		Duty duty = null;
		try {
			DutyIterator dutyIterator = null;
			dutyIterator = new DutyIterator("UPPER(id) = '" + code.toUpperCase() + "'");
			duty = (Duty) dutyIterator.next();
		} catch (Exception e) {
			System.out.println("Duty find by condition error in DutyHome");
		}
		return duty;
	}
	/**
	 * 根据查询条件 ，获得符合条件的Duty对象的个数
	 * @param conditionStr 查询条件
	 * @return 职务对象个数
	 */
	public static int getDutyCount(String conditionStr) {
		return SysFunction.getCnt(Duty.TABLENAME, conditionStr);
	}
	/**
	 * 删除一个Duty对象
	 * @param id 职务编号
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static void remove(int id) throws SystemException {
		SysFunction.delRecord(Duty.TABLENAME, id);
	}
}
