/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;

import java.sql.*;
import java.util.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author songle
 */

public class FlowIterator {
	private static final String ALLFLOW = "SELECT * FROM " + Flow.TABLENAME;
	private int[] flows;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * 构造函数一
	 */
	public FlowIterator() {
	}
	/**
	 * 根据条件查找过程
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 数目
	 */
	public FlowIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLFLOW);
			} else {
				pstmt = con.prepareStatement(ALLFLOW + " where " + condition);
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
			"SQLException in FlowIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in FlowIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		flows = new int[temp.size()];
		for (int i = 0; i < flows.length; i++) {
			flows[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param condition 条件
	 * @throws SystemException 捕获错误
	 */
	public FlowIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLFLOW;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLFLOW + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in FlowIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in FlowIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		flows = new int[temp.size()];
		for (int i = 0; i < flows.length; i++) {
			flows[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param sql sql语句
	 * @return FlowIterator 过程集合
	 * @throws SystemException 捕获错误
	 */
	public static FlowIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FlowIterator iter = new FlowIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in FlowIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in FlowIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.flows = new int[temp.size()];
		for (int i = 0; i < iter.flows.length; i++) {
			iter.flows[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * 判断是否存在下一个对象
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < flows.length);
	}
	/**
	 * 获取下一个对象
	 * @return object
	 */
	public Object next() {
		Flow fs = null;
		currentIndex++;
		if (currentIndex >= flows.length) {
			throw new NoSuchElementException();
		}
		try {
			fs = new Flow(flows[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in FlowIterator::next(): " + e);
		}
		return flows;
	}
	/**
	 * 删除一个对象
	 */
	public void remove() {
		int[] tmpFlows;
		tmpFlows = new int[flows.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpFlows[i] = flows[i];
		}
		for (int i = currentIndex + 1; i < tmpFlows.length; i++) {
			tmpFlows[i - 1] = flows[i];
		}
		flows = tmpFlows;
	}
	/**
	 * 获取Iterator 的长度
	 * @return int
	 */
	public int getLength() {
		return flows.length;
	}
}
