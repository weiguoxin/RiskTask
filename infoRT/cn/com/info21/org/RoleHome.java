package cn.com.info21.org;

import cn.com.info21.system.*;
/**
 * @author db2admin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RoleHome {
	public static Role create(String name) throws SystemException {
		Role role = null;
		if ((name==null) || ("".equals(name))){
			throw new SystemException(" 角色名称不能为空！");
		}else{
			role = new Role(name);
		}
		return role;
	}
	/*
	*/
	public static RoleIterator findByCondition(String conditionStr) throws SystemException {
		RoleIterator roleiterator = null;
		roleiterator = new RoleIterator(conditionStr);
		return roleiterator;
	}
	/*
	*/
	public static RoleIterator findByCondition(String conditionStr, int startIndex, int num) throws SystemException {
		RoleIterator roleiterator = null;
		roleiterator = new RoleIterator(conditionStr,startIndex,num);
		return roleiterator;
	}
	/*
	*/
	public static Role findById(int id) throws SystemException {
		Role role = new Role(id);
		return role;
	}
	/*
	*/
	public static int getRoleCount(String conditionStr) throws SystemException {
		return SysFunction.getCnt(Role.TABLENAME, conditionStr);
	}

	public static void remove(int id) throws SystemException {
		String conditionStr = " ROLEID = " + id;
		//删除角色和人员的对应关系
		SysFunction.delRecord("SYS_USERROLE", conditionStr);
		SysFunction.delRecord("SYS_ROLEMENUS", conditionStr);
		//删除角色
		SysFunction.delRecord(Role.TABLENAME, id);
	}
}
