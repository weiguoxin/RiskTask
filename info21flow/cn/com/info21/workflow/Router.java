/*
 * �������� 2005-7-20
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
public class Router {
   /*��������*/
	//·����ID
	private int id;
	//�ID
	private int actid;
	//���תģʽ������·��|�ֶ�·��|����ѡ��|
	private int runmode;
	//�Ƿ����û�����
	private int isinteract;
	//PROCID ���� WF_ROUTER
	private int procid;

	/*�������ݿ����************************/
	public static final String TABLENAME = "WF_router";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,act_id) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET act_id=?,runmode=?,isinteract=?,"
		 + "procid=? WHERE ID=?";
	/***********************************/

	/*����������ȫ�ֱ���*/
	private String errorStr;

	/**
	 * ���캯��һ
	 */
	public Router() {
	}
	/**
	 * ���캯����
	 * @param actid �ID
	 * @exception SystemException ϵͳ�쳣
	 */
	public Router(String actid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.actid = Integer.parseInt(actid);
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setLong(2, this.actid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Router:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Router:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ���캯����
	 * @param id ·����ID
	 * @throws SystemException ϵͳ�쳣
	 */
	public Router(int id) throws SystemException {
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
				this.actid = rs.getInt("act_id");
				this.runmode = rs.getInt("runmode");
				this.isinteract = rs.getInt("isinteract");
				this.procid = rs.getInt("procid");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Router:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Router:constructor()-" + e;
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
			pstmt.setInt(1, this.actid);
			pstmt.setInt(2, this.runmode);
			pstmt.setInt(3, this.isinteract);
			pstmt.setInt(4, this.procid);
			pstmt.setInt(5, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Router.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Router.java:update(): " + e;
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
	 * @return Returns the act_id.
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
	 * @return Returns the runmode.
	 */
	public int getRunMode() {
		return runmode;
	}
	/**
	 * @param runmode The runmode to set.
	 */
	public void setRunMode(int runmode) {
		this.runmode = runmode;
	}
	/**
	 * @return Returns the isinteract.
	 */
	public int getIsInteract() {
		return isinteract;
	}
	/**
	 * @param isinteract The isinteract to set.
	 */
	public void setIsInteract(int isinteract) {
		this.isinteract = isinteract;
	}
	/**
	 * @return Returns the procid.
	 */
	public int getProcid() {
		return procid;
	}
	/**
	 * @param procid The procid to set.
	 */
	public void setProcid(int procid) {
		this.procid = procid;
	}
	/**
	 * ��ȡ·�ɹ���
	 * @return RuleRouter
	 * @throws SystemException ϵͳ�쳣
	 */
	public RuleRouterIterator getRuleRouters() throws SystemException {
	    return RuleRouterHome.findByCondition("routerid = " + this.id);
	}
}
