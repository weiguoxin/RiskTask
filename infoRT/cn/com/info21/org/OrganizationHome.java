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
	 * 根据条件获取组织机构列表
	 * @param conditionStr 查询条件。
	 * @return OrganizationIterator
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static OrganizationIterator findByCondition(String conditionStr)
	throws SystemException {
		OrganizationIterator organizationIterator = null;
		organizationIterator = new OrganizationIterator(conditionStr);
		return organizationIterator;
	}
	/**
	 * 根据查询条件 ，从startIndex开始，获得num个Organization对象的列表
	 * @param conditionStr 查询条件
	 * @param startIndex 开始位置
	 * @param num 要求返回对象个数
	 * @return OrganizationIterator 组织机构对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static OrganizationIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		OrganizationIterator organizationIterator = null;
		organizationIterator = new OrganizationIterator(conditionStr, startIndex, num);
		return organizationIterator;
	}
	/**
	 * 根据组织机构编号 ，获得一个Organization对象
	 * @param id int 组织机构编号
	 * @return Organization 组织机构对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static Organization findById(int id) throws SystemException {
		Organization organization = new Organization(id);
		return organization;
	}
	/**
	 * 根据组织机构代码 ，获得一个Organization对象
	 * @param code 组织机构编号
	 * @return 组织机构对象
	 * @throws SystemException 将错误写入到系统日志
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
	 * 创建一个Organization对象
	 * @param orgname 组织名
	 * @return Organization对象
	 * @throws SystemException 把错误写入日志
	 */
	public static Organization create(String orgname) throws SystemException {
		return new Organization(orgname);
	}
	/**
	 * 根据查询条件 ，获得符合条件的Organization对象的个数
	 * @param conditionStr 查询条件
	 * @return 组织机构对象个数
	 */
	public static int getOrganizationCount(String conditionStr) {
		return SysFunction.getCnt(Organization.TABLENAME, conditionStr);
	}
	/**
	 * 删除一个Organization对象
	 * @param id 组织机构编号
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static void remove(int id) throws SystemException {
		SysFunction.delRecord(Organization.TABLENAME, id);
	}
}
