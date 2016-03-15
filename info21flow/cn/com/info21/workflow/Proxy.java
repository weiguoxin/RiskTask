/*
 * 创建日期 2005-8-1
 *
 */
package cn.com.info21.workflow;

import java.sql.Connection;
import java.sql.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 *
 */
public class Proxy {
	/*定义属性*/
	//代理服务器ID
	private int id;
	//交付人ID
	private int consignorid;
	//代理人ID
	private int attorneyid;
	//返回日期
	private Timestamp returndate;
	//离开时间
	private Timestamp leftdate;
	//返回标记
	private int returnflag;

	/*定义数据库操作************/
	public static final String TABLENAME = "WF_PROXY";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,consignorid) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET consignorid=?,attorneyid=?,"
		 + "returndate=?,leftdate=?,returnflag=? WHERE ID=?";
	/***********************************/

	/*定义类运行全局变量*/
	private String errorStr;

	/**
	 * 构造函数一
	 */
	public Proxy() {
	}
	/**
	 * 构造函数二
	 * @param consignorid 交付人ID
	 * @exception SystemException 系统异常
	 */
	public Proxy(String consignorid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.consignorid = Integer.parseInt(consignorid);
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, this.consignorid);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Proxy:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Proxy:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 构造函数三
	 * @param id 代理服务器ID
	 * @throws SystemException 系统异常
	 */
	public Proxy(int id) throws SystemException {
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
				this.consignorid = rs.getInt("consignorid");
				this.attorneyid = rs.getInt("attorneyid");
				this.returndate = rs.getTimestamp("returndate");
				this.leftdate = rs.getTimestamp("leftdate");
				this.returnflag = rs.getInt("returnflag");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Proxy:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Proxy:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 更新对象
	 * @throws SystemException 系统异常
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_OBJ);
			pstmt.setInt(1, this.consignorid);
			pstmt.setInt(2, this.attorneyid);
			pstmt.setTimestamp(3, this.returndate);
			pstmt.setTimestamp(4, this.leftdate);
			pstmt.setInt(5, this.returnflag);
			pstmt.setInt(6, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Proxy.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Proxy.java:update(): " + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 *删除一条记录
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
	 * @return Returns the consignorid.
	 */
	public int getConsignorId() {
		return consignorid;
	}
	/**
	 * @param consignorid The consignorid to set.
	 */
	public void setConsignorId(int consignorid) {
		this.consignorid = consignorid;
	}
	/**
	 * @return Returns the attorneyid.
	 */
	public int getAttorneyId() {
		return attorneyid;
	}
	/**
	 * @param attorneyid The attorneyid to set.
	 */
	public void setAttorneyId(int attorneyid) {
		this.attorneyid = attorneyid;
	}
	/**
	 * @return Returns the returndate.
	 */
	public Timestamp getReturnDate() {
		return returndate;
	}
	/**
	 * @param returndate The returndate to set.
	 */
	public void setReturnDate(Timestamp returndate) {
		this.returndate = returndate;
	}
	/**
	 * @return the leftdate
	 */
	public Timestamp getLeftDate() {
	    return this.leftdate;
	}
	/**
	 * @param leftdate set the leftdate
	 */
	public void setLeftDate(Timestamp leftdate) {
	    this.leftdate = leftdate;
	}
	/**
	 * @return the returnflag
	 */
	public int getReturnFlag() {
	    return this.returnflag;
	}
	/**
	 * @param returnflag set the returnflag
	 */
	public void setReturnFlag(int returnflag) {
	    this.returnflag = returnflag;
	}
}
