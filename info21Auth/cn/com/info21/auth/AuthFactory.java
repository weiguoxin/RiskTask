package cn.com.info21.auth;

import cn.com.info21.util.*;
import cn.com.info21.auth.db.*;
import cn.com.info21.authtools.Blowfish;
import cn.com.info21.authtools.CookieUtils;

import javax.servlet.http.*;
import cn.com.info21.system.*;

public abstract class AuthFactory {
	
	public static final String SESSION_AUTHORIZATION = "info21.authToken";
	public static final String COOKIE_AUTOLOGIN = "info21.authToken.autologin";
	private static final long SECOND = 1000;
	private static final long MINUTE = 60 * SECOND;
	private static final long HOUR = 60 * MINUTE;
	private static final long DAY = 24 * HOUR;
	private static final long WEEK = 7 * DAY;
	private static final int MAX_COOKIE_AGE = (int) (WEEK / 1000) * 8;
	private static String className =
		"cn.com.info21.auth.db.DbAuthFactory";
	private static AuthFactory factory = null;
	private static Blowfish cipher = null;

	static {
		String keyString = Info21Config.getProperty("cookieKey");
		if (keyString == null) {
			keyString = StringUtils.randomString(15);
			Info21Config.setProperty("cookieKey", keyString);
		}
		cipher = new Blowfish(keyString);
	}
	
	public AuthFactory() {
	}

	public static AuthToken getAuthToken(String username, String password)
		throws UnauthorizedException {
		loadAuthFactory();
		return factory.createAuthToken(username, password);
	}

	public static AuthToken getAuthToken(
		HttpServletRequest request,
		HttpServletResponse response)
		throws UnauthorizedException {
		HttpSession session = request.getSession();
		String authType = Info21Config.getProperty("AuthType");
		AuthToken authToken = null;
		if ("server".equals(authType)) {
			authToken = (AuthToken) session.getAttribute(SESSION_AUTHORIZATION);
			if (authToken == null) {
				authToken =
					new DbAuthFactory().createAuthToken(
						request.getRemoteUser());
				session.setAttribute(SESSION_AUTHORIZATION, authToken);
			}
		} else {
			loadAuthFactory();
			authToken = factory.createAuthToken(request, response);
		}
		return authToken;
	}

	public static AuthToken getAnonymousAuthToken() {
		loadAuthFactory();
		return factory.createAnonymousAuthToken();
	}

	public static String encryptAuthInfo(String username, String password) {
		if (username == null || password == null)
			throw new NullPointerException("Username or password was null.");
		else
			return cipher.encryptString(username + '\002' + password);
	}

	public static String[] decryptAuthInfo(String value) {
		if (value == null || value.length() <= 0)
			return null;
		value = cipher.decryptString(value);
		if (value == null) {
			return null;
		} else {
			int pos = value.indexOf('\002');
			String username = pos >= 0 ? value.substring(0, pos) : "";
			String password = pos >= 0 ? value.substring(pos + 1) : "";
			return (new String[] { username, password });
		}
	}

	protected abstract AuthToken createAuthToken(String s, String s1)
		throws UnauthorizedException;

	protected AuthToken createAuthToken(
		HttpServletRequest request,
		HttpServletResponse response)
		throws UnauthorizedException {
		HttpSession session = request.getSession();
		AuthToken authToken =
			(AuthToken) session.getAttribute(SESSION_AUTHORIZATION);
		if (authToken != null)
			return authToken;
		Cookie cookie = CookieUtils.getCookie(request, COOKIE_AUTOLOGIN);
		if (cookie != null)
			try {
				String authInfo[] = decryptAuthInfo(cookie.getValue());
				if (authInfo != null) {
					String username = authInfo[0];
					String password = authInfo[1];
					authToken = getAuthToken(username, password);
					session.setAttribute(SESSION_AUTHORIZATION, authToken);
					return authToken;
				}
				CookieUtils.deleteCookie(response, cookie);
			} catch (UnauthorizedException ue) {
				CookieUtils.deleteCookie(response, cookie);
				throw ue;
			}
		return authToken;
	}
	public static AuthToken setAuthToken(
		HttpServletRequest request,
		HttpServletResponse response,
		String username,
		String password,
		boolean autoLogin)
		throws UnauthorizedException {
		HttpSession session = request.getSession();
		AuthToken authToken = null;
		try {
			authToken = getAuthToken(username, password);
			session.setAttribute(SESSION_AUTHORIZATION, authToken);
			if (autoLogin) {
				Cookie cookie =
					new Cookie(
						COOKIE_AUTOLOGIN,
						encryptAuthInfo(username, password));
				cookie.setMaxAge(MAX_COOKIE_AGE);
				response.addCookie(cookie);
			}
		} catch (Exception e) {
			throw new UnauthorizedException(e.getMessage());
		}
		return authToken;
	}
	protected abstract AuthToken createAnonymousAuthToken();
	public static void removeUserAuthorization(
		HttpServletRequest request,
		HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.removeAttribute(SESSION_AUTHORIZATION);
		Cookie cookie = new Cookie(COOKIE_AUTOLOGIN, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	private static void loadAuthFactory() {
		if (factory == null)
			synchronized (className) {
				if (factory == null) {
					String classNameProp =
						Info21Config.getProperty("AuthFactory.className");
					if (classNameProp != null)
						className = classNameProp;
					try {
						Class c = Class.forName(className);
						factory = (AuthFactory) c.newInstance();
					} catch (Exception e) {
					}
				}
			}
	}


}