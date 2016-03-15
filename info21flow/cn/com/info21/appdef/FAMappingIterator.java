/*
 * �������� 2005-9-14
 */

package cn.com.info21.appdef;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class FAMappingIterator implements Iterator {
	private static final String ALLFAM = "SELECT * FROM " + FAMapping.TABLENAME;
	private int[] fams;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public FAMappingIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public FAMappingIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLFAM);
			} else {
				pstmt = con.prepareStatement(ALLFAM  + " where " + condition);
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
			"SQLException in FAMappingIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in FAMappingIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		fams = new int[temp.size()];
		for (int i = 0; i < fams.length; i++) {
			fams[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public FAMappingIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLFAM;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLFAM  + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in FAMappingIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in FAMappingIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		fams = new int[temp.size()];
		for (int i = 0; i < fams.length; i++) {
			fams[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return FAMappingIterator ���̼���
	 * @throws SystemException �������
	 */
	public static FAMappingIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FAMappingIterator iter = new FAMappingIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in FAMappingIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in FAMappingIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
		iter.fams = new int[temp.size()];
		for (int i = 0; i < iter.fams.length; i++) {
			iter.fams[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < fams.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
	    FAMapping bam = null;
		currentIndex++;
		if (currentIndex >= fams.length) {
			throw new NoSuchElementException();
		}
		try {
			bam = new FAMapping(fams[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in FAMappingIterator::next(): " + e);
		}
		return bam;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpfams;
		tmpfams = new int[fams.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpfams[i] = fams[i];
		}
		for (int i = currentIndex + 1; i < tmpfams.length; i++) {
			tmpfams[i - 1] = fams[i];
		}
		fams = tmpfams;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return fams.length;
	}
}
