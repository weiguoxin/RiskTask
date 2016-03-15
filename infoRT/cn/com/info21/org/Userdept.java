/*
 * 创建日期 2006-1-16
 */

package cn.com.info21.org;

import java.sql.*;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author songle
 */

public class Userdept {
	private int pid;
	private int userid;
	private int deptid;
	private int dutyid;
	private int id;


	/*定义数据库操作************************/
	public static final String TABLENAME = "ORG_USER_DEPT";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,userid) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET userid=?,deptid=?,dutyid=? WHERE ID=?";
	/***********************************/
	/*定义类运行全局变量*/
	private String errorStr;
	/**
	 * 构造函数一
	 */
	public Userdept() {
	}
	/**
	 * 构造函数二
	 * @param id 
	 * @throws SystemException 系统异常
	 */
	public Userdept(int id) throws SystemException {
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
				this.userid = rs.getInt("userid");
				this.deptid = rs.getInt("deptid");
				this.dutyid = rs.getInt("dutyid");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Userdept:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Userdept:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 构造函数三
	 * @param pid 
	 * @throws SystemException 系统异常
	 */
	public Userdept(String userid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.userid = Integer.parseInt(userid);
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, this.userid);
                pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Userdept:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Userdept:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 更新
	 * @throws SystemException 系统异常
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_OBJ);
			pstmt.setInt(1, this.userid);
			pstmt.setInt(2, this.deptid);
			pstmt.setInt(3, this.dutyid);
			pstmt.setInt(4, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Userdept.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Userdept.java:update(): " + e;
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
	/**
	 * @return Returns the dutyid.
	 */
	public int getDutyid() {
		return dutyid;
	}
	/**
	 * @param dutyid The dutyid to set.
	 */
	public void setDutyid(int dutyid) {
		this.dutyid = dutyid;
	}
	/**
	 * @return Returns the userid.
	 */
	public int getUserid() {
		return userid;
	}
	/**
	 * @param userid The userid to set.
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}
	/**
	 * @return Returns the deptid.
	 */
	public int getDeptid() {
		return this.deptid;
	}
	/**
	 * @param deptid The deptid to set.
	 */
	public void setDeptid(int deptid) {
		this.deptid = deptid;
	}
}
