/*
 * Created on 2004-6-23
 */

package cn.com.info21.org;
//import cn.com.info21.system.*;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;
/**
 * @author liukh
 */

public class OrganizationHome {
	/**
	 * ����������ȡ��֯�����б�
	 * @param conditionStr ��ѯ������
	 * @return OrganizationIterator
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static OrganizationIterator findByCondition(String conditionStr)
	throws SystemException {
		OrganizationIterator organizationIterator = null;
		organizationIterator = new OrganizationIterator(conditionStr);
		return organizationIterator;
	}
	/**
	 * ���ݲ�ѯ���� ����startIndex��ʼ�����num��Organization������б�
	 * @param conditionStr ��ѯ����
	 * @param startIndex ��ʼλ��
	 * @param num Ҫ�󷵻ض������
	 * @return OrganizationIterator ��֯��������
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static OrganizationIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		OrganizationIterator organizationIterator = null;
		organizationIterator = new OrganizationIterator(conditionStr, startIndex, num);
		return organizationIterator;
	}
	/**
	 * ������֯������� �����һ��Organization����
	 * @param id int ��֯�������
	 * @return Organization ��֯��������
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static Organization findById(int id) throws SystemException {
		Organization organization = new Organization(id);
		return organization;
	}
	/**
	 * ������֯�������� �����һ��Organization����
	 * @param code ��֯�������
	 * @return ��֯��������
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static Organization findByCode(String code) throws SystemException {
		Organization organization = null;
		OrganizationIterator organizationIterator = null;
		organizationIterator = new OrganizationIterator("UPPER(id) = '"
				+ code.toUpperCase() + "'");
		try {
			organization = (Organization) organizationIterator.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return organization;
	}
	/**
	 * ����һ��Organization����
	 * @param orgname ��֯��
	 * @return Organization����
	 * @throws SystemException �Ѵ���д����־
	 */
	public static Organization create(String orgname) throws SystemException {
		return new Organization(orgname);
	}
	/**
	 * ���ݲ�ѯ���� ����÷���������Organization����ĸ���
	 * @param conditionStr ��ѯ����
	 * @return ��֯�����������
	 */
	public static int getOrganizationCount(String conditionStr) {
		return SysFunction.getCnt(Organization.TABLENAME, conditionStr);
	}
	/**
	 * ɾ��һ��Organization����
	 * @param id ��֯�������
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static void remove(int id) throws SystemException {
		SysFunction.delRecord(Organization.TABLENAME, id);
	}
}
