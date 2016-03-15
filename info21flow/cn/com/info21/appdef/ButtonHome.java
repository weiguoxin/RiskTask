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
public class ButtonHome {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public ButtonHome() {
	}
	/**
	 * �����
	 * @param name ��ť����
	 * @return ��ť
	 */
	public static Button create(String name) {
		Button button = null;
		try {
			button = new Button(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return button;
	}
	/**
	 * ����ID���Ұ�ť
	 * @param id Ψһ�Ա��ID
	 * @return ��ť
	 */
	public static Button findById(int id) {
		Button button = null;
		try {
			button = new Button(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return button;
	}
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return ButtonIterator
	 * @throws SystemException �˻����
	 */
	public static ButtonIterator findBySQL(String sql)
	throws SystemException {
		return ButtonIterator.findBySQL(sql);
	}
	/**
	 * ���캯��
	 * @param condition ��ѯ����
	 * @return ButtonIterator
	 * @throws SystemException �������
	 */
	public static ButtonIterator findByCondition(String condition)
	throws SystemException {
		return new ButtonIterator(condition);
	}
	/**
	 * ���ҷ��������ļ�¼
	 * @return ButtonIterator
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ����
	*/
	public static ButtonIterator findByCondition(String condition ,
			int start , int num) {
		return new ButtonIterator(condition, start, num);
	}
	/**
	 * ɾ��һ����¼
	 * @param id int
	 */
	public static void remove(int id) {
		SysFunction.delRecord(Button.TABLENAME, id);
	}
	/**
	 * ��ȡ����������¼����Ŀ
	 * @param conditionStr ����
	 * @return int
	 */
	public static int getButtonCount(String conditionStr) {
		return SysFunction.getCnt(Button.TABLENAME, conditionStr);
	}
}
