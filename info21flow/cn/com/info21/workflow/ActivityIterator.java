/*
 * 创建日期 2005-7-19
 */
package cn.com.info21.workflow;

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
public class ActivityIterator {
	private static final String ALLACTIVITY = "SELECT * FROM " + Activity.TABLENAME;
	private int[] actys;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * 构造函数一
	 */
	public ActivityIterator() {
	}
	/**
	 * 根据条件查找过程
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 数目
	 */
	public ActivityIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLACTIVITY);
			} else {
				pstmt = con.prepareStatement(ALLACTIVITY + " where " + condition);
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
			"SQLException in ActivityIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in ActivityIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		actys = new int[temp.size()];
		for (int i = 0; i < actys.length; i++) {
			actys[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param condition 条件
	 * @throws SystemException 捕获错误
	 */
	public ActivityIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLACTIVITY;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLACTIVITY + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in ActivityIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in ActivityIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		actys = new int[temp.size()];
		for (int i = 0; i < actys.length; i++) {
			actys[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param sql sql语句
	 * @return ActivityIterator 过程集合
	 * @throws SystemException 捕获错误
	 */
	public static ActivityIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ActivityIterator iter = new ActivityIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in ActivityIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in ActivityIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.actys = new int[temp.size()];
		for (int i = 0; i < iter.actys.length; i++) {
			iter.actys[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * 判断是否存在下一个对象
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < actys.length);
	}
	/**
	 * 获取下一个对象
	 * @return object
	 */
	public Object next() {
		Activity proc = null;
		currentIndex++;
		if (currentIndex >= actys.length) {
			throw new NoSuchElementException();
		}
		try {
			proc = new Activity(actys[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in ActivityIterator::next(): " + e);
		}
		return proc;
	}
	/**
	 * 删除一个对象
	 */
	public void remove() {
		int[] tmpActys;
		tmpActys = new int[actys.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpActys[i] = actys[i];
		}
		for (int i = currentIndex + 1; i < tmpActys.length; i++) {
			tmpActys[i - 1] = actys[i];
		}
		actys = tmpActys;
	}
	/**
	 * 获取Iterator 的长度
	 * @return int
	 */
	public int getLength() {
		return actys.length;
	}
}
