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
	private String orgName = ""; //��֯��
	private String shortName = ""; //���
	private String address = ""; //��ַ
	private String postcode = ""; //��������
	private String errorStr = "";
	//���ʼ��SQL��䶨��
	public static final String TABLENAME = "ORG_ORGANIZATION";
	private static final String LOAD_ORGANIZATION_BY_ID =
						"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_ORGANIZATION = "INSERT INTO " + TABLENAME + "("
			+ "id, orgname) VALUES(?, ?)";
	private static final String UPDATE_USERS = "UPDATE " + TABLENAME + " SET orgname=?,"
			+ "shortname=?,address=?,postcode=? where id=?";
	/**
	 * ���캯��
	 * @param id ��λID
	 * @throws SystemException ������д�뵽ϵͳ��־
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
	 * ���캯��
	 * @param orgName ��֯��
	 * @throws SystemException д����־
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
			//��鲿����֯�Ƿ����
			if (SysFunction.getCnt(
				"WF_Organization", "orgname = '" + orgName + "'") > 0) {
				errorStr = "�Ѿ����ڵ�orgname!";
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
	 * ���ӿͻ���֯��Ϣ
	 * @throws SystemException ������д�뵽ϵͳ��־
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
	 * ������֯������Ϣ
	 * @throws SystemException ������д�뵽ϵͳ��־
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
	 * ��ȡ��֯����
	 * @return String
	 */
	public String getOrgName() {
		return this.orgName;
	}
	/**
	 * ��ȡ��֯���
	 * @return String
	 */
	public String getShortName() {
		return this.shortName;
	}
	/**
	 * ������֯��������
	 * @param orgName ��֯��������
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * ������֯�������
	 * @param shortName ��֯�������
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