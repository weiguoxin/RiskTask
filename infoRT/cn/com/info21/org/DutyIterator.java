/*
 * Created on 2004-6-19
 */

package cn.com.info21.org;
import java.util.*;
import java.sql.*;
import cn.com.info21.database.*;
//import cn.com.info21.system.*;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class DutyIterator implements Iterator {
	private static final String GET_DUTY_CNT = "SELECT COUNT(*) AS CNT FROM " + Duty.TABLENAME;
	private static final String GET_DUTY = "SELECT * FROM " + Duty.TABLENAME;
	private int currentIndex = -1;
	private int[] dutys;
	private String errorStr = "";
	/**
	* 构造方法一：根据查询条件 ，获得一个Duty对象列表。
	* 创建日期：(2003-3-25 10:24:42)
	* @param conditionStr 查询条件。
	* @throws SystemException 将错误写入到系统日志
	*/
	public DutyIterator(String conditionStr) throws SystemException {
		ArrayList tempDutys = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con = DbConnectionManager.getConnection();
			if ((conditionStr == null) || ("".equals(conditionStr))) {
				pstmt = con.prepareStatement(GET_DUTY);
				sql = GET_DUTY;
			} else {
				pstmt =
					con.prepareStatement(GET_DUTY + " WHERE " + conditionStr);
				sql = GET_DUTY + " WHERE " + conditionStr;
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tempDutys.add(new Integer(rs.getInt("ID")));
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in DutyIterator:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in DutyIterator:" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);

		}

		dutys = new int[tempDutys.size()];
		for (int i = 0; i < dutys.length; i++) {
			dutys[i] = ((Integer) tempDutys.get(i)).intValue();
		}
	}
	/**
	* 构造方法二：根据查询条件 ，从startIndex开始，获得num个Duty对象的列表。
	* 创建日期：(2003-3-25 10:24:42)
	* @param conditionStr 查询条件。
	* @param startIndex int 开始位置。
	* @param num int 要求返回对象个数。
	* @throws SystemException 将错误写入到系统日志
	*/
	public DutyIterator(String conditionStr, int startIndex, int num)
	throws SystemException {
		ArrayList tempDutys = new ArrayList();
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
				pstmt = con.prepareStatement(GET_DUTY);
			} else {
				pstmt =
					con.prepareStatement(GET_DUTY + " WHERE " + conditionStr);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (index >= (startIndex + num)) {
					break;
				}
				if (index >= startIndex) {
					tempDutys.add(new Integer(rs.getInt("ID")));
				}
				index++;
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in DutyIterator:constructor()-" + sqle;
			throw new SystemException(this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in DutyIterator:" + exception;
			throw new SystemException(this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);

		}
		dutys = new int[tempDutys.size()];
		for (int i = 0; i < dutys.length; i++) {
			dutys[i] = ((Integer) tempDutys.get(i)).intValue();
		}
	}
	/**
	 * 获取用户数目
	 * @return 数组长度
	 */
	public int getLength() {
		return dutys.length;
	}
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < dutys.length);
	}
	/**
	 * 返回下一个用户
	 * @return Duty
	 */
	public Object next() {
		Duty duty = null;
		currentIndex++;
		try {
			duty = DutyHome.findById(dutys[currentIndex]);
		} catch (Exception e) {
			System.err.println(e);
		}
		return duty;
	}
	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		int[] tempDutys;
		tempDutys = new int[dutys.length - 1];

		for (int i = 0; i < currentIndex; i++) {
			tempDutys[i] = dutys[i];
		}

		for (int i = currentIndex + 1; i < tempDutys.length; i++) {
			tempDutys[i - 1] = dutys[i];
		}
		dutys = tempDutys;
	}
}
