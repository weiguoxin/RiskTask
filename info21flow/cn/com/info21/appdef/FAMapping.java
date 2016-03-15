/*
 * 创建日期 2005-9-14
 */

package cn.com.info21.appdef;

/**
 * @author lkh
 */

import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 * 操作、活动映射
 */

public class FAMapping {
    private int id;
    private int fieldid;
    private int actid;
	/*定义数据库操作************************/
	public static final String TABLENAME = "App_activity_field";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE id=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,fieldid,actid) VALUES(?,?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET fieldid=?,actid=? WHERE ID=?";
	/***********************************/
	
	/*定义类运行全局变量*/
	private String errorStr;

	public FAMapping() {
	}
	public FAMapping(int id) throws SystemException {
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
				this.fieldid = rs.getInt("fieldid");
				this.actid = rs.getInt("actid");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in FAMapping:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in FAMapping:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	public FAMapping(int fieldid, int actid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.fieldid = fieldid;
			this.actid = actid;
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, this.fieldid);
				pstmt.setInt(3, this.actid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in FAMapping:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in FAMapping:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_OBJ);
			pstmt.setInt(1, this.fieldid);
			pstmt.setInt(2, this.actid);
			pstmt.setInt(3, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in FAMapping.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in FAMapping.java:update(): " + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	public int getId() {
	    return this.id;
	}
	public void setId(int id) {
	    this.id = id;
	}
	public int getFieldId() {
	    return this.fieldid;
	}
	public void setFieldId(int fieldid) {
	    this.fieldid = fieldid;
	}
	public int getActId() {
	    return this.actid;
	}
	public void setActId(int actid) {
	    this.actid = actid;
	}
}
