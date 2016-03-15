/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;

import java.sql.*;
import java.util.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class ProcIterator {
	private static final String ALLPROC = "SELECT * FROM " + Proc.TABLENAME;
	private int[] procs;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * 构造函数一
	 */
	public ProcIterator() {
	}
	/**
	 * 根据条件查找过程
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 数目
	 */
	public ProcIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLPROC);
			} else {
				pstmt = con.prepareStatement(ALLPROC + " where " + condition);
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
			"SQLException in ProcIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in ProcIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		procs = new int[temp.size()];
		for (int i = 0; i < procs.length; i++) {
			procs[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param condition 条件
	 * @throws SystemException 捕获错误
	 */
	public ProcIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLPROC;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLPROC + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in ProcIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in ProcIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		procs = new int[temp.size()];
		for (int i = 0; i < procs.length; i++) {
			procs[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param sql sql语句
	 * @return ProcIterator 过程集合
	 * @throws SystemException 捕获错误
	 */
	public static ProcIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProcIterator iter = new ProcIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in ProcIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in ProcIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.procs = new int[temp.size()];
		for (int i = 0; i < iter.procs.length; i++) {
			iter.procs[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * 判断是否存在下一个对象
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < procs.length);
	}
	/**
	 * 获取下一个对象
	 * @return object
	 */
	public Object next() {
		Proc proc = null;
		currentIndex++;
		if (currentIndex >= procs.length) {
			throw new NoSuchElementException();
		}
		try {
			proc = new Proc(procs[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in ProcIterator::next(): " + e);
		}
		return proc;
	}
	/**
	 * 删除一个对象
	 */
	public void remove() {
		int[] tmpProcs;
		tmpProcs = new int[procs.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpProcs[i] = procs[i];
		}
		for (int i = currentIndex + 1; i < tmpProcs.length; i++) {
			tmpProcs[i - 1] = procs[i];
		}
		procs = tmpProcs;
	}
	/**
	 * 获取Iterator 的长度
	 * @return int
	 */
	public int getLength() {
		return procs.length;
	}
}
