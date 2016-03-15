/*
 * �������� 2006-1-16
 */

package cn.com.info21.org;

import java.sql.*;
import java.util.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author songle
 */

public class UserdeptIterator {
	private static final String ALLOBJ = "SELECT * FROM " + Userdept.TABLENAME;
	private int[] entries;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public UserdeptIterator() {
	}
	/**
	 * ������������
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public UserdeptIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLOBJ);
			} else {
				pstmt = con.prepareStatement(ALLOBJ + " where " + condition);
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
			"SQLException in UserdeptIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in UserdeptIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		entries = new int[temp.size()];
		for (int i = 0; i < entries.length; i++) {
			entries[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public UserdeptIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLOBJ;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLOBJ + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in UserdeptIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in UserdeptIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		entries = new int[temp.size()];
		for (int i = 0; i < entries.length; i++) {
			entries[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return UserdeptIterator ����վ����
	 * @throws SystemException �������
	 */
	public static UserdeptIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserdeptIterator iter = new UserdeptIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in UserdeptIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in UserdeptIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.entries = new int[temp.size()];
		for (int i = 0; i < iter.entries.length; i++) {
			iter.entries[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < entries.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Userdept obj = null;
		currentIndex++;
		if (currentIndex >= entries.length) {
			throw new NoSuchElementException();
		}
		try {
			obj = new Userdept(entries[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in UserdeptIterator::next(): " + e);
		}
		return obj;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpentry;
		tmpentry = new int[entries.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpentry[i] = entries[i];
		}
		for (int i = currentIndex + 1; i < tmpentry.length; i++) {
			tmpentry[i - 1] = entries[i];
		}
		entries = tmpentry;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return entries.length;
	}
}
