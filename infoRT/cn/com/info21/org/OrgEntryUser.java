/*
 * Created on 2004-5-10
 *
 */

package cn.com.info21.org;
import java.sql.*;

import cn.com.info21.database.*;
import cn.com.info21.system.*;
/**
 * @author Hu Guohua
 */
public class OrgEntryUser {
	public static  String TABLENAME = "V_ORG_USER";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT = "INSERT into " + TABLENAME + "(id, username) values(?,?)";
	private static final String UPDATE =
		"Update " + TABLENAME + " set username = ?,cnname = ?,deptid = ? where id =?";

	private String errorStr;

	private int id;
	public static final String[] TBS = {"org_user", "org_user_dept"};
	private String username;
	private String cnname;
	private int deptid;
	private Object obj = null;
	//private String[] childids;

	/**
	 * 默认构造函数
	 */
	public OrgEntryUser() {
	}
	/**
	 * 构造函数
	 * @param id0 传入的ID0
	 * @param type 类型
	 */
	public OrgEntryUser(int id, String username) {
		init(id, username);
	}

	/**
	 * @param id0 传入的ID0
	 * @param type 类型
	 */
	private void init(int id, String username) {
    	java.sql.Connection con = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_BY_ID);
			pstmt.setString(1, TBS[id] + "_" + id + "_" + username);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				OrgEntryUser.TABLENAME = rs.getString("tablename");
				//this.etype = rs.getInt("entrytype");
				
				this.id = rs.getInt("ID");
				this.username = rs.getString("username");
				this.cnname = rs.getString("cnname");
				this.deptid = rs.getInt("deptid");
				
				
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Dept:constructor(int)-" + sqle;
			System.err.println(errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Dept:constructor(int)-" + exception;
			System.err.println(errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 构造函数
	 * @param id 传入的tbname+"_"+etype+ID0
	 * @throws SystemException
	 */
	public OrgEntryUser(String username) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.username = username;
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT);
				pstmt.setInt(1, this.id);
				pstmt.setString(2, this.username);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in orgentry1:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in orgentry1:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}

public OrgEntryUser(int id) throws SystemException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
		con = DbConnectionManager.getConnection();
		pstmt = con.prepareStatement(LOAD_BY_ID);
		pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			this.id = id;
			this.username = rs.getString("username");
			this.cnname = rs.getString("cnname");
			this.deptid = rs.getInt("deptid");
		}
	} catch (SQLException sqle) {
		errorStr = "SQLException in orgentry1:constructor()-" + sqle;
		throw new SystemException (this, errorStr);
	} catch (Exception e) {
		errorStr = "Exception in oreentry1:constructor()-" + e;
		throw new SystemException (this, errorStr);
	} finally {
		DbConnectionManager.close(con, pstmt, null, rs);
	}
}
	/**
	 * @param id0 传入的ID0
	 * @param type 类型
	 */
public void update() throws SystemException {
	Connection con = null;
	PreparedStatement pstmt = null;
	try {
		con = DbConnectionManager.getConnection();
		pstmt = con.prepareStatement(UPDATE);
		pstmt.setString(1, this.username);
		pstmt.setString(2, this.cnname);
		pstmt.setInt(3, this.deptid);
		pstmt.setInt(4, this.id);
		pstmt.executeUpdate();
	} catch (SQLException sqle) {
		errorStr = "SQLException in orgentry1.java:update(): " + sqle;
		throw new SystemException (this, errorStr);
	} catch (Exception e) {
		errorStr = "Exception in orgentry1.java:update(): " + e;
		throw new SystemException (this, errorStr);
	} finally {
		DbConnectionManager.close(con, pstmt, null, null);
	}
}
/**
 *删除一条记录
 */
public void remove() {
	SysFunction.delRecord(TABLENAME, this.id);
}
	/**
	 * @return 当前entry的entry角色
	 */
/**	public Role getRole() {
		Role rtn = null;
		String conStr = "kindOf=1 and cnname='" + getId() + "'";
		try {
			RoleIterator iter = new RoleIterator(conStr);
			if (iter.hasNext()) {
				rtn = (Role) iter.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

*/
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return Returns the deptName.
	 */
	public String getCnname() {
		return cnname;
	}
	public void setCnname(String cnname) {
		this.cnname = cnname;
	}
	public int getDeptid() {
		return deptid;
	}
	public void setDeptid(int deptid) {
		this.deptid = deptid;
	}
	

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
}
	