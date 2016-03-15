/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class UserHome {
	/**
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return Iterator
	 * @throws SystemException �˻����
	 */
	public static UserIterator findBySQL(String sql)
	throws SystemException {
		return UserIterator.findBySQL(sql);
	}
	/**
	 * ����������ȡ�û��б�
	 * @param conditionStr ��ѯ������
	 * @return UserIterator
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static UserIterator findByCondition(String conditionStr)
	throws SystemException {
		UserIterator userIterator = null;
		userIterator = new UserIterator(conditionStr);
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
	public static UserIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		UserIterator userIterator = null;
		userIterator = new UserIterator(conditionStr, startIndex, num);
		return userIterator;
	}
	/**
	 * �����û���� �����һ��User����
	 * @param id int �û����
	 * @return User �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static User findById(int id) throws SystemException {
		User user = new User(id);
		return user;
	}
	/**
	 * �����û����� �����һ��User����
	 * @param code �û����
	 * @return �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static User findByCode(String code) throws SystemException {
		User user = null;
		UserIterator userIterator = null;
		userIterator = new UserIterator("UPPER(id) = '" + code.toUpperCase() + "'");
		try {
			user = (User) userIterator.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * �����û����� �����һ��User����
	 * @param username �û���
	 * @return �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static User findByUserName(String username) throws SystemException {
		User user = null;
		UserIterator userIterator = null;
		userIterator = new UserIterator("username = '" + username + "'");
		try {
			if (userIterator.hasNext()) {
				user = (User) userIterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * ����һ���µ�User����
	 * @param username ������
	 * @return User ����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static User create(String username) throws SystemException {
		User user = null;
		try {
			//�½��û�
			user = new User(username);
		} catch (SystemException se) {
			System.out.println("user create error in UserHome");
			try {
				if (user != null) {
					UserHome.remove(user.getId());
				}
			} catch (Exception e2) {
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("user create error in UserHome");
			try {
				if (user != null) {
					UserHome.remove(user.getId());
				}
			} catch (Exception e2) {
				System.out.println();
			}
		}
		return  user;
	}
	/**
	 * ���ݲ�ѯ���� ����÷���������User����ĸ���
	 * @param conditionStr ��ѯ����
	 * @return �û��������
	 */
	public static int getUserCount(String conditionStr) {
		return SysFunction.getCnt(User.TABLENAME, conditionStr);
	}
	/**
	 * ɾ��һ��User����
	 * @param id �û����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static void remove(int id) throws SystemException {
		SysFunction.delRecord(User.TABLENAME, id);
	}

}
