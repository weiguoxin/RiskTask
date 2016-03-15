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
	 * Ĭ�Ϲ��캯��
	 * @author liusihde
	 * DeptHome()
	 */
	public DeptHome () {
	}
	/**
	 * ����һ���µ�Dept����
	 * @param deptname �ַ���
	 * @return Dept ����
	 */
	public static Dept create(String deptname) {
		Dept dept = null;
		try {
			//�½�����
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
	 * ��ID��ֵ
	 * @param id id��
	 * @return Dept ����
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
	 * ��������ѯ
	 * @param condition ����
	 * @return DeptIterator ����
	 */
	public static DeptIterator findByCondition(String condition) {
		return new DeptIterator(condition);
	}
	/**
	 * ��������ѯ
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	 * @return DeptIterator ����
	 */
	public static DeptIterator findByCondition(String condition ,
			int start , int num) {
		return new DeptIterator(condition, start, num);
	}
	/**
	 * ɾ��
	 * @param id ��id��
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Dept.TABLENAME, id);
	}
	/**
	 * �������
	 * @param condition ����
	 * @return int ����
	 */
	public static int getCnt(String condition) {
		return SysFunction.getCnt(Dept.TABLENAME, condition);
	}
}