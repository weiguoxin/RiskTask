/*
 * �������� 2006-1-16
 */

package cn.com.info21.org;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author songle
 */

public class UserdeptHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public UserdeptHome() {
	}
	/**
	 * ������Ϣ��
	 * @param userid
	 * @return ��Ա������ϢID
	 */
	public static Userdept create(String userid) {
		Userdept obj = null;
		try {
			obj = new Userdept(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
   }

	/**
	 * ����ID������Ϣ��
	 * @param pid ��Ա������ϢID
	 * @return ��Ϣ��
	 */
	public static Userdept findById(int id) {
		Userdept ep = null;
		try {
			ep = new Userdept(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ep;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return UserdeptIterator
	 * @throws SystemException �˻����
	 */
	public static UserdeptIterator findBySQL(String sql)
	throws SystemException {
		return UserdeptIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return UserdeptIterator
	 * @throws SystemException �������
	 */
	public static UserdeptIterator findByCondition(String condition)
	throws SystemException {
		return new UserdeptIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return UserdeptIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static UserdeptIterator findByCondition(String condition ,
			int start , int num) {
		return new UserdeptIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Userdept.TABLENAME, id);
	}

	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(Userdept.TABLENAME, conditionStr);
	}
}