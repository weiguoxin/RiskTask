/*
 * �������� 2005-7-20
 */
package cn.com.info21.workflow;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RuleChoosePersonHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RuleChoosePersonHome() {
	}
	/**
	 * �����
	 * @param rule ��������
	 * @return ����
	 */
	public static RuleChoosePerson create(String rule) {
		RuleChoosePerson rcps = null;
		try {
			rcps = new RuleChoosePerson(rule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcps;
	}
	/**
	 * ����ID����ѡ����Ա����
	 * @param id ѡ����Ա����ID
	 * @return ����
	 */
	public static RuleChoosePerson findById(int id) {
		RuleChoosePerson rcps = null;
		try {
			rcps = new RuleChoosePerson(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcps;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return RuleChoosePersonIterator
	 * @throws SystemException �˻����
	 */
	public static RuleChoosePersonIterator findBySQL(String sql)
	throws SystemException {
		return RuleChoosePersonIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return RuleChoosePersonIterator
	 * @throws SystemException �������
	 */
	public static RuleChoosePersonIterator findByCondition(String condition)
	throws SystemException {
		return new RuleChoosePersonIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return RuleChoosePersonIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static RuleChoosePersonIterator findByCondition(String condition ,
			int start , int num) {
		return new RuleChoosePersonIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(RuleChoosePerson.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getRcpsCount(String conditionStr) {
		return SysFunction.getCnt(RuleChoosePerson.TABLENAME, conditionStr);
	}
}
