/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class OrgEntryUserHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public OrgEntryUserHome() {
	}
	/**
	 * �����
	 * @param appname Ӧ������
	 * @return Ӧ��
	 */
	public static OrgEntryUser create(String username) {
		OrgEntryUser wa = null;
		try {
			wa = new OrgEntryUser(username);
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
	public static OrgEntryUser findById(int id) {
		OrgEntryUser wa = null;
		try {
			wa = new OrgEntryUser(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wa;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return OrgEntryUserIterator
	 * @throws SystemException �˻����
	 */
	public static OrgEntryUserIterator findBySQL(String sql)
	throws SystemException {
		return OrgEntryUserIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return OrgEntryUserIterator
	 * @throws SystemException �������
	 */
	public static OrgEntryUserIterator findByCondition(String condition) throws SystemException {
		return new OrgEntryUserIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return OrgEntryUserIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static OrgEntryUserIterator findByCondition(String condition ,
			int start , int num) {
		return new OrgEntryUserIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(OrgEntryUser.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getBasedataCount(String conditionStr) {
		return SysFunction.getCnt(OrgEntryUser.TABLENAME, conditionStr);
	}
}

