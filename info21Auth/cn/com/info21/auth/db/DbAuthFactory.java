// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DbAuthFactory.java

package cn.com.info21.auth.db;

import cn.com.info21.auth.*;

import cn.com.info21.system.*;
import cn.com.info21.authtools.*;
import cn.com.info21.database.*;
import java.sql.*;

public class DbAuthFactory extends AuthFactory {
	private String userTable = Info21Config.getProperty("userTable");
	private String AUTHORIZE =
		"SELECT * FROM WLuser WHERE usercode=? AND userpass=?";
	//private String AA = "SELECT * FROM sys_user1 WHERE username='admin' AND password='password'";
	private static final AuthToken anonymousAuth = new DbAuthToken(-1L);
	public boolean encryptPassword = true;
	public DbAuthFactory() {
		if (userTable == null || "".equals(userTable)) {
			userTable = "sys_user";
			AUTHORIZE =
				"SELECT * FROM WLuser WHERE usercode=? AND userpass=?";
		}
		try {
			if ("false"
				.equals(
					Info21Config
						.getProperty("encryptPassword")
						.toLowerCase())) {
				encryptPassword = false;
			}
		} catch (Exception e) {
		}
	}

	public AuthToken createAuthToken(String username, String password)
		throws UnauthorizedException {
		if (username == null || password == null)
			throw new UnauthorizedException();
		if (encryptPassword) {
			password = Encrypt.hash(password);
		}
		long userID = 0L;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(AUTHORIZE);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new UnauthorizedException(
					Encrypt.cnToEn("确认用户名和密码是否正确！"));
			}
			userID = rs.getLong(1);
			DbAuthToken authToken = new DbAuthToken(userID);
			authToken.setUserName(username);
			return authToken;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UnauthorizedException();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
			}
			try {
				con.close();
			} catch (Exception e) {
			}
		}
	}

	//采用系统验证方式时，可以调用此方法，不需要验证密码可以取得用户信息。
	public AuthToken createAuthToken(String username)
		throws UnauthorizedException {
		if (username == null)
			throw new UnauthorizedException();
		long userID = 0L;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt =
				con.prepareStatement(
					"SELECT id FROM " + userTable + " WHERE username=?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				//如果用户不在表中，把用户信息添加进去.
				pstmt =
					con.prepareStatement(
						"INSERT INTO "
							+ userTable
							+ "(id,username,password, createTime, status) "
							+ "VALUES(?,?,?,?,?)");
				userID = SequenceManager.nextID(3);
				pstmt.setLong(1, userID);
				pstmt.setString(2, username);
				//密码和用户名相同。
				pstmt.setString(3, Encrypt.hash(username));
				long now = System.currentTimeMillis();
				java.util.Date createDate = new java.util.Date(now);
				pstmt.setString(4, Encrypt.dateToMillis(createDate));
				pstmt.setString(5, "1");
				pstmt.executeUpdate();
			} else {
				userID = rs.getLong(1);
			}
			DbAuthToken authToken = new DbAuthToken(userID);
			authToken.setUserName(username);
			return authToken;
		} catch (SQLException e) {
			throw new UnauthorizedException();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
			}
			try {
				con.close();
			} catch (Exception e) {
			}
		}
	}

	public AuthToken createAnonymousAuthToken() {
		return anonymousAuth;
	}
}
