// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2004-5-17 14:22:16
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LdapAuthFactory.java

package cn.com.info21.auth.ldap;
import cn.com.info21.auth.*;
import cn.com.info21.org.*;
import javax.naming.directory.*;
// Referenced classes of package com.jivesoftware.base.ldap:
//			  LdapUser, LdapAuthToken, LdapManager

public class LdapAuthFactory extends AuthFactory {

	public LdapAuthFactory() {
		manager = LdapManager.getInstance();
	}

	public AuthToken createAuthToken(String username, String password)
		throws UnauthorizedException {
		long userID = -1L;
		String userDN = null;
		try {
			userDN = manager.findUserDN(username);
			if (!manager.checkAuthentication(userDN, password)) {
				throw new UnauthorizedException("Username and password don't match");
			}
		} catch (Exception e) {
			throw new UnauthorizedException(e);
		}
		DirContext ctx = null;
		try {
			ctx = manager.getContext();
			String attributes[] = { "jiveUserID" };
			Attributes attrs = ctx.getAttributes(userDN, attributes);
			if (attrs.get("jiveUserID") == null) {
				try {
					userID = UserHome.findByUserName(username).getId();
				} catch (Exception e) {
					userID = (UserHome.create(username)).getId();
				}
			} else {
				userID =
					Integer.parseInt((String) attrs.get("jiveUserID").get());
			}
		} catch (Exception e) {
			throw new UnauthorizedException(e);
		} finally {
			try {
				ctx.close();
			} catch (Exception exception1) {
			}
		}
		LdapAuthToken authToken = new LdapAuthToken(userID);
		authToken.setUserName(username);
		return authToken;
	}

	public AuthToken createAnonymousAuthToken() {
		return new LdapAuthToken(-1L);
	}

	private LdapManager manager;
}