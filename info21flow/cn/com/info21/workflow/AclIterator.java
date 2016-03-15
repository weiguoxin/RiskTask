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

public class AclIterator {
	private static final String ALLPROC = "SELECT * FROM " + Acl.TABLENAME;
	private int[] flow;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public AclIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public AclIterator(String condition , int start , int num) {
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
			"SQLException in AclIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in AclIterator(String,int,int): " + e);
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
	public AclIterator(String condition) throws SystemException {
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
			System.err.println("SQLException in AclIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in AclIterator(String): " + e);
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
	 * @return AclIterator ���̼���
	 * @throws SystemException �������
	 */
	public static AclIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AclIterator iter = new AclIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in AclIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in AclIterator(String): " + e);
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
		Acl proc = null;
		currentIndex++;
		if (currentIndex >= flow.length) {
			throw new NoSuchElementException();
		}
		try {
			proc = new Acl(flow[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in AclIterator::next(): " + e);
		}
		return proc;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpProcs;
		tmpProcs = new int[flow.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpProcs[i] = flow[i];
		}
		for (int i = currentIndex + 1; i < tmpProcs.length; i++) {
			tmpProcs[i - 1] = flow[i];
		}
		flow = tmpProcs;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return flow.length;
	}
}
