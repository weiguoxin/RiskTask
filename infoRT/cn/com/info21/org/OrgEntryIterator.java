/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cn.com.info21.database.DbConnectionManager;

/**
 * @author Hu Guohua
 */
public class OrgEntryIterator implements Iterator {

	/**
	 *  global variant define
	 */
	private String error = "";
	private String[] ids;
	private static int parentid;
	private final String clsname = getClass().getName();
	private int currentIndex = -1;
	private static final String ALLOBJ = "select distinct * from " + OrgEntry.TABLENAME;
	private static final String CHILDREN = "parentid =? and enable=1";
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public OrgEntryIterator () {
	}
  /**
   * create with condition
   * @param condition String is ��ѯ����
   */
  protected OrgEntryIterator(String condition) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLOBJ;
			} else {
				sql = ALLOBJ + " where " + condition;
			}
			pstmt = con.prepareStatement(sql);
			if (condition.equals(CHILDREN)) {
				pstmt.setInt(1, parentid);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
      			temp.add(rs.getString("id"));
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.err.println("SQLException in OrgEntryIterator(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in OrgEntryIterator(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ids = new String[temp.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] =  (String) temp.get(i);
		}
	}
    /**
     * create with condition and start and num
     * @param condition ��ѯ����
     * @param orderBy ����
     * @param start ��ʼ��
     * @param num ����
     */
	protected OrgEntryIterator(String condition, String orderBy, int start , int num) {
		ArrayList temp = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    int index = 0;
	    String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if (condition == null || condition.equals("")) {
				sql = ALLOBJ;
			} else {
				sql = ALLOBJ + " where " + condition;
			}
			if ((orderBy != null) && (!"".equals(orderBy))) {
				sql += " order by " + orderBy;
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			//int id = 0;
			while (rs.next()) {
      			if (index >= (start + num)) { break; }
      			if (index >= start) {
      				temp.add(rs.getString("id"));
      			}
		        index++;
			}
		} catch (SQLException sqle) {
			System.err.println(
			"SQLException in OrgEntryIterator(String,int,int): " + sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception in OrgEntryIterator(String,int,int): " + e);
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}

		ids = new String[temp.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] =  (String) temp.get(i);
		}
	}
	/**
	 * @param entry ��ǰentry
	 * @return ��entry
	 */
	public static OrgEntryIterator getChildren(OrgEntry entry) {
		if (entry != null) {
			return getChildren(entry.getId0());
		} else {
			return null;
		}
	}
	/**
	 * @param id0 ��ǰentry id0
	 * @return ������entries��OrgEntryIterator
	 */
	public static OrgEntryIterator getChildren(int id0) {
		String conStr = CHILDREN;		//Ӧ����Ȩ������
		OrgEntryIterator rtn = null;
		parentid = id0;
		try {
			rtn = new OrgEntryIterator(conStr);
		} catch (Exception e) {
			String errStr = "";
			errStr = "Exception in ArticleIterator:" + e;
			System.err.println(errStr);
		}
		return rtn;
	}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		//System.out.println("currentIndex :" + currentIndex + 1);
		//System.out.println("tempDept.length" + tempDept.length);
		return (currentIndex + 1 < ids.length);
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		OrgEntry entry = null;
		currentIndex++;
		if (currentIndex >= ids.length) {
			throw new NoSuchElementException();
		}
		try {
			entry = new OrgEntry(ids[currentIndex]);
		} catch (Exception e) {
			System.err.println("SystemException in DeptIterator::next(): " + e);
		}

		return entry;
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
	}

    /**
     * ����֮��
     * @param args ����Ĳ���
     */
	public static void main(String[] args) {
		DeptIterator deptIter = new DeptIterator("");
		Dept dept = null;
		while (deptIter.hasNext()) {
			dept = (Dept) deptIter.next();
//			System.out.println(Dept.getId());
		}
	}
	/**
	 * @return Returns the ids.
	 */
	public String[] getIds() {
		return ids;
	}
	/**
	 * @param ids The ids to set.
	 */
	public void setIds(String[] ids) {
		this.ids = ids;
	}
}

