/*
 * Created on 2004-6-18
 */

package cn.com.info21.org;
import java.sql.*;
import java.util.*;
import cn.com.info21.database.*;
//import cn.com.info21.system.*;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class Duty {
	private int id = 0;;
	private String name = "";
	//���ʼ��SQL��䶨��
	public static final String TABLENAME = "ORG_DUTY";
	private static final String LOAD_DUTY_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_DUTY =
		"INSERT INTO " + TABLENAME + "(id,name) VALUES(?,?)";
	private static final String UPDATE_DUTY =
		 "UPDATE " + TABLENAME + " SET name=? where id = ?";
	private String errorStr;
	private static final String SET_USER =
		"INSERT INTO ORG_USER_DUTY(userid,dutyid,deptid) VALUES(?, ?, ?)";
	/**
	 * Ĭ�Ϲ�����
	 */
	public Duty() {
	}
	/**
	 * ���캯��
	 * ����ְ��ID��,�����ݿ��ж�ȡְ����Ϣ
	 * @param id ְ����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public Duty(int id) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_DUTY_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id;
				this.name = rs.getString("name");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in DUTY:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in DUTY:constructor()-" + e;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * ����ְ������
	 * @param name ְ��
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return ְ��ID
	 */
	public int getID() {
		return this.id;
	}
	/**
	 * @return ְ������
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * ���캯��
	 * @param name ��nameֵ
	 * @throws SystemException ����
	 */
	public Duty(String name) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_DUTY);
			this.id = (int) (SysFunction.getMaxID(TABLENAME) + 1);
			this.name = name;
			pstmt.setInt(1, this.id);
			pstmt.setString(2, this.name);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Duty:constructor(String)-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Duty:constructor(string)-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ����һ��ְ����Ϣ
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public void add() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			//����û��Ƿ����
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_DUTY);
			pstmt.setString(1, this.name);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Duty:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Duty:constructor()-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ����ְ����Ϣ
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		//"UPDATE WF_duty SET name=? where id = ?";
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_DUTY);
			pstmt.setString(1, this.name);
			pstmt.setInt(2, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Duty:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Duty:constructor()-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ��ȡ�û���Ա��
	 * @return int[][]
	 * @throws SystemException ����ϵͳ����
	 */
	public int[][] getUserDepts() throws SystemException {
		int[][] rtn = new int [0][0];
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList idxs = new ArrayList();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement("SELECT userid,deptid FROM WF_user_duty"
				+ " WHERE dutyid=?");
			pstmt.setInt(1, this.id);
			rs = pstmt.executeQuery();
			int[] tmp = new int[2];
			while (rs.next()) {
				tmp[0] = rs.getInt("userid");
				tmp[1] = rs.getInt("deptid");
				idxs.add(tmp);
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Role:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Role:constructor()-" + e;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
		rtn = new int[idxs.size()][2];
		for (int i = 0; i < rtn.length; i++) {
			rtn[i][0] = ((int[]) idxs.get(i))[0];
			rtn[i][1] = ((int[]) idxs.get(i))[1];
		}
		return rtn;
	}
	/**
	 * �����û���Ա
	 * @param userdeptids �û�/����ID
	 * @throws SystemException ����ϵͳ����
	 */
	public void setUserDepts(int[][] userdeptids) throws SystemException {
		try {
			SysFunction.delRecord(
				"WF_user_duty",
				"dutyid = " + this.id);
		} catch (Exception e) {
			System.err.println("Exception in setUsers(): " + e);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(SET_USER);
			for (int i = 0; i < userdeptids.length; i++) {
				pstmt.setInt(1, userdeptids[i][0]);
				pstmt.setInt(2, this.id);
				pstmt.setInt(3, userdeptids[i][1]);
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
		} catch (Exception e) {
			errorStr =
				"SQLException in Duty.java:setUsers(): " + e.getMessage();
			System.err.println(errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
}
