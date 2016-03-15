/*
 * �������� 2005-7-18
 */

package cn.com.info21.workflow;

import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author songle
 * READERӦ�ö���
 */

public class Acl {
	/*��������*/
	//����ID
	private int id;
	//����ID
	private int flowid;
	//�û�ID
	private int userid;
	//����
	private int type;

	/*�������ݿ����************************/
	public static final String TABLENAME = "WF_acl";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,flowid) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME + " SET flowid=?,userid=?,type=? WHERE ID=?";
	/***********************************/
	/*����������ȫ�ֱ���*/
	private String errorStr;
	public static final int READER_TYPE = 0;
	public static final int AUTHOR_TYPE = 1;
	/**
	 * ���캯��һ
	 */
	public Acl() {
	}
	/**
	 * ���캯����
	 * @param flowid ����ID
	 * @exception SystemException ϵͳ�쳣
	 */
	public Acl(String flowid) throws SystemException {
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
			errorStr = "SQLException in acl:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in acl:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ���캯����
	 * @param id �û�ID
	 * @throws SystemException ϵͳ�쳣
	 */
	public Acl(int id) throws SystemException {
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
		        this.userid = rs.getInt("userid");
		        this.type = rs.getInt("type");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in acl:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in acl:constructor()-" + e;
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
			pstmt.setInt(1, this.flowid);
			pstmt.setInt(2, this.userid);
			pstmt.setInt(3, this.type);
			pstmt.setInt(4, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Acl.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Acl.java:update(): " + e;
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
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
}