/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;
import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author lkh
 * 一个过程包含多个活动。
 */

public class Proc {
	/*定义属性*/
	//过程ID
	private int id;
	//过程名称
	private String procname;
	//过程激活类型，包含两种方式：手动激活和外消息激活
	private int activateType;
	//过程是否被禁用
	private int enable;
	//过程描述
	private String remark;

	/*定义数据库操作************************/
	public static final String TABLENAME = "WF_proc";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,procname) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET procname=?,activateType=?,enable=?,remark=? WHERE ID=?";
	/***********************************/

	/*定义类运行全局变量*/
	private String errorStr;

	/**
	 * 构造函数一
	 */
	public Proc() {
	}
	/**
	 * 构造函数二
	 * @param procname 过程名称
	 * @exception SystemException 系统异常
	 */
	public Proc(String procname) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.procname = procname;
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setString(2, this.procname);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Proc:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Proc:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 构造函数三
	 * @param id 过程ID
	 * @throws SystemException 系统异常
	 */
	public Proc(int id) throws SystemException {
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
				this.procname = rs.getString("procname");
				this.activateType = rs.getInt("activateType");
				this.enable = rs.getInt("enable");
				this.remark = rs.getString("remark");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Proc:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Proc:constructor()-" + e;
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
			pstmt.setString(1, this.procname);
			pstmt.setInt(2, this.activateType);
			pstmt.setInt(3, this.enable);
			pstmt.setString(4, this.remark);
			pstmt.setInt(5, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Proc.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Proc.java:update(): " + e;
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
	private void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the procname.
	 */
	public String getProcName() {
		return procname;
	}
	/**
	 * @param procname The procname to set.
	 */
	public void setProcName(String procname) {
		this.procname = procname;
	}
	/**
	 * @return Returns the activateType.
	 */
	public int getActivateType() {
		return activateType;
	}
	/**
	 * @param activateType The activateType to set.
	 */
	public void setActivateType(int activateType) {
		this.activateType = activateType;
	}
	/**
	 * @return Returns the enable.
	 */
	public int getEnable() {
		return enable;
	}
	/**
	 * @param enable The enable to set.
	 */
	public void setEnable(int enable) {
		this.enable = enable;
	}
	/**
	 * @return Returns the remark.
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark The remark to set.
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取活动集
	 * @return ActivityIterator
	 * @throws SystemException 系统异常
	 */
	public ActivityIterator getActivities() throws SystemException {
	    return ActivityHome.findByCondition(
				"procid=" + this.id);
	}
	/**
	 * 获取条件活动集
	 * @param strCon 条件
	 * @return ActivityIterator
	 * @throws SystemException 捕获系统错误
	 */
	public ActivityIterator getActivities(String strCon) throws SystemException {
		String condition = "procid=" + this.id;
		if (strCon != null && !strCon.equals("")) {
			condition += " and " + strCon;
		}
		return ActivityHome.findByCondition(condition);
	}
}
