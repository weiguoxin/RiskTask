/*
 * �������� 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class TrackHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public TrackHome() {
	}
	/**
	 * �����
	 * @param flowid ����ʵ��ID
	 * @return ����
	 */
	public static Track create(String flowid) {
		Track flow = null;
		try {
			flow = new Track(flowid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flow;
   }

	/**
	 * ����ID���ҹ���
	 * @param id ���̸������к�
	 * @return ����
	 */
	public static Track findById(int id) {
		Track flowid = null;
		try {
			flowid = new Track(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return TrackIterator
	 * @throws SystemException �˻����
	 */
	public static TrackIterator findBySQL(String sql)
	throws SystemException {
		return TrackIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return TrackIterator
	 * @throws SystemException �������
	 */
	public static TrackIterator findByCondition(String condition)
	throws SystemException {
		return new TrackIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return TrackIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static TrackIterator findByCondition(String condition ,
			int start , int num) {
		return new TrackIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Track.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getTrackCount(String conditionStr) {
		return SysFunction.getCnt(Track.TABLENAME, conditionStr);
	}
}
