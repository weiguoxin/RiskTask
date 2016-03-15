/*
 * Created on 2004-6-19
 */

package cn.com.shasha.sys;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import cn.com.info21.auth.AuthToken;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author liukh
 */

public class WLUserHome {
	/**
	 * 构造函数
	 * @param sql 查询条件
	 * @return Iterator
	 * @throws SystemException 扑获错误
	 */
	public static WLUserIterator findBySQL(String sql)
	throws SystemException {
		return WLUserIterator.findBySQL(sql);
	}
	/**
	 * 根据条件获取用户列表
	 * @param conditionStr 查询条件。
	 * @return UserIterator
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static WLUserIterator findByCondition(String conditionStr)
	throws SystemException {
		WLUserIterator userIterator = null;
		userIterator = new WLUserIterator(conditionStr);
		return userIterator;
	}
	/**
	 * 根据查询条件 ，从startIndex开始，获得num个User对象的列表
	 * @param conditionStr 查询条件
	 * @param startIndex 开始位置
	 * @param num 要求返回对象个数
	 * @return UserIterator 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static WLUserIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		WLUserIterator userIterator = null;
		userIterator = new WLUserIterator(conditionStr, startIndex, num);
		return userIterator;
	}
	/**
	 * 根据用户编号 ，获得一个User对象
	 * @param id int 用户编号
	 * @return User 用户对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static WLUser findById(int userid,HttpSession session,AuthToken authToken) throws SystemException {
		WLUser user = new WLUser(userid);
		if(null != session){
			session.setAttribute("Session_User", user);
			session.setAttribute("Session_isSuperAdmin",authToken.isSuperAdmin());
		}
		return user;
	}
	public static WLUser findById(int userid) throws SystemException {
		WLUser user = new WLUser(userid);
		return user;
	}
	/**
	 * 创建一个新的User对象
	 * @param username 对象名
	 * @return User 对象
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static WLUser create(String usercode,String username,String userpass,String dsdm,String userquan,String userkebe,int userstatus,String usertype,String telnum) throws SystemException {
		WLUser user = null;
		try {
			//新建用户
			user = new WLUser(usercode,username,userpass,dsdm,userquan,userkebe,userstatus,usertype,telnum);
		} catch (SystemException se) {
			System.out.println("WLUser create error in WLUserHome");
			try {
				if (user != null) {
					WLUserHome.remove(user.getUserid());
				}
			} catch (Exception e2) {
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("WLUser create error in WLUserHome");
			try {
				if (user != null) {
					WLUserHome.remove(user.getUserid());
				}
			} catch (Exception e2) {
				System.out.println();
			}
		}
		return  user;
	}
	/**
	 * 根据查询条件 ，获得符合条件的User对象的个数
	 * @param conditionStr 查询条件
	 * @return 用户对象个数
	 */
	public static int getUserCount(String conditionStr) {
		return SysFunction.getCnt(WLUser.TABLENAME, conditionStr);
	}
	/**
	 * 删除一个User对象
	 * @param id 用户编号
	 * @throws SystemException 将错误写入到系统日志
	 */
	public static void remove(int userid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		String delsql = "";
		String errStr="";
		try {
			con = DbConnectionManager.getConnection();
			delsql = "DELETE FROM WLuser WHERE USERID = " + userid;
			pstmt = con.prepareStatement(delsql);
			pstmt.executeUpdate();
			WLUser wluser = WLUserHome.findById(userid);
			//删除角色和人员的对应关系
			String conditionStr = " USERNAME = '" + wluser.getUsercode().trim() + "'";
			SysFunction.delRecord("SYS_USERROLE", conditionStr);			
		} catch (SQLException sqle) {
			errStr = "SQLException in Same.java:delRecord(): " + sqle;
			//Log.error(errStr);
		} catch (Exception e) {
			errStr = "Exception in User.java:delete(): " + e;
			//Log.error(errStr);
		} finally {
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
	}

}
