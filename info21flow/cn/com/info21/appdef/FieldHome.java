/*
 * �������� 2005-8-1
 *
 */
package cn.com.info21.appdef;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 *
 */
public class FieldHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public FieldHome() {
	}
	/**
	 * ������
	 * @param fieldname ������
	 * @return ��
	 */
	public static Field create(String fieldname) {
		Field field = null;
		try {
			field = new Field(fieldname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}
	/**
	 * ����ID������
	 * @param id ��ID
	 * @return ��
	 */
	public static Field findById(int id) {
		Field acti = null;
		try {
			acti = new Field(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acti;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return FieldIterator
	 * @throws SystemException �˻����
	 */
	public static FieldIterator findBySQL(String sql)
	throws SystemException {
		return FieldIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return FieldIterator
	 * @throws SystemException �������
	 */
	public static FieldIterator findByCondition(String condition)
	throws SystemException {
		return new FieldIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return FieldIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static FieldIterator findByCondition(String condition ,
			int start , int num) {
		return new FieldIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Field.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getProcCount(String conditionStr) {
		return SysFunction.getCnt(Field.TABLENAME, conditionStr);
	}
}
