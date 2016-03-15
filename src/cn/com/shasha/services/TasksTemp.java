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
/**临时任务类
 * 
 */
public class TasksTemp extends Tasks{
	public static final String TABLENAME = "tasks_temp";
	private static final String LOAD_BY_ID = "SELECT * FROM " + TABLENAME
			+ " WHERE ID=?";

	private String errorStr;

	private static final String INSERT_OBJ = "INSERT INTO " + TABLENAME
	+ "(nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, " +
			"limit_time, fxydcs, status, swjg_dm, zgy_dm, task_man, imp_date, imp_userid,zxr_dm,fxzb_dm,zx_swjg_dm)" +
	" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**新增记录,把自己新增入库
	 * 
	 */
	public int insertSelfTask() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int r = 0;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_OBJ,PreparedStatement.RETURN_GENERATED_KEYS);
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
			if(res.next()){
				this.setId(res.getInt(1));
			}
			//this.id = (int) pstmt.getGeneratedKeys();
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
	
	
	private static final String INSERT_TaskPackage = "INSERT INTO " + TABLENAME
	+ "(nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, " +
			"limit_time, fxydcs, status, swjg_dm, zgy_dm, task_man, imp_date, imp_userid,zxr_dm,fxzb_dm,zx_swjg_dm,shijuxiafa)" +
	" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**新增记录,把自己新增入库,市局下发任务
	 * 
	 */
	public int insertSelfTask_TaskPackage() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int r = 0;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_TaskPackage,PreparedStatement.RETURN_GENERATED_KEYS);
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
			pstmt.setInt(20, this.getShijuxiafa());
			r += pstmt.executeUpdate();
			res = pstmt.getGeneratedKeys();
			if(res.next()){
				this.setId(res.getInt(1));
			}
			//this.id = (int) pstmt.getGeneratedKeys();
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
	
	
	private static final String UPDATE_OBJ = "update " + TABLENAME
	+ " set nsrsbh=?, nsrmc=?, swjg_mc=?, zgy_mc=?, fxzb=?, fxms=?,  " +
	"limit_time=?, fxydcs=?, status=?, swjg_dm=?, zgy_dm=?, task_man=?, zxr_dm=?,fxzb_dm=?,zx_swjg_dm=?" +
	"  where id = ?";
	
	public int upDateSelf(String whereStr){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int r = 0;
		try {
			con = DbConnectionManager.getConnection();
			if(null == whereStr)
				pstmt = con.prepareStatement(UPDATE_OBJ);
			else{
				pstmt = con.prepareStatement(UPDATE_OBJ + whereStr);
			}
			pstmt.setString(1, this.getNsrsbh());
			pstmt.setString(2, this.getNsrmc());
			pstmt.setString(3, this.getSwjg_mc());
			pstmt.setString(4, this.getZgy_mc());
			pstmt.setString(5, this.getFxzb());
			pstmt.setString(6, this.getFxms());
			pstmt.setTimestamp(7, this.getLimit_time());
			pstmt.setString(8, this.getFxydcs());
			pstmt.setInt(9, -1);
			pstmt.setString(10, this.getSwjg_dm());
			pstmt.setString(11, this.getZgy_dm());
			pstmt.setString(12, this.getTask_man());
			pstmt.setInt(13, this.getZxr_dm());
			pstmt.setString(14, this.getFxzb_dm());
			pstmt.setString(15, this.getZx_swjg_dm());
			pstmt.setInt(16, this.getId());
			r = pstmt.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, res);
		}
		return r;
	}
}
