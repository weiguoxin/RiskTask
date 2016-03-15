package cn.com.info21.system;

import java.sql.*;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.menu.*;;

/**
 * @author db2admin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SysFunction {
	private static final String GET_MAX_ID = "SELECT MAX(ID) AS ID FROM ";
	private static final String DEL_RECORD = "DELETE FROM ";
	private static final String GET_TB_CNT = "SELECT COUNT(*) AS CNT FROM ";
	private static String errStr = "";
	private static final int SHORT = 1;
	private static final int LONG = 2;
	public static final String WEB_APP = "webapp_workflow";
	public static final String SESSION_MANAGER = "workflow_sessionmanager";
	public static final String TASK_MANAGER = "workflow_taskmanager";
	/**
	 * @constuctor SysFunction constructor
	 */
	public SysFunction() {
	}
	/**
	 * @return webApplication
	 */
	public static ServletContext getApplication() {
		Context ctx = null;
		ServletContext rtn = null;
		try {
			ctx = new InitialContext();
			rtn = (ServletContext) ctx.lookup(WEB_APP);
			//ServletContext application = null;
		} catch (Exception e) {
			e.printStackTrace();
			rtn = null;
		}
		return rtn;
	}
	/**
	 * 通常由jsp调用
	 * @param app webApplication
	 * @return 是否绑定成功
	 */
	public static boolean bindApplication(ServletContext app) {
		Context ctx = null;
		boolean rtn = false;
		try {
			try {
				ctx = new InitialContext();
				if (ctx.lookup(WEB_APP) == null) {
					ctx.bind(WEB_APP, app);
				}
			} catch (Exception e) {
				ctx.bind(WEB_APP, app);
			}
			rtn = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	/**
	 * 此处插入方法描述。
	 * 创建日期：(2003-3-31 10:31:29)
	 * @param tbname java.lang.String 表名
	 * @param id int 主键ID	 
	 * @exception cn.com.info21.system.SystemException 异常说明。
	 */
	private static String getYear() {
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.get(Calendar.YEAR));
	}
	private static String getYear(int type) {
		Calendar calendar = Calendar.getInstance();
		if (type == SHORT)
			return String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
		if (type == LONG)
			return String.valueOf(calendar.get(Calendar.YEAR));
		return null;
	}
	public static void delRecord(String tbname, int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String delsql = "";
		try {
			con = DbConnectionManager.getConnection();
			delsql = DEL_RECORD + tbname + " WHERE ID = " + String.valueOf(id);
			pstmt = con.prepareStatement(delsql);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errStr = "SQLException in Same.java:delRecord(): " + sqle;
			//Log.error(errStr);
		} catch (Exception e) {
			errStr = "Exception in User.java:delete(): " + e;
			//Log.error(errStr);
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

	/**
	 * 从表中删除一条记录
	 * @param tbname
	 *            表名
	 * @param id
	 *            流水号
	 * @throws SystemException
	 */
	public static void delRecord(String tbname, long id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String delsql = "";
		try {
			con = DbConnectionManager.getConnection();
			delsql = DEL_RECORD + tbname + " WHERE ID = " + String.valueOf(id);
			pstmt = con.prepareStatement(delsql);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errStr = "SQLException in Same.java:delRecord(): " + sqle;
			System.err.println(errStr);
		} catch (Exception e) {
			errStr = "Exception in User.java:delete(): " + e;
			System.err.println(errStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 此处插入方法描述。
	 * 创建日期：(2003-3-31 10:31:29)
	 * @param tbname java.lang.String 表名
	 * @param id int 主键ID	 
	 * @exception cn.com.info21.system.SystemException 异常说明。
	 */
	public static void delRecord(String tbname, String conditionstr) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String delsql = "";
		try {
			con = DbConnectionManager.getConnection();
			delsql = DEL_RECORD + tbname + " WHERE " + conditionstr;
			pstmt = con.prepareStatement(delsql);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errStr = "SQLException in Same.java:delRecord(): " + sqle;
			//Log.error(delsql);
			//Log.error(errStr);
		} catch (Exception e) {
			//Log.error(errStr);
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
	/**
	 * 返回tbname的行数
	 * 创建日期：(2003-3-31 10:26:09)
	 * @return int
	 * @param tbname java.lang.String 表名
	 * @param conditionStr java.lang.String 查询条件
	 */
	public static int getCnt(String sql) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			con = DbConnectionManager.getConnection();

				pstmt =
					con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (SQLException sqle) {
			errStr = "SQLException in " + sql + ".java:getCnt(): " + sqle;
			//Log.error(errStr);
		} catch (Exception e) {
			errStr = "Exception in " + sql + ".java:getCnt(): " + e;
			//Log.error(errStr);
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
		return count;
	}
	/**
	 * 返回tbname的行数
	 * 创建日期：(2003-3-31 10:26:09)
	 * @return int
	 * @param tbname java.lang.String 表名
	 * @param conditionStr java.lang.String 查询条件
	 */
	public static int getCnt(String tbname, String conditionStr) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			con = DbConnectionManager.getConnection();

			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(GET_TB_CNT + tbname);
			} else {
				pstmt =
					con.prepareStatement(
						GET_TB_CNT + tbname + " WHERE " + conditionStr);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (SQLException sqle) {
			errStr = "SQLException in " + tbname + ".java:getCnt(): " + sqle;
			//Log.error(errStr);
		} catch (Exception e) {
			errStr = "Exception in " + tbname + ".java:getCnt(): " + e;
			//Log.error(errStr);
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
		return count;
	}
	/**
	 * 初始化流水号辅助表辅助工具
	 */
	public static void initMaxID() {
		Connection con = null;
		PreparedStatement pstmt = null;
		DatabaseMetaData dmd = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		Statement stmt4 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		try {
			con = DbConnectionManager.getConnection();
			dmd = con.getMetaData();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			stmt4 = con.createStatement();

	        String[] types = {"TABLE", "VIEW"};
	        //查找当前连接所有的表和视图
	        rs = dmd.getTables(null, null, "%", types);
	        rs2 = null;
	        rs3 = null;
	        int j = 0;
	        int size = rs.getMetaData().getColumnCount();
	        for (j = 1; j <= size; j++) {
	        	if ("TABLE_NAME".equals(rs.getMetaData().getColumnName(j))) {
	        		break;
	        	}
	        }
	        if (j > size) {
	        	return;
	        }
	        while (rs.next()) {
	        	try {
	        		Object obj = rs.getObject(j);
	        		rs2 = stmt2.executeQuery("select * from MetaIdentity where "
	        				+ "LOWER(keycode)='" + obj.toString().toLowerCase() + "'");
	        		rs3 = stmt3.executeQuery("select max(id) from " + obj.toString());
	        		if (rs3.next()) {
	        			int mid = rs3.getInt(1);
	        			if (rs2.next()) {
	        				rs2.updateInt("maxid", mid);
	        				rs2.updateRow();
	        			} else {
	        				stmt4.executeUpdate("INSERT INTO metaidentity(keycode,maxid)"
	        						+ "VALUES('" + obj.toString().toLowerCase()
									+ "'," + mid + ")");
	        			}
	        		}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(null, null, stmt4, rs3);
			DbConnectionManager.close(null, null, stmt3, rs2);
			DbConnectionManager.close(null, null, stmt2, rs);
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 返回tbname的ID最大值
	 * 创建日期：(2003-3-31 10:26:09)
	 * @return int
	 * @param tbname java.lang.String 表名
	 */
	public static int getMaxid(String tbname) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int maxid = 0;
		try {
			if (con == null) {
				con = DbConnectionManager.getConnection();
			}

			pstmt = con.prepareStatement(GET_MAX_ID + tbname);

			//Log.info(GET_MAX_ID+"|"+tbname);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxid = rs.getInt("ID");
			} else {
				maxid = 0;
			}
		} catch (SQLException sqle) {
			errStr = "SQLException in SameCtr.java:getMaxid()-" + sqle;
			//Log.error(errStr);
			throw new SystemException(errStr);
		} catch (Exception exception) {
			errStr = "Exception in SameCtr.java:getMaxid()-" + exception;
			//Log.error(errStr);
			throw new SystemException(errStr);
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
		return maxid;
	}
	/**
	 * 从流水号维护表metaidentity表返回key对应的流水号
	 * @param key 表名
	 * @return 返回当前的最大值
	 */
	public static int getMaxID(String key) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int rtn;
		try {
			con = DbConnectionManager.getConnection();
			rtn = getMaxID(con, key);
		} catch (Exception e) {
			rtn = -1;
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
		return rtn;
	}
	/**
	 * 从流水号维护表metaidentity表返回key对应的流水号
	 * @param con 通常是一个(AutoCommit=false)的用于insert数据的连接
	 * 		但通常该连接不应在AutoCommit=true时使用PrepareStatement
	 * @param key 表名
	 * @return 返回当前的最大值
	 */
	private static int getMaxID(Connection con, String key) {
		int rtn = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String preSelect = "SELECT keyvalue FROM SYS_SEQUENCE WHERE LOWER(keycode)='";
		String preInsert = "INSERT INTO SYS_SEQUENCE(keycode) VALUES('";
		String preUpdate = "UPDATE SYS_SEQUENCE set keyvalue=";
		
		String keyl = key.toLowerCase();

		try {
			//设置为手动提交
			con.setAutoCommit(false);
			stmt = con.createStatement();

			rs = stmt.executeQuery(preSelect + keyl + "'");
			if (rs.next()) {
				rtn = rs.getInt("keyvalue");
			} else {

				stmt.executeUpdate(preInsert + keyl + "')");
				rtn = 0;
			}
			stmt.executeUpdate(preUpdate + (rtn + 1) + " WHERE keycode='" + keyl + "'");
			con.commit();
			con.setAutoCommit(true);
		} catch (Exception e) {
			rtn = -1;
			e.printStackTrace();
			System.out.println("getMaxID exception: " + e.toString());
			try {
				con.rollback();
			} catch (Exception e2) {
				System.out.println("getMaxID exception: " + e2.toString());
			}
			try {
				con.setAutoCommit(true);
			} catch (Exception e2) {
				System.out.println("getMaxID exception: " + e2.toString());
			}
		} finally {
			DbConnectionManager.close(null, null, stmt, rs);
		}
		return rtn;
	}
	/**
	 * 返回tbname的ID最大值
	 * 创建日期：(2003-3-31 10:26:09)
	 * @return int
	 * @param tbname java.lang.String 表名
	 */
	public static int getMaxid(String tbname, String conditionStr) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int maxid = 0;
		try {
			if (con == null) {
				con = DbConnectionManager.getConnection();
			}

			pstmt =
				con.prepareStatement(
					GET_MAX_ID + tbname + " WHERE " + conditionStr);

			//Log.info(GET_MAX_ID+"|"+tbname);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxid = rs.getInt("ID");
			} else {
				maxid = 0;
			}
		} catch (SQLException sqle) {
			errStr = "SQLException in SameCtr.java:getMaxid()-" + sqle;
			//Log.error(errStr);
		} catch (Exception exception) {
			errStr = "Exception in SameCtr.java:getMaxid()-" + exception;
			//Log.error(errStr);
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
		return maxid;
	}
	/**
	 * 获取时间
	 * @return String 字符串
	 */
	public static String getDate() {
	  	SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	String dateStr = dformat.format(new java.util.Date());
	  	return dateStr;
	}
	public static Object[] toArray(java.util.Iterator iter) {
		Object[] objs = null;
		if (iter.hasNext()) {
			ArrayList list = new ArrayList();
			while (iter.hasNext()) {
				list.add(iter.next());
			}
			objs = list.toArray();
		}
		return objs;
	}
	public static Object getQueryValue(String sql) {
		Object obj = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				obj = rs.getObject(1);
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in SysFunction.getQueryValue(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in SysFunction.getQueryValue(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
		return obj;
	}
	public static ArrayList getData(String sql) {
		ArrayList rtn = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int colcount = rs.getMetaData().getColumnCount();
			Object[] obj = null;
			int i = 0;
			while (rs.next()) {
				obj = new Object[colcount];
				for (i = 1; i <= colcount; i++) {
					obj[i - 1] = rs.getObject(i);
				}
				rtn.add(obj);
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in SysFunction.getData(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in SysFunction.getData(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
		return rtn;
	}
	public static ArrayList getData(String sql, int startIndex, int num) {
		ArrayList rtn = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int colcount = rs.getMetaData().getColumnCount();
			Object[] obj = null;
			int i = 0;
			while (rs.next()) {
                if (index >= (startIndex + num)) {
                    break;
                }
                if (index >= startIndex) {
    				obj = new Object[colcount];
    				for (i = 1; i <= colcount; i++) {
    					obj[i - 1] = rs.getObject(i);
    				}
    				rtn.add(obj);
                }
                index++;
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in SysFunction.getData(String,int,int): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in SysFunction.getData(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
		return rtn;
	}
	public static void ExecuteData(String sql) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.execute();
		} catch (SQLException sqle) {
			System.err.println("SQLException in SysFunction.getData(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in SysFunction.getData(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	public static String getColTree(
			String checkValue,
			boolean canSelFolder) {
			Connection conn = null;
			PreparedStatement sMenus = null;
			ResultSet rsMenus = null;
			String menuStr = "";
			String TREE_SQL = "";
			TREE_SQL = "SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF,ZIGUI,FENSHU  FROM SYS_INVCLASS WHERE ISENABLE = 'Y' ORDER BY ID";
			try {
				String tempStr = "";
				conn = DbConnectionManager.getConnection();
				sMenus = conn.prepareStatement(TREE_SQL);
				rsMenus = sMenus.executeQuery();
				int prevLevel = 1;
				rsMenus.next();
				while (rsMenus.next()) {
					String menuType;
					String mpid, mid,zigui,fenshu;
					mid = rsMenus.getString("ID");
					zigui = rsMenus.getString("ZIGUI");
					fenshu = rsMenus.getString("FENSHU");
					int mlevel = rsMenus.getInt("MLEVEL");
					String entryName = rsMenus.getString("NAME");
					if (entryName == null)
						entryName = "";
					String entryHref = rsMenus.getString("HREF");
					if (entryHref == null)
						entryHref = "";
					String entryExplain = rsMenus.getString("MEXPLAIN");
					if (entryExplain == null)
						entryExplain = "";
					if (mlevel > 0) {
						mpid = rsMenus.getString("PID");
						if (rsMenus.getString("ISLEAF").equals("N")) {
							menuType = "Folder";
						} else {
							menuType = "Document";
						}

						for (int i=1; i<= prevLevel-mlevel; i++) {
							menuStr = menuStr + "</ul>";
							menuStr = menuStr + "</li>";
						}
						if ("Folder".equals(menuType)) {
							menuStr = menuStr + "\n" + "<li>";
							menuStr = menuStr + "\n";
							menuStr = menuStr + "<nobr>";
							menuStr = menuStr + "\n";
							menuStr =
								menuStr
									+ "<img src='../res/img/tree/plus.gif' class='collapsed'>";
							menuStr = menuStr + "\n";
							menuStr =
								menuStr
									+ "<img src='../res/img/tree/container_obj.gif'>";

							menuStr = menuStr + "\n";
							String check = "";
							if (mid.equals(checkValue)) {
								check = "checked ";
							}
							if (canSelFolder) {

								menuStr =
									menuStr
										+ "<input type='radio'  id='' name='colId' value='"
										+ mid
										+ "' "
										+ check
										+ " title='"
										+ entryName
										+"' alt='"
										+ zigui
										+ "|"
										+ fenshu
										+ "' onclick='setCol(this)'>";

							}
							menuStr = menuStr + entryName;
							menuStr = menuStr + "\n";
							menuStr = menuStr + "</nobr>";
							menuStr = menuStr + "\n";
							menuStr = menuStr + "<ul class='collapsed'>";
						} else {
							menuStr = menuStr + "\n" + "<li>";
							menuStr = menuStr + "\n";
							menuStr = menuStr + "<nobr>";
							menuStr = menuStr + "\n";
							menuStr =
								menuStr
									+ "<img src='../res/img/tree/plus.gif' style='visibility:hidden;'>";
							menuStr = menuStr + "\n";
							menuStr =
								menuStr
									+ "<img src='../res/img/tree/topic.gif'>";
							menuStr = menuStr + "\n";
							String check = "";
							if (mid.equals(checkValue)) {
								check = "checked ";
							}
							String fulName = entryName;
						/*
						 if (mlevel >3) {
								fulName =
									MenuHome
										.findById(rsMenus.getString("PID"))
										.getName()
										+ ">>"
										+ entryName;
							}
						 */
							menuStr =
								menuStr
									+ "<input type='radio'  id='' name='colId' value='"
									+ mid
									+ "' "
									+ check
									+ " title='"
									+ fulName
									+"' alt='"
									+ zigui
									+ "|"
									+ fenshu
									+ "' onclick='setCol(this)'>";
							menuStr = menuStr + entryName;
							menuStr = menuStr + "\n";
							menuStr = menuStr + "</nobr>";
							menuStr = menuStr + "\n";
							menuStr = menuStr + "</li>";
						}
					}
					prevLevel = mlevel;
				}
				menuStr = menuStr + "</ul>";
				menuStr = menuStr + "</li>";
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					sMenus.close();
					rsMenus.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return menuStr;
		}
	public static String getCheckTree(
			String checkValue,
			boolean canSelFolder) {
			Connection conn = null;
			PreparedStatement sMenus = null;
			ResultSet rsMenus = null;
			String menuStr = "\n" + "<ul class='unorderedlisttree'>";
			String TREE_SQL = "";
			TREE_SQL = "SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM SYS_INVCLASS WHERE ISENABLE = 'Y' ORDER BY ID";
			try {
				String tempStr = "";
				conn = DbConnectionManager.getConnection();
				sMenus = conn.prepareStatement(TREE_SQL);
				rsMenus = sMenus.executeQuery();
				int prevLevel = 1;
				rsMenus.next();
				while (rsMenus.next()) {
					String menuType;
					String mpid, mid;
					mid = rsMenus.getString("ID");
					int mlevel = rsMenus.getInt("MLEVEL");
					String entryName = rsMenus.getString("NAME");
					if (entryName == null)
						entryName = "";
					String entryHref = rsMenus.getString("HREF");
					if (entryHref == null)
						entryHref = "";
					String entryExplain = rsMenus.getString("MEXPLAIN");
					if (entryExplain == null)
						entryExplain = "";
					if (mlevel > 0) {
						mpid = rsMenus.getString("PID");
						if (rsMenus.getString("ISLEAF").equals("N")) {
							menuType = "Folder";
						} else {
							menuType = "Document";
						}
						for (int i=1; i<= prevLevel-mlevel; i++) {
							menuStr = menuStr + "</ul>";
							menuStr = menuStr + "</li>";
						}
						if ("Folder".equals(menuType)) {
							menuStr = menuStr + "\n" + "<li>";
							menuStr = menuStr + "\n";
							String check = "";
							if (mid.equals(checkValue)) {
								check = "checked ";
							}
							if (canSelFolder) {
							/*如果还有下级栏目,则没有checkbox.因为只有删除了下级栏目才能删除上级栏目	
								menuStr =
									menuStr
										+ "<input type='checkbox'  id='' name='colId' value='"
										+ mid
										+ "' "
										+ check
										+ " title='"
										+ entryName
										+ "'>";
							*/			
							}
							menuStr = menuStr + "\n";
							menuStr = menuStr + "<span><a href=\"invclass_Add.jsp?id=" + mid + " \" target='edittree'>" + entryName + "</a></span>";
							menuStr = menuStr + "\n";
							menuStr = menuStr + "<ul>";
						} else {
							menuStr = menuStr + "\n" + "<li>";
							menuStr = menuStr + "\n";
							String check = "";
							if (mid.equals(checkValue)) {
								check = "checked ";
							}
							String fulName = entryName;
							if (mlevel > 3) {
								fulName =
									MenuHome
										.findById(rsMenus.getString("PID"))
										.getName()
										+ ">>"
										+ entryName;
							}
							menuStr =
								menuStr
									+ "<input type='checkbox'  id='' name='colId' value='"
									+ mid
									+ "' "
									+ check
									+ " title='"
									+ fulName
									+ "'>";
							menuStr = menuStr + "\n";
							menuStr = menuStr + "<label><a href=\"invclass_Add.jsp?id=" + mid + " \" target='edittree'>" + entryName + "</a></label>";
							menuStr = menuStr + "\n";
							menuStr = menuStr + "</li>";
						}
					}
					prevLevel = mlevel;
				}
				menuStr = menuStr + "</ul>";
				menuStr = menuStr + "</li></ul>";
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					sMenus.close();
					rsMenus.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return menuStr;
		}		
}
