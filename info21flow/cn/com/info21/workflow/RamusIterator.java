/*
 * �������� 2005-7-20
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
public class RamusIterator {
	private static final String ALLPROC = "SELECT * FROM " + Ramus.TABLENAME;
	private int[] ramuss;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public RamusIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public RamusIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLPROC);
			} else {
				pstmt = con.prepareStatement(ALLPROC + " where " + condition);
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
			"SQLException in RamusIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in RamusIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ramuss = new int[temp.size()];
		for (int i = 0; i < ramuss.length; i++) {
			ramuss[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public RamusIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLPROC;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLPROC + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in RamusIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in RamusIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ramuss = new int[temp.size()];
		for (int i = 0; i < ramuss.length; i++) {
			ramuss[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return RamusIterator ���̼���
	 * @throws SystemException �������
	 */
	public static RamusIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RamusIterator iter = new RamusIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in RamusIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in RamusIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.ramuss = new int[temp.size()];
		for (int i = 0; i < iter.ramuss.length; i++) {
			iter.ramuss[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < ramuss.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Ramus proc = null;
		currentIndex++;
		if (currentIndex >= ramuss.length) {
			throw new NoSuchElementException();
		}
		try {
			proc = new Ramus(ramuss[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in RamusIterator::next(): " + e);
		}
		return proc;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpRamuss;
		tmpRamuss = new int[ramuss.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpRamuss[i] = ramuss[i];
		}
		for (int i = currentIndex + 1; i < tmpRamuss.length; i++) {
			tmpRamuss[i - 1] = ramuss[i];
		}
		ramuss = tmpRamuss;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return ramuss.length;
	}
}
