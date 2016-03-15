/*
 * Created on 2004-6-18
 */

package cn.com.info21.org;
import java.sql.*;
import cn.com.info21.database.*;
//import cn.com.info21.system.*;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class Organization {
	private int id = 0;
	private String orgName = ""; //组织名
	private String shortName = ""; //简称
	private String address = ""; //地址
	private String postcode = ""; //邮政编码
	private String errorStr = "";
	//类初始化SQL语句定义
	public static final String TABLENAME = "ORG_ORGANIZATION";
	private static final String LOAD_ORGANIZATION_BY_ID =
						"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_ORGANIZATION = "INSERT INTO " + TABLENAME + "("
			+ "id, orgname) VALUES(?, ?)";
	private static final String UPDATE_USERS = "UPDATE " + TABLENAME + " SET orgname=?,"
			+ "shortname=?,address=?,postcode=? where id=?";
	/**
	 * 构造函数
	 * @param id 单位ID
	 * @throws SystemException 将错误写入到系统日志
	 */
	public Organization(int id) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_ORGANIZATION_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id;
				this.orgName = rs.getString("orgname");
				this.shortName = rs.getString("shortname");
				this.address = rs.getString("address");
				this.postcode = rs.getString("postcode");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Organization:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Organization:constructor()-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 构造函数
	 * @param orgName 组织名
	 * @throws SystemException 写入日志
	 */
	protected Organization(String orgName) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		/*
		 "INSERT INTO WF_organization("
			+ "id, orgname) VALUES(?, ?)";
		 */
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_ORGANIZATION);
			//检查部门组织是否存在
			if (SysFunction.getCnt(
				"WF_Organization", "orgname = '" + orgName + "'") > 0) {
				errorStr = "已经存在的orgname!";
				System.err.println(errorStr);
				throw new SystemException(this, errorStr);
			}
			this.id = (int) (SysFunction.getMaxID("WF_Organization") + 1);
			this.orgName = orgName;
			pstmt.setInt(1, this.id);
			pstmt.setString(2, this.orgName);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Dept:constructor(String)-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Dept:constructor(String)-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 增加客户组织信息
	 * @throws SystemException 将错误写入到系统日志
	 */
	public void add() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_ORGANIZATION);
			pstmt.setString(1, this.orgName);
			pstmt.setString(2, this.shortName);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Organization:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Organization:constructor()-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 更新组织机构信息
	 * @throws SystemException 将错误写入到系统日志
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		/*
		 UPDATE_USERS = "UPDATE WF_organization SET orgname=?,"
			+ "shortname=?,address=?,postcode=? where id=?";
		 */
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_USERS);
			pstmt.setString(1, this.orgName);
			pstmt.setString(2, this.shortName);
			pstmt.setString(3, this.address);
			pstmt.setString(4, this.postcode);
			pstmt.setInt(5, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Organization:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Organization:constructor()-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 获取组织名称
	 * @return String
	 */
	public String getOrgName() {
		return this.orgName;
	}
	/**
	 * 获取组织简称
	 * @return String
	 */
	public String getShortName() {
		return this.shortName;
	}
	/**
	 * 设置组织机构名称
	 * @param orgName 组织机构名称
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * 设置组织机构简称
	 * @param shortName 组织机构简称
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @return Returns the postcode.
	 */
	public String getPostcode() {
		return postcode;
	}
	/**
	 * @param postcode The postcode to set.
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}