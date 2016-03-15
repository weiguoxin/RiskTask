/*
 * 创建日期 2005-7-17
 */

package cn.com.info21.workflow;

import java.sql.*;
import java.util.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class SequenceIterator {
	private static final String ALLSEQUENCE = "SELECT * FROM " + Sequence.TABLENAME;
	private int[] flow;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * 构造函数一
	 */
	public SequenceIterator() {
	}
	/**
	 * 根据条件查找过程
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 数目
	 */
	public SequenceIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLSEQUENCE);
			} else {
				pstmt = con.prepareStatement(ALLSEQUENCE + " where " + condition);
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
			"SQLException in SequenceIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println
			("Exception in SequenceIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		flow = new int[temp.size()];
		for (int i = 0; i < flow.length; i++) {
			flow[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param condition 条件
	 * @throws SystemException 捕获错误
	 */
	public SequenceIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLSEQUENCE;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLSEQUENCE + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in SequenceIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in SequenceIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		flow = new int[temp.size()];
		for (int i = 0; i < flow.length; i++) {
			flow[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param sql sql语句
	 * @return SequenceIterator 过程集合
	 * @throws SystemException 捕获错误
	 */
	public static SequenceIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SequenceIterator iter = new SequenceIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in SequenceIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in SequenceIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.flow = new int[temp.size()];
		for (int i = 0; i < iter.flow.length; i++) {
			iter.flow[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * 判断是否存在下一个对象
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < flow.length);
	}
	/**
	 * 获取下一个对象
	 * @return object
	 */
	public Object next() {
		Sequence sequence = null;
		currentIndex++;
		if (currentIndex >= flow.length) {
			throw new NoSuchElementException();
		}
		try {
			sequence = new Sequence(flow[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in SequenceIterator::next(): " + e);
		}
		return sequence;
	}
	/**
	 * 删除一个对象
	 */
	public void remove() {
		int[] tmpSequences;
		tmpSequences = new int[flow.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpSequences[i] = flow[i];
		}
		for (int i = currentIndex + 1; i < tmpSequences.length; i++) {
			tmpSequences[i - 1] = flow[i];
		}
		flow = tmpSequences;
	}
	/**
	 * 获取Iterator 的长度
	 * @return int
	 */
	public int getLength() {
		return flow.length;
	}
}
