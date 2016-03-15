/*
 * �������� 2005-7-19
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class FAMappingHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public FAMappingHome () {
	}
	/**
	 * ����������֮���ӳ��
	 * @param appname Ӧ������
	 * @return Ӧ��
	 */
	public static FAMapping create(int fieldid, int actid) {
	    FAMapping fam = null;
		try {
			fam = new FAMapping(fieldid, actid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fam;
	}
	/**
	 * ����ID����Ӧ��
	 * @param id Ӧ��ID
	 * @return Ӧ��
	 */
	public static FAMapping findById(int id) {
	    FAMapping fam = null;
		try {
			fam = new FAMapping(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fam;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return AppIterator
	 * @throws SystemException �˻����
	 */
	public static FAMappingIterator findBySQL(String sql)
	throws SystemException {
		return FAMappingIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return AppIterator
	 * @throws SystemException �������
	 */
	public static FAMappingIterator findByCondition(String condition) throws SystemException {
		return new FAMappingIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return AppIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static FAMappingIterator findByCondition(String condition ,
			int start , int num) {
		return new FAMappingIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(FAMapping.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getFAMappingCount(String conditionStr) {
		return SysFunction.getCnt(FAMapping.TABLENAME, conditionStr);
	}
	public static void removeByXId(int xid) {
	    try {
	        FAMappingIterator fi = FAMappingHome.findByCondition(" fieldid = " + xid);
	        FAMapping fam = null;
	        while (fi.hasNext()) {
	            fam = (FAMapping) fi.next();
	            FAMappingHome.remove(fam.getId());
	        }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
