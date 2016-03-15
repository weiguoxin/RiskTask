package cn.com.shasha.sys;


import java.util.*;
import java.sql.*;
import cn.com.info21.system.*;
import cn.com.info21.database.*;

public class ArticleIterator {
	private int currentIndex = -1;
	private int[] articles;
	private String ALL_ARTICLE = "SELECT * FROM SYSNOTICE ";
	private String errStr = "";
	/**
	* 构造方法一：根据查询条件 ，获得一个Article对象列表。
	* 创建日期：(2003-6-8 00:18:37)
	* @param condition String 查询条件。
	* @throws SystemException。
	*/
	public ArticleIterator(java.lang.String conditionStr)
		throws SystemException {
		//We don't know how many results will be returned, so store them in an ArrayList.
		ArrayList tempArticles = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(ALL_ARTICLE);
			} else {
				pstmt =
					con.prepareStatement(
						ALL_ARTICLE + " WHERE " + conditionStr);
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
	* 构造方法二：根据查询条件 ，从startIndex开始，获得num个Article对象的列表。
	* 创建日期：(2003-6-8 00:20:00)
	* @param condition String 查询条件。
	* @param startIndex int 开始位置。
	* @param num int 要求返回对象个数。
	* @throws SystemException。
	*/
	public ArticleIterator(String conditionStr, int startIndex, int num)
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
				pstmt = con.prepareStatement(ALL_ARTICLE);
			} else {
				pstmt =
					con.prepareStatement(
						ALL_ARTICLE + " WHERE " + conditionStr);
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
		Article article = null;
		currentIndex++;
		if (currentIndex >= articles.length) {
			throw new NoSuchElementException();
		}
		try {
			article = ArticleHome.findById(articles[currentIndex]);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return article;
	}

}