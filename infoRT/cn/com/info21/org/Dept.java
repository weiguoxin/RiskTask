/*
 * Created on 2004-5-10
 *
 */

package cn.com.info21.org;
import java.sql.*;
import cn.com.info21.database.*;
//import cn.com.info21.system.*;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liusihde
 * 部门类
 */
public class Dept {
	public static final String TABLENAME = "Org_Dept";
	private static final String INSERT = "INSERT into " + TABLENAME + "(id, deptName) values(?,?)";
	private static final String LOADBYID = "Select * from "+ TABLENAME + " where id = ?";
	private static final String UPDATE =
		"Update " + TABLENAME + " set email = ?,classNum = ?,parentid = ?,status = ?,deptname=?,enable=?,alias=? where id =?";
	private String error = "";

	private int id = 0;
	private String deptName = "";
	private String email = "";
	private int classNum = 0;
	private int parentid = 0;
	private int status = 0;
	private int enable = 0;
	private String alias = null;
	/**
	 * 默认构造函数
	 */
	public Dept() {
	}
	/**
	 * 构造函数
	 * @param id 是传入的ID
	 * @throws SystemException 将错误写入到系统日志
	 */
	public Dept(int id) throws SystemException {
    	java.sql.Connection con = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOADBYID);
			pstmt.setInt(1 , id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = rs.getInt("ID");
				this.deptName = rs.getString("deptName");
				this.email = rs.getString("email");
				this.classNum = rs.getInt("classNum");
				this.parentid = rs.getInt("parentid");
				this.status = rs.getInt("status");
				this.enable = rs.getInt("enable");
				this.alias = rs.getString("alias");
			}
		} catch (SQLException sqle) {
			error = "SQLException in Dept:constructor(int)-" + sqle;
			throw new SystemException(this, error);
		} catch (Exception exception) {
			error = "Exception in Dept:constructor(int)-" + exception;
			throw new SystemException(this, error);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 构造函数
	 * @param deptName 是传入的deptName值
	 * @exception SystemException 系统异常
	 */
	public Dept(String deptName) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT);
			this.id = (int) (SysFunction.getMaxID("WF_Dept") + 1);
			this.deptName = deptName;
			pstmt.setInt(1, this.id);
			pstmt.setString(2, this.deptName);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			error = "SQLException in Dept:constructor(String,string)-" + sqle;
			throw new SystemException(this, error);
		} catch (Exception exception) {
			error = "Exception in Dept:constructor(String,string)-" + exception;
			throw new SystemException(this, error);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 更改数据库
	 * @throws SystemException 将错误写入到系统日志
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, this.email);
			pstmt.setInt(2, this.classNum);
			pstmt.setInt(3, this.parentid);
			pstmt.setInt(4, this.status);
			pstmt.setString(5, this.deptName);
			pstmt.setInt(6, this.enable);
			pstmt.setString(7, this.alias);
			pstmt.setInt(8, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			error = "SQLException in Dept::update(): " + sqle;
			throw new SystemException(this, error);
		} catch (Exception e) {
			error = "Exception in Dept::update(): " + e;
			throw new SystemException(this, error);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
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
	 * @return Returns the deptName.
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName The deptName to set.
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the alias.
	 */
	public String getAlias() {
		return alias;
	}
	/**
	 * @param alias The alias to set.
	 */
	public void setAlias(String alias) {
		this.alias = alias;
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
	 * @return Returns the parentid.
	 */
	public int getParentid() {
		return parentid;
	}
	/**
	 * @param parentid The parentid to set.
	 */
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	/**
	 * @return Returns the classNum.
	 */
	public int getClassNum() {
		return classNum;
	}
	/**
	 * @param classNum The classNum to set.
	 */
	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}
}