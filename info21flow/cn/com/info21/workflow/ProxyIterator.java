/*
 * �������� 2005-8-1
 *
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
 *
 */
public class ProxyIterator {
	private static final String ALLPROXY = "SELECT * FROM " + Proxy.TABLENAME;
	private int[] proxys;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public ProxyIterator() {
	}
	/**
	 * �����������Ҵ��������
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public ProxyIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLPROXY);
			} else {
				pstmt = con.prepareStatement(ALLPROXY + " where " + condition);
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
			"SQLException in ProxyIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in ProxyIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		proxys = new int[temp.size()];
		for (int i = 0; i < proxys.length; i++) {
			proxys[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public ProxyIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLPROXY;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLPROXY + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in ProxyIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in ProxyIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		proxys = new int[temp.size()];
		for (int i = 0; i < proxys.length; i++) {
			proxys[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return ProxyIterator ���̼���
	 * @throws SystemException �������
	 */
	public static ProxyIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProxyIterator iter = new ProxyIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in ProxyIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in ProxyIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.proxys = new int[temp.size()];
		for (int i = 0; i < iter.proxys.length; i++) {
			iter.proxys[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < proxys.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Proxy proxy = null;
		currentIndex++;
		if (currentIndex >= proxys.length) {
			throw new NoSuchElementException();
		}
		try {
			proxy = new Proxy(proxys[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in ProxyIterator::next(): " + e);
		}
		return proxy;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpProxys;
		tmpProxys = new int[proxys.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpProxys[i] = proxys[i];
		}
		for (int i = currentIndex + 1; i < tmpProxys.length; i++) {
			tmpProxys[i - 1] = proxys[i];
		}
		proxys = tmpProxys;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return proxys.length;
	}
}
