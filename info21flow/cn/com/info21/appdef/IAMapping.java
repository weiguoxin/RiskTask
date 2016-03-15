/*
 * 创建日期 2005-9-14
 */

package cn.com.info21.appdef;
import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 * 操作、活动映射
 */

public class IAMapping {
    private int id;
    private int ideaid;
    private int actid;
	/*定义数据库操作************************/
	public static final String TABLENAME = "App_activity_idea";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE id=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,ideaid,actid) VALUES(?,?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET ideaid=?,actid=? WHERE ID=?";
	/***********************************/
	
	/*定义类运行全局变量*/
	private String errorStr;

	public IAMapping() {
	}
	public IAMapping(int id) throws SystemException {
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
				this.ideaid = rs.getInt("ideaid");
				this.actid = rs.getInt("actid");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in IAMapping:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in IAMapping:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	public IAMapping(int ideaid, int actid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.ideaid = ideaid;
			this.actid = actid;
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, this.ideaid);
				pstmt.setInt(3, this.actid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in IAMapping:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in IAMapping:constructor()-" + exception;
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
			pstmt.setInt(1, this.ideaid);
			pstmt.setInt(2, this.actid);
			pstmt.setInt(3, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in IAMapping.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in IAMapping.java:update(): " + e;
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
	public int getIdeaId() {
	    return this.ideaid;
	}
	public void setIdeaId(int ideaid) {
	    this.ideaid = ideaid;
	}
	public int getActId() {
	    return this.actid;
	}
	public void setActId(int actid) {
	    this.actid = actid;
	}
}