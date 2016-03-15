package cn.com.info21.menu;

import cn.com.info21.system.SystemException;
import cn.com.info21.database.*;

import java.util.*;
import java.sql.*;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-4-8 10:00:56)
 * @author：Administrator
 */
public class MenuIterator implements Iterator{
	private int currentIndex = -1;
	private String[] menus;
	private String GET_MENUS = "SELECT * FROM SYS_MENUS";
	private static final String GET_MENUS_CNT =
		"SELECT COUNT(*) AS CNT FROM SYS_MENUS";
	private String strError="";
	/**
	* 构造方法一：根据查询条件 ，获得一个Menus对象列表。
	* 创建日期：(2003-3-25 10:24:42)
	* @param condition String 查询条件。
	* @throws SystemException。
	*/
	protected MenuIterator(String conditionStr) throws SystemException {
		//We don't know how many results will be returned, so store them in an ArrayList.
		ArrayList tempMenus = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(GET_MENUS);
				sql = GET_MENUS;
			} else {
				pstmt =
					con.prepareStatement(GET_MENUS + " WHERE " + conditionStr);
				sql = GET_MENUS + " WHERE " + conditionStr;	
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tempMenus.add(new String(rs.getString(1)));
			}
		} catch (SQLException sqle) {
			strError = "SQLException in MenuIterator(String)-" + sqle;
			//Log.error(strError);
			throw new SystemException(strError);
		} catch (Exception exception) {
			strError = "Exception in MenuIterator(String)-" + exception;
			//Log.error(strError);
			throw new SystemException(strError);			
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
		menus = new String[tempMenus.size()];
		for (int i = 0; i < menus.length; i++) {
			menus[i] = (String)tempMenus.get(i);
		}
	}
	/**
	* 构造方法二：根据查询条件 ，从startIndex开始，获得num个Menus对象的列表。
	* 创建日期：(2003-3-25 10:24:42)
	* @param condition String 查询条件。
	* @param startIndex int 开始位置。
	* @param num int 要求返回对象个数。
	* @throws SystemException。
	*/
	protected MenuIterator(String conditionStr, int startIndex, int num)
		throws SystemException {
		ArrayList tempMenus = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int	cnt = 0;
		int index = 0;
		int counter = 1;
		
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(GET_MENUS);
			} else {
				pstmt =
					con.prepareStatement(GET_MENUS + " WHERE " + conditionStr);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (index >= (startIndex + num))
					break;
				if (index >= startIndex) {
					tempMenus.add(rs.getString("ID"));
				}
				index++;
			}
		} catch (SQLException sqle) {
			strError = "SQLException  in MenuIterator(String,int,int)-" + sqle;
			//Log.error(strError);
			throw new SystemException(strError);
		} catch (Exception exception) {
			strError = "Exception in MenuIterator(String,int,int)-" + exception;
			//Log.error(strError);
			throw new SystemException(strError);
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
		menus = new String[tempMenus.size()];
		for (int i = 0; i < menus.length; i++) {
			menus[i] = (String) tempMenus.get(i);
		}
	}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < menus.length);
	}
	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		Menu menus1 = null;
		currentIndex++;
		if (currentIndex >= menus.length) {
			throw new NoSuchElementException();
		}
		try {
			menus1 = MenuHome.findById(menus[currentIndex]);
		} catch (SystemException e) {
			//Log.error(e);
		}
		return menus1;
	}
	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
	}
}
