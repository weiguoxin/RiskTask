package cn.com.info21.filter;
import cn.com.info21.auth.*;
import javax.servlet.*;
import javax.servlet.http.*;
import cn.com.info21.system.*;
import cn.com.info21.database.*;
import cn.com.info21.util.*;
import cn.com.shasha.sys.WLUser;

import java.sql.*;


public class LoginFilter implements Filter {
	protected FilterConfig filterConfig;
	java.util.List revokeList;
	String[] publicUrl = null;
	boolean filterUrl = false;
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		publicUrl = null;
		try {
		    publicUrl = StringUtils.explode(Info21Config.getProperty("publicUrl"), ";");
		    //System.out.println(publicUrl);
			if ("true"
				.equals(Info21Config.getProperty("filterUrl").toLowerCase())) {
				filterUrl = true;
			}
		} catch (Exception e) {
		}
	}
	public void destroy() {
//		this.filterConfig = null;
	}
	public void doFilter(
		ServletRequest request,
		ServletResponse response,
		FilterChain chain)
		throws java.io.IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		AuthToken authToken = null;
		try {
			authToken = AuthFactory.getAuthToken(req, res);
		} catch (UnauthorizedException ue) {
			ue.printStackTrace();
		}
		
		String reqstring = req.getQueryString(); //changed by xjm 050107
		String urls = "";
		if (reqstring == null) {
		    urls = req.getRequestURI()+req.getServletPath();
		} else {
		    urls = req.getRequestURI()+req.getServletPath() + "?" + reqstring;
		}
		if (authToken != null && null != req.getSession().getAttribute("Session_User")) {
			chain.doFilter(request, response);
			/*shashazhudiao
			 * 	if (authToken.isSuperAdmin() || (!filterUrl) || canAccess(
					authToken.getUserName(),
					urls)) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect(req.getContextPath() + "/noPopedom.jsp");
			}*/
		} else {
			String URI = req.getRequestURI();
			if (URI.indexOf("LoginForm.jsp") >= 0
				|| URI.indexOf("/res/") >= 0 
				|| URI.indexOf("CheckCode") >= 0
				|| URI.indexOf("shownotice.jsp") >= 0
				|| URI.indexOf("login") >= 0
				|| URI.indexOf("Logout") >= 0) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect(req.getContextPath() + "/LoginForm.jsp");
				HttpSession session = req.getSession();
				if (req.getQueryString() == null
					|| "".equals(req.getQueryString())) {
					session.setAttribute("refererURL", req.getRequestURI());
				} else {
					session.setAttribute(
						"refererURL",
						req.getRequestURI() + "?" + req.getQueryString());
				}
			}
		}
		
		
	}
	private boolean canAccess(String userName, String url) {
		boolean canAccess = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//1、判断当前URL是否属于公用访问URL。
		canAccess = this.isPublicUrl(url);
		//2、判断当前URL是否属于该用户有权限访问菜单相关URL。
		if (!canAccess) {
			try {
				con = DbConnectionManager.getConnection();
				pstmt =
					con.prepareStatement(
						"SELECT href, corHref FROM sys_menus WHERE id IN (SELECT menuId FROM sys_roleMenus WHERE roleid IN (SELECT roleid FROM org_user_role WHERE userid in (SELECT id FROM org_user WHERE username=?) AND NOT (href IS NULL)))");
				pstmt.setString(1, userName);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (url.indexOf(filterUrl(rs.getString("href"))) >= 0) {
						canAccess = true;
						break;
					}
					String[] resources =
						StringUtils.explode(rs.getString("corHref"), ";");
					for (int i = 0; i < resources.length; i++) {
						if (url.indexOf(filterUrl(resources[i])) >= 0) {
							canAccess = true;
							break;
						}
					}
				}
			} catch (Exception e) {
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		//3、判断当前URL是否属于该用户有权限操作的相关URL。
		if (!canAccess) {
			try {
				con = DbConnectionManager.getConnection();
				pstmt =
					con.prepareStatement(
						"SELECT \"RESOURCE\" FROM sys_menu_operation WHERE operation IN (SELECT POPEDOM FROM sys_rolemenus WHERE roleid IN (SELECT roleid FROM sys_userrole WHERE username=?))");
				pstmt.setString(1, userName);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					String[] resources =
						StringUtils.explode(rs.getString(1), ";");
					for (int i = 0; i < resources.length; i++) {
						if (url.indexOf(filterUrl(resources[i])) >= 0) {
							canAccess = true;
							break;
						}
					}
				}
			} catch (Exception e) {
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		return canAccess;
	}
	
	private String filterUrl(String url) {
		String tempUrl = url;
		if (tempUrl == null) {
			tempUrl = "";
		}
		tempUrl = StringUtils.replace(tempUrl, "../", "");
		tempUrl = StringUtils.replace(tempUrl, "./", "");
		tempUrl = StringUtils.replace(tempUrl, "*", "");
		if ("".equals(tempUrl.trim())) {
			tempUrl = "#$%##$";
		}
		return tempUrl;
	}
	
	public boolean isPublicUrl(String url) {
		boolean canAccess = false;
		//1、判断当前URL是否属于公用访问URL。
		try {
			for (int i = 0; i < publicUrl.length; i++) {
				if (publicUrl[i] != null && !"".equals(publicUrl[i])) {
					if (url.indexOf(filterUrl(publicUrl[i])) >= 0) {
						canAccess = true;
						//System.out.println(publicUrl[i]);
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		return canAccess;
		
	}
	
}
