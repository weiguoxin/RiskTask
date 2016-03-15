package cn.com.info21.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author Hu Guohua
 */

public class DataIterator implements Iterator {
	private String tablename = "";
	private String allObj = "SELECT * FROM ";
	private String[] ids;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public DataIterator() {
	}
	/**
	 * ������������
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public DataIterator(String condition, String tablename, int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    setTablename(tablename);
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(allObj);
			} else {
				pstmt = con.prepareStatement(allObj + " where " + condition);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			if (index >= (start + num)) { break; }
      			if (index >= start) {
        			temp.add(rs.getString("id"));
      			}
		        index++;
			}
		} catch (SQLException sqle) {
			System.err.println(
			"SQLException in DataIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in DataIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ids = new String[temp.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = (String) temp.get(i);
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public DataIterator(String condition, String tablename) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		setTablename(tablename);
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = allObj;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = allObj + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(rs.getString("id"));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in DataIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in DataIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ids = new String[temp.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = (String) temp.get(i);
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return Iterator ����
	 * @throws SystemException �������
	 */
	public static DataIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DataIterator iter = new DataIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(rs.getString("id"));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in DataIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in DataIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.ids = new String[temp.size()];
		for (int i = 0; i < iter.ids.length; i++) {
			iter.ids[i] = (String) temp.get(i);
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < ids.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Data obj = null;
		currentIndex++;
		if (currentIndex >= ids.length) {
			throw new NoSuchElementException();
		}
		try {
			obj = new Data(ids[currentIndex], tablename);
		} catch (Exception e) {
			System.err.println("SystemException in DataIterator::next(): " + e);
		}
		return obj;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		String[] tmpIds;
		tmpIds = new String[ids.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpIds[i] = ids[i];
		}
		for (int i = currentIndex + 1; i < tmpIds.length; i++) {
			tmpIds[i - 1] = ids[i];
		}
		ids = tmpIds;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return ids.length;
	}

	/**
	 * @return Returns the tablename.
	 */
	protected String getTablename() {
		return tablename;
	}
	/**
	 * @param tablename The tablename to set.
	 */
	protected void setTablename(String tablename) {
		this.tablename = tablename;
		allObj = "SELECT * FROM " + tablename;
	}
}
