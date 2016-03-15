/*
 * �������� 2005-7-19
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class ActivityHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public ActivityHome() {
	}
	/**
	 * �����
	 * @param name �����
	 * @return �
	 */
	public static Activity create(String name) {
		Activity acti = null;
		try {
			acti = new Activity(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acti;
	}
	/**
	 * ����ID���һ
	 * @param id �ID
	 * @return �
	 */
	public static Activity findById(int id) {
		Activity acti = null;
		try {
			acti = new Activity(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acti;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return ActivityIterator
	 * @throws SystemException �˻����
	 */
	public static ActivityIterator findBySQL(String sql)
	throws SystemException {
		return ActivityIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return ActivityIterator
	 * @throws SystemException �������
	 */
	public static ActivityIterator findByCondition(String condition)
	throws SystemException {
		return new ActivityIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return ActivityIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static ActivityIterator findByCondition(String condition ,
			int start , int num) {
		return new ActivityIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Activity.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getActivityCount(String conditionStr) {
		return SysFunction.getCnt(Activity.TABLENAME, conditionStr);
	}

}
