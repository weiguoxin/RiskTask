/*
 * Created on 2004-6-19
 */

package cn.com.shasha.sys;
import java.util.*;
import java.sql.*;

import cn.com.info21.database.*;
//import cn.com.info21.system.*;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class WLUserIterator implements Iterator {
	private static final String ALLOBJ = "SELECT * FROM " + WLUser.TABLENAME;
	private int currentIndex = -1;
	private int[] objs;
	private String errorStr = "";
	/**
	 * 
	 */
	public WLUserIterator() {
	}
	/**
	 * ���캯��
	 * @param sql sql���
	 * @return Iterator ����
	 * @throws SystemException �������
	 */
	public static WLUserIterator findBySQL(String sql) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WLUserIterator iter = new WLUserIterator();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(new Integer(rs.getInt("userid")));
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in WLUserIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in WLUserIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		iter.objs = new int[temp.size()];
		for (int i = 0; i < iter.objs.length; i++) {
			iter.objs[i] =  ((Integer) temp.get(i)).intValue();
		}
		return iter;
	}
	/**
	* ���췽��һ�����ݲ�ѯ���� �����һ��User�����б�
	* �������ڣ�(2003-3-25 10:24:42)
	* @param conditionStr ��ѯ������
	* @throws SystemException ������д�뵽ϵͳ��־
	*/
	public WLUserIterator(String conditionStr) throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				sql = ALLOBJ;
			} else {
				sql = ALLOBJ + " WHERE " + conditionStr;
			}

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				temp.add(new Integer(rs.getInt("USERID")));
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in WLUserIterator:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in WLUserIterator:" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);

		}
		objs = new int[temp.size()];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	* ���췽���������ݲ�ѯ���� ����startIndex��ʼ�����num��User������б�
	* �������ڣ�(2003-3-25 10:24:42)
	* @param conditionStr ��ѯ������
	* @param startIndex int ��ʼλ�á�
	* @param num int Ҫ�󷵻ض��������
	* @throws SystemException ������д�뵽ϵͳ��־
	*/
	public WLUserIterator(String conditionStr, int startIndex, int num)
	throws SystemException {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		int index = 0;
		int counter = 1;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(ALLOBJ);
			} else {
				pstmt =
					con.prepareStatement(ALLOBJ + " WHERE " + conditionStr);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (index >= (startIndex + num)) {
					break;
				}
				if (index >= startIndex) {
					temp.add(new Integer(rs.getInt("USERID")));
				}
				index++;
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in WLUserIterator:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in WLUserIterator:" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);

		}
		objs = new int[temp.size()];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = ((Integer) temp.get(i)).intValue();
		}
	}
	/**
	 * ��ȡ��ǰIterator�ж�����Ŀ
	 * @return ���鳤��
	 */
	public int getLength() {
		return objs.length;
	}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < objs.length);
	}
	/**
	 * ������һ���û�
	 * @return user
	 */
	public Object next() {
		WLUser user = null;
		currentIndex++;
		try {
			user = WLUserHome.findById(objs[currentIndex]);
		} catch (Exception e) {
			System.err.println(e);
		}
		return user;
	}
	/**
	 * ɾ��һ������
	 */
	public void remove() {
		int[] tmpArr;
		tmpArr = new int[objs.length - 1];
		for (int i = 0; i < currentIndex; i++) {
		    tmpArr[i] = objs[i];
		}
		for (int i = currentIndex + 1; i < tmpArr.length; i++) {
		    tmpArr[i - 1] = objs[i];
		}
		objs = tmpArr;
	}
}



