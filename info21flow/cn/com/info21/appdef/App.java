/*
 * �������� 2005-7-19
 */
package cn.com.info21.appdef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */

public class App {
	/*��������*/
	//Ӧ��ID
	private int id;
	//Ӧ������
	private String appname;
	//����ID,����ID��Ӧ��ID��һһ��Ӧ�Ĺ�ϵ
	private int procid;
	//��ע������
	private String remark;

	/*�������ݿ����************************/
	public static final String TABLENAME = "WF_APP";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,appname) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET appname=?,procid=?,remark=? WHERE ID=?";
	/***********************************/

	/*����������ȫ�ֱ���*/
	private String errorStr;

	/**
	 * ���캯��һ
	 */
	public App() {
	}
	/**
	 * ���캯����
	 * @param appname Ӧ������
	 * @exception SystemException ϵͳ�쳣
	 */
	public App(String appname) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.appname = appname;
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setString(2, this.appname);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in App:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in App:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ���캯����
	 * @param id Ӧ��ID
	 * @throws SystemException ϵͳ�쳣
	 */
	public App(int id) throws SystemException {
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
				this.appname = rs.getString("appname");
				this.procid = rs.getInt("procid");
				this.remark = rs.getString("remark");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in App:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in App:constructor()-" + e;
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
			pstmt.setString(1, this.appname);
			pstmt.setInt(2, this.procid);
			pstmt.setString(3, this.remark);
			pstmt.setInt(4, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in App.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in App.java:update(): " + e;
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
	 * @return Returns the procname.
	 */
	public String getAppName() {
		return appname;
	}
	/**
	 * @param appname The appname to set.
	 */
	public void setAppName(String appname) {
		this.appname = appname;
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
	public static ButtonIterator getButtonByActId(int actid) throws SystemException {
	    return ButtonHome.findByCondition(
				"ID IN (SELECT BUTTONID FROM App_activity_button WHERE actid="
					+ actid
					+ ")");
	}
	public static FieldIterator getFieldByActid(int actid) throws SystemException {
	    return FieldHome.findByCondition(
				"ID IN (SELECT FIELDID FROM App_activity_field WHERE actid="
					+ actid
					+ ")");
	}
	public static IdeaIterator getIdeaByActId(int actid) throws SystemException {
	    return IdeaHome.findByCondition(
				"ID IN (SELECT IDEAID FROM App_activity_idea WHERE actid="
					+ actid
					+ ")");
	}
}
