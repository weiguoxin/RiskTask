/*
 * Created on 2004-6-23
 */

package cn.com.info21.org;
//import cn.com.info21.system.*;
import cn.com.info21.system.SystemException;
import cn.com.info21.database.*;
import java.util.*;
import java.sql.*;

/**
 * @author liukh
 */

public class OrganizationIterator {
	private static final String GET_ORGANIZATION_CNT =
	"SELECT COUNT(*) AS CNT FROM " + Organization.TABLENAME;
	private static final String GET_ORGANIZATION = "SELECT * FROM " + Organization.TABLENAME;
	private int currentIndex = -1;
	private int[] organizations;
	private String errorStr = "";
	/**
	* 构造方法一：根据查询条件 ，获得一个Organization对象列表。
	* 创建日期：(2003-3-25 10:24:42)
	* @param conditionStr 查询条件。
	* @throws SystemException 将错误写入到系统日志
	*/
	public OrganizationIterator(String conditionStr) throws SystemException {
		ArrayList temporganizations = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(GET_ORGANIZATION);
				sql = GET_ORGANIZATION;
			} else {
				pstmt =
					con.prepareStatement(GET_ORGANIZATION + " WHERE " + conditionStr);
				sql = GET_ORGANIZATION + " WHERE " + conditionStr;
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				temporganizations.add(new Integer(rs.getInt("ID")));
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in OrganizationIterator:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in OrganizationIterator:" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);

		}
		organizations = new int[temporganizations.size()];
		for (int i = 0; i < organizations.length; i++) {
			organizations[i] = ((Integer) temporganizations.get(i)).intValue();
		}
	}
	/**
	* 构造方法二：根据查询条件 ，从startIndex开始，获得num个Organization对象的列表。
	* 创建日期：(2003-3-25 10:24:42)
	* @param conditionStr 查询条件。
	* @param startIndex int 开始位置。
	* @param num int 要求返回对象个数。
	* @throws SystemException 将错误写入到系统日志
	*/
	public OrganizationIterator(String conditionStr, int startIndex, int num)
	throws SystemException {
		ArrayList tempOrganizations = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		int index = 0;
		int counter = 1;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(GET_ORGANIZATION);
			} else {
				pstmt =	con.prepareStatement(GET_ORGANIZATION + " WHERE " + conditionStr);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (index >= (startIndex + num)) {
					break;
				}
				if (index >= startIndex) {
					tempOrganizations.add(new Integer(rs.getInt("ID")));
				}
				index++;
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in OrganizationIterator:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in OrganizationIterator:" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);

		}
		organizations = new int[tempOrganizations.size()];
		for (int i = 0; i < organizations.length; i++) {
			organizations[i] = ((Integer) tempOrganizations.get(i)).intValue();
		}
	}
	/**
	 * 获取组织机构数目
	 * @return 数组长度
	 */
	public int getLength() {
		return organizations.length;
	}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < organizations.length);
	}
	/**
	 * 返回下一个组织机构
	 * @return Organization
	 */
	public Object next() {
		Organization organization = null;
		currentIndex++;
		try {
			organization = OrganizationHome.findById(organizations[currentIndex]);
		} catch (Exception e) {
			System.err.println(e);
		}
		return organization;
	}
	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		int[] tempOrganizations;
		tempOrganizations = new int[organizations.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tempOrganizations[i] = organizations[i];
		}
		for (int i = currentIndex + 1; i < tempOrganizations.length; i++) {
			tempOrganizations[i - 1] = organizations[i];
		}
		organizations = tempOrganizations;
	}
}
