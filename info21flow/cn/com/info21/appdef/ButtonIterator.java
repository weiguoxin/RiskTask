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
public class ButtonIterator {
	private static final String ALLBUTTON = "SELECT * FROM " + Button.TABLENAME;
	private int[] buttons;
	private int currentIndex = -1;
	private String error = "";
	/**
	 * ���캯��һ
	 */
	public ButtonIterator() {
	}
	/**
	 * �����������Ұ�ť
	 * @param condition ����
	 * @param start ��ʼ��
	 * @param num ��Ŀ
	 */
	public ButtonIterator(String condition , int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				pstmt = con.prepareStatement(ALLBUTTON);
			} else {
				pstmt = con.prepareStatement(ALLBUTTON + " where " + condition);
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
			"SQLException in ButtonIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in ButtonIterator(String,int,int): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		buttons = new int[temp.size()];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param condition ����
	 * @throws SystemException �������
	 */
	public ButtonIterator(String condition) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLBUTTON;
				pstmt = con.prepareStatement(sql);
			} else {
				sql = ALLBUTTON + " where " + condition;
				pstmt = con.prepareStatement(sql);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in ButtonIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in ButtonIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		buttons = new int[temp.size()];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] =  ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return ButtonIterator ��ť����
	 * @throws SystemException �������
	 */
	public static ButtonIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ButtonIterator iter = new ButtonIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("id")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in ButtonIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in ButtonIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.buttons = new int[temp.size()];
		for (int i = 0; i < iter.buttons.length; i++) {
			iter.buttons[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	 * �ж��Ƿ������һ������
	 * @return boolean
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < buttons.length);
	}
	/**
	 * ��ȡ��һ������
	 * @return object
	 */
	public Object next() {
		Button button = null;
		currentIndex++;
		if (currentIndex >= buttons.length) {
			throw new NoSuchElementException();
		}
		try {
			button = new Button(buttons[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in ButtonIterator::next(): " + e);
		}
		return button;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpButtons;
		tmpButtons = new int[buttons.length - 1];
		for (int i = 0; i < currentIndex; i++) {
			tmpButtons[i] = buttons[i];
		}
		for (int i = currentIndex + 1; i < tmpButtons.length; i++) {
			tmpButtons[i - 1] = buttons[i];
		}
		buttons = tmpButtons;
	}
	/**
	 * ��ȡIterator �ĳ���
	 * @return int
	 */
	public int getLength() {
		return buttons.length;
	}
}
