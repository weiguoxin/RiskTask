/*
 * �������� 2005-7-20
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RuleRouterHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RuleRouterHome() {
	}
	/**
	 * �����
	 * @param condition ��������
	 * @return ��������
	 */
	public static RuleRouter create(String condition) {
		RuleRouter rurt = null;
		try {
			rurt = new RuleRouter(condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rurt;
	}
	/**
	 * ����ID����·�ɹ���
	 * @param id ·�ɹ���ID
	 * @return ·��
	 */
	public static RuleRouter findById(int id) {
		RuleRouter rurt = null;
		try {
			rurt = new RuleRouter(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rurt;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return RuleRouterIterator
	 * @throws SystemException �˻����
	 */
	public static RuleRouterIterator findBySQL(String sql)
	throws SystemException {
		return RuleRouterIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return RuleRouterIterator
	 * @throws SystemException �������
	 */
	public static RuleRouterIterator findByCondition
	(String condition)
	throws SystemException {
		return new RuleRouterIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return RuleRouterIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static RuleRouterIterator findByCondition(String condition ,
			int start , int num) {
		return new RuleRouterIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(RuleRouter.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(RuleRouter.TABLENAME, conditionStr);
	}
}
