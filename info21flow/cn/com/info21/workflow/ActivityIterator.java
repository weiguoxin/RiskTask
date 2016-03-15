/*
 * �������� 2005-7-19
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
	 * ���캯��һ
	 */
	public ActivityIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
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
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
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
	 * ���캯��
	 * @param sql sql���
	 * @return ActivityIterator ���̼���
	 * @throws SystemException �������
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
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < actys.length);
	}
	/**
	 * ��ȡ��һ������
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
	 * ɾ��һ������
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
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return actys.length;
	}
}
