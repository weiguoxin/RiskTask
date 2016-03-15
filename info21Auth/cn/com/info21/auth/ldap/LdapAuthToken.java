// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2004-5-17 15:45:19
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LdapAuthToken.java

package cn.com.info21.auth.ldap;
import cn.com.info21.system.*;
import cn.com.info21.auth.*;
import java.io.Serializable;

public class LdapAuthToken implements AuthToken, Serializable {

	protected LdapAuthToken(long userID) {
		this.userID = userID;
	}

	public long getUserID() {
		return userID;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		userName = name;
	}
	public boolean isAnonymous() {
		return userID == -1L;
	}
	public boolean isSuperAdmin() {
		return userName.equals(Info21Config.getProperty("superAdmin"));
	}
	private long userID;
	private String userName = "";
}