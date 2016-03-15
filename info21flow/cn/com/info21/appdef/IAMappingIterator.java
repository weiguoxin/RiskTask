/*
 * �������� 2005-9-14
 */

package cn.com.info21.appdef;
import java.sql.*;
import java.util.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class IAMappingIterator implements Iterator {
	private static final String ALLobj = "SELECT * FROM " + IAMapping.TABLENAME;
	private int[] iams;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public IAMappingIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public IAMappingIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLobj);
			} else {
				pstmt = con.prepareStatement(ALLobj  + " where " + condition);
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
			"SQLException in IAMappingIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in IAMappingIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iams = new int[temp.size()];
		for (int i = 0; i < iams.length; i++) {
			iams[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public IAMappingIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLobj;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLobj  + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in IAMappingIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in IAMappingIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iams = new int[temp.size()];
		for (int i = 0; i < iams.length; i++) {
			iams[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return IAMappingIterator ���̼���
	 * @throws SystemException �������
	 */
	public static IAMappingIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		IAMappingIterator iter = new IAMappingIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in IAMappingIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in IAMappingIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.iams = new int[temp.size()];
		for (int i = 0; i < iter.iams.length; i++) {
			iter.iams[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < iams.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
	    IAMapping iam = null;
		currentIndex++;
		if (currentIndex >= iams.length) {
			throw new NoSuchElementException();
		}
		try {
			iam = new IAMapping(iams[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in IAMappingIterator::next(): " + e);
		}
		return iam;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpiams;
		tmpiams = new int[iams.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpiams[i] = iams[i];
		}
		for (int i = currentIndex + 1; i < tmpiams.length; i++) {
			tmpiams[i - 1] = iams[i];
		}
		iams = tmpiams;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return iams.length;
	}
}
