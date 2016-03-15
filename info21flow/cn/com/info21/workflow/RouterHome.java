/*
 * �������� 2005-7-20
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RouterHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RouterHome() {
	}
	/**
	 * �����
	 * @param actid �ID
	 * @return ����
	 */
	public static Router create(String actid) {
		Router router = null;
		try {
			router = new Router(actid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return router;
	}
	/**
	 * ����ID����·����
	 * @param id ·����ID
	 * @return ·����
	 */
	public static Router findById(int id) {
		Router router = null;
		try {
			router = new Router(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return router;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return RouterIterator
	 * @throws SystemException �˻����
	 */
	public static RouterIterator findBySQL(String sql)
	throws SystemException {
		return RouterIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return RouterIterator
	 * @throws SystemException �������
	 */
	public static RouterIterator findByCondition(String condition)
	throws SystemException {
		return new RouterIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return RouterIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static RouterIterator findByCondition(String condition ,
			int start , int num) {
		return new RouterIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Router.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getRouterCount(String conditionStr) {
		return SysFunction.getCnt(Router.TABLENAME, conditionStr);
	}
}
