package cn.com.info21.database;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
/**
 * SQL
 * @author lkh
 */
public final class DbConnectionManager {
	private static final DbConnectionManager INSTANCE = new DbConnectionManager();
	/**
	 * 默认构造器
	 */
	private DbConnectionManager () {
	}
	/**
	 * @return 获取实例
	 */
	public static DbConnectionManager getInstance() {
		return INSTANCE;
	}
	/**
	 * @return 获取连接池连接
	 */
	public static Connection getConnection() {
		return getMicrosoftConnection();
	}
	/**
	 * 关闭连接
	 * @param con 数据库连接
	 * @param pstmt 预编译语句
	 * @param stmt Statement
	 * @param rs 结果集
	 */
	public static void close(Connection con, PreparedStatement pstmt,
			Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Oracle JDBC驱动
	 * jndi name:	jdbc/orasql
	 * url:			jdbc:oracle:oci8:@space
	 * driver:		oracle.jdbc.driver.OracleDriver
	 * @return con 返回一个数据库的连接
	*/
	public static Connection getMicrosoftConnection() {
		DataSource ds = null;
		Connection con = null;
		try {
			Context ctx = new InitialContext();
			String dataSource = "java:comp/env/jdbc/orasql";
			ds = (DataSource) ctx.lookup(dataSource);
		} catch (Exception e) {
			System.out.println("Naming service exception: " + e.toString());
		}
		try {
			con = ds.getConnection();
			
		} catch (Exception e) {
			System.out.println(
				"Get connection, process, or close statement "
					+ "exception: "
					+ e.toString());
		}
		return con;
	}
	public static Connection getMSConnection() {

		Connection con = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mssql");
			con = ds.getConnection();
			return con;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static Connection getTransactionConnection() throws SQLException {
		Connection con = getConnection();
		if (isTransactionsSupported())
			con.setAutoCommit(false);
		return con;
	}

	public static void closeTransactionConnection(
		Connection con,
		boolean abortTransaction) {
		if (isTransactionsSupported())
			try {
				if (abortTransaction)
					con.rollback();
				else
					con.commit();
			} catch (Exception e) {
				
			}
		try {
			if (isTransactionsSupported())
				con.setAutoCommit(true);
		} catch (Exception e) {
			
		}
		try {
			con.close();
		} catch (Exception e) {
			
		}
	}
	public static boolean isTransactionsSupported() {
		return transactionsSupported;
	}

	private static boolean transactionsSupported;	
}
