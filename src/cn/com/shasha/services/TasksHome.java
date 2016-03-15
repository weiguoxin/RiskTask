package cn.com.shasha.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;
import cn.com.shasha.sys.Article;
import cn.com.shasha.sys.ArticleHome;

/**
 * @author liukh
 */

public class TasksHome {
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return Iterator
	 * @throws SystemException �˻����
	 */
	public static TasksIterator findBySQL(String sql)
	throws SystemException {
		return TasksIterator.findBySQL(sql);
	}
	/**
	 * ����������ȡ�û��б�
	 * @param conditionStr ��ѯ������
	 * @return UserIterator
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static TasksIterator findByCondition(String conditionStr)
	throws SystemException {
		TasksIterator userIterator = null;
		userIterator = new TasksIterator(conditionStr);
		return userIterator;
	}
	/**
	 * ���ݲ�ѯ���� ����startIndex��ʼ�����num��User������б�
	 * @param conditionStr ��ѯ����
	 * @param startIndex ��ʼλ��
	 * @param num Ҫ�󷵻ض������
	 * @return UserIterator �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static TasksIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		TasksIterator userIterator = null;
		userIterator = new TasksIterator(conditionStr, startIndex, num);
		return userIterator;
	}
	/**
	 * �����û���� �����һ��User����
	 * @param id int �û����
	 * @return User �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static Tasks findById(int userid) throws SystemException {
		Tasks user = new Tasks(userid);
		return user;
	}
	/**
	 * ����һ���µ�User����
	 * @param username ������
	 * @return User ����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static Tasks create(String title) throws SystemException {
		Tasks grds = null;
		if ((title == null) || ("".equals(title))) {
			throw new SystemException("null or empty parameter 'title'");
		} else {
			grds = new Tasks(title);
		}
		return grds;
	}
	/**
	 * ���ݲ�ѯ���� ����÷���������User����ĸ���
	 * @param conditionStr ��ѯ����
	 * @return �û��������
	 */
	public static int getTasksCount(String conditionStr) {
		return SysFunction.getCnt(Tasks.TABLENAME, conditionStr);
	}
	/**
	 * ɾ��һ��User����
	 * @param id �û����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static void remove(int id) throws SystemException {
		TasksHome.findById(id).remove();
	}

}
