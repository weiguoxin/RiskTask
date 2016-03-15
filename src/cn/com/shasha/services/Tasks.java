package cn.com.shasha.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import cn.com.info21.authtools.Encrypt;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

//id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, limit_time, fxydcs, status, swjg_dm, zgy_dm, task_man
public class Tasks {
	public static final String TABLENAME = "tasks";
	private static final String LOAD_BY_ID = "SELECT * FROM " + TABLENAME
			+ " WHERE ID=?";
	private static final String UPDATE_OBJ = "UPDATE "
			+ TABLENAME
			+ " SET title=?,fqr=?,qc_riqi=?,beizhu=?,filename=?,status=?,fj_yj=?,"
			+ "zj_yj=?,isok=?,zs_riqi=?,py_riqi=?,pyr=?,jsr=?,zsr=?,taskstatus=? WHERE id=?";
	private String errorStr;

	/* 定义属性 */

	private int id = -1;
	private int status = -1;
	private int imp_userid = -1;
	private int zxr_dm = -1;
	private int checked = 0;

	private String nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, fxydcs, swjg_dm,
			zgy_dm, task_man, rwhk, fxzb_dm, zx_swjg_dm;

	private Timestamp begin_time, end_time, limit_time, imp_date;

	
	private int shijuxiafa = 0; //市局下发,非0就是市局下发,对应TaskPackage的ID
	private int jieshouren = 0; //县局接收人ID
	private String jieshouren_c ; //接收人姓名
	private Date jieshoudate; //接受日期
	public int getShijuxiafa() {
		return shijuxiafa;
	}

	public void setShijuxiafa(int shijuxiafa) {
		this.shijuxiafa = shijuxiafa;
	}

	public int getJieshouren() {
		return jieshouren;
	}

	public void setJieshouren(int jieshouren) {
		this.jieshouren = jieshouren;
	}

	public String getJieshouren_c() {
		return jieshouren_c;
	}

	public void setJieshouren_c(String jieshouren_c) {
		this.jieshouren_c = jieshouren_c;
	}

	public Date getJieshoudate() {
		return jieshoudate;
	}

	public void setJieshoudate(Date jieshoudate) {
		this.jieshoudate = jieshoudate;
	}

	public String getZx_swjg_dm() {
		return zx_swjg_dm;
	}

	public void setZx_swjg_dm(String zx_swjg_dm) {
		this.zx_swjg_dm = zx_swjg_dm;
	}

	public String getFxzb_dm() {
		return fxzb_dm;
	}

	public void setFxzb_dm(String fxzb_dm) {
		this.fxzb_dm = fxzb_dm;
	}

	public String getRwhk() {
		return rwhk;
	}

	public void setRwhk(String rwhk) {
		this.rwhk = rwhk;
	}

	/**
	 * 默认构造函数
	 */
	public Tasks() {
	}

