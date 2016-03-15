/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class OrgEntryHome {
	/**
	 * ����������ȡ�û��б�
	 * @param conditionStr ��ѯ������
	 * @return UserIterator
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static OrgEntryIterator findByCondition(String conditionStr)
	throws SystemException {
		OrgEntryIterator iter = null;
		iter = new OrgEntryIterator(conditionStr);
		return iter;
	}
	/**
	 * ���ݲ�ѯ���� ����startIndex��ʼ�����num��User������б�
	 * @param conditionStr ��ѯ����
	 * @param startIndex ��ʼλ��
	 * @param num Ҫ�󷵻ض������
	 * @return UserIterator �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static OrgEntryIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		OrgEntryIterator iter = null;
		iter = new OrgEntryIterator(conditionStr, "entrytype desc", startIndex, num);
		return iter;
	}
	/**
	 * �����û���� �����һ��User����
	 * @param id int �û����
	 * @return User �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static OrgEntry findById(String id) throws SystemException {
		OrgEntry entry = new OrgEntry(id);
		return entry;
	}
	/**
	 * �����û���� �����һ��User����
	 * @param id0 �û����
	 * @param etype entry����
	 * @return User �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static OrgEntry find(int etype, int id0) throws SystemException {
		OrgEntry entry = new OrgEntry(etype, id0);
		return entry;
	}
	/**
	 * �����û����� �����һ��User����
	 * @param code �û����
	 * @return �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static OrgEntry findByCode(String code) throws SystemException {
		OrgEntry entry = null;
		OrgEntryIterator iter = null;
		iter = new OrgEntryIterator("UPPER(id) = '" + code.toUpperCase() + "'");
		try {
			entry = (OrgEntry) iter.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entry;
	}
	/**
	 * ����һ���µ�User����
	 * @param user ������
	 * @return User ����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static User create(String user) throws SystemException {
		return new User(user);
	}
	/**
	 * ���ݲ�ѯ���� ����÷���������User����ĸ���
	 * @param conditionStr ��ѯ����
	 * @return �û��������
	 */
	public static int getUserCount(String conditionStr) {
		return SysFunction.getCnt(OrgEntry.TABLENAME, conditionStr);
	}
	/**
	 * �������
	 * @param condition ����
	 * @return int ����
	 */
	public static int getCnt(String condition) {
		return SysFunction.getCnt(OrgEntry.TABLENAME, condition);
	}
	/**
	 * ɾ��һ��User����
	 * @param id �û����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static void remove(String id) throws SystemException {
		OrgEntry entry = new OrgEntry(id);
		Role role = entry.getRole();
		if (role != null) {
			RoleHome.remove(role.getId());
		}
		switch (entry.getEtype()) {
			case 0:
				User u = (User) entry.getObj();
				UserHome.remove(((User) entry.getObj()).getId());
				break;
			case 1:
				DeptHome.remove(((Dept) entry.getObj()).getId());
				break;
			default:
		}
		//SysFunction.delRecord(OrgEntry.TABLENAME, id);
	}
	/**
	 * �жϵ�ǰ�����Ƿ�����Ӳ���
	 * @param id0 ������ID
	 * @return boolean
	 * @throws SystemException ϵͳ�쳣
	 */
	public static  boolean hasSubDept(int id0) throws SystemException {
		boolean flag = false;
		try {
			String sql = "entrytype=1 and parentid=" + id0;
			OrgEntryIterator oi = OrgEntryHome.findByCondition(sql);
			if (null != oi) {
				if (oi.hasNext()) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}

