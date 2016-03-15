/*
 * 创建日期 2005-9-2
 */

package cn.com.info21.workflow;

import java.sql.*;
import java.util.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class FlowTaskMappingIterator {
	private static final String ALLFTM = "SELECT * FROM " + FlowTaskMapping.TABLENAME;
	private int[] ftms;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * 构造函数一
	 */
	public FlowTaskMappingIterator() {
	}
	/**
	 * 根据条件查找过程
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 数目
	 */
	public FlowTaskMappingIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLFTM);
			} else {
				pstmt = con.prepareStatement(ALLFTM + " where " + condition);
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
			"SQLException in FlowTaskMappingIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in FlowTaskMappingIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ftms = new int[temp.size()];
		for (int i = 0; i < ftms.length; i++) {
			ftms[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param condition 条件
	 * @throws SystemException 捕获错误
	 */
	public FlowTaskMappingIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLFTM;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLFTM + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in FlowTaskMappingIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in FlowTaskMappingIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ftms = new int[temp.size()];
		for (int i = 0; i < ftms.length; i++) {
			ftms[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param sql sql语句
	 * @return FlowTaskMappingIterator 过程集合
	 * @throws SystemException 捕获错误
	 */
	public static FlowTaskMappingIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FlowTaskMappingIterator iter = new FlowTaskMappingIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in FlowTaskMappingIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in FlowTaskMappingIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.ftms = new int[temp.size()];
		for (int i = 0; i < iter.ftms.length; i++) {
			iter.ftms[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * 判断是否存在下一个对象
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < ftms.length);
	}
	/**
	 * 获取下一个对象
	 * @return object
	 */
	public Object next() {
	    FlowTaskMapping ftm = null;
		currentIndex++;
		if (currentIndex >= ftms.length) {
			throw new NoSuchElementException();
		}
		try {
			ftm = new FlowTaskMapping(ftms[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in FlowTaskMappingIterator::next(): " + e);
		}
		return ftm;
	}
	/**
	 * 删除一个对象
	 */
	public void remove() {
		int[] tmpFlows;
		tmpFlows = new int[ftms.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpFlows[i] = ftms[i];
		}
		for (int i = currentIndex + 1; i < tmpFlows.length; i++) {
			tmpFlows[i - 1] = ftms[i];
		}
		ftms = tmpFlows;
	}
	/**
	 * 获取Iterator 的长度
	 * @return int
	 */
	public int getLength() {
		return ftms.length;
	}
}
