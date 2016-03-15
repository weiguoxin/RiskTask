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
	//类初始化SQL语句定义
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
	 * 默认构造器
	 */
	public Duty() {
	}
	/**
	 * 构造函数
	 * 根据职务ID号,从数据库中读取职务信息
	 * @param id 职务编号
	 * @throws SystemException 将错误写入到系统日志
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
	 * 设置职务名称
	 * @param name 职务
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return 职务ID
	 */
	public int getID() {
		return this.id;
	}
	/**
	 * @return 职务名称
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * 构造函数
	 * @param name 是name值
	 * @throws SystemException 返回
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
	 * 增加一条职务信息
	 * @throws SystemException 将错误写入到系统日志
	 */
	public void add() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			//检查用户是否存在
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
	 * 更新职务信息
	 * @throws SystemException 将错误写入到系统日志
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
	 * 获取用户成员集
	 * @return int[][]
	 * @throws SystemException 捕获系统错误
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
	 * 设置用户成员
	 * @param userdeptids 用户/部门ID
	 * @throws SystemException 捕获系统错误
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
