package cn.com.info21.org;

import java.util.*;
import java.sql.*;
import cn.com.info21.database.*;
import cn.com.info21.system.*;
/**
 * @author db2admin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RoleIterator implements Iterator {
	
	private int currentIndex = -1;
	private int[] roles;
	private String GET_ROLE = "SELECT ID FROM " + Role.TABLENAME;
	private String errorStr="";
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-28 11:17:49)
 * @param conditionStr java.lang.String
 * @exception cn.com.info21.system.SystemException 异常说明。
 */
public RoleIterator(String conditionStr) throws SystemException {
		ArrayList tempRoles = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(GET_ROLE);
				sql = GET_ROLE;
			} else {
				pstmt = con.prepareStatement(GET_ROLE + " WHERE " + conditionStr);
				sql = GET_ROLE + " WHERE " + conditionStr;	
			}

			//Log.info(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tempRoles.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in RoleIterator:constructor()-" + sqle;
			throw new SystemException(errorStr);
		} catch (Exception exception) {
			errorStr = "SQLException in RoleIterator:" + exception;
			throw new SystemException(errorStr);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		//Now copy into an array.
		roles = new int[tempRoles.size()];
		for (int i = 0; i < roles.length; i++) {
			roles[i] = ((Integer) tempRoles.get(i)).intValue();
		}
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-28 11:19:13)
 * @param conditionStr java.lang.String
 * @param startIndex int
 * @param num int
 * @exception cn.com.info21.system.SystemException 异常说明。
 */
public RoleIterator(String conditionStr, int startIndex, int num) throws SystemException {
		ArrayList tempRoles = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int index = 0;
		int counter = 1;
		
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(GET_ROLE);
			} else {
				pstmt =
					con.prepareStatement(GET_ROLE + " WHERE " + conditionStr);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (index >= (startIndex + num))
					break;
				if (index >= startIndex) {
					tempRoles.add(new Integer(rs.getInt("ID")));
				}
				index++;
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException  in RoleIterator:constructor()-" + sqle;
			throw new SystemException(errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in RoleIterator:constructor()-" + exception;
			throw new SystemException(errorStr);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		//Now copy into an array.
		roles = new int[tempRoles.size()];
		for (int i = 0; i < roles.length; i++) {
			roles[i] = ((Integer) tempRoles.get(i)).intValue();
		}	
}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < roles.length);
	}
	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() throws NoSuchElementException{
		Role role = null;
		currentIndex++;
		if (currentIndex >= roles.length) {
			throw new NoSuchElementException();
		}
		try {
			role = RoleHome.findById(roles[currentIndex]);
		} catch (SystemException e) {
		}
		return role;
	}
	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
	}
}
