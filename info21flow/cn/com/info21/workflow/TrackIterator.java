/*
 * �������� 2005-7-17
 */

package cn.com.info21.workflow;

import java.sql.*;
import java.util.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.*;

/**
 * @author songle
 */

public class TrackIterator {
	private static final String ALLTRACK = "SELECT * FROM " + Track.TABLENAME;
	private int[] flow;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public TrackIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public TrackIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLTRACK);
			} else {
				pstmt = con.prepareStatement(ALLTRACK + " where " + condition);
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
			"SQLException in TrackIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in TrackIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		flow = new int[temp.size()];
		for (int i = 0; i < flow.length; i++) {
			flow[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public TrackIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLTRACK;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLTRACK + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in TrackIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in TrackIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		flow = new int[temp.size()];
		for (int i = 0; i < flow.length; i++) {
			flow[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return TrackIterator ���̼���
	 * @throws SystemException �������
	 */
	public static TrackIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TrackIterator iter = new TrackIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in TrackIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in TrackIterator(String): " + e);
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
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < flow.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Track track = null;
		currentIndex++;
		if (currentIndex >= flow.length) {
			throw new NoSuchElementException();
		}
		try {
			track = new Track(flow[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in TrackIterator::next(): " + e);
		}
		return track;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpTracks;
		tmpTracks = new int[flow.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpTracks[i] = flow[i];
		}
		for (int i = currentIndex + 1; i < tmpTracks.length; i++) {
			tmpTracks[i - 1] = flow[i];
		}
		flow = tmpTracks;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return flow.length;
	}
}
