package cn.com.info21.menu;

import cn.com.info21.system.*;
import cn.com.info21.database.*;

import java.sql.*;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-4-8 10:00:24)
 * @author：Administrator
 */
public class MenuHome {

	public static Menu create(String id, String name) throws SystemException {
		Menu menu = null;
		if ((id == null) || ("".equals(id))) {
			throw new SystemException("编号和名称不能为空！");
		} else {
			menu = new Menu(id, name);
		}
		return menu;
	}
	public static MenuIterator findByCondition(String conditionStr)
		throws SystemException {
		return new MenuIterator(conditionStr);
	}
	public static MenuIterator findByCondition(
		String conditionStr,
		int startIndex,
		int num)
		throws SystemException {
		return new MenuIterator(conditionStr, startIndex, num);
	}
	public static Menu findById(String id) throws SystemException {
		if (id ==null || "".equals(id)) {
			return null;
		}
		return new Menu(id);
	}
	public static Menu findByName(String name) throws SystemException {
		Menu menu = null;
		try {
			menu =
				(Menu) (MenuHome.findByCondition("NAME='" + name + "'").next());
		} catch (Exception e) {
			//Log.error(e);
		}
		return menu;
	}
	public static String getMenuId(String conditionStr)
		throws SystemException {
		try {
			return ((Menu) (MenuHome.findByCondition(conditionStr).next()))
				.getId();
		} catch (Exception e) {
			return "";
		}

	}

	public static String getColId(String name) throws SystemException {
		try {
			return (
				(Menu) (MenuHome
					.findByCondition("NAME='" + name + "' AND TYPE='3'")
					.next()))
				.getId();
		} catch (Exception e) {
			return "";
		}
	}
	public static String getFirstColId(String name) throws SystemException {
		String colId = "";
		try {
			MenuIterator menuIterator = MenuHome.findByCondition("PID IN (SELECT ID FROM SYS_MENUS WHERE NAME='" + name + "') AND TYPE='3' ORDER BY ID");
			if (menuIterator.hasNext())	{
				colId = ((Menu)menuIterator.next()).getId();	
			} else {
				colId = getColId(name);
			}
		} catch (Exception e) {
			//Log.error(e);
		}
		return colId;
	}
	public static int getMenuCount(String conditionStr)
		throws SystemException {
		return SysFunction.getCnt("SYS_MENUS", conditionStr);
	}
	public static void remove(String id) throws SystemException {
		findById(id).remove();
	}

	public static String getChildMenuId(String fId) {
		String GET_FOLDER_ID = "SELECT MAX(ID) FROM SYS_MENUS WHERE PID=?";
		String foldId = fId + "01";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(GET_FOLDER_ID);
			pstmt.setString(1, fId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				foldId = rs.getString(1);
				String tempStr =
					foldId.substring(foldId.indexOf(fId) + fId.length());
				while ("0".equals(tempStr.substring(0, 1))) {
					tempStr = tempStr.substring(1, tempStr.length());
				}
				int tempInt = Integer.parseInt(tempStr) + 1;
				if (tempInt > 9) {
					foldId = fId + String.valueOf(tempInt);
				} else {
					foldId = fId + "0" + String.valueOf(tempInt);
				}
			}
		} catch (SQLException sqle) {
			//Log.error(sqle);
		} catch (Exception exception) {
			//Log.info(exception);
			foldId = fId + "01";
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
		return foldId;
	}
}
