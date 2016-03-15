// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DbAuthToken.java

package cn.com.info21.auth.db;
import cn.com.info21.auth.*;
import cn.com.info21.system.*;
import java.io.Serializable;

public final class DbAuthToken implements AuthToken, Serializable {

	protected DbAuthToken(long userID) {
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

	private static final long serialVersionUID = 1L;
	private long userID;
	private String userName = "";
}
