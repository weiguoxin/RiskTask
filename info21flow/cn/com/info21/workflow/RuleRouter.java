/*
 * �������� 2005-7-19
 */
package cn.com.info21.workflow;

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
public class RuleRouter {
	/*��������*/
	//·�ɹ���ID
	private int id;
	//��������
	private String condition;
	//Ŀ��ID
	private int targetActid;
	//Ŀ�����ID
	private int targetProcid;
	//·����ID
	private int routerid;

	/*�������ݿ����************************/
	public static final String TABLENAME = "WF_rule_router";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,condition) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET condition=?,targetActid=?,targetProcid=?,Routerid=?,"
		 + "WHERE ID=?";
	/***********************************/
	/*����������ȫ�ֱ���*/
	private String errorStr;

	/**
	 * ���캯��һ
	 */
	public RuleRouter() {
	}
	/**
	 * ���캯����
	 * @param condition ��������
	 * @exception SystemException ϵͳ�쳣
	 */
	public RuleRouter(String condition) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.condition = condition;
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setString(2, this.condition);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in RuleRouter:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in RuleRouter:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ���캯����
	 * @param id ·�ɹ���ID
	 * @throws SystemException ϵͳ�쳣
	 */
	public RuleRouter(int id) throws SystemException {
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
				this.condition = rs.getString("condition");
				this.targetActid = rs.getInt("targetActid");
				this.targetProcid = rs.getInt("targetProcid");
				this.routerid = rs.getInt("routerid");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in RuleRouter:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in RuleRouter:constructor()-" + e;
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
			pstmt.setString(1, this.condition);
			pstmt.setInt(2, this.targetActid);
			pstmt.setInt(3, this.targetProcid);
			pstmt.setInt(4, this.routerid);
			pstmt.setInt(5, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in RuleRouter.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in RuleRouter.java:update(): " + e;
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
	 * @return Returns the condition.
	 */
	public String getCondition() {
		return condition;
	}
	/**
	 * @param condition The condition to set.
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
	/**
	 * @return Returns the targetActid.
	 */
	public String getTargetActid() {
		return condition;
	}
	/**
	 * @param targetActid The targetActid to set.
	 */
	public void setTargetActid(int targetActid) {
		this.targetActid = targetActid;
	}
	/**
	 * @return Returns the targetProcid.
	 */
	public int getTargetProcid() {
		return targetProcid;
	}
	/**
	 * @param targetProcid The targetProcid to set.
	 */
	public void setTargetProcid(int targetProcid) {
		this.targetProcid = targetProcid;
	}
	/**
	 * @return Returns the routerid.
	 */
	public int getRouterid() {
		return routerid;
	}
	/**
	 * @param routerid The routerid to set.
	 */
	public void setRouterid(int routerid) {
		this.routerid = routerid;
	}
}
