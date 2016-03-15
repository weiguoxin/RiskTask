/*
 * �������� 2005-9-2
 */
package cn.com.info21.workflow;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class FlowTaskMappingHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public FlowTaskMappingHome() {
	}
	public static FlowTaskMapping findById(int id) {
	    FlowTaskMapping ftm = null;
		try {
			ftm = new FlowTaskMapping(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ftm;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return FlowTaskMappingIterator
	 * @throws SystemException �˻����
	 */
	public static FlowTaskMappingIterator findBySQL(String sql)
	throws SystemException {
		return FlowTaskMappingIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return FlowTaskMappingIterator
	 * @throws SystemException �������
	 */
	public static FlowTaskMappingIterator findByCondition(String condition)
	throws SystemException {
		return new FlowTaskMappingIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return FlowTaskMappingIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static FlowTaskMappingIterator findByCondition(String condition ,
			int start , int num) {
		return new FlowTaskMappingIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Flow.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getFlowTaskMappingCount(String conditionStr) {
		return SysFunction.getCnt(FlowTaskMapping.TABLENAME, conditionStr);
	}
}
