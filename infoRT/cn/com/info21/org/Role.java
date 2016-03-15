package cn.com.info21.org;


import java.sql.*;

import cn.com.info21.database.*;
import cn.com.info21.system.*;
import cn.com.info21.menu.*;
import cn.com.info21.util.StringUtils;
/**
 * @author db2admin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Role {
	public static final String TABLENAME = "SYS_ROLES";
	private static final String LOAD_ROLE_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_ROLE =
		"INSERT INTO " + TABLENAME + "(ID,NAME) VALUES(?,?)";
	private static final String UPDATE_ROLE =
		"UPDATE " + TABLENAME + " SET NAME=?, TYPE=?, DETAIL=? WHERE ID=?";
	private static final String SET_ROLE_MENU =
		"INSERT INTO SYS_ROLEMENUS(ROLEID, MENUID, POPEDOM) VALUES(?, ?, ?)";
	private static final String HAS_ROLE_MENU =
		"SELECT * FROM SYS_ROLEMENUS WHERE ROLEID=? AND MENUID=? AND POPEDOM=?";
	private int id; //���;
	private java.lang.String name; //��ɫ��;
	private java.lang.String type; //��ɫ����;
	private java.lang.String detail; //��ע;
	private String errorStr = "";
	private java.lang.String emptyStr = "";

	/**
	 * ���췽��һ�����ݽ�ɫid�ţ������ݿ��ж�ȡ��ɫ��Ϣ
	 * �������ڣ�(2003-3-28 10:00:40)
	 * @param id int
	 * @exception cn.com.info21.system.SystemException �쳣˵����
	 */
	public Role(int id) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_ROLE_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id;
				this.name = rs.getString("NAME");
				this.type = rs.getString("TYPE");
				this.detail = rs.getString("DETAIL");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Role:constructor()-" + sqle;
			throw new SystemException(errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Role:constructor()-" + exception;
			throw new SystemException(errorStr);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * ���췽������������ɫ���룬�����ݿ��д���һ����ɫ��¼��
	 * �������ڣ�(2003-3-28 10:05:40)
	 * @param code java.lang.String
	 * @exception cn.com.info21.system.SystemException �쳣˵����
	 */
	public Role(String name) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			//����Ƿ��иô���Ľ�ɫ����
			if (SysFunction.getCnt("SYS_ROLES", "NAME = '" + name + "'") > 0) {
				errorStr = "�Ѿ����ڵĽ�ɫ����!";
				throw new SystemException(errorStr);
			} else {
				this.id = SysFunction.getMaxid("SYS_ROLES") + 1;
				this.name = name;
				con = DbConnectionManager.getConnection();
				pstmt = con.prepareStatement(INSERT_ROLE);
				pstmt.setInt(1, this.id);
				pstmt.setString(2, this.name);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Role:constructor()-" + sqle;
			throw new SystemException(errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Role:constructor()-" + exception;
			throw new SystemException(errorStr);
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	* ���ؽ�ɫID
	* @return String��
	*/
	public int getId() {
		return this.id;
	}
	/**
	* ���ؽ�ɫ����
	* @return String��
	*/
	public String getName() {
		//return this.name;
		if (this.name == null) {
			return emptyStr;
		} else {
			return this.name;
		}
	}

	/**
	* ���ؽ�ɫ����
	* @return String��
	*/
	public String getType() {
		//return this.name;
		if (this.type == null) {
			return emptyStr;
		} else {
			return this.type;
		}
	}

	/**
	* ���ؽ�ɫ��ע
	* @return String��
	*/
	public String getDetail() {
		// return this.remark;
		if (this.detail == null) {
			return emptyStr;
		} else {
			return this.detail;
		}
	}

	/**
	* ���ý�ɫ����
	* @return String��
	*/
	public void setName(String name) {
		this.name = name;
	}

	/**
	* ���ý�ɫ����
	* @return String��
	*/
	public void setType(String type) {
		this.type = type;
	}

	/**
	* ���ý�ɫ��ע
	* @return String��
	*/
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/*
	*/
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_ROLE);
			pstmt.setString(1, this.name);
			pstmt.setString(2, this.type);
			pstmt.setString(3, this.detail);
			pstmt.setInt(4, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Role.java:update(): " + sqle;
			throw new SystemException(errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Role.java:update(): " + e;
			throw new SystemException(errorStr);
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void setMenus(String[] menus) throws SystemException {
		try {
			SysFunction.delRecord("SYS_ROLEMENUS", "ROLEID =" + this.id);
		} catch (Exception e) {
			//Log.error("Exception in setMenu(): " + e);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(SET_ROLE_MENU);
			for (int i = 0; i < menus.length; i++) {
				pstmt.setInt(1, this.id);
				String[] tempArray = StringUtils.explode(menus[i], "||");
				pstmt.setString(2, tempArray[0]);
				if (tempArray.length > 1) {
					pstmt.setString(3, tempArray[1]);
				} else {
					pstmt.setString(3, "0");
				}
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
		} catch (Exception e) {
			errorStr = "SQLException in Role.java:setMenu(): " + e.getMessage();
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setMenus(String type, String[] menus) throws SystemException {
		try {
			SysFunction.delRecord(
				"SYS_ROLEMENUS",
				"ROLEID = "
					+ this.id
					+ " AND MENUID IN (SELECT ID FROM SYS_MENUS WHERE TYPE='"
					+ type
					+ "')");
		} catch (Exception e) {
			//Log.error("Exception in setMenu(): " + e);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(SET_ROLE_MENU);
			for (int i = 0; i < menus.length; i++) {
				pstmt.setInt(1, this.id);
				String[] tempArray = StringUtils.explode(menus[i], "||");
				pstmt.setString(2, tempArray[0]);
				if (tempArray.length > 1) {
					pstmt.setString(3, tempArray[1]);
				} else {
					pstmt.setString(3, "0");
				}
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
		} catch (Exception e) {
			errorStr = "SQLException in Role.java:setMenu(): " + e.getMessage();
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void setMenuPopedom(String menuId, String[] popedoms)
		throws SystemException {
		String tempMenuId = menuId;
		int count = 0;
		while ((tempMenuId != null) && (!"".equals(tempMenuId))) {
			try {
				SysFunction.delRecord(
					"SYS_ROLEMENUS",
					"ROLEID = " + this.id + " AND MENUID ='" + tempMenuId + "'");
			} catch (Exception e) {
				//Log.error("Exception in setMenu(): " + e);
				tempMenuId = null;
			}
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DbConnectionManager.getConnection();
				pstmt = con.prepareStatement(SET_ROLE_MENU);
				for (int i = 0; i < popedoms.length; i++) {
					pstmt.setInt(1, this.id);
					pstmt.setString(2, tempMenuId);
					pstmt.setString(3, popedoms[i]);
					pstmt.executeUpdate();
					pstmt.clearParameters();
				}
			} catch (Exception e) {
				errorStr =
					"SQLException in Role.java:setMenuPopedom(): "
						+ e.getMessage();
			//	Log.error(errorStr);
				tempMenuId = null;
				throw new SystemException(errorStr);
			} finally {
				try {
					pstmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					tempMenuId = null;
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
					tempMenuId = null;
				}
			}
			try {
				tempMenuId = MenuHome.findById(tempMenuId).getFid();
			} catch (Exception e) {
				tempMenuId = null;
			}
		}
	}
	public boolean hasMenu(String menu) throws SystemException {
		boolean returnValue = false;
		if (menu == null) {
			return false;
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(HAS_ROLE_MENU);
			pstmt.setInt(1, this.id);
			String[] tempArray = StringUtils.explode(menu, "||");
			pstmt.setString(2, tempArray[0]);
			if (tempArray.length > 1) {
				pstmt.setString(3, tempArray[1]);
			} else {
				pstmt.setString(3, "0");
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				returnValue = true;
			}
		} catch (Exception e) {
			errorStr = "SQLException in Role.java:setMenu(): " + e.getMessage();
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
	public void remove() throws SystemException {
		//ɾ����ɫ����Ա�Ķ�Ӧ��ϵ
		String conditionStr = " ROLEID = " + this.id;
		SysFunction.delRecord("SYS_USERROLE", conditionStr);
		//ɾ����ɫ
		SysFunction.delRecord("SYS_ROLES", this.id);
	}

}
