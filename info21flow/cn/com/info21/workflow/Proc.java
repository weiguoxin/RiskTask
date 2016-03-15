/*
 * �������� 2005-7-17
 */

package cn.com.info21.workflow;
import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author lkh
 * һ�����̰���������
 */

public class Proc {
	/*��������*/
	//����ID
	private int id;
	//��������
	private String procname;
	//���̼������ͣ��������ַ�ʽ���ֶ����������Ϣ����
	private int activateType;
	//�����Ƿ񱻽���
	private int enable;
	//��������
	private String remark;

	/*�������ݿ����************************/
	public static final String TABLENAME = "WF_proc";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,procname) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET procname=?,activateType=?,enable=?,remark=? WHERE ID=?";
	/***********************************/

	/*����������ȫ�ֱ���*/
	private String errorStr;

	/**
	 * ���캯��һ
	 */
	public Proc() {
	}
	/**
	 * ���캯����
	 * @param procname ��������
	 * @exception SystemException ϵͳ�쳣
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
	 * ���캯����
	 * @param id ����ID
	 * @throws SystemException ϵͳ�쳣
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
	 * ���¶���
	 * @throws SystemException ϵͳ�쳣
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
	 * ��ȡ���
	 * @return ActivityIterator
	 * @throws SystemException ϵͳ�쳣
	 */
	public ActivityIterator getActivities() throws SystemException {
	    return ActivityHome.findByCondition(
				"procid=" + this.id);
	}
	/**
	 * ��ȡ�������
	 * @param strCon ����
	 * @return ActivityIterator
	 * @throws SystemException ����ϵͳ����
	 */
	public ActivityIterator getActivities(String strCon) throws SystemException {
		String condition = "procid=" + this.id;
		if (strCon != null && !strCon.equals("")) {
			condition += " and " + strCon;
		}
		return ActivityHome.findByCondition(condition);
	}
}
