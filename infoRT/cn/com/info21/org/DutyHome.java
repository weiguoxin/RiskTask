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
	 * ���캯��
	 * DutyHome()
	 */
	public DutyHome() {
	}
	/**
	 * ����������ȡְ���б�
	 * @param conditionStr ��ѯ������
	 * @return DutyIterator
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static DutyIterator findByCondition(String conditionStr)
	throws SystemException {
		DutyIterator dutyIterator = null;
		dutyIterator = new DutyIterator(conditionStr);
		return dutyIterator;
	}
	/**
	 * ���ݲ�ѯ���� ����startIndex��ʼ�����num��Duty������б�
	 * @param conditionStr ��ѯ����
	 * @param startIndex ��ʼλ��
	 * @param num Ҫ�󷵻ض������
	 * @return DutyIterator ְ�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static DutyIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		DutyIterator dutyIterator = null;
		dutyIterator = new DutyIterator(conditionStr, startIndex, num);
		return dutyIterator;
	}
	/**
	 * ����ְ���� �����һ��Duty����
	 * @param id int ְ����
	 * @return Duty ְ�����
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
	 * ����һ���µ�Duty����
	 * @param dutyname �ַ���
	 * @return Duty ����
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
	 * ����ְ����� �����һ��Duty����
	 * @param code ְ����
	 * @return ְ�����
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
	 * ���ݲ�ѯ���� ����÷���������Duty����ĸ���
	 * @param conditionStr ��ѯ����
	 * @return ְ��������
	 */
	public static int getDutyCount(String conditionStr) {
		return SysFunction.getCnt(Duty.TABLENAME, conditionStr);
	}
	/**
	 * ɾ��һ��Duty����
	 * @param id ְ����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static void remove(int id) throws SystemException {
		SysFunction.delRecord(Duty.TABLENAME, id);
	}
}
