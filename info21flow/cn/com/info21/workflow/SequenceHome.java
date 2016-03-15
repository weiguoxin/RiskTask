/*
 * �������� 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class SequenceHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public SequenceHome() {
	}
	/**
	 * �����
	 * @param flowid �����������̵�ID
	 * @return ����
	 */
	public static Sequence create(String flowid) {
		Sequence flow = null;
		try {
			flow = new Sequence(flowid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flow;
   }

	/**
	 * ����ID���ҹ���
	 * @param id �û��Զ�������ID
	 * @return ����
	 */
	public static Sequence findById(int id) {
		Sequence flowid = null;
		try {
			flowid = new Sequence(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return SequenceIterator
	 * @throws SystemException �˻����
	 */
	public static SequenceIterator findBySQL(String sql)
	throws SystemException {
		return SequenceIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return SequenceIterator
	 * @throws SystemException �������
	 */
	public static SequenceIterator findByCondition(String condition)
	throws SystemException {
		return new SequenceIterator(condition);
	}
	/** 
	 * ���ҷ��������ļ�¼
	 * @return SequenceIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static SequenceIterator findByCondition(String condition ,
			int start , int num) {
		return new SequenceIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Sequence.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getSequenceCount(String conditionStr) {
		return SysFunction.getCnt(Sequence.TABLENAME, conditionStr);
	}
}
