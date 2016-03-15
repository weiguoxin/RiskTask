package cn.com.info21.workflow;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */
public class RuleChoosePersonIterator {
	private static final String ALLPROC = "SELECT * FROM " + RuleChoosePerson.TABLENAME;
	private int[] ruleChoosePersons;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * 构造函数一
	 */
	public RuleChoosePersonIterator() {
	}
	/**
	 * 根据条件查找过程
	 * @param condition 条件
	 * @param start 开始行
	 * @param num 数目
	 */
	public RuleChoosePersonIterator(String condition , int start , int num) {
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
			"SQLException in RuleChoosePersonIterator(String,int,int): "
			        + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in "
			        + "RuleChoosePersonIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ruleChoosePersons = new int[temp.size()];
		for (int i = 0; i < ruleChoosePersons.length; i++) {
			ruleChoosePersons[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param condition 条件
	 * @throws SystemException 捕获错误
	 */
	public RuleChoosePersonIterator(String condition) throws SystemException {
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
			System.err.println("SQLException in "
			        + "RuleChoosePersonIterator"
			        + "(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in RuleChoosePersonIterator"
			        + "(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ruleChoosePersons = new int[temp.size()];
		for (int i = 0; i < ruleChoosePersons.length; i++) {
			ruleChoosePersons[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * 构造函数
	 * @param sql sql语句
	 * @return RuleChoosePersonIterator 过程集合
	 * @throws SystemException 捕获错误
	 */
	public static RuleChoosePersonIterator findBySQL(String sql)
	throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RuleChoosePersonIterator iter = new RuleChoosePersonIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in RuleChoosePersonIterator"
			        + "(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in RuleChoosePersonIterator"
			        + "(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.ruleChoosePersons = new int[temp.size()];
		for (int i = 0; i < iter.ruleChoosePersons.length; i++) {
			iter.ruleChoosePersons[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * 判断是否存在下一个对象
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < ruleChoosePersons.length);
	}
	/**
	 * 获取下一个对象
	 * @return object
	 */
	public Object next() {
		RuleChoosePerson rcps = null;
		currentIndex++;
		if (currentIndex >= ruleChoosePersons.length) {
			throw new NoSuchElementException();
		}
		try {
			rcps = new RuleChoosePerson(ruleChoosePersons[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in "
			        + "RuleChoosePersonIterator::next(): " + e);
		}
		return rcps;
	}
	/**
	 * 删除一个对象
	 */
	public void remove() {
		int[] tmpRuleChoosePersons;
		tmpRuleChoosePersons = new int[ruleChoosePersons.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpRuleChoosePersons[i] = ruleChoosePersons[i];
		}
		for (int i = currentIndex + 1; i < tmpRuleChoosePersons.length; i++) {
			tmpRuleChoosePersons[i - 1] = ruleChoosePersons[i];
		}
		ruleChoosePersons = tmpRuleChoosePersons;
	}
	/**
	 * 获取Iterator 的长度
	 * @return int
	 */
	public int getLength() {
		return ruleChoosePersons.length;
	}
}