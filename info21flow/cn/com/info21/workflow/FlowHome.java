/*
 * �������� 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class FlowHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public FlowHome() {
	}
	/**
	 * �����
	 * @param procid ��������
	 * @return ����
	 */
	public static Flow create(String procid) {
		Flow flow = null;
		try {
			flow = new Flow(procid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flow;
   }

	/**
	 * ����ID���ҹ���
	 * @param id ����ID
	 * @return ����
	 */
	public static Flow findById(int id) {
		Flow procid = null;
		try {
			procid = new Flow(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return procid;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return FlowIterator
	 * @throws SystemException �˻����
	 */
	public static FlowIterator findBySQL(String sql)
	throws SystemException {
		return FlowIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return FlowIterator
	 * @throws SystemException �������
	 */
	public static FlowIterator findByCondition(String condition)
	throws SystemException {
		return new FlowIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return FlowIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static FlowIterator findByCondition(String condition ,
			int start , int num) {
		return new FlowIterator(condition, start, num);
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
	public static int getFlowCount(String conditionStr) {
		return SysFunction.getCnt(Flow.TABLENAME, conditionStr);
	}
}
