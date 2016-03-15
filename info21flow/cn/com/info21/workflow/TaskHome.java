/*
 * �������� 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class TaskHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public TaskHome() {
	}
	/**
	 * �����
	 * @param procid ��������
	 * @return Task ����
	 */
	public static Task create(String procid) {
		Task flow = null;
		try {
			flow = new Task(procid);
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
	public static Task findById(int id) {
		Task flowid = null;
		try {
			flowid = new Task(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return TaskIterator
	 * @throws SystemException �˻����
	 */
	public static TaskIterator findBySQL(String sql)
	throws SystemException {
		return TaskIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return TaskIterator
	 * @throws SystemException �������
	 */
	public static TaskIterator findByCondition(String condition)
	throws SystemException {
		return new TaskIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return TaskIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static TaskIterator findByCondition(String condition ,
			int start , int num) {
		return new TaskIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Task.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getTaskCount(String conditionStr) {
		return SysFunction.getCnt(Task.TABLENAME, conditionStr);
	}
}
