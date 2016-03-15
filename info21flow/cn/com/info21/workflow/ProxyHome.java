/*
 * �������� 2005-8-1
 *
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 *
 */
public class ProxyHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public ProxyHome() {
	}
	/**
	 * �������������
	 * @param consignorid ������ID
	 * @return ���������
	 */
	public static Proxy create(String consignorid) {
		Proxy proxy = null;
		try {
			proxy = new Proxy(consignorid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proxy;
	}
	/**
	 * ����ID���Ҵ��������
	 * @param id ���������ID
	 * @return ���������
	 */
	public static Proxy findById(int id) {
		Proxy proxy = null;
		try {
			proxy = new Proxy(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proxy;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return ProxyIterator
	 * @throws SystemException �˻����
	 */
	public static ProxyIterator findBySQL(String sql)
	throws SystemException {
		return ProxyIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return ProxyIterator
	 * @throws SystemException �������
	 */
	public static ProxyIterator findByCondition(String condition)
	throws SystemException {
		return new ProxyIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return ProxyIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static ProxyIterator findByCondition(String condition ,
			int start , int num) {
		return new ProxyIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Proxy.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getProxyCount(String conditionStr) {
		return SysFunction.getCnt(Proxy.TABLENAME, conditionStr);
	}

}
