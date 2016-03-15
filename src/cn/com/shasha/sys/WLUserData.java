package cn.com.shasha.sys;


import cn.com.info21.util.ParameterUtils;



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wgx.util.json.JSONArray;
import com.wgx.util.json.JSONObject;


import java.util.*;

import cn.com.shasha.services.TasksData;
import cn.com.shasha.utils.*;

import cn.com.info21.auth.AuthFactory;
import cn.com.info21.auth.AuthToken;
import cn.com.info21.auth.UnauthorizedException;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;
public class WLUserData extends HttpServlet {
	WLUser nowuser = null;
	public void init(ServletConfig config) throws ServletException {
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		nowuser = ((WLUser)request.getSession().getAttribute("Session_User"));
		//System.out.println("usercode--"+nowuser.getUsercode());
		String sReturnStr = "";
		String sAction = request.getParameter("action");
		//System.out.println("sAction:" + sAction);
		if (sAction.equalsIgnoreCase("login")) {
			try {
				sReturnStr = login(request,response);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (sAction.equalsIgnoreCase("show")) {
			sReturnStr = show(request);
		} else if (sAction.equalsIgnoreCase("delete")) {
			sReturnStr = deleteInfo(request);
		} else if (sAction.equalsIgnoreCase("new")) {
			sReturnStr = this.addNew(request, 1);
		} else if (sAction.equalsIgnoreCase("edit")) {
			sReturnStr = this.addNew(request, 2);
		} else if (sAction.equalsIgnoreCase("getform")) {
			sReturnStr = getUserById(request);
		} else if (sAction.equalsIgnoreCase("filterform")) {
			sReturnStr = filterRecord(request);
		}else if (sAction.equalsIgnoreCase("liveuser")) {
			sReturnStr = this.LiveUser(request);
		}else if (sAction.equalsIgnoreCase("addRole")) {
			sReturnStr = this.ADDUserRole(request);
		}else if (sAction.equalsIgnoreCase("getRole")) {
			sReturnStr = this.GetUserRole(request);
		}else if (sAction.equalsIgnoreCase("shownotice")) {
			sReturnStr = noticeInfo(request);
		}else if (sAction.equalsIgnoreCase("newnotice")) {
			sReturnStr = this.addNoticeNew(request, 1);
		} else if (sAction.equalsIgnoreCase("editnotice")) {
			sReturnStr = this.addNoticeNew(request, 2);
		}else if (sAction.equalsIgnoreCase("delnotice")) {
			sReturnStr = delNoticeInfo(request);
		}else if (sAction.equalsIgnoreCase("getnotice")) {
			sReturnStr = getNoticeInfo(request);
		} else if (sAction.equalsIgnoreCase("getUserRoleItems")) {
			sReturnStr = getUserRoleItems(request);
		} else if (sAction.equalsIgnoreCase("getUserByTaskId")) {
			sReturnStr = getUserByTaskId(request);
		}
		
		PrintWriter out = response.getWriter();
//System.out.println(sReturnStr);
		out.print(sReturnStr);
		out.flush();
		out.close();
	}


	/**
	 * 查询可执行任务人
	 * @param request
	 * @return
	 */
	private String getUserByTaskId(HttpServletRequest request) {
		String id = request.getParameter("taskId");
		String swjg_dm = request.getParameter("swjg_dm");
		String _dc = request.getParameter("_dc");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONObject o = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement("select a.* from wluser a,tasks b ,dm_gy_swjg c "+
						"where b.id=? and (b.zx_swjg_dm=c.swjg_dm) and CHARINDEX('3', a.usertype)>0 and a.userquan like '%'+b.fxzb_dm+'%'  "+
						"and  a.userkebe  like ?");
			pstmt.setString(1, id);
			pstmt.setString(2, TasksData.getSwjg_Like_Dm(swjg_dm,false)+"%");
//System.out.println(TasksData.getSwjg_Like_Dm(swjg_dm));
			rs = pstmt.executeQuery();
			while(rs.next()){
				JSONObject jo = new JSONObject();
				jo.put("id", rs.getString("userid"));
				jo.put("username", rs.getString("username"));
				ja.put(jo);
			}
			if(ja.length() == 0){
				JSONObject jo = new JSONObject();
				jo.put("id", "-1");
				jo.put("username", "没有可选工作人员.");
				ja.put(jo);
			}
			o.put("data", ja);
			o.put("success", true);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
		return o.toString();
	}

	private String login(HttpServletRequest request,HttpServletResponse response) throws IOException, SystemException {
		String username = ParameterUtils.getParameter(request,"username");
		String password = ParameterUtils.getParameter(request,"password");
		HttpSession session = request.getSession();
		boolean autoLogin = ParameterUtils.getBooleanParameter(request,"autoLogin");
		boolean doLogin = request.getParameter("login") != null;
		boolean doContinue = request.getParameter("continue") != null;
		Boolean errors = false;
		String referringPage = (String)session.getAttribute("refererURL");
		AuthToken authToken = null;
		boolean canTryAutoLogin = false;
		try {
			authToken = AuthFactory.getAuthToken(request, response);
		        canTryAutoLogin = true;
		} catch (UnauthorizedException ue) {}
		if (ParameterUtils.getBooleanParameter(request,"login")) {
			try {
		            	authToken = AuthFactory.setAuthToken(request, response, username, password, autoLogin);
			} catch (UnauthorizedException ue) {
		            	errors = true;
			}
		}
		if (!errors && (doLogin || doContinue)) {
			WLUser wluser = WLUserHome.findById((int)authToken.getUserID());
			session.setAttribute("Session_User", wluser);
			session.setAttribute("Session_isSuperAdmin",authToken.isSuperAdmin());
			referringPage = request.getContextPath() + "/index.jsp";
			response.sendRedirect("index.jsp");	
		}else{
			response.sendRedirect("LoginForm.jsp?errors="+errors);	
		}
		return null;
	}

	/**
	 * 用于加载数据
	 * d
	 */
	private String show(HttpServletRequest request) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		//String _dc = request.getParameter("_dc");
		String type = request.getParameter("type");
		String swjg_dm = request.getParameter("swjg_dm");
		//String fxzb_dm = request.getParameter("fxzb_dm");
		String swry_name = request.getParameter("swry_name");
		StringBuffer strWhere = new StringBuffer();
		
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;
		
		strWhere.append(" usercode<>'admin' ");
		if(!"admin".equals(nowuser.getUsercode())){
			strWhere.append(" and userkebe like '" + TasksData.getSwjg_Like_Dm(nowuser.getUserkebe(),true) + "%'");
		}
		if(null != swjg_dm && !"".equals(swjg_dm)){
			strWhere.append(" and userkebe like '" + TasksData.getSwjg_Like_Dm(swjg_dm,false) + "%'");
		}
//		if(null != fxzb_dm && !"".equals(fxzb_dm)){
//			strWhere.append(" and userquan like '%" + fxzb_dm + "%' ");
//		}
		if(null != type && !"".equals(type)){
			strWhere.append(" and CHARINDEX('"+type+"',usertype)>0 ");
		}
		if(null != swry_name && !"".equals(swry_name)){
			strWhere.append(" and username like '%" + swry_name + "%'");
		}
		
//sSystem.out.println(strWhere);
		DataCenter odata = new DataCenter();
		String sJson = odata.ResultSetToJsonForShow("WLUSER",
				"userid,usercode,username,userkebe,usertype,userquan,userstatus,telnum,swjg_mc", "userid", pageSize, index,
				true, strWhere.toString());
		// System.out.println("User show json--"+sJson);
		return sJson;
	}

	/**
	 * 用于增加数据 addtype用来标识是更新还是增加 1代表add 2代表update,usertype用来区分是企业用户还是相关部门用户
	 */
	private String addNew(HttpServletRequest request, int addtype) {
		String sRetHTML = "";
		String id = request.getParameter("userid");
		String userCode = request.getParameter("usercode");
		String userName =request.getParameter("username");//用户名;
		String userPass =request.getParameter("uerPass1");//用户密码;
		String userrole = request.getParameter("userrole");
        String userquan = request.getParameter("userquan");
        String userkebe = request.getParameter("userkebe");
        String usertype = request.getParameter("usertype");

		WLUser user = null;
		try {
			if (addtype == 1) {
				// 新文档保存
				//检查是否存在该用户名
				if (SysFunction.getCnt("WLUSER", "usercode = '" + userCode + "'")
					> 0) {
					sRetHTML = "{success:false,because:'该用户名已存在!'}";
				}else
				{
					//相关部门的默认kebe设置为00
					user = WLUserHome.create(userCode, userName, userPass, "", userquan, userkebe, 1, usertype, "");
					if (userrole != null && !userrole.equals("")) {
						user.setRoles(userrole,userCode);
					}			
					sRetHTML = "{success:true}";
				}
			} else {
				user = WLUserHome.findById(Integer.parseInt(id));
				sRetHTML = "{success:true}";
				if (null != user) {
					//userCode不允许修改；
					//user.setUsercode(userCode);
					user.setUsername(userName);
					//userPass 如果为空,则不传值
					if(userPass!=null && !userPass.equals("")) user.setUserpass(userPass);				
					user.setTelnum("");
					user.setUserquan(userquan);
					user.setUserkebe(userkebe);
					user.setUsertype(usertype);
					user.update();
					//user.setRoles(userrole,user.getUsercode().trim());					
				}				
			}

		} catch (Exception e) {
			// TODO: handle exception
			// 0表示失败
			e.printStackTrace();
			sRetHTML = "{success:false,because:''}";
			e.getMessage();
		}
		return sRetHTML;
	}

	/**
	 * 用于删除记录
	 */
	private String deleteInfo(HttpServletRequest request) {
		String sRetHTML = "";
		String[] selectitems = request.getParameter("delid").split("-");
		
		try {
			sRetHTML = String.valueOf(WLUserHome.getUserCount("1=1"));
			for (int i = 0; i < selectitems.length; i++) {				
				WLUserHome.remove(Integer.parseInt(selectitems[i]));				
			}
			sRetHTML = "1|" + sRetHTML;
		} catch (Exception e) {
			// TODO: handle exception
			sRetHTML = "0|" + sRetHTML;
			e.getMessage();
		}		
		return sRetHTML;
	}
	/**
	 * 用于激活或冻结用户
	 */
	private String LiveUser(HttpServletRequest request) {
		String sRetHTML = "";

		String[] selectitems = request.getParameter("userid").split("-");
		int type = ParameterUtils.getIntParameter(request, "type", 0);
		try {
			if (type == 1) {
				for (int i = 0; i < selectitems.length; i++) {
					WLUser user = WLUserHome.findById(Integer
							.parseInt(selectitems[i]));
					user.unfreeze();
				}
			}
			if (type == 2) {
				for (int i = 0; i < selectitems.length; i++) {
					WLUser user = WLUserHome.findById(Integer
							.parseInt(selectitems[i]));
					user.freeze();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return sRetHTML;	
	}		
	/**
	 * 分配权限
	 */
	private String ADDUserRole(HttpServletRequest request) {
		String sRetHTML = "";

		String[] selectitems = request.getParameter("userid").split("-");
		String userquan = request.getParameter("menuId");
		try {
			for (int i = 0; i < selectitems.length; i++) {
				WLUser user = WLUserHome.findById(Integer
						.parseInt(selectitems[i]));
				user.setPriv(userquan);
			}
			sRetHTML = "{success:true}";
		} catch (Exception e) {
			// TODO: handle exception
			sRetHTML = "{success:false}";
			e.getMessage();
		}
		return sRetHTML;	
	}	
	/**
	 * 查询form查询数据
	 */
	private String filterRecord(HttpServletRequest request) {
		String sJson = "";
		String usercode = request.getParameter("usercode");
		String username =request.getParameter("username");

		String strWhere = "1=1 ";
		if (usercode != null && !"".equals(usercode.trim())) {
			strWhere += "and usercode like '%" + usercode + "%' ";
		}

		if (username != null && !"".equals(username.trim())) {
			strWhere += "and username like '%" + username + "%' ";
		}	
	

		//System.out.println(strWhere);
		sJson = "{'success':true,'strWhere':\"" + strWhere + "\"}";
		return sJson;
	}


	/**
	 * 根据id返回Form信息sjon格式的
	 * 
	 */
	private String getUserById(HttpServletRequest request) {
		String id = request.getParameter("userid");
		String sJson = "";
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForCombox("WLuser", "userid,usercode,username,usertype,telnum,userquan,userkebe,userstatus", "userid=" + id, 0,
				"data");

		//添加用户角色
		WLUser employee = null;
		//该系统一个用户只能属于一个角色
		try {
			employee = WLUserHome.findById(Integer.parseInt(id));
			sJson = sJson.substring(0, sJson.length()-3) + ",userrole:'" + (employee.getRoles()==0?"":employee.getRoles())+ "'}]}";			
		} catch (Exception e) {
			// TODO: handle exception
		}			
		return sJson;
	}
	
	
	/**
	 * 列出所有的角色名称
	 * 
	 */
	private String GetUserRole(HttpServletRequest request) {

		String sJson = "";
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForCombox("SYS_ROLES", "id,name", "1=1 order by id", 0,
				"data");		
		return sJson;
	}	
	public static java.util.Date toDate(String s) {
		java.util.Date rtn = null;
		if (s != null && !"".equals(s.trim())) {
			SimpleDateFormat dtformat = new SimpleDateFormat("yyyy年MM月dd日",
					Locale.getDefault());
			try {
				rtn = dtformat.parse(s);
			} catch (Exception e) {
				rtn = null;
				e.printStackTrace();
			}
		}
		return rtn;
	}
	/**
	 * 用于加载系统通知公告数据
	 * d
	 */
	private String noticeInfo(HttpServletRequest request) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		
		String sJson = "";
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;

		//System.out.println("strWhere" + strWhere);
		DataCenter odata = new DataCenter();

		sJson = odata.ResultSetToJsonForShow("sysnotice",
				"id,title,createdate,notice", "id", pageSize, index,
				true, "");
		return sJson;
	}	
	
	/**
	 * 用于编辑和修改系统通知公告数据
	 * d
	 */
	private String addNoticeNew(HttpServletRequest request, int addtype) {
		String sRetHTML = "";
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String doccontent =request.getParameter("doccontent");
		String notice =request.getParameter("notice");
		Article at = null;
		try {
			if (addtype == 1) {
				// 新文档保存
				at = ArticleHome.create(title);
				id = String.valueOf(at.getId());
				// 第一个1表示成功
				sRetHTML = "{success:true}";
			} else {
				at = ArticleHome.findById(Integer.parseInt(id));
				sRetHTML = "{success:true}";
			}
			//System.out.println(null != at);
			
			if (null != at) {
				
				at.setTitle(title);
				at.setDoccontent(doccontent);
				at.setNotice(notice);
				at.update();
			}
		} catch (Exception e) {
			// TODO: handle exception
			// 0表示失败
			e.printStackTrace();
			sRetHTML = "{success:false,because:''}";
			e.getMessage();
		}
		return sRetHTML;
	}
	/**
	 * 用于删除系统通知公告数据
	 * d
	 */
	private String delNoticeInfo(HttpServletRequest request) {
		String sRetHTML = "";
		String[] selectitems = request.getParameter("delid").split("-");
		
		try {
			sRetHTML = String.valueOf(ArticleHome.getArticleCount("1=1"));
			for (int i = 0; i < selectitems.length; i++) {				
				ArticleHome.remove(Integer.parseInt(selectitems[i]));				
			}
			sRetHTML = "1|" + sRetHTML;
		} catch (Exception e) {
			// TODO: handle exception
			sRetHTML = "0|" + sRetHTML;
			e.getMessage();
		}		
		return sRetHTML;
	}
	/**
	 * 用于获取系统通知公告数据
	 * d
	 */
	private String getNoticeInfo(HttpServletRequest request) {
		String id = request.getParameter("id");

		String sJson = "";
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForCombox("sysnotice",
				"id,title,doccontent,notice", "id=" + id, 0,
				"data");
		
		return sJson;
	}
	/**
	 * 获取用户角色列表
	 */
	private String getUserRoleItems(HttpServletRequest request) {
		String sJson = "[";   	
	   	Connection conn = null;
		PreparedStatement sMenus = null;
		PreparedStatement uMenus = null;
		ResultSet rsMenus = null;
		ResultSet rsuMenus = null;
		
		try {
			conn = DbConnectionManager.getConnection();
			sMenus = conn.prepareStatement("select id,name from sys_roles where id>" + nowuser.getRoles());
			rsMenus = sMenus.executeQuery();
			//System.out.println(rsMenus.next());
			while (rsMenus.next()) {
				int roleid;
				String rolename;
				roleid = rsMenus.getInt("id");
				rolename = rsMenus.getString("name").trim();
				sJson += "{text:'"+rolename+"',iconCls:'file-jz',menu: {items: [";

				//加入人员显示
                //显示人员  
                uMenus = conn.prepareStatement("select userid,username from wluser where usercode in (select username from sys_userrole where roleid = "+ roleid +")");
                rsuMenus = uMenus.executeQuery();
                while(rsuMenus.next())
                {                            	
                    sJson += "{text:'"+rsuMenus.getString("username").trim()+"',userid:'"+rsuMenus.getInt("userid")+"',checked:false, group: 'theme',iconCls:'file-fj',handler: GRDS.onUserCheck},";
                }
                sJson = sJson.substring(0, sJson.length() - 1);
        		sJson +="]}},";                  													
			}
			if(!sJson.equals("[")) sJson = sJson.substring(0, sJson.length() - 1);
			sJson +="]";
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		} finally {
			try {
				sMenus.close();
				rsMenus.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}				
		return sJson;
	}
	

	public static int GetUserByNameAndSwjgDM(String name, String swjg_dm){
		Connection conn = null;
		PreparedStatement sTask = null;
		ResultSet res = null;
		conn = DbConnectionManager.getConnection();
		try {
			sTask = conn
					.prepareStatement("select top 1 userid from wluser where username =? and userkebe=? ");
			sTask.setString(1, name);
			sTask.setString(2, swjg_dm);
			res = sTask.executeQuery();
			while (res.next()) {
				return res.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(conn, sTask, null, res);
		}
		return -1;
	}
	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}


