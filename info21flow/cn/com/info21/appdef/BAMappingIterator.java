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

public class BAMappingIterator implements Iterator {
	private static final String ALLBAM = "SELECT * FROM " + BAMapping.TABLENAME;
	private int[] bams;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public BAMappingIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public BAMappingIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLBAM);
			} else {
				pstmt = con.prepareStatement(ALLBAM  + " where " + condition);
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
			"SQLException in BAMappingIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in BAMappingIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		bams = new int[temp.size()];
		for (int i = 0; i < bams.length; i++) {
			bams[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public BAMappingIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLBAM;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLBAM  + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in BAMappingIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in BAMappingIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		bams = new int[temp.size()];
		for (int i = 0; i < bams.length; i++) {
			bams[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return AppIterator ���̼���
	 * @throws SystemException �������
	 */
	public static BAMappingIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BAMappingIterator iter = new BAMappingIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in BAMappingIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in BAMappingIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.bams = new int[temp.size()];
		for (int i = 0; i < iter.bams.length; i++) {
			iter.bams[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < bams.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
	    BAMapping bam = null;
		currentIndex++;
		if (currentIndex >= bams.length) {
			throw new NoSuchElementException();
		}
		try {
		    System.out.println(bams[currentIndex]);
			bam = new BAMapping(bams[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in BAMappingIterator::next(): " + e);
		}
		return bam;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpbams;
		tmpbams = new int[bams.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpbams[i] = bams[i];
		}
		for (int i = currentIndex + 1; i < tmpbams.length; i++) {
			tmpbams[i - 1] = bams[i];
		}
		bams = tmpbams;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return bams.length;
	}
}
