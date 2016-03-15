package cn.com.shasha.sys;


import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class TaskManIterator {
	private int currentIndex = -1;
	private int[] articles;
	private String ALL_TASKMAN = "SELECT * FROM TASKMAN ";
	private String errStr = "";
	/**
	* ���췽��һ�����ݲ�ѯ���� �����һ��Article�����б�
	* �������ڣ�(2003-6-8 00:18:37)
	* @throws cn.com.info21.system.SystemException��
	*/
	public TaskManIterator(String conditionStr)
		throws SystemException {
		//We don't know how many results will be returned, so store them in an ArrayList.
		ArrayList tempArticles = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(ALL_TASKMAN);
			} else {
				pstmt =
					con.prepareStatement(
                            ALL_TASKMAN + " WHERE " + conditionStr);
			}

			//Log.info(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tempArticles.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new SystemException(errStr);
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new SystemException(errStr);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		//Now copy into an array.
		articles = new int[tempArticles.size()];
		//Log.info(tempArticles.size());
		for (int i = 0; i < articles.length; i++) {
			articles[i] = ((Integer) tempArticles.get(i)).intValue();
		}
	}
	/**
	* ���췽���������ݲ�ѯ���� ����startIndex��ʼ�����num��Article������б�
	* �������ڣ�(2003-6-8 00:20:00)
	* @param startIndex int ��ʼλ�á�
	* @param num int Ҫ�󷵻ض��������
	* @throws cn.com.info21.system.SystemException��
	*/
	public TaskManIterator(String conditionStr, int startIndex, int num)
		throws SystemException {
		ArrayList tempArticles = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement st = null;
		ResultSet rs = null;
		int index = 0;
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(ALL_TASKMAN);
			} else {
				pstmt =
					con.prepareStatement(
                            ALL_TASKMAN + " WHERE " + conditionStr);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (index >= (startIndex + num))
					break;
				if (index >= startIndex) {
					tempArticles.add(new Integer(rs.getInt("ID")));
				}
				index++;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new SystemException(errStr);
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new SystemException(errStr);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		//Now copy into an array.
		articles = new int[tempArticles.size()];
		for (int i = 0; i < articles.length; i++) {
			articles[i] = ((Integer) tempArticles.get(i)).intValue();
		}
	}
	public int getLength() {
		return articles.length;
	}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < articles.length);
	}
	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() throws NoSuchElementException {
		TaskMan article = null;
		currentIndex++;
		if (currentIndex >= articles.length) {
			throw new NoSuchElementException();
		}
		try {
			article = TaskManHome.findById(articles[currentIndex]);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return article;
	}

}