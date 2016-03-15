/*
 * �������� 2005-8-1
 *
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
 *
 */
public class Idea {
	/*��������*/
	//����ID
	private int id;
	//��������
	private String ideaname;
	//����ID
	private int appid;
	//��ע����
	private String remark;
	private int procid;

	/*�������ݿ����************/
	public static final String TABLENAME = "APP_IDEA";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,ideaname) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET ideaname=?,appid=?,"
		 + "remark=?,procid=? WHERE ID=?";
	/***********************************/

	/*����������ȫ�ֱ���*/
	private String errorStr;

	/**
	 * ���캯��һ
	 */
	public Idea() {
	}
	/**
	 * ���캯����
	 * @param ideaname ��������
	 * @exception SystemException ϵͳ�쳣
	 */
	public Idea(String ideaname) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.ideaname = ideaname;
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setString(2, this.ideaname);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Idea:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Idea:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ���캯����
	 * @param id ����ID
	 * @throws SystemException ϵͳ�쳣
	 */
	public Idea(int id) throws SystemException {
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
				this.ideaname = rs.getString("ideaname");
				this.appid = rs.getInt("appid");
				this.remark = rs.getString("remark");
				this.procid = rs.getInt("procid");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Idea:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Idea:constructor()-" + e;
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
			pstmt.setString(1, this.ideaname);
			pstmt.setInt(2, this.appid);
			pstmt.setString(3, this.remark);
			pstmt.setInt(4, this.procid);
			pstmt.setInt(5, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Idea.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Idea.java:update(): " + e;
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
	private void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the ideaname.
	 */
	public String getIdeaName() {
		return ideaname;
	}
	/**
	 * @param ideaname The ideaname to set.
	 */
	public void setIdeaName(String ideaname) {
		this.ideaname = ideaname;
	}
	/**
	 * @return Returns the appid.
	 */
	public int getAppId() {
		return appid;
	}
	/**
	 * @param appid The appid to set.
	 */
	public void setAppId(int appid) {
		this.appid = appid;
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
	public int getProcId() {
	    return this.procid;
	}
	public void setProcId(int procid) {
	    this.procid = procid;
	}
}
