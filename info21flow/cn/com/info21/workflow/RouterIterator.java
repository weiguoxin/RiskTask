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
public class RouterIterator {
	private static final String ALLROUTER = "SELECT * FROM " + Router.TABLENAME;
	private int[] routers;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public RouterIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public RouterIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLROUTER);
			} else {
				pstmt = con.prepareStatement(ALLROUTER + " where " + condition);
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
			"SQLException in RouterIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in RouterIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		routers = new int[temp.size()];
		for (int i = 0; i < routers.length; i++) {
			routers[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public RouterIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLROUTER;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLROUTER + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in RouterIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in RouterIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		routers = new int[temp.size()];
		for (int i = 0; i < routers.length; i++) {
			routers[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return RouterIterator ���̼���
	 * @throws SystemException �������
	 */
	public static RouterIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RouterIterator iter = new RouterIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in RouterIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in RouterIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.routers = new int[temp.size()];
		for (int i = 0; i < iter.routers.length; i++) {
			iter.routers[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < routers.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Router rout = null;
		currentIndex++;
		if (currentIndex >= routers.length) {
			throw new NoSuchElementException();
		}
		try {
			rout = new Router(routers[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in RouterIterator::next(): " + e);
		}
		return rout;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpRouters;
		tmpRouters = new int[routers.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpRouters[i] = routers[i];
		}
		for (int i = currentIndex + 1; i < tmpRouters.length; i++) {
			tmpRouters[i - 1] = routers[i];
		}
		routers = tmpRouters;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return routers.length;
	}

}
