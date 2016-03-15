/*
 * �������� 2005-7-18
 */

package cn.com.info21.workflow;

import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author songle
 * TASKӦ�ö���
 */

public class Task {
	/*��������*/
	//����ID
	private int id;
	//����ID
	private int procid;
	//�ID
	private int actid;
	//����ID
	private int flowid;
	//��һ����ID
	private int pretaskid;
	//��һ����ID
	private int nexttaskid;
	//���������
	private int author;
	//���񴴽�ʱ��
	private Timestamp createtime;
	//�������ʱ��
	private Timestamp endtime;
	//����״̬
	private int status;
	//������������ڲ���ID����������Ҫ�����û��粿�����
	private int deptid;
	//���񴴽���
	private int creator;
	//����ί����
	private int consignorid = 0;
	/*�������ݿ����************************/
	public static final String TABLENAME = "WF_task";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,procid) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME + " SET procid=?,actid=?,flowid=?,"
		 + "pretaskid=?,nexttaskid=?,"
		 + "author=?,endtime=?,status=?,deptid=?,"
		 + "creator=?,consignorid=? WHERE ID=?";
	/***********************************/
	/*����������ȫ�ֱ���*/
	private String errorStr;
	public static final int PENDING = 0;
	public static final int PROCESSING = 1;
	public static final int DONE = 2;

	/**
	 * ���캯��һ
	 */
	public Task() {
	}
	/**
	 * ���캯����
	 * @param procid ����ID
	 * @exception SystemException ϵͳ�쳣
	 */
	public Task(String procid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.flowid = Integer.parseInt(procid);
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, this.procid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in task:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in task:constructor()-" + exception;
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
	public Task(int id) throws SystemException {
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
				this.procid = rs.getInt("procid");
		        this.actid = rs.getInt("actid");
		        this.flowid = rs.getInt("flowid");
		        this.pretaskid = rs.getInt("pretaskid");
		        this.nexttaskid = rs.getInt("nexttaskid");
		        this.author = rs.getInt("author");
		        this.createtime = rs.getTimestamp("createtime");
		        this.endtime = rs.getTimestamp("endtime");
		        this.status = rs.getInt("status");
		        this.deptid = rs.getInt("deptid");
		        this.creator = rs.getInt("creator");
		        this.consignorid = rs.getInt("consignorid");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in task:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in task:constructor()-" + e;
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
			pstmt.setInt(1, this.procid);
			pstmt.setInt(2, this.actid);
			pstmt.setInt(3, this.flowid);
			pstmt.setInt(4, this.pretaskid);
			pstmt.setInt(5, this.nexttaskid);
			pstmt.setInt(6, this.author);
			pstmt.setTimestamp(7, this.endtime);
			pstmt.setInt(8, this.status);
			pstmt.setInt(9, this.deptid);
			pstmt.setInt(10, this.creator);
			pstmt.setInt(11, this.consignorid);
			pstmt.setInt(12, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Task.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Task.java:update(): " + e;
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
	 * @return Returns the actid.
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
	 * @return Returns the pretaskid.
	 */
	public int getPreTaskId() {
		return pretaskid;
	}
	/**
	 * @param pretaskid The pretaskid to set.
	 */
	public void setPreTaskId(int pretaskid) {
		this.pretaskid = pretaskid;
	}
	/**
	 * @return Returns the nexttaskid.
	 */
	public int getNextTaskId() {
		return nexttaskid;
	}
	/**
	 * @param nexttaskid The nexttaskid to set.
	 */
	public void setNextTaskId(int nexttaskid) {
		this.nexttaskid = nexttaskid;
	}
	/**
	 * @return Returns the author.
	 */
	public int getAuthor() {
		return author;
	}
	/**
	 * @param author The author to set.
	 */
	public void setAuthor(int author) {
		this.author = author;
	}
	/**
	 * @return Returns the createtime.
	 */
	public Timestamp getCreateTime() {
		return createtime;
	}
	/**
	 * @param createtime The createtime to set.
	 */
	public void setCreateTime(Timestamp createtime) {
		this.createtime = createtime;
	}
	/**
	 * @return Returns the endtime.
	 */
	public Timestamp getEndTime() {
		return endtime;
	}
	/**
	 * @param endtime The endtime to set.
	 */
	public void setEndTime(Timestamp endtime) {
		this.endtime = endtime;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return Returns the deptid.
	 */
	public int getDeptId() {
		return deptid;
	}
	/**
	 * @param deptid The deptid to set.
	 */
	public void setDeptId(int deptid) {
		this.deptid = deptid;
	}
	/**
	 * ��ȡ�û�����
	 * @return int
	 */
	public int getCreator() {
	    return this.creator;
	}
	/**
	 * �������񴴽���
	 * @param creator ����
	 */
	public void setCreator(int creator) {
	    this.creator = creator;
	}
	/**
	 * @return the consignorid
	 */
	public int getConsignorId() {
	    return this.consignorid;
	}
	/**
	 * @param consignorid the consignorid to set
	 */
	public void setConsignorId(int consignorid) {
	    this.consignorid = consignorid;
	}
}
