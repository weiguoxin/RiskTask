package cn.com.shasha.sys;

import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

public class TaskManHome {
	/**
	* �����ĵ����⣬����һ��Article����
	* �������ڣ�(2003-6-8 12:21:00)
	* @param id int �ĵ���š�
	* @param title String ���⡣
	* @return Article �ĵ�����
	* @throws cn.com.info21.system.SystemException��
	*/
	public static TaskMan create(String title) throws SystemException {
        TaskMan article = null;
		if ((title == null) || ("".equals(title))) {
			throw new SystemException("null or empty parameter 'title'");
		} else {
			article = new TaskMan(title);
		}
		return article;
	}
	/**
		* ���ݲ�ѯ���� �����һ��Article�����б�
		* �������ڣ�(2003-6-7 23:30:00)
		* @param condition String ��ѯ������
		* @return ArticleIterator��
		* @throws cn.com.info21.system.SystemException��
		*/
	public static TaskManIterator findByCondition(String conditionStr)
		throws SystemException {
        TaskManIterator articleIterator = null;
		articleIterator = new TaskManIterator(conditionStr);
		return articleIterator;
	}
	/**
		* ���ݲ�ѯ���� ����startIndex��ʼ�����num��Article������б�
		* �������ڣ�(2003-6-7 23:39:12)
		* @param condition String ��ѯ������
		* @param startIndex int ��ʼλ�á�
		* @param num int Ҫ�󷵻ض��������
		* @return ArticleIterator ����
		* @throws cn.com.info21.system.SystemException��
		*/
	public static TaskManIterator findByCondition(
		String conditionStr,
		int startIndex,
		int num)
		throws SystemException {
        TaskManIterator articleIterator = null;
		articleIterator = new TaskManIterator(conditionStr, startIndex, num);
		return articleIterator;
	}
	/**
	* �����ĵ���� �����һ��Article����
	* �������ڣ�(2003-6-7 23:39:20)
	* @param id int �ĵ���š�
	* @return Article ����
	* @throws cn.com.info21.system.SystemException��
	*/
	public static TaskMan findById(int id) throws SystemException {
        TaskMan article = new TaskMan(id);
		return article;
	}

	/**
	* ���ݲ�ѯ���� ����÷���������Article����ĸ�����
	* �������ڣ�(2003-6-7 23:40:08)
	* @param condition String ��ѯ������
	* @return int �û����������
	* @throws cn.com.info21.system.SystemException��
	*/
	public static int getTaskManCount(String conditionStr)
		throws SystemException {
		return SysFunction.getCnt("TASKMAN", conditionStr);
	}
	/**
	* �����ĵ����
	* �������ڣ�(2003-6-7 23:44:12)
	* @param id int �ĵ���š�
	* @return Article void��
	* @throws cn.com.info21.system.SystemException��
	*/
	public static void remove(int id) throws SystemException {
		TaskManHome.findById(id).remove();
	}
}