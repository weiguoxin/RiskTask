// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2004-5-17 13:45:30
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LdapManager.java

package cn.com.info21.auth.ldap;

import cn.com.info21.system.*;
import cn.com.info21.auth.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;

public class LdapManager {

	public static LdapManager getInstance() {
		return instance;
	}

	private LdapManager() {
		port = 389;
		usernameField = "uid";
		nameField = "cn";
		emailField = "mail";
		baseDN = "";
		mode = 0;
		debugEnabled = false;
		host = Info21Config.getProperty("ldap.host");
		String portStr = Info21Config.getProperty("ldap.port");
		if (portStr != null)
			try {
				port = Integer.parseInt(portStr);
			} catch (NumberFormatException numberformatexception) {
			}
		if (Info21Config.getProperty("ldap.usernameField") != null)
			usernameField = Info21Config.getProperty("ldap.usernameField");
		if (Info21Config.getProperty("ldap.baseDN") != null)
			baseDN = Info21Config.getProperty("ldap.baseDN");
		adminDN = Info21Config.getProperty("ldap.adminDN");
		adminPassword = Info21Config.getProperty("ldap.adminPassword");
		debugEnabled =
			"true".equals(Info21Config.getProperty("ldap.debugEnabled"));
		String modeStr = Info21Config.getProperty("ldap.mode");
		if (modeStr != null)
			try {
				mode = Integer.parseInt(modeStr);
			} catch (NumberFormatException numberformatexception1) {
			}
	}

	public DirContext getContext() throws NamingException {
		Hashtable env = new Hashtable();
		env.put(
			"java.naming.factory.initial",
			"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(
			"java.naming.provider.url",
			"ldap://" + host + ":" + port + "/" + baseDN);
		env.put("java.naming.security.authentication", "simple");
		if (adminDN != null)
			env.put("java.naming.security.principal", adminDN);
		if (adminPassword != null)
			env.put("java.naming.security.credentials", adminPassword);
		if (debugEnabled)
			env.put("com.sun.jndi.ldap.trace.ber", System.err);
		return new InitialDirContext(env);
	}

	public boolean checkAuthentication(String userDN, String password) {
		DirContext ctx = null;
		;
		try {
			Hashtable env = new Hashtable();
			env.put(
				"java.naming.factory.initial",
				"com.sun.jndi.ldap.LdapCtxFactory");
			env.put(
				"java.naming.provider.url",
				"ldap://" + host + ":" + port + "/" + baseDN);
			env.put("java.naming.security.authentication", "simple");

			if (userDN.indexOf(baseDN) > 0) {
				env.put("java.naming.security.principal", userDN);
			} else {
				env.put(
					"java.naming.security.principal",
					userDN + "," + baseDN);
			}
			env.put("java.naming.security.credentials", password);
			if (debugEnabled)
				env.put("com.sun.jndi.ldap.trace.ber", System.err);
			ctx = new InitialDirContext(env);
		} catch (NamingException namingexception) {
			boolean flag = false;
			return flag;
		} finally {
			try {
				ctx.close();
			} catch (Exception exception1) {
			}
		}
		return true;
	}

	public String findUserDN(String username) throws Exception {
		DirContext ctx = null;
		try {
			ctx = getContext();
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(2);
			constraints.setReturningAttributes(new String[] { usernameField });
			StringBuffer filter = new StringBuffer();
			filter.append("(").append(usernameField).append("=");
			filter.append(username).append(")");
			NamingEnumeration answer =
				ctx.search("", filter.toString(), constraints);
			if (answer == null || !answer.hasMoreElements())
				throw new UnauthorizedException(
					"Username " + username + " not found");
			String userDN = ((SearchResult) answer.next()).getName();
			if (answer.hasMoreElements())
				throw new Exception(
					"LDAP username lookup for "
						+ username
						+ " matched multiple entries.");
			String s = userDN;
			return s;
		} finally {
			try {
				ctx.close();
			} catch (Exception exception1) {
			}
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		Info21Config.setProperty("ldap.host", host);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
		Info21Config.setProperty("ldap.port", "" + port);
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	public void setDebugEnabled(boolean enabled) {
		debugEnabled = enabled;
		Info21Config.setProperty("ldap.debuggingEnabled", "" + debugEnabled);
	}

	public String getUsernameField() {
		return usernameField;
	}

	public void setUsernameField(String usernameField) {
		this.usernameField = usernameField;
		if (usernameField == null)
		Info21Config.deleteProperty("ldap.usernameField");
		else
		Info21Config.setProperty("ldap.usernameField", usernameField);
	}

	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
		if (nameField == null)
		Info21Config.deleteProperty("ldap.nameField");
		else
		Info21Config.setProperty("ldap.nameField", nameField);
	}

	public String getEmailField() {
		return emailField;
	}

	public void setEmailField(String emailField) {
		this.emailField = emailField;
		if (emailField == null)
		Info21Config.deleteProperty("ldap.emailField");
		else
		Info21Config.setProperty("ldap.emailField", emailField);
	}

	public String getBaseDN() {
		return baseDN;
	}

	public void setBaseDN(String baseDN) {
		this.baseDN = baseDN;
		Info21Config.setProperty("ldap.baseDN", baseDN);
	}

	public String getAdminDN() {
		return adminDN;
	}

	public void setAdminDN(String adminDN) {
		this.adminDN = adminDN;
		Info21Config.setProperty("ldap.adminDN", adminDN);
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
		Info21Config.setProperty("ldap.adminPassword", adminPassword);
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
		Info21Config.setProperty("ldap.mode", "" + mode);
	}

	public static final int ALL_LDAP_MODE = 0;
	public static final int LDAP_DB_MODE = 1;
	private String host;
	private int port;
	private String usernameField;
	private String nameField;
	private String emailField;
	private String baseDN;
	private String adminDN;
	private String adminPassword;
	private int mode;
	private boolean debugEnabled;
	private static LdapManager instance = new LdapManager();
}