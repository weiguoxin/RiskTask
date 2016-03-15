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
	 * ���캯��
	 * @param sql ��ѯ����
	 * @return Iterator
	 * @throws SystemException �˻����
	 */
	public static WLUserIterator findBySQL(String sql)
	throws SystemException {
		return WLUserIterator.findBySQL(sql);
	}
	/**
	 * ����������ȡ�û��б�
	 * @param conditionStr ��ѯ������
	 * @return UserIterator
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static WLUserIterator findByCondition(String conditionStr)
	throws SystemException {
		WLUserIterator userIterator = null;
		userIterator = new WLUserIterator(conditionStr);
		return userIterator;
	}
	/**
	 * ���ݲ�ѯ���� ����startIndex��ʼ�����num��User������б�
	 * @param conditionStr ��ѯ����
	 * @param startIndex ��ʼλ��
	 * @param num Ҫ�󷵻ض������
	 * @return UserIterator �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static WLUserIterator findByCondition(String conditionStr,
	int startIndex, int num) throws SystemException {
		WLUserIterator userIterator = null;
		userIterator = new WLUserIterator(conditionStr, startIndex, num);
		return userIterator;
	}
	/**
	 * �����û���� �����һ��User����
	 * @param id int �û����
	 * @return User �û�����
	 * @throws SystemException ������д�뵽ϵͳ��־
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
	 * ����һ���µ�User����
	 * @param username ������
	 * @return User ����
	 * @throws SystemException ������д�뵽ϵͳ��־
	 */
	public static WLUser create(String usercode,String username,String userpass,String dsdm,String userquan,String userkebe,int userstatus,String usertype,String telnum) throws SystemException {
		WLUser user = null;
		try {
			//�½��û�
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
	 * ���ݲ�ѯ���� ����÷���������User����ĸ���
	 * @param conditionStr ��ѯ����
	 * @return �û��������
	 */
	public static int getUserCount(String conditionStr) {
		return SysFunction.getCnt(WLUser.TABLENAME, conditionStr);
	}
	/**
	 * ɾ��һ��User����
	 * @param id �û����
	 * @throws SystemException ������д�뵽ϵͳ��־
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
			//ɾ����ɫ����Ա�Ķ�Ӧ��ϵ
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
