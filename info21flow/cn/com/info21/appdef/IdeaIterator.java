/*
 * �������� 2005-8-1
 *
 */
package cn.com.info21.appdef;

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
public class IdeaIterator {
	private static final String ALLIDEA = "SELECT * FROM " + Idea.TABLENAME;
	private int[] ideas;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public IdeaIterator() {
	}
	/**
	 * �����������ҷ���
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public IdeaIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLIDEA);
			} else {
				pstmt = con.prepareStatement(ALLIDEA + " where " + condition);
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
			"SQLException in IdeaIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in IdeaIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ideas = new int[temp.size()];
		for (int i = 0; i < ideas.length; i++) {
			ideas[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public IdeaIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLIDEA;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLIDEA + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in IdeaIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in IdeaIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ideas = new int[temp.size()];
		for (int i = 0; i < ideas.length; i++) {
			ideas[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return IdeaIterator ��������
	 * @throws SystemException �������
	 */
	public static IdeaIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		IdeaIterator iter = new IdeaIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in IdeaIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in IdeaIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.ideas = new int[temp.size()];
		for (int i = 0; i < iter.ideas.length; i++) {
			iter.ideas[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < ideas.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Idea idea = null;
		currentIndex++;
		if (currentIndex >= ideas.length) {
			throw new NoSuchElementException();
		}
		try {
			idea = new Idea(ideas[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in IdeaIterator::next(): " + e);
		}
		return idea;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpIdeas;
		tmpIdeas = new int[ideas.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpIdeas[i] = ideas[i];
		}
		for (int i = currentIndex + 1; i < tmpIdeas.length; i++) {
			tmpIdeas[i - 1] = ideas[i];
		}
		ideas = tmpIdeas;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return ideas.length;
	}
}
