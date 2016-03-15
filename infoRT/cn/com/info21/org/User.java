/*
 * Created on 2004-5-10
 */

package cn.com.info21.org;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

import cn.com.info21.database.*;
import cn.com.info21.system.*;
import cn.com.info21.util.*;
/**
 * @author liukh
 */

public class User {
	public static final String TABLENAME = "ORG_USER";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,username) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET username=?,cnname=?,borndate=?,academy=?,mobile=?,status=?,tel=?,"
		 + "email=?,password=?,question=?,answer=?,createtime=?,lastacctime=?,"
		 + "freezetime=?,baseinfo=?,enable=?,usertype=?,hasverify=?,sn_seral=?,sn_password=? WHERE ID=?";
	private static final String SET_DEPT =
		"INSERT INTO ORG_USER_DEPT(userid,deptid,dutyid) VALUES(?, ?, ?)";
	private static final String SET_ROLE =
		"INSERT INTO ORG_USER_ROLE(userid,roleid) VALUES(?, ?)";
	private static final String HAS_PEPOM = 
	    "select userid from org_user_role a,sys_rolemenus b where a.userid=?" +
	    " and a.roleid = b.roleid and b.menuid=?";
	private String errorStr;

	private int id = 0;
	private String username; 	//用户名
	private String cnname;  	//中文名
	private Timestamp borndate;		//出生日期
	private String academy;  		//院校
	private String mobile; 		//手机号
	private int status; 		//状态
	private String tel; 			//电话
	private String email; 		//电邮
	private String password; 	//密码
	private String question; 	//问题
	private String answer; 		//回答
	private Timestamp createTime;	//创建时间
	private Timestamp lastAccTime;	//最新访问时间
	private Timestamp freezeTime;	//冻结时间
	private String baseinfo; 		//基本信息
	private int enable = 0;		//启用
	private int usertype = 0;	//用户类型

	private int deptID;			//会话用户的当前部门
	private int roleID;			//会话用户的当前角色
	private int dutyID;
	
	private int hasverify;
	private String sn_seral;
	private String sn_password;
	
	private User proxyuser = null;			//代办人员
	//shasha20090908修改
	private String userkebe;
	/**
	 * 默认构造函数
	 */
	public User() {
	}
	/**
	 * 构造函数二
	 * 根据ID号,从数据库中读取信息
	 * @param id 对象ID
	 * @exception SystemException 系统异常
	 */
	public User(int id) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id;
				this.username = rs.getString("username");
				this.cnname = rs.getString("cnname");
				this.borndate = rs.getTimestamp("borndate");
				this.academy = rs.getString("academy");
				this.mobile = rs.getString("mobile");
				this.status = rs.getInt("status");
				this.tel = rs.getString("tel");
				this.email = rs.getString("email");
				this.password = rs.getString("password");
				this.question = rs.getString("question");
				this.answer = rs.getString("answer");
				this.createTime = rs.getTimestamp("createTime");
				this.lastAccTime = rs.getTimestamp("lastAccTime");
				this.freezeTime = rs.getTimestamp("freezeTime");
				this.baseinfo = rs.getString("baseinfo");
				this.enable = rs.getInt("enable");
				this.usertype = rs.getInt("usertype");
				this.hasverify = rs.getInt("hasverify");
				this.sn_seral = rs.getString("sn_seral");
				this.sn_password = rs.getString("sn_password");
				this.userkebe = rs.getString("userkebe");
			} else {
				errorStr = "Not found!";
            	throw new SystemException(errorStr);
            }
		} catch (SQLException sqle) {
			errorStr = "SQLException in User:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in User:constructor()-" + e;
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
	public User(String username) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.username = username;
			con = DbConnectionManager.getConnection();
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setString(2, this.username);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in User:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in User:constructor()-" + exception;
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
			pstmt.setString(1, this.username);
			pstmt.setString(2, this.cnname);
			pstmt.setTimestamp(3, this.borndate);
			pstmt.setString(4, this.academy);
			pstmt.setString(5, this.mobile);
			pstmt.setInt(6, this.status);
			pstmt.setString(7, this.tel);
			pstmt.setString(8, this.email);
			pstmt.setString(9, this.password);
			pstmt.setString(10, this.question);
			pstmt.setString(11, this.answer);
			pstmt.setTimestamp(12, this.createTime);
			pstmt.setTimestamp(13, this.lastAccTime);
			pstmt.setTimestamp(14, this.freezeTime);
			pstmt.setString(15, this.baseinfo);
			pstmt.setInt(16, this.enable);
			pstmt.setInt(17, this.usertype);
			pstmt.setInt(18, this.hasverify);
			pstmt.setString(19, this.sn_seral);
			pstmt.setString(20, this.sn_password);
			pstmt.setInt(21, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			errorStr = "SQLException in User.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in User.java:update(): " + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	public boolean hasPopedom(String menuid) throws SystemException {
	    boolean rtn = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(HAS_PEPOM);
			pstmt.setInt(1,this.id);
			pstmt.setString(2, menuid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
			    rtn = true;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			errorStr = "SQLException in User.java:hasPopedom(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in User.java:hasPopedom(): " + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	    return rtn;
	}
	/**
	 * 获取部门集
	 * @return DeptIterator
	 * @throws SystemException 捕获系统错误
	 */
	public DeptIterator getDepts() throws SystemException {
		return DeptHome.findByCondition(
				"ID IN (SELECT deptid FROM ORG_USER_DEPT WHERE userid="
					+ this.id
					+ ")");
	}
	/**
	 * 获取userkebe
	 * @return DeptIterator
	 * @throws SystemException 捕获系统错误
	 */	
	public String getKebeName() throws SystemException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		String kebename = "";
		try {
			con = DbConnectionManager.getConnection();
			sql = "select * from dm902 where txdm='"+ this.userkebe +"'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				kebename = rs.getString("name");
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in kebename(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in kebename(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}	
		return kebename;
	}
	public int getKebeid() throws SystemException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		int kebeid = 0;
		String tkey = null;
		try {
			con = DbConnectionManager.getConnection();
			sql = "select * from dm902 where txdm='"+ this.userkebe +"'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				tkey = rs.getString("tkey");
				//System.out.println("ddd---"+tkey.substring(0, tkey.length()-1));
				kebeid =Integer.parseInt(tkey.split("_")[0]);
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in Kebeid(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in Kebeid(String): " + e);			
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}	
		return kebeid;
	}	
	/**
	 * 获取职务集
	 * @return DutyIterator
	 * @throws SystemException 捕获系统错误
	 */
	public DutyIterator getDutys() throws SystemException {
		return DutyHome.findByCondition(
				"ID IN (SELECT dutyid FROM ORG_USER_DEPT WHERE userid="
					+ this.id
					+ ")");
	}
	/**
	 * 设置部门
	 * @param deptids 部门ID
	 * @throws SystemException 捕获系统错误
	 */
	public void setDepts(int[] deptids,int[] dutyid) throws SystemException {
		try {
			SysFunction.delRecord(
				"ORG_USER_DEPT",
				"userid = " + this.id);
		} catch (Exception e) {
			System.err.println("Exception in setDepts(): " + e);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(SET_DEPT);
			for (int i = 0; i < deptids.length; i++) {
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, deptids[i]);
				pstmt.setInt(3, dutyid[i]);
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
		} catch (Exception e) {
			errorStr =
				"SQLException in User.java:setDepts(): " + e.getMessage();
			System.err.println(errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 获取角色集
	 * @return RoleIterator
	 * @throws SystemException 捕获系统错误
	 */
	public RoleIterator getRoles() throws SystemException {
		return RoleHome.findByCondition(
				"ID IN (SELECT roleid FROM ORG_USER_ROLE WHERE userid="
					+ this.id + ")");
	}
	/**
	 * 设置角色
	 * @param roleids 角色ID
	 * @throws SystemException 捕获系统错误
	 */
	public void setRoles(int[] roleids) throws SystemException {
		try {
			SysFunction.delRecord(
				"ORG_USER_ROLE",
				"userid = " + this.id);
		} catch (Exception e) {
			System.err.println("Exception in setRoles(): " + e);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(SET_ROLE);
			for (int i = 0; i < roleids.length; i++) {
				pstmt.setInt(1, this.id);
				pstmt.setInt(2, roleids[i]);
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
		} catch (Exception e) {
			errorStr =
				"SQLException in USER.java:setRoles(): " + e.getMessage();
			System.err.println(errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	
	/**
	 * @return Returns the academy.
	 */
	public String getAcademy() {
		return academy;
	}
	/**
	 * @param academy The academy to set.
	 */
	public void setAcademy(String academy) {
		this.academy = academy;
	}
	/**
	 * @return Returns the answer.
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer The answer to set.
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * @return Returns the createTime.
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime The createTime to set.
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the enable.
	 */
	public int getEnable() {
		return enable;
	}
	/**
	 * @param enable The enable to set.
	 */
	public void setEnable(int enable) {
		this.enable = enable;
	}
	/**
	 * @return Returns the freezeTime.
	 */
	public Timestamp getFreezeTime() {
		return freezeTime;
	}
	/**
	 * @param freezeTime The freezeTime to set.
	 */
	public void setFreezeTime(Timestamp freezeTime) {
		this.freezeTime = freezeTime;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the tel.
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel The telp to set.
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return Returns the lastAccTime.
	 */
	public Timestamp getLastAccTime() {
		return lastAccTime;
	}
	/**
	 * @param lastAccTime The lastAccTime to set.
	 */
	public void setLastAccTime(Timestamp lastAccTime) {
		this.lastAccTime = lastAccTime;
	}
	/**
	 * @return Returns the mobile.
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile The mobile to set.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return Returns the cnname.
	 */
	public String getCNName() {
		return this.cnname;
	}
	/**
	 * @param cnname The name to set.
	 */
	public void setCNName(String cnname) {
		this.cnname = cnname;
	}
	/**
	 * @return Returns the borndate
	 */
	public Timestamp getBorndate() {
		return this.borndate;
	}
	/**
	 * @param borndate The borndate to set
	 */
	public void setBorndate(Timestamp borndate) {
		this.borndate = borndate;
	}
	/**
	 * @return Returns the pwd.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param pwd The pwd to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the question.
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question The question to set.
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return Returns the baseinfo.
	 */
	public String getBaseinfo() {
		return baseinfo;
	}
	/**
	 * @param baseinfo The baseinfo to set.
	 */
	public void setBaseinfo(String baseinfo) {
		this.baseinfo = baseinfo;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return Returns the username.
	 */
	public String getUserName() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUserName(String username) {
		this.username = username;
	}
	/**
	 * @return Returns the usertype
	 */
	public int getUserType() {
		return this.usertype;
	}
	/**
	 * @param usertype The usertype to set
	 */
	public void setUserType(int usertype) {
		this.usertype = usertype;
	}
	public int getDeptID() {
		return deptID;
	}
	/**
	 * @param deptID The deptID to set.
	 */
	public void setDeptID(int deptID) {
		this.deptID = deptID;
	}
	/**
	 * @return Returns the roleID.
	 */
	public int getRoleID() {
		return roleID;
	}
	/**
	 * @param roleID The roleID to set.
	 */
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	/**
	 * @return Returns the dutyID.
	 */
	public int getDutyID() {
		return dutyID;
	}
	/**
	 * @param dutyID The dutyID to set.
	 */
	public void setDutyID(int dutyID) {
		this.dutyID = dutyID;
	}
	/**
	 * @return the proxy
	 */
	public User getProxyUser() {
	    return this.proxyuser;
	}
	/** 
	 * @param proxyuser the proxyuser to set
	 */
	public void setProxyUser(User proxyuser) {
	    this.proxyuser = proxyuser;
	}
	/**
	 * @return Returns the sn_seral.
	 */
	public String getSn_seral() {
		return sn_seral;
	}
	/**
	 * @param sn_seral The sn_seral to set.
	 */
	public void setSn_seral(String sn_seral) {
		this.sn_seral = sn_seral;
	}
	/**
	 * @return Returns the sn_password.
	 */
	public String getSn_password() {
		return sn_password;
	}
	/**
	 * @param sn_password The sn_password to set.
	 */
	public void setSn_password(String sn_password) {
		this.sn_password = sn_password;
	}
	/**
	 * @return Returns the hasverify.
	 */
	public int getHasverify() {
		return hasverify;
	}
	/**
	 * @param hasverify The hasverify to set.
	 */
	public void setHasverify(int hasverify) {
		this.hasverify = hasverify;
	}
	
	public String getUserkebe() {
		return userkebe;
	}
	public void setUserkebe(String userkebe) {
		this.userkebe = userkebe;
	}
	public static boolean reader(String s) 
    	throws IOException, SystemException {
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(s)));
    String data = null;
    User user = null;
    while((data = br.readLine())!=null) {
    	String[] str = data.split(",");
    	user = UserHome.create(str[0]);
    	if (null!=user) {
    		user.setUserName(str[0]);
    		user.setCNName(str[1]);
    		user.setBorndate(StringUtils.toTimestamp(str[2],1));
    		user.setAcademy(str[3]);
    		user.setMobile(str[4]);
    		user.setStatus(Integer.parseInt(str[5]));
    		user.setTel(str[6]);
    		user.setEmail(str[7]);
    		user.setPassword(str[8]);
    		user.setQuestion(str[9]);
    		user.setAnswer(str[10]);
    		user.setUserType(Integer.parseInt(str[11]));
    		user.setBaseinfo(str[12]);
    		user.update();
    	}
    	
    }
    br.close();
    return true;
    } 
}
