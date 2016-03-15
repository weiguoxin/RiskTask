/*
 * 创建日期 2005-7-19
 */
package cn.com.info21.appdef;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class AppIterator implements Iterator {
	private static final String ALLAPP = "SELECT * FROM " + App.TABLENAME;
	private int[] apps;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * 构造函数一
	 */
	public AppIterator() {
	}
	/**
	 * 根据条件查找过程
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 数目
	 */
	public AppIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLAPP);
			} else {
				pstmt = con.prepareStatement(ALLAPP  + " where " + condition);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			if (index >= (start + num)) { break; }
      			if (index >= start) {
        			temp.add(new Integer(rs.getInt("id")));
      			}
		        index++;
			}
	    } catch (SQLException sqle) {
			System.err.println(
			"SQLException in AppIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in AppIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		apps = new int[temp.size()];
		for (int i = 0; i < apps.length; i++) {
			apps[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param condition 条件
	 * @throws SystemException 捕获错误
	 */
	public AppIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLAPP;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLAPP  + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in AppIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in AppIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		apps = new int[temp.size()];
		for (int i = 0; i < apps.length; i++) {
			apps[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param sql sql语句
	 * @return AppIterator 过程集合
	 * @throws SystemException 捕获错误
	 */
	public static AppIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AppIterator iter = new AppIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in AppIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in AppIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.apps = new int[temp.size()];
		for (int i = 0; i < iter.apps.length; i++) {
			iter.apps[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * 判断是否存在下一个对象
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < apps.length);
	}
	/**
	 * 获取下一个对象
	 * @return object
	 */
	public Object next() {
		App app = null;
		currentIndex++;
		if (currentIndex >= apps.length) {
			throw new NoSuchElementException();
		}
		try {
			app = new App(apps[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in AppIterator::next(): " + e);
		}
		return app;
	}
	/**
	 * 删除一个对象
	 */
	public void remove() {
		int[] tmpProcs;
		tmpProcs = new int[apps.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpProcs[i] = apps[i];
		}
		for (int i = currentIndex + 1; i < tmpProcs.length; i++) {
			tmpProcs[i - 1] = apps[i];
		}
		apps = tmpProcs;
	}
	/**
	 * 获取Iterator 的长度
	 * @return int
	 */
	public int getLength() {
		return apps.length;
	}
}
