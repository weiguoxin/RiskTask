/*
 * �������� 2005-7-20
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RamusHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RamusHome() {
	}
	/**
	 * �����
	 * @param nextprocid Ŀ�����ID
	 * @return Ŀ�����ID
	 */
	public static Ramus create(String nextprocid) {
		Ramus ramus = null;
		try {
			ramus = new Ramus(nextprocid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ramus;
	}
	/**
	 * ����ID���ҹ���
	 * @param id ����ID
	 * @return ����
	 */
	public static Ramus findById(int id) {
		Ramus ramus = null;
		try {
			ramus = new Ramus(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ramus;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return RamusIterator
	 * @throws SystemException �˻����
	 */
	public static RamusIterator findBySQL(String sql)
	throws SystemException {
		return RamusIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return RamusIterator
	 * @throws SystemException �������
	 */
	public static RamusIterator findByCondition(String condition) throws SystemException {
		return new RamusIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return RamusIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static RamusIterator findByCondition(String condition ,
			int start , int num) {
		return new RamusIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Ramus.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getRamusCount(String conditionStr) {
		return SysFunction.getCnt(Ramus.TABLENAME, conditionStr);
	}
}
