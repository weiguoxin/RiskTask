package cn.com.info21.workflow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/*
 * �������� 2005-7-20
 */

/**
 * @author Puhongtao
 */
public class Ramus {
	/*��������*/
	//��֧ID
	private int id;
	//Ŀ�����ID
	private int nextprocid;
	//Ŀ��ID
	private int nextactid;
	//��֧�����ID
	private int actid;
	//Ŀ����������ʱ��
	private int handletime;
	//��ע
	private String remark;
	//��Աѡ�����
	private String rulexml;
	//��Աѡ��ģʽ
	private int choosemode;

	/*�������ݿ����************************/
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

	/*����������ȫ�ֱ���*/
	private String errorStr;

	/**
	 * ���캯��һ
	 */
	public Ramus() {
	}
	/**
	 * ���캯����
	 * @param nextprocid Ŀ�����
	 * @exception SystemException ϵͳ�쳣
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
	 * ���캯����
	 * @param id ��֧ID
	 * @throws SystemException ϵͳ�쳣
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
	 * ���¶���
	 * @throws SystemException ϵͳ�쳣
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
	 *ɾ��һ����¼
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
	 * ��ȡ��Աѡ�����
	 * @return String
	 */
	public String getRuleXml() {
	    return this.rulexml;
	}
	/**
	 * ������Աѡ�����
	 * @param rulexml String
	 */
	public void setRuleXml(String rulexml) {
	    this.rulexml = rulexml;
	}
	/**
	 * ��ȡ��Աѡ��ģʽ
	 * @return int
	 */
	public int getChooseMode() {
	    return this.choosemode;
	}
	/**
	 * ������Աѡ��ģʽ
	 * @param choosemode ѡ��ģʽ
	 */
	public void setChooseMode(int choosemode) {
	    this.choosemode = choosemode;
	}
}
