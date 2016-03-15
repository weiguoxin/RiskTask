/*
 * Created on 2004-6-19
 */
package cn.com.info21.org;
import java.util.*;
import java.sql.*;
import cn.com.info21.database.*;
/**
 * @author liusihde
 */
public class DeptIterator implements Iterator {
	private int[] tempDept;
	private int currentIndex = -1;
	private static String allDept = "select * from " + Dept.TABLENAME;
	private String error = "";
	private static final String CHILDREN = "Select * from " + Dept.TABLENAME + " where parentid = ?";
	/**
	 * 默认构造函数
	 */
	public DeptIterator() {
	}
  /**
   * @param condition String is 查询条件
   */
  protected DeptIterator(String condition) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = allDept;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = allDept + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in DeptIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in DeptIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		tempDept = new int[temp.size()];
		for (int i = 0; i < tempDept.length; i++) {
			tempDept[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
    /**
     * create with condition and start and num
     * @param condition 查询条件
     * @param start 开始行
     * @param num 行数
     */
	protected DeptIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(allDept);
			} else {
				pstmt = con.prepareStatement(allDept + " where " + condition);
			}

			rs = pstmt.executeQuery();
			//int id = 0;
			while (rs.next()) {
      			if (index >= (start + num)) { break; }
      			if (index >= start) {
        			temp.add(new Integer(rs.getInt("id")));
      			}
		        index++;
			}
		} catch (SQLException sqle) {
			System.err.println(
			"SQLException in DeptIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in DeptIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		tempDept = new int[temp.size()];
		for (int i = 0; i < tempDept.length; i++) {
			tempDept[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * @param dept 当前部门
	 * @return 子部门
	 */
	public static DeptIterator getChildren(Dept dept) {
		if (dept != null) {
			return getChildren(dept.getId());
		} else {
			return null;
		}
	}
	/**
	 * @param id 当前部门id
	 * @return 子部门
	 */
	public static DeptIterator getChildren(int id) {
		DeptIterator rtn = null;
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(CHILDREN);
			pstmt.setInt(1 , id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			rtn = null;
			System.err.println("SQLException in DeptIterator(String): " + sqle);
		} catch (Exception e) {
			rtn = null;
			System.err.println("Exception in DeptIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		rtn.tempDept = new int[temp.size()];
		for (int i = 0; i < rtn.tempDept.length; i++) {
			rtn.tempDept[i] =  ((Integer) temp.get(i)).intValue();
		}
		return rtn;
	}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < tempDept.length);
	}
	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		Dept dept = null;
		currentIndex++;
		if (currentIndex >= tempDept.length) {
			throw new NoSuchElementException();
		}
		try {
			dept = new Dept(tempDept[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in DeptIterator::next(): " + e);
		}
		return dept;
	}
	public void remove() {
	}
}

