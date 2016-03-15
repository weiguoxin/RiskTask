/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;
//import cn.com.info21.system.*;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;
/**
 * @author liusihde
 *
 */
public class DeptHome {
	/**
	 * 默认构造函数
	 * @author liusihde
	 * DeptHome()
	 */
	public DeptHome () {
	}
	/**
	 * 创建一个新的Dept对象
	 * @param deptname 字符串
	 * @return Dept 对象
	 */
	public static Dept create(String deptname) {
		Dept dept = null;
		try {
			//新建部门
			dept = new Dept(deptname);
		} catch (SystemException se) {
			System.out.println("dept create error in DeptHome");
			try {
				if (dept != null) {
					DeptHome.remove(dept.getId());
				}
			} catch (Exception e2) {
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("dept create error in DeptHome");
			try {
				if (dept != null) {
					DeptHome.remove(dept.getId());
				}
			} catch (Exception e2) {
				System.out.println();
			}
		}
		return dept;
	}
	/**
	 * 按ID找值
	 * @param id id号
	 * @return Dept 对象
	 */
	public static Dept findById(int id) {
		Dept dept = null;
		try {
			dept = new Dept(id);
		} catch (SystemException e) {
			System.out.println("dept find error in DeptHome");
		} catch (Exception e) {
			System.out.println("dept find error in DeptHome");
		}
		return dept;
	}
	/**
	 * 按条件查询
	 * @param condition 条件
	 * @return DeptIterator 对象
	 */
	public static DeptIterator findByCondition(String condition) {
		return new DeptIterator(condition);
	}
	/**
	 * 按条件查询
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 行数
	 * @return DeptIterator 对象
	 */
	public static DeptIterator findByCondition(String condition ,
			int start , int num) {
		return new DeptIterator(condition, start, num);
	}
	/**
	 * 删除
	 * @param id 是id号
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Dept.TABLENAME, id);
	}
	/**
	 * 获得行数
	 * @param condition 条件
	 * @return int 行数
	 */
	public static int getCnt(String condition) {
		return SysFunction.getCnt(Dept.TABLENAME, condition);
	}
}