/*
 * 创建日期 2005-7-18
 */

package cn.com.info21.workflow;

import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author songle
 * TRACK应用定义
 */

public class Track {
	/*定义属性*/
	//流程跟踪序列号
	private int id;
	//流程实例ID
	private int flowid;
	//部门名称
	private String deptname = null;
	//操作时间
	private Timestamp otime = null;
	//操作名称
	private String actname = null;
	//操作任务ID
	private int taskid;
	//操作人员
	private String username = null;
	//备注
	private String remark = null;

	/*定义数据库操作************************/
	public static final String TABLENAME = "WF_track";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,flowid) VALUES(?,?)";
	private static final String UPDATE_OBJ = "UPDATE " + TABLENAME
		 + " SET flowid=?,deptname=?,otime=?,actname=?,"
		 + "taskid=?,username=?,remark=? WHERE ID=?";
	/***********************************/
	/*定义类运行全局变量*/
	private String errorStr;

	/**
	 * 构造函数一
	 */
	public Track() {
	}
	/**
	 * 构造函数二
	 * @param flowid 流程实例ID
	 * @exception SystemException 系统异常
	 */
	public Track(String flowid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.flowid = Integer.parseInt(flowid);
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, this.flowid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Track:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Track:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 构造函数三
	 * @param id 流程跟踪序列号
	 * @throws SystemException 系统异常
	 */
	public Track(int id) throws SystemException {
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
		        this.deptname = rs.getString("deptname");
		        this.otime = rs.getTimestamp("otime");
		        this.actname = rs.getString("actname");
		        this.taskid = rs.getInt("taskid");
		        this.username = rs.getString("username");
		        this.remark = rs.getString("remark");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Track:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Track:constructor()-" + e;
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
			pstmt.setString(2, this.deptname);
			pstmt.setTimestamp(3, this.otime);
			pstmt.setString(4, this.actname);
			pstmt.setInt(5, this.taskid);
			pstmt.setString(6, this.username);
			pstmt.setString(7, this.remark);
			pstmt.setInt(8, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Track.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Track.java:update(): " + e;
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
	 * @return Returns the deptname.
	 */
	public String getDeptName() {
		return deptname;
	}
	/**
	 * @param deptname The deptname to set.
	 */
	public void setDeptName(String deptname) {
		this.deptname = deptname;
	}
	/**
	 * @return Returns the otime.
	 */
	public Timestamp getOtime() {
		return otime;
	}
	/**
	 * @param otime The otime to set.
	 */
	public void setOtime(Timestamp otime) {
		this.otime = otime;
	}
	/**
	 * @return Returns the actname.
	 */
	public String getActName() {
		return actname;
	}
	/**
	 * @param actname The actname to set.
	 */
	public void setActName(String actname) {
		this.actname = actname;
	}
	/**
	 * @return Returns the taskid.
	 */
	public int getTaskId() {
		return taskid;
	}
	/**
	 * @param taskid The taskid to set.
	 */
	public void setTaskId(int taskid) {
		this.taskid = taskid;
	}
	/**
	 * @return the username
	 */
	public String getUserName() {
	    return this.username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUserName(String username) {
	    this.username = username;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
	    return this.remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
	    this.remark = remark;
	}
}
