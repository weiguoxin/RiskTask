/*
 * �������� 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author lkh
 */

public class ProcHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public ProcHome() {
	}
	/**
	 * �����
	 * @param procname ��������
	 * @return ����
	 */
	public static Proc create(String procname) {
		Proc proc = null;
		try {
			proc = new Proc(procname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proc;
	}
	/**
	 * ����ID���ҹ���
	 * @param id ����ID
	 * @return ����
	 */
	public static Proc findById(int id) {
		Proc proc = null;
		try {
			proc = new Proc(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proc;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return ProcIterator
	 * @throws SystemException �˻����
	 */
	public static ProcIterator findBySQL(String sql)
	throws SystemException {
		return ProcIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return ProcIterator
	 * @throws SystemException �������
	 */
	public static ProcIterator findByCondition(String condition) throws SystemException {
		return new ProcIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return ProcIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static ProcIterator findByCondition(String condition ,
			int start , int num) {
		return new ProcIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Proc.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(Proc.TABLENAME, conditionStr);
	}
}
