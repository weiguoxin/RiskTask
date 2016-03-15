package cn.com.info21.workflow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/*
 * 创建日期 2005-7-20
 */

/**
 * @author Puhongtao
 */
public class Ramus {
	/*定义属性*/
	//分支ID
	private int id;
	//目标过程ID
	private int nextprocid;
	//目标活动ID
	private int nextactid;
	//分支所属活动ID
	private int actid;
	//目标活动办理任务时间
	private int handletime;
	//备注
	private String remark;
	//人员选择规则
	private String rulexml;
	//人员选择模式
	private int choosemode;

	/*定义数据库操作************************/
	public static final String TABLENAME = "WF_ramus";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,nextproc_id) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET nextproc_id=?,nextact_id=?,actid=?,handletime=?,"
		 + "remark=?,rulexml=?,choosemode=? WHERE ID=?";
	/***********************************/

	/*定义类运行全局变量*/
	private String errorStr;

	/**
	 * 构造函数一
	 */
	public Ramus() {
	}
	/**
	 * 构造函数二
	 * @param nextprocid 目标过程
	 * @exception SystemException 系统异常
	 */
	public Ramus(String nextprocid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.nextprocid = Integer.parseInt(nextprocid);
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setLong(2, this.nextprocid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Ramus:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Ramus:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 构造函数三
	 * @param id 分支ID
	 * @throws SystemException 系统异常
	 */
	public Ramus(int id) throws SystemException {
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
				this.nextprocid = rs.getInt("nextproc_id");
				this.nextactid = rs.getInt("nextact_id");
				this.actid = rs.getInt("actid");
				this.handletime = rs.getInt("handletime");
				this.remark = rs.getString("remark");
				this.rulexml = rs.getString("rulexml");
				this.choosemode = rs.getInt("choosemode");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Ramus:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Ramus:constructor()-" + e;
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
			pstmt.setInt(1, this.nextprocid);
			pstmt.setInt(2, this.nextactid);
			pstmt.setInt(3, this.actid);
			pstmt.setInt(4, this.handletime);
			pstmt.setString(5, this.remark);
			pstmt.setString(6, this.rulexml);
			pstmt.setInt(7, this.choosemode);
			pstmt.setInt(8, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Ramus.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Ramus.java:update(): " + e;
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
	 * @return Returns the nextprocid.
	 */
	public int getNextProcId() {
		return nextprocid;
	}
	/**
	 * @param nextprocid The nextprocid to set.
	 */
	public void setNextProcId(int nextprocid) {
		this.nextprocid = nextprocid;
	}
	/**
	 * @return Returns the nextactid.
	 */
	public int getNextActId() {
		return nextactid;
	}
	/**
	 * @param nextactid The nextactid to set.
	 */
	public void setNextActId(int nextactid) {
		this.nextactid = nextactid;
	}
	/**
	 * @return Returns the actid.
	 */
	public int getActid() {
		return actid;
	}
	/**
	 * @param actid The actid to set.
	 */
	public void setActid(int actid) {
		this.actid = actid;
	}
	/**
	 * @return Returns the handletime.
	 */
	public int getHandletime() {
		return handletime;
	}
	/**
	 * @param handletime The handletime to set.
	 */
	public void setHandletime(int handletime) {
		this.handletime = handletime;
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
	 * 获取人员选择规则
	 * @return String
	 */
	public String getRuleXml() {
	    return this.rulexml;
	}
	/**
	 * 设置人员选择规则
	 * @param rulexml String
	 */
	public void setRuleXml(String rulexml) {
	    this.rulexml = rulexml;
	}
	/**
	 * 获取人员选择模式
	 * @return int
	 */
	public int getChooseMode() {
	    return this.choosemode;
	}
	/**
	 * 设置人员选择模式
	 * @param choosemode 选择模式
	 */
	public void setChooseMode(int choosemode) {
	    this.choosemode = choosemode;
	}
}
