/*
 * �������� 2005-8-1
 *
 */

package cn.com.info21.appdef;
import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 *
 */

public class FieldIterator {
	private static final String ALLFIELD = "SELECT * FROM " + Field.TABLENAME;
	private int[] fields;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public FieldIterator() {
	}
	/**
	 * ��������������
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public FieldIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLFIELD);
			} else {
				pstmt = con.prepareStatement(ALLFIELD + " where " + condition);
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
			"SQLException in FieldIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in FieldIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		fields = new int[temp.size()];
		for (int i = 0; i < fields.length; i++) {
			fields[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public FieldIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLFIELD;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLFIELD + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in FieldIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in FieldIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		fields = new int[temp.size()];
		for (int i = 0; i < fields.length; i++) {
			fields[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return FieldIterator �򼯺�
	 * @throws SystemException �������
	 */
	public static FieldIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FieldIterator iter = new FieldIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in FieldIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in FieldIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.fields = new int[temp.size()];
		for (int i = 0; i < iter.fields.length; i++) {
			iter.fields[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < fields.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Field field = null;
		currentIndex++;
		if (currentIndex >= fields.length) {
			throw new NoSuchElementException();
		}
		try {
			field = new Field(fields[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in FieldIterator::next(): " + e);
		}
		return field;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpFields;
		tmpFields = new int[fields.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpFields[i] = fields[i];
		}
		for (int i = currentIndex + 1; i < tmpFields.length; i++) {
			tmpFields[i - 1] = fields[i];
		}
		fields = tmpFields;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return fields.length;
	}
}
