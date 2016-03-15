/*
 * 创建日期 2005-7-18
 */

package cn.com.info21.workflow;

import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author songle
 * Flow_sequence应用定义
 */

public class Sequence {
	/*定义属性*/
	//用户自定义序列ID
	private int id;
	//序列所属流程的ID
	private int flowid;
	//过程ID
	private int procid;
	//活动ID
	private int actid;
	//任务办理人员
	private int userid;
	//序列处理状态，未处理|处理中|已处理
	private int status;

	/*定义数据库操作************************/
	public static final String TABLENAME = "WF_flow_sequence";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,flowid) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME + " SET flowid=?,procid=?"
		 +
		 ",actid=?,userid=?,status=? WHERE ID=?";
	/***********************************/
	/*定义类运行全局变量*/
	private String errorStr;

	/**
	 * 构造函数一
	 */
	public Sequence() {
	}
	/**
	 * 构造函数二
	 * @param flowid 序列所属流程的ID
	 * @exception SystemException 系统异常
	 */
	public Sequence(String flowid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			this.flowid = Integer.parseInt(flowid);
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, this.flowid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Sequence:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Sequence:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 构造函数三
	 * @param id 用户自定义序列ID
	 * @throws SystemException 系统异常
	 */
	public Sequence(int id) throws SystemException {
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
				this.flowid = rs.getInt("flowid");
				this.procid = rs.getInt("procid");
				this.actid = rs.getInt("actid");
				this.userid = rs.getInt("userid");
				this.status = rs.getInt("status");
		     }
		} catch (SQLException sqle) {
			errorStr = "SQLException in Sequence:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Sequence:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 更新对象
	 * @throws SystemException 系统异常
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_OBJ);
			pstmt.setInt(1, this.flowid);
			pstmt.setInt(2, this.procid);
			pstmt.setInt(3, this.actid);
			pstmt.setInt(4, this.userid);
			pstmt.setInt(5, this.status);
			pstmt.setInt(6, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Sequence.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Sequence.java:update(): " + e;
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
	 * @return Returns the flowid
	 */
	public int getFlowId() {
		return flowid;
	}
	/**
	 * @param flowid The flowid to set.
	 */
	public void setFlowId(int flowid) {
		this.flowid = flowid;
	}
	/**
	 * @return Returns the procid.
	 */
	public int getProcId() {
		return procid;
	}
	/**
	 * @param procid The procid to set.
	 */
	public void setProcId(int procid) {
		this.procid = procid;
	}
	/**
	 * @return Returns the actid.
	 */
	public int getActId() {
		return actid;
	}
	/**
	 * @param actid The actid to set.
	 */
	public void setActId(int actid) {
		this.actid = actid;
	}
	/**
	 * @return Returns the userid.
	 */
	public int getUserId() {
		return userid;
	}
	/**
	 * @param userid The userid to set.
	 */
	public void setUserId(int userid) {
		this.userid = userid;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
}
