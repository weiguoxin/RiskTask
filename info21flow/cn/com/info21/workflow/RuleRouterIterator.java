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
public class RuleRouterIterator {
	private static final String ALLRULEROUTER = "SELECT * FROM " + RuleRouter.TABLENAME;
	private int[] rurts;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public RuleRouterIterator() {
	}
	/**
	 * �����������ҹ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public RuleRouterIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLRULEROUTER);
			} else {
				pstmt = con.prepareStatement(ALLRULEROUTER + " where " + condition);
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
			"SQLException in RuleRouterIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in RuleRouterIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		rurts = new int[temp.size()];
		for (int i = 0; i < rurts.length; i++) {
			rurts[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public RuleRouterIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLRULEROUTER;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLRULEROUTER + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in RuleRouterIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in RuleRouterIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		rurts = new int[temp.size()];
		for (int i = 0; i < rurts.length; i++) {
			rurts[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return RuleRouterIterator ���̼���
	 * @throws SystemException �������
	 */
	public static RuleRouterIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RuleRouterIterator iter = new RuleRouterIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in RuleRouterIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in RuleRouterIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.rurts = new int[temp.size()];
		for (int i = 0; i < iter.rurts.length; i++) {
			iter.rurts[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < rurts.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		RuleRouter ruleRouter = null;
		currentIndex++;
		if (currentIndex >= rurts.length) {
			throw new NoSuchElementException();
		}
		try {
			ruleRouter = new RuleRouter(rurts[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in RuleRouterIterator::next(): " + e);
		}
		return ruleRouter;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpRurts;
		tmpRurts = new int[rurts.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpRurts[i] = rurts[i];
		}
		for (int i = currentIndex + 1; i < tmpRurts.length; i++) {
			tmpRurts[i - 1] = rurts[i];
		}
		rurts = tmpRurts;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return rurts.length;
	}
}
