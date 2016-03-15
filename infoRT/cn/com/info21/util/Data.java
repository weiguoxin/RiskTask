/*
 * Created on 2004-11-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.com.info21.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author realfox
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Data {
	private String tablename = "";
	private String loadById =
		"SELECT * FROM " + tablename + " WHERE ID=?";
	private String insert_obj =
		"INSERT INTO " + tablename + "(id) VALUES(?)";
	private static final String UPDATE_OBJ = "";
	private String errorStr;

	private String id;
	private Hashtable data = new Hashtable();

	/**
	 * @return Returns the tablename.
	 */
	protected String getTablename() {
		return tablename;
	}
	/**
	 * @param tablename The tablename to set.
	 */
	protected void setTablename(String tablename) {
		this.tablename = tablename;
		loadById = "SELECT * FROM " + tablename + " WHERE ID=?";
		insert_obj = "INSERT INTO " + tablename + "(id) VALUES(?)";
	}
	/**
	 * 构造函数一
	 */
	public Data() {
	}
	/**
	 * 构造函数二
	 * 根据ID号,从数据库中读取信息
	 * @param id 对象ID
	 * @exception SystemException 系统异常
	 */
	public Data(String id, String tablename) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		setTablename(tablename);
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(loadById);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id;
				ResultSetMetaData rsmd = rs.getMetaData();
				int size = rsmd.getColumnCount();
				for (int i = 1; i < size + 1; i++) {
					Object val = rs.getObject(i);
					if (val == null) {
						val = new Object();
					}
					data.put(rsmd.getColumnName(i).toUpperCase(), val);
				}
			} else {
				errorStr = "Not found!";
            	throw new SystemException(errorStr);
            }
		} catch (SQLException sqle) {
			errorStr = "SQLException in Data:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Data:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 构造函数三 新建指定参数的流程并记录到数据库中
	 * 
	 * @param pid pid
	 * @exception SystemException 系统异常
	 */
	public Data(String tablename) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		setTablename(tablename);
		try {
			con = DbConnectionManager.getConnection();
			this.id = String.valueOf(SysFunction.getMaxID(tablename)) + 1;
			if (!this.id.equals("-1")) {
				pstmt = con.prepareStatement(insert_obj);
				pstmt.setString(1, this.id);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Data:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Data:constructor()-" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 更新发文记录
	 * @exception SystemException 系统异常
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sqlUpdate = "Update " + tablename + " set ";
		try {
			con = DbConnectionManager.getConnection();
			String tmp = "";
			ArrayList list = new ArrayList();
			for (Enumeration keys = data.keys(); keys.hasMoreElements() ;) {
				String key = keys.nextElement().toString();
				if (!key.toUpperCase().equals("ID")) {
					if (!tmp.equals("")) {
						tmp += ", ";
					}
					tmp += key + "=?";
					list.add(key);
				}
			}
			sqlUpdate += tmp + " WHERE ID=?";
			pstmt = con.prepareStatement(sqlUpdate);
			int size = list.size();
			String nvl = null;
			for (int i = 0; i < size; i++) {
				String key = list.get(i).toString();
				Object val = data.get(key);
				String valClass = val.getClass().getName();
				//if (val.getClass().getName())
				if (valClass.equals("java.lang.Object")) {
					pstmt.setString(i + 1, nvl);
				} else if (valClass.equals("java.lang.Integer")) {
					pstmt.setInt(i + 1, ((Integer) val).intValue());
				} else if (valClass.equals("java.lang.String")) {
					pstmt.setString(i + 1, (String) val);
				} else if (valClass.equals("java.lang.Timestamp")) {
					pstmt.setTimestamp(i + 1, (Timestamp) val);	
				} else {
					pstmt.setObject(i + 1, val);
				}

			}
			pstmt.setString(size + 1, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "Exception in Data.java:update()";
			sqle.printStackTrace();
			throw new SystemException(this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Data.java:update()";
			e.printStackTrace();
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}

	/**
	 * @param fieldname fieldname
	 * @return int value
	 */
	public int getInt(String fieldname) {
		int rtn = 0;
		try {
			Object obj = data.get(fieldname.toUpperCase());
			if (obj!=null && !obj.getClass().getName().equals("java.lang.Object")) {
				//rtn = ((Integer) obj).intValue();
				rtn = ((java.math.BigDecimal) obj).intValue();
			}
		} catch (Exception exception) {
			rtn = 0;
			exception.printStackTrace();
		}
		return rtn;
	}
	/**
	 * @param fieldname fieldname
	 * @return int value
	 */
	public float getFloat(String fieldname) {
		float rtn = 0;
		try {
			Object obj = data.get(fieldname.toUpperCase());
			if (obj!=null && !obj.getClass().getName().equals("java.lang.Object")) {
				rtn = ((java.math.BigDecimal) obj).floatValue();
			}
		} catch (Exception exception) {
			rtn = 0;
			exception.printStackTrace();
		}
		return rtn;
	}
	/**
	 * @param fieldname fieldname
	 * @return int value
	 */
	public double getDouble(String fieldname) {
		double rtn = 0;
		try {
			Object obj = data.get(fieldname.toUpperCase());
			if (obj!=null && !obj.getClass().getName().equals("java.lang.Object")) {
				rtn = ((java.math.BigDecimal) obj).doubleValue();
			}
		} catch (Exception exception) {
			rtn = 0;
			exception.printStackTrace();
		}
		return rtn;
	}
	/**
	 * @param fieldname fieldname
	 * @return int value
	 */
	public String getString(String fieldname) {
		String rtn = null;
		try {
			Object obj = data.get(fieldname.toUpperCase());
			if (obj!=null && !obj.getClass().getName().equals("java.lang.Object")) {
				rtn = (String) data.get(fieldname.toUpperCase());
			}
		} catch (Exception exception) {
			rtn = null;
			exception.printStackTrace();
		}
		return rtn;
	}
	/**
	 * @param fieldname fieldname
	 * @return int value
	 */
	public Timestamp getTimestamp(String fieldname) {
		Timestamp rtn = null;
		try {
			Object obj = data.get(fieldname.toUpperCase());
			if (obj!=null && !obj.getClass().getName().equals("java.lang.Object")) {
				rtn = (Timestamp) data.get(fieldname.toUpperCase());
			}
		} catch (Exception exception) {
			rtn = null;
			exception.printStackTrace();
		}
		return rtn;
	}
	/**
	 * @param fieldname fieldname
	 * @return int value
	 */
	public Object get(String fieldname) {
		Object rtn = null;
		try {
			rtn = data.get(fieldname.toUpperCase());
			if (rtn.getClass().getName().equals("java.lang.Object")) {
				rtn = null;
			}
		} catch (Exception exception) {
			rtn = null;
			exception.printStackTrace();
		}
		return rtn;
	}


	/**
	 * @return 文件Id
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * @param fieldname fieldname
	 * @param value int值
	 */
	public void set(String fieldname, int value) {
		try {
			data.put(fieldname.toUpperCase(), new Integer(value));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	/**
	 * @param fieldname fieldname
	 * @param value int值
	 */
	public void set(String fieldname, float value) {
		try {
			data.put(fieldname.toUpperCase(), new Float(value));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	/**
	 * @param fieldname fieldname
	 * @param value int值
	 */
	public void set(String fieldname, double value) {
		try {
			data.put(fieldname.toUpperCase(), new Double(value));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	/**
	 * @param fieldname fieldname
	 * @param value 值
	 */
	public void set(String fieldname, Object value) {
		try {
			if (value != null) {
				data.put(fieldname.toUpperCase(), value);
			} else {
				data.put(fieldname.toUpperCase(), new Object());
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}


	/**
	 * @param tabname tablename
	 * @param fieldname fieldname
	 * @return field type
	 */
	public static String getFieldType(String tabname, String fieldname) {
		String rtn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		DatabaseMetaData dma = null;
		Connection con = null;
		try {
			con = DbConnectionManager.getConnection();
			//con = DbConnectionManager.getMicrosoftnoPoolConnection();	//for test only
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + tabname + " where 1=0");
			rsmd = rs.getMetaData();
			int cc = rsmd.getColumnCount();
			for (int i = 1; i < cc + 1; i++) {
				//System.out.println(rsmd.getColumnClassName(i));
				if (rsmd.getColumnName(i).toLowerCase().equals(fieldname.toLowerCase())) {
					return rsmd.getColumnClassName(i);
				}
			}
		} catch (SQLException sqle) {
			rtn = null;
			sqle.printStackTrace();
		} catch (Exception exception) {
			rtn = null;
			exception.printStackTrace();
		} finally {
			DbConnectionManager.close(con, null, null, rs);
		}
		return rtn;
	}

}
