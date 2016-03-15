/*
 * �������� 2005-7-19
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class IAMappingHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public IAMappingHome () {
	}
	/**
	 * ����������֮���ӳ��
	 * @param appname Ӧ������
	 * @return Ӧ��
	 */
	public static IAMapping create(int ideaid, int actid) {
	    IAMapping iam = null;
		try {
			iam = new IAMapping(ideaid, actid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iam;
	}
	/**
	 * ����ID����Ӧ��
	 * @param id Ӧ��ID
	 * @return Ӧ��
	 */
	public static IAMapping findById(int id) {
	    IAMapping iam = null;
		try {
			iam = new IAMapping(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iam;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return AppIterator
	 * @throws SystemException �˻����
	 */
	public static IAMappingIterator findBySQL(String sql)
	throws SystemException {
		return IAMappingIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return AppIterator
	 * @throws SystemException �������
	 */
	public static IAMappingIterator findByCondition(String condition) throws SystemException {
		return new IAMappingIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return AppIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static IAMappingIterator findByCondition(String condition ,
			int start , int num) {
		return new IAMappingIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(IAMapping.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getIAMappingCount(String conditionStr) {
		return SysFunction.getCnt(IAMapping.TABLENAME, conditionStr);
	}
}