	/**
	 * 构造函数二 根据ID号,从数据库中读取信息
	 * 
	 * @param id
	 *            对象ID
	 * @exception SystemException
	 *                系统异常
	 */
	public Tasks(int id) throws SystemException {
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

			} else {
				errorStr = "Not found!";
				throw new SystemException(errorStr);
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Gardens:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Gardens:constructor()-" + e;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}

	/**
	 * 构造函数二
	 * 
	 * @param name
	 *            项目名称
	 * @exception SystemException
	 *                系统异常
	 */
	public Tasks(String title) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.id = (int) SysFunction.getMaxID("gardens") + 1;
			con = DbConnectionManager.getConnection();
			// if (this.id != -1) {
			pstmt = con.prepareStatement(INSERT_OBJ);
			pstmt.setInt(1, this.id);
			pstmt.executeUpdate();
			this.id = (int) SysFunction.getMaxID(TABLENAME);
			// }
		} catch (SQLException sqle) {
			errorStr = "SQLException in Gardens:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Gardens:constructor()-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}

	private static final String INSERT_OBJ = "INSERT INTO "
			+ TABLENAME
			+ "(nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, "
			+ "limit_time, fxydcs, status, swjg_dm, zgy_dm, task_man, imp_date, imp_userid,zxr_dm,fxzb_dm,zx_swjg_dm)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 新增记录,把自己新增入库
	 * 
	 */
	public int insertSelfTask() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int r = 0;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_OBJ,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, this.getNsrsbh());
			pstmt.setString(2, this.getNsrmc());
			pstmt.setString(3, this.getSwjg_mc());
			pstmt.setString(4, this.getZgy_mc());
			pstmt.setString(5, this.getFxzb());
			pstmt.setString(6, this.getFxms());
			pstmt.setTimestamp(7, null);
			pstmt.setTimestamp(8, null);
			pstmt.setTimestamp(9, this.getLimit_time());
			pstmt.setString(10, this.getFxydcs());
			pstmt.setInt(11, this.getStatus());
			pstmt.setString(12, this.getSwjg_dm());
			pstmt.setString(13, this.getZgy_dm());
			pstmt.setString(14, this.getTask_man());
			pstmt.setTimestamp(15, this.getImp_date());
			pstmt.setInt(16, this.getImp_userid());
			pstmt.setInt(17, this.getZxr_dm());
			pstmt.setString(18, this.getFxzb_dm());
			pstmt.setString(19, this.getZx_swjg_dm());
			r += pstmt.executeUpdate();
			res = pstmt.getGeneratedKeys();
			if (res.next()) {
				this.id = res.getInt(1);
			}
			// this.id = (int) pstmt.getGeneratedKeys();
		} catch (SQLException sqle) {
			errorStr = "insertSelfTask - SQL " + sqle;
			sqle.printStackTrace();
		} catch (Exception exception) {
			errorStr = "Exception in Gardens:constructor()-" + exception;
			exception.printStackTrace();
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, res);
		}
		return r;
	}

	/**
	 * 更新纪录
	 * 
	 * @exception SystemException
	 *                系统异常
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_OBJ);

			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			errorStr = "SQLException in Gardens.java:update(): " + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception e) {
			e.printStackTrace();
			errorStr = "Exception in Gardens.java:update(): " + e;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}

	public void remove() throws SystemException {
		SysFunction.delRecord(TABLENAME, this.id);
	}

	public String getErrorStr() {
		return errorStr;
	}

	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNsrsbh() {
		return nsrsbh;
	}

	public void setNsrsbh(String nsrsbh) {
		this.nsrsbh = nsrsbh;
	}

	public String getNsrmc() {
		return nsrmc;
	}

	public void setNsrmc(String nsrmc) {
		this.nsrmc = nsrmc;
	}

	public String getSwjg_mc() {
		return swjg_mc;
	}

	public void setSwjg_mc(String swjg_mc) {
		this.swjg_mc = swjg_mc;
	}

	public String getZgy_mc() {
		return zgy_mc;
	}

	public void setZgy_mc(String zgy_mc) {
		this.zgy_mc = zgy_mc;
	}

	public String getFxzb() {
		return fxzb;
	}

	public void setFxzb(String fxzb) {
		this.fxzb = fxzb;
	}

	public String getFxms() {
		return fxms;
	}

	public void setFxms(String fxms) {
		this.fxms = fxms;
	}

	public String getFxydcs() {
		return fxydcs;
	}

	public void setFxydcs(String fxydcs) {
		this.fxydcs = fxydcs;
	}

	public String getSwjg_dm() {
		return swjg_dm;
	}

	public void setSwjg_dm(String swjg_dm) {
		this.swjg_dm = swjg_dm;
	}

	public String getZgy_dm() {
		return zgy_dm;
	}

	public void setZgy_dm(String zgy_dm) {
		this.zgy_dm = zgy_dm;
	}

	public String getTask_man() {
		return task_man;
	}

	public void setTask_man(String task_man) {
		this.task_man = task_man;
	}

	public Timestamp getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(Timestamp begin_time) {
		this.begin_time = begin_time;
	}

	public Timestamp getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}

	public Timestamp getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(Timestamp limit_time) {
		this.limit_time = limit_time;
	}

	public int getImp_userid() {
		return imp_userid;
	}

	public void setImp_userid(int imp_userid) {
		this.imp_userid = imp_userid;
	}

	public Timestamp getImp_date() {
		return imp_date;
	}

	public void setImp_date(Timestamp imp_date) {
		this.imp_date = imp_date;
	}

	public int getZxr_dm() {
		return zxr_dm;
	}

	public void setZxr_dm(int zxr_dm) {
		this.zxr_dm = zxr_dm;
	}

	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

}
