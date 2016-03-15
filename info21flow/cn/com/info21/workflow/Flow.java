/*
 * �������� 2005-7-20
 */

package cn.com.info21.workflow;

import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author songle
 * FlowӦ�ö���
 */

public class Flow {
	/*��������*/
	//����ʵ�����к�ID
	private int id;
	//����ID
	private int procid;
	//��������ģʽ���Զ�������|���̹���
	private int runmode;
	//���̵�һ������ID
	private int starttaskid;
	//���̽�������ID
	private int endtaskid;
	//���̴�����
	private int createuser;
	//����״̬,����|����|����
	private int status;
	//������ID
	private int parflowid;
	//���������������̵�����ID
	private int partaskid;

	/*�������ݿ����************************/
	public static final String TABLENAME = "WF_flow";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,procid) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME + " SET procid=?,runmode=?,start_task_id=?,"
		 + "end_task_id=?,createuser=?,status=?,par_flowid=?,par_taskid=? WHERE ID=?";
	/***********************************/
	/*����������ȫ�ֱ���*/
	private String errorStr;
	public static final int STATUS_ACTIVATE = 0;
	public static final int STATUS_PAUSING = 1;
	public static final int STATUS_DONE = 2;
	public static final int RUNMODE_FLOW = 0;
	public static final int RUNMODE_SEQUENCE = 1;
	/**
	 * ���캯��һ
	 */
	public Flow() {
	}
	/**
	 * ���캯����
	 * @param procid ����ID
	 * @exception SystemException ϵͳ�쳣
	 */
	public Flow(String procid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.procid = Integer.parseInt(procid);
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, this.procid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Flow:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Flow:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * ���캯����
	 * @param id ����ʵ�����к�ID
	 * @throws SystemException ϵͳ�쳣
	 */
	public Flow(int id) throws SystemException {
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
			    this.runmode = rs.getInt("runmode");
			    this.starttaskid = rs.getInt("start_task_id");
			    this.endtaskid = rs.getInt("end_task_id");
			    this.createuser = rs.getInt("createuser");
			    this.status = rs.getInt("status");
			    this.parflowid = rs.getInt("par_flowid");
			    this.partaskid = rs.getInt("par_taskid");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Flow:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Flow:constructor()-" + e;
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
			pstmt.setInt(2, this.runmode);
			pstmt.setInt(3, this.starttaskid);
			pstmt.setInt(4, this.endtaskid);
			pstmt.setInt(5, this.createuser);
			pstmt.setInt(6, this.status);
			pstmt.setInt(7, this.parflowid);
			pstmt.setInt(8, this.partaskid);
			pstmt.setInt(9, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Flow.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Flow.java:update(): " + e;
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
	 * @return Returns the procid
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
	 * @return Returns the starttaskid.
	 */
	public int getStartTaskId() {
		return starttaskid;
	}
	/**
	 * @param starttaskid The starttaskid to set.
	 */
	public void setStartTaskId(int starttaskid) {
		this.starttaskid = starttaskid;
	}
	/**  
	 * @return Returns the endtaskid.
	 */
	public int getEndTaskId() {
		return endtaskid;
	}
	/**
	 * @param endtaskid The endtaskid to set.
	 */
	public void setEndTaskId(int endtaskid) {
		this.endtaskid = endtaskid;
	}
	/**
	 * @return Returns the createuser.
	 */
	public int getCreateUser() {
		return createuser;
	}
	/**
	 * @param createuser The createruser to set.
	 */
	public void setCreateUser(int createuser) {
		this.createuser = createuser;
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
	 * @return Returns the parflowid.
	 */
	public int getParFlowId() {
		return parflowid;
	}
	/**
	 * @param parflowid The parflowid to set.
	 */
	public void setParFlowId(int parflowid) {
		this.parflowid = parflowid;
	}
	/**
	 * @return Returns the partaskid.
	 */
	public int getParTaskId() {
		return partaskid;
	}
	/**
	 * @param partaskid The partaskid to set.
	 */
	public void setParTaskId(int partaskid) {
		this.partaskid = partaskid;
	}
	/**
	 * ��ȡ��ǰ���̵����񼯺�
	 * @return TaskIterator
	 * @throws SystemException ϵͳ�쳣
	 */
	public TaskIterator getTasks() throws SystemException {
	    return TaskHome.findByCondition(" flowid=" + this.id);
	}
	/**
	 * ��ȡ���̸��ټ�¼
	 * @return TrackIterator
	 */
	public TrackIterator getTracks() {
	    TrackIterator tracks = null;
	    try {
	        tracks = TrackHome.findByCondition(" flowid=" + this.id + " order by id");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return tracks;
	}
}