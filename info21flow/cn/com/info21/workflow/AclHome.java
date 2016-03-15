/*
 * �������� 2005-7-17
 */

package cn.com.info21.workflow;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class AclHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public AclHome() {
	}
	/**
	 * �����
	 * @param flowid ��������
	 * @return ����
	 */
	public static Acl create(String flowid) {
		Acl flow = null;
		try {
			flow = new Acl(flowid);
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
	public static Acl findById(int id) {
		Acl flowid = null;
		try {
			flowid = new Acl(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return AclIterator
	 * @throws SystemException �˻����
	 */
	public static AclIterator findBySQL(String sql)
	throws SystemException {
		return AclIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return AclIterator
	 * @throws SystemException �������
	 */
	public static AclIterator findByCondition(String condition)
	throws SystemException {
		return new AclIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return AclIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static AclIterator findByCondition(String condition ,
			int start , int num) {
		return new AclIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Acl.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(Acl.TABLENAME, conditionStr);
	}
}
