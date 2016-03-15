/*
 * Created on 2004-5-10
 */

package cn.com.shasha.sys;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

import cn.com.info21.authtools.Encrypt;
import cn.com.info21.database.*;
import cn.com.info21.system.*;
import cn.com.info21.util.*;
/**
 * @author liukh
 */

public class WLUser {
	public static final String TABLENAME = "WLuser";
	private static final String SET_USER_ROLE =
		"INSERT INTO SYS_USERROLE(ROLEID, USERNAME) VALUES(?, ?)";	
	private static final String GET_USER_ROLE =
		"SELECT ROLEID FROM SYS_USERROLE WHERE USERNAME=?";		
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE USERID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(userid,usercode,username,userpass,dsdm,userquan,userkebe,userstatus,usertype,telnum) VALUES(?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET usercode=?,username=?,userpass=?,dsdm=?,userquan=?,userkebe=?,userstatus=?,"
		 + "usertype=?,telnum=? WHERE userid=?";
	private String errorStr;

	private int userid = 0;
	private String usercode; 	//用户名
	private String username;  	//中文名
	private String userpass;  	//密码
	private String dsdm; 		//企业代码
	private String userquan; 	//权限
	private String userkebe; 	//税务机关
	private int	userstatus; 	//状态
	private String usertype; 		//类型
	private String telnum; 		//电话
	private String swjg_mc;
	public void setSwjg_mc(String swjg_mc) {
		this.swjg_mc = swjg_mc;
	}
	public String getSwjg_mc() {
		return swjg_mc;
	}
	/**
	 * 默认构造函数
	 */
	public WLUser() {
	}
	/**
	 * 构造函数二
	 * 根据ID号,从数据库中读取信息
	 * @param id 对象ID
	 * @exception SystemException 系统异常
	 */
	public WLUser(int userid) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_BY_ID);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.userid = userid;
				this.usercode = rs.getString("usercode");
				this.username = rs.getString("username");
				this.dsdm = rs.getString("dsdm");
				this.userpass = rs.getString("userpass");
				this.userquan = rs.getString("userquan");
				this.setUserkebe(rs.getString("userkebe"));
				this.userstatus = rs.getInt("userstatus");
				this.usertype = rs.getString("usertype");
				this.telnum = rs.getString("telnum");
				this.swjg_mc = rs.getString("swjg_mc");
			} else {
				errorStr = "Not found!";
            	throw new SystemException(errorStr);
            }
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			errorStr = "SQLException in WLUser:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			e.printStackTrace();
			errorStr = "Exception in WLUser:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 构造函数三
	 * 新建指定参数的对象并记录到数据库中
	 * @param username username
	 * @exception SystemException 系统异常
	 */
	public WLUser(String usercode,String username,String userpass,String dsdm,String userquan,String userkebe,int userstatus,String usertype,String telnum) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			this.userid = (int) SysFunction.getMaxID("wluser") + 1;
			pstmt = con.prepareStatement(INSERT_OBJ);
			int index=1;
			pstmt.setInt(index++, this.userid);
			pstmt.setString(index++, usercode);
			pstmt.setString(index++, username);
			
			if(userpass!=null || !userpass.equals("")) userpass = Encrypt.hash(userpass);
			
			pstmt.setString(index++, userpass);
			pstmt.setString(index++, dsdm);
			pstmt.setString(index++, userquan);
			pstmt.setString(index++, userkebe);
			pstmt.setInt(index++, userstatus);
			pstmt.setString(index++, usertype);
			pstmt.setString(index++, telnum);			
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in WLUser:constructor()-" + sqle;
			 sqle.printStackTrace();
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			exception.printStackTrace();
			errorStr = "Exception in WLUser:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 更新纪录
	 * @exception SystemException 系统异常
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_OBJ);
			pstmt.setString(1, this.usercode);
			pstmt.setString(2, this.username);
			if((this.userpass!=null || !this.userpass.equals("")) && this.userpass.length()!=32) this.userpass = Encrypt.hash(this.userpass);
			pstmt.setString(3, this.userpass);
			pstmt.setString(4, this.dsdm);
			pstmt.setString(5, this.userquan);
			pstmt.setString(6, this.userkebe);
			pstmt.setInt(7, this.userstatus);
			pstmt.setString(8, this.usertype);
			pstmt.setString(9, this.telnum);
			pstmt.setInt(10, this.userid);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			errorStr = "SQLException in WLUser.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			e.printStackTrace();
			errorStr = "Exception in WLUser.java:update(): " + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	* 冻结用户
	*/
	public void freeze() {
		this.userstatus = 2;
		try {
			this.update();
		} catch (Exception e) {
			//Log.error(e);
		}
	}

	/**
	* 解冻用户
	*/
	public void unfreeze() {
		this.userstatus = 1;
		try {
			this.update();
		} catch (Exception e) {
			e.printStackTrace();
			//Log.error(e);
		}
	}

	/**
	* 分配权限
	*/
	public void setPriv(String userquan) {
		this.userquan = userquan;
		try {
			this.update();
		} catch (Exception e) {
			e.printStackTrace();
			//Log.error(e);
		}
	}	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getDsdm() {
		return dsdm;
	}
	public void setDsdm(String dsdm) {
		this.dsdm = dsdm;
	}
	public String getUserquan() {
		return userquan;
	}
	public void setUserquan(String userquan) {
		this.userquan = userquan;
	}
	public String getUserkebe() {
		return userkebe;
	}
	public void setUserkebe(String userkebe) {
		if(null != userkebe && userkebe.length() >= 11 && "01".equals(userkebe.substring(9, 11))){
			userkebe = userkebe.substring(0,9) + "00";
		}
		this.userkebe = userkebe;
	}
	public int getUserstatus() {
		return userstatus;
	}
	public void setUserstatus(int userstatus) {
		this.userstatus = userstatus;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getTelnum() {
		return telnum;
	}
	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}
	//得到当前的销售单位名称
	public String getDwmc(){
		String dwmc="";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement("select dwmc from wlghdj where dsdm=?");
			pstmt.setString(1, this.dsdm);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dwmc = rs.getString("dwmc").trim();
			} else {
				errorStr = "Not found!";
            	throw new SystemException(errorStr);
            }
		} catch (SQLException sqle) {
			errorStr = "SQLException in WLUser:constructor()-" + sqle;
			sqle.printStackTrace();
		} catch (Exception e) {
			errorStr = "Exception in WLUser:constructor()-" + e;
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}		
		return dwmc;
	}
	//得到当前的开户角行和账号
	public String getBankInfo(){
		String bankinfo="";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement("select znum,bank from wlghdj where dsdm=?");
			pstmt.setString(1, this.dsdm);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bankinfo = rs.getString("bank").trim() + "$" + rs.getString("znum");
				
			} else {
				errorStr = "Not found!";
            	throw new SystemException(errorStr);
            }
		} catch (SQLException sqle) {
			errorStr = "SQLException in WLUser:constructor()-" + sqle;
			sqle.printStackTrace();
		} catch (Exception e) {
			errorStr = "Exception in WLUser:constructor()-" + e;
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}		
		return bankinfo;
	}
	//设置用户角色
	public void setRoles(String roles,String usercode) throws SystemException {
		try {
			SysFunction.delRecord(
				"SYS_USERROLE",
				"USERNAME ='" + usercode + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(SET_USER_ROLE);
			pstmt.setInt(1, Integer.parseInt(roles));
			pstmt.setString(2, usercode);
			pstmt.executeUpdate();
			pstmt.clearParameters();

		} catch (Exception e) {
			errorStr =
				"SQLException in User.java:setRoles(): " + e.getMessage();
			throw new SystemException(errorStr);
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
	//取用户角色
	public int  getRoles() throws SystemException {
		int roleid= 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(GET_USER_ROLE);
			pstmt.setString(1, this.usercode);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				roleid = rs.getInt("roleid");				
			}

		} catch (Exception e) {
			errorStr =
				"SQLException in User.java:getRoles(): " + e.getMessage();
			throw new SystemException(errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
		return roleid;
	}
	/**
	 * 将用户代码取出来
	 */	
	public String getUserDM(){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		String str_dm = "[";
		try {
			con = DbConnectionManager.getConnection();
			sql = "select userid,username from wluser";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				str_dm +="['"+rs.getString("userid").trim()+"','"+rs.getString("username").trim()+"'],";
			}			
			if(str_dm.length()>1)
			{
				str_dm = str_dm.substring(0, str_dm.length()-1);
			}
			str_dm +="]";
			//System.out.println(dmtype+"----"+str_dm);
		} catch (SQLException sqle) {
			System.err.println("SQLException in userDM (String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in userDM (String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}	
		return str_dm;
	}		
}
