/*
 * �������� 2005-7-19
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class AppHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public AppHome() {
	}
	/**
	 * �����
	 * @param appname Ӧ������
	 * @return Ӧ��
	 */
	public static App create(String appname) {
		App wa = null;
		try {
			wa = new App(appname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wa;
	}
	/**
	 * ����ID����Ӧ��
	 * @param id Ӧ��ID
	 * @return Ӧ��
	 */
	public static App findById(int id) {
		App wa = null;
		try {
			wa = new App(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wa;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return AppIterator
	 * @throws SystemException �˻����
	 */
	public static AppIterator findBySQL(String sql)
	throws SystemException {
		return AppIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return AppIterator
	 * @throws SystemException �������
	 */
	public static AppIterator findByCondition(String condition) throws SystemException {
		return new AppIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return AppIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static AppIterator findByCondition(String condition ,
			int start , int num) {
		return new AppIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(App.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(App.TABLENAME, conditionStr);
	}
}
