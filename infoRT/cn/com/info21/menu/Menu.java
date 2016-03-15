package cn.com.info21.menu;
import java.sql.*;
import cn.com.info21.system.*;
import cn.com.info21.database.*;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-4-8 9:59:47)
 * @author：Administrator
 */
public class Menu {
	private static final String LOAD_MENUS_BY_ID =
		"SELECT * FROM SYS_MENUS WHERE ID=?";
	private static final String INSERT_MENUS =
		"INSERT INTO SYS_MENUS(ID, NAME) VALUES(?, ?)";
	private static final String UPDATE_MENUS =
		"UPDATE SYS_MENUS SET NAME=?,PID=?,MLEVEL=?,MEXPLAIN=?,ISLEAF=?,ISENABLE=?,HREF=?,TYPE=? WHERE ID=? ";
	private static final String GET_OPERATION =
		"SELECT OPERATION FROM SYS_MENU_OPERATION WHERE MENUID=?";
	private java.lang.String id; //编号;
	private java.lang.String name; //菜单名;
	private java.lang.String fid; //上级菜单ID
	private int mlevel; //菜单层次
	private java.lang.String explain; //菜单说明
	private java.lang.String isleaf; //是否叶节点
	private java.lang.String isenable; //是否可用
	private java.lang.String href; //链接地址
	private java.lang.String type; //类别
	private String errorStr = "";

	/**
	* 构造方法一：根据菜单的id号，从数据库中读取菜单信息。
	* 创建日期：(2003-3-25 10:24:42)
	* @param id java.lang.String 菜单编号。
	* @throws SystemException。
	*/
	protected Menu(String id) throws SystemException {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_MENUS_BY_ID);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id;
				this.name = rs.getString("NAME");
				this.fid = rs.getString("PID");
				this.mlevel = rs.getInt("MLEVEL");
				this.explain = rs.getString("MEXPLAIN");
				this.isleaf = rs.getString("ISLEAF");
				this.isenable = rs.getString("ISENABLE");
				this.href = rs.getString("HREF");
				this.type = rs.getString("TYPE");
			} else {
				errorStr =
					"Exception in Menus:constructor()-"
						+ "id="
						+ id
						+ "的对象不存在！";
				throw new SystemException(errorStr);
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Menus:constructor()-" + sqle;
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Menus:constructor()-" + exception;
			//Log.error(errorStr);
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
	}
	protected Menu(String id, String name) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_MENUS);
			this.id = id;
			this.name = name;
			pstmt.setString(1, this.id);
			pstmt.setString(2, this.name);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Menu:constructor()-" + sqle;
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Menu:constructor()-" + exception;
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} finally {
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
	}
	public java.lang.String getExplain() {
		return this.explain;
	}
	public java.lang.String getFid() {
		return this.fid;
	}
	public String getHref() {
		return this.href;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public java.lang.String getIsenable() {
		return this.isenable;
	}
	public java.lang.String getIsleaf() {
		return this.isleaf;
	}
	public int getMlevel() {
		return this.mlevel;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setIsenable(String isenable) {
		this.isenable = isenable;
	}
	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}
	public void setMlevel(int mlevel) {
		this.mlevel = mlevel;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_MENUS);
			pstmt.setString(1, this.name);
			pstmt.setString(2, this.fid);
			pstmt.setInt(3, this.mlevel);
			pstmt.setString(4, this.explain);
			pstmt.setString(5, this.isleaf);
			pstmt.setString(6, this.isenable);
			pstmt.setString(7, this.href);
			pstmt.setString(8, this.type);
			pstmt.setString(9, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Menu.java:update(): " + sqle;
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Menu.java:update(): " + e;
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} finally {
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
	}

	public void remove() throws SystemException {
		SysFunction.delRecord("SYS_MENUS", " ID = '" + this.id + "'");
		SysFunction.delRecord("SYS_MENUS", " PID = '" + this.id + "'");
		SysFunction.delRecord("SYS_ROLEMENUS", " MENUID = '" + this.id + "'");
		SysFunction.delRecord(
			"INFO_ARTICLE",
			" INTERCOL = '" + this.id + "' OR INTRACOL = '" + this.id + "'");
		SysFunction.delRecord(
			"INFO_ARTICLE",
			" INTERCOL IN (SELECT ID FROM SYS_MENUS WHERE PID = '"
				+ this.id
				+ "') OR INTRACOL IN (SELECT ID FROM SYS_MENUS WHERE PID = '"
				+ this.id
				+ "')");
	}

	public String getFullName() throws SystemException {
		String fullName = this.name;
		int tempLevel = this.mlevel;
		String tempId = this.fid;
		while (tempLevel > 3) {
			Menu fMenu = MenuHome.findById(tempId);
			fullName = fMenu.getName() + ">>" + fullName;
			tempId = fMenu.getFid();
			tempLevel = fMenu.getMlevel();
		}
		return fullName;
	}
	public String getPathName() throws SystemException {
		String fullName = this.name;
		int tempLevel = this.mlevel;
		String tempId = this.fid;
		while (tempLevel > 2) {
			Menu fMenu = MenuHome.findById(tempId);
			fullName = fMenu.getName() + ">>" + fullName;
			tempId = fMenu.getFid();
			tempLevel = fMenu.getMlevel();
		}
		return fullName;
	}
	public MenuIterator getChilds() throws SystemException {
		return MenuHome.findByCondition("PID = '" + this.id + "' ORDER BY ID");
	}

	public Menu getFather() throws SystemException {
		return MenuHome.findById(this.fid);
	}
	public String[] getOperations() throws SystemException {
		String[] operations = null;
		java.util.ArrayList tempLists = new java.util.ArrayList();
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(GET_OPERATION);
			pstmt.setString(1, this.id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tempLists.add(rs.getString("operation"));
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Menus:getOperations()-" + sqle;
			//Log.error(errorStr);
			throw new SystemException(errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Menus:getOperations()-" + exception;
			//Log.error(errorStr);
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
		operations = new String[tempLists.size()];
		for (int i = 0; i < operations.length; i++) {
			operations[i] = (String) tempLists.get(i);
		}
		return operations;
	}
}
