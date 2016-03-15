package cn.com.shasha.sys;


import cn.com.info21.util.ParameterUtils;
import cn.com.info21.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import cn.com.shasha.utils.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.org.*;
import cn.com.info21.system.SystemException;
public class RoleData extends HttpServlet {
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
		String sReturnStr = "";
		String sAction = request.getParameter("action");
		//System.out.println("sAction:" + sAction);
		if (sAction.equalsIgnoreCase("show")) {
			sReturnStr = show(request);
		} else if (sAction.equalsIgnoreCase("delete")) {
			sReturnStr = deleteInfo(request);
		} else if (sAction.equalsIgnoreCase("new")) {
			sReturnStr = this.addNew(request, 1);
		} else if (sAction.equalsIgnoreCase("edit")) {
			sReturnStr = this.addNew(request, 2);
		} else if (sAction.equalsIgnoreCase("getform")) {
			sReturnStr = getRoleById(request);
		}
		PrintWriter out = response.getWriter();
//System.out.println(sReturnStr);
		out.print(sReturnStr);
		out.flush();
		out.close();
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

	/**
	 * 用于加载数据
	 * d
	 */
	private String show(HttpServletRequest request) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String strWhere = request.getParameter("strWhere");
		String sJson = "";
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;
		if (strWhere == null || strWhere.equals("")) {
			strWhere = "";
		}
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForShow("SYS_ROLES",
				"id,name,detail", "id", pageSize, index,
				true, strWhere);
		return sJson;
	}

	/**
	 * 用于增加数据 addtype用来标识是更新还是增加 1代表add 2代表update
	 */
	private String addNew(HttpServletRequest request, int addtype) {
		String sRetHTML = "";
		int id = ParameterUtils.getIntParameter(request, "id", 0);
		String name = request.getParameter("name");
		String detail = request.getParameter("detail");
		String menuIds = request.getParameter("menuId");
		String[] selectitems = menuIds.split(",");
		Role role = null;
		try {
			if (addtype == 1) {
				// 新文档保存
				role = RoleHome.create(name);
				id = role.getId();
				sRetHTML = "{success:true}";
			} else {
				role = RoleHome.findById(id);
				sRetHTML = "{success:true}";
			}
			if (null != role) {
				role.setName(name);
				role.setDetail(detail);	
				role.update();	
				role = RoleHome.findById(id);
				if (menuIds != null) {
					//System.out.println("coming");
					role.setMenus("3", selectitems);
				}	
			}
		} catch (Exception e) {
			// TODO: handle exception
			// 0表示失败
			e.printStackTrace();
			sRetHTML = "{success:false}";
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
		int id = 0;
		
		try {
			sRetHTML = String.valueOf(UserHome.getUserCount("1=1"));
			for (int i = 0; i < selectitems.length; i++) {				
				RoleHome.remove(Integer.parseInt(selectitems[i]));	
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
	 * 根据id返回Form信息sjon格式的
	 * 
	 */
	private String getRoleById(HttpServletRequest request) {
		String id = request.getParameter("id");
		//System.out.println("id---" + id);
		String sJson = "";
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForCombox("sys_roles", "id,name,detail", "id=" + id, 0,
				"data");
		////System.out.println("sJson---" + sJson);
		//得到该角色的,菜单权限
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String menuId = "";
		String[] data_priv = null;
		String priv_str = "";
		String data_range = "";
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement("select menuid from sys_rolemenus where roleid =" + id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				menuId += rs.getString("menuid") + ",";
			}
			menuId = menuId.substring(0,menuId.length()-1);
		} catch (Exception e) {
			e.printStackTrace();
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
		if(!menuId.equals("")){
			
			sJson = sJson.substring(0, sJson.length()-3) + ",menuId:'" + menuId +"'";
			sJson = sJson + "}]}";
		}else
		{
			sJson = sJson.substring(0, sJson.length()-3) + ",menuId:''}]}";
		}
		//System.out.println("get role --"+sJson);
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

}


