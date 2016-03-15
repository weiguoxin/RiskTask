/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author Hu Guohua
 */
public class OrgEntryUserIterator implements Iterator {
	private static final String ALLORGENTRY1 = "SELECT * FROM " + OrgEntryUser.TABLENAME;
	private int[] orgentry1s;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * 构造函数一
	 */
	public OrgEntryUserIterator() {
	}
	/**
	 * 根据条件查找过程
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 数目
	 */
	public OrgEntryUserIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLORGENTRY1);
			} else {
				pstmt = con.prepareStatement(ALLORGENTRY1  + " where " + condition);
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
			"SQLException in OrgEntryUserIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in OrgEntryUserIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		orgentry1s = new int[temp.size()];
		for (int i = 0; i < orgentry1s.length; i++) {
			orgentry1s[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param condition 条件
	 * @throws SystemException 捕获错误
	 */
	public OrgEntryUserIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLORGENTRY1;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLORGENTRY1  + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in OrgEntryUserIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in OrgEntryUserIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		orgentry1s = new int[temp.size()];
		for (int i = 0; i < orgentry1s.length; i++) {
			orgentry1s[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param sql sql语句
	 * @return AppIterator 过程集合
	 * @throws SystemException 捕获错误
	 */
	public static OrgEntryUserIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrgEntryUserIterator iter = new OrgEntryUserIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in OrgEntryUSerIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in OrgEntryUserIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.orgentry1s = new int[temp.size()];
		for (int i = 0; i < iter.orgentry1s.length; i++) {
			iter.orgentry1s[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * 判断是否存在下一个对象
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < orgentry1s.length);
	}
	/**
	 * 获取下一个对象
	 * @return object
	 */
	public Object next() {
		OrgEntryUser orgentry1 = null;
		currentIndex++;
		if (currentIndex >= orgentry1s.length) {
			throw new NoSuchElementException();
		}
		try {
			orgentry1 = new OrgEntryUser(orgentry1s[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in OrgEntryUserIterator::next(): " + e);
		}
		return orgentry1;
	}
	/**
	 * 删除一个对象
	 */
	public void remove() {
		int[] tmpProcs;
		tmpProcs = new int[orgentry1s.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpProcs[i] = orgentry1s[i];
		}
		for (int i = currentIndex + 1; i < tmpProcs.length; i++) {
			tmpProcs[i - 1] = orgentry1s[i];
		}
		orgentry1s = tmpProcs;
	}
	/**
	 * 获取Iterator 的长度
	 * @return int
	 */
	public int getLength() {
		return orgentry1s.length;
	}
}
