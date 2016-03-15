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
	 * 根据条件获取用户列表
	 * @param conditionStr 查询条件。
	 * @return UserIterator
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static OrgEntryIterator findByCondition(String conditionStr)
	throws SystemException {
		OrgEntryIterator iter = null;
		iter = new OrgEntryIterator(conditionStr);
		return iter;
	}
	/**
	 * 根据查询条件 ，从startIndex开始，获得num个User对象的列表
	 * @param conditionStr 查询条件
	 * @param startIndex 开始位置
	 * @param num 要求返回对象个数
	 * @return UserIterator 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static OrgEntryIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		OrgEntryIterator iter = null;
		iter = new OrgEntryIterator(conditionStr, "entrytype desc", startIndex, num);
		return iter;
	}
	/**
	 * 根据用户编号 ，获得一个User对象
	 * @param id int 用户编号
	 * @return User 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static OrgEntry findById(String id) throws SystemException {
		OrgEntry entry = new OrgEntry(id);
		return entry;
	}
	/**
	 * 根据用户编号 ，获得一个User对象
	 * @param id0 用户编号
	 * @param etype entry类型
	 * @return User 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static OrgEntry find(int etype, int id0) throws SystemException {
		OrgEntry entry = new OrgEntry(etype, id0);
		return entry;
	}
	/**
	 * 根据用户代码 ，获得一个User对象
	 * @param code 用户编号
	 * @return 用户对象
	 * @throws SystemException 将错误写入到系统日志
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
	 * 创建一个新的User对象
	 * @param user 对象名
	 * @return User 对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static User create(String user) throws SystemException {
		return new User(user);
	}
	/**
	 * 根据查询条件 ，获得符合条件的User对象的个数
	 * @param conditionStr 查询条件
	 * @return 用户对象个数
	 */
	public static int getUserCount(String conditionStr) {
		return SysFunction.getCnt(OrgEntry.TABLENAME, conditionStr);
	}
	/**
	 * 获得行数
	 * @param condition 条件
	 * @return int 行数
	 */
	public static int getCnt(String condition) {
		return SysFunction.getCnt(OrgEntry.TABLENAME, condition);
	}
	/**
	 * 删除一个User对象
	 * @param id 用户编号
	 * @throws SystemException 将错误写入到系统日志
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
	 * 判断当前部门是否包含子部门
	 * @param id0 父部门ID
	 * @return boolean
	 * @throws SystemException 系统异常
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

