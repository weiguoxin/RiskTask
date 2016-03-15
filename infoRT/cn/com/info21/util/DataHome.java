/*
 * Created on 2004-7-22
 */

package cn.com.info21.util;
import java.sql.Timestamp;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Administrator
 */

public class DataHome {

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public DataHome() {
	}
	/**
	 * ��������
	 * @param pid PID
	 * @return ����
	 */
	public static Data create(Timestamp recTime) {
		System.out.println("VIEW can't create!");
		return null;
	}
	public static Data create(String tablename) {
		Data rtn = null;
		try {
			rtn = new Data(tablename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	/**
	 * ����ID���Ҷ���
	 * @param id ����ID
	 * @return ����
	 */
	public static Data findById(String id, String tablename) {
		Data obj = null;
		try {
			obj = new Data(id, tablename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return Iterator
	 * @throws SystemException �˻����
	 */
	public static DataIterator findBySQL(String sql)
	throws SystemException {
		return DataIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return Iterator
	 * @throws SystemException �������
	 */
	public static DataIterator findByCondition(String condition, String tablename)
			throws SystemException {
		return new DataIterator(condition, tablename);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return Iterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static DataIterator findByCondition(String condition, String tablename,
			int start , int num) {
		return new DataIterator(condition, tablename, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		System.out.println("VIEW can't remove!");
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getCount(String conditionStr, String tablename) {
		return SysFunction.getCnt(tablename, conditionStr);
	}
}
