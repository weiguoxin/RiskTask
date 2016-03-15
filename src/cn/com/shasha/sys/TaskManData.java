package cn.com.shasha.sys;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.util.ParameterUtils;
import cn.com.shasha.services.TasksData;
import cn.com.shasha.utils.DataCenter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wgx.util.json.JSONArray;
import com.wgx.util.json.JSONException;
import com.wgx.util.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TaskManData extends HttpServlet {
	WLUser nowuser = null;

	public void init(ServletConfig config) throws ServletException {
	}

	/**
	 * Handles the HTTP <code>GET</code> method. 树的管理
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		nowuser = ((WLUser) request.getSession().getAttribute("Session_User"));
		String sReturnStr = "";
		String sAction = request.getParameter("action");
		if (sAction.equalsIgnoreCase("showtask")) {
			sReturnStr = noticeInfo(request);
		} else if (sAction.equalsIgnoreCase("newtask")) {
			sReturnStr = this.addNoticeNew(request, 1);
		} else if (sAction.equalsIgnoreCase("edittask")) {
			sReturnStr = this.addNoticeNew(request, 2);
		} else if (sAction.equalsIgnoreCase("deltask")) {
			sReturnStr = delNoticeInfo(request);
		} else if (sAction.equalsIgnoreCase("gettask")) {
			sReturnStr = getNoticeInfo(request);
		} else if (sAction.equalsIgnoreCase("treeQuanXian")) { // 风险指标树
			sReturnStr = getTaskTreeQuanXian(request);
		} else if (sAction.equalsIgnoreCase("treeSwjg")) { // 税务机关树
			sReturnStr = getTaskTreeSwjg(request);
		} else if (sAction.equalsIgnoreCase("treeQuanXian_add")) { // 风险指标 - 增加
			sReturnStr = TaskTreeQuan_Add(request);
		} else if (sAction.equalsIgnoreCase("treeQuanXian_update")) { // 风险指标 -
																		// 更新
			sReturnStr = TaskTreeQuan_Update(request);
		} else if (sAction.equalsIgnoreCase("treeQuanXian_remove")) { // 风险指标 -
																		// 删除
			sReturnStr = TaskTreeQuan_Remove(request);
		}

		PrintWriter out = response.getWriter();
		out.print(sReturnStr);
		out.flush();
		out.close();

	}

	private static String FXZX_BEFOR_ADD_SQL = "select top 1 * from dm_fxzb where pid = ? order by sid desc";
	private static String FXZX_BEFOR_ADD_SQL2 = "select top 1 * from dm_fxzb where sid = ? order by sid desc";
	private static String FXZX_ADD_SQL = "insert into dm_fxzb(fxzb, pid, sid, fxydjy, leaf, status, [level],zgy) values(?,?,?,?,?,?,?,?)";
	private static String FXZX_ADD_UPDATE_PID_SQL = "update dm_fxzb set leaf = 0 where sid=?";

	private String TaskTreeQuan_Add(HttpServletRequest request) {
		String fxydjy = request.getParameter("fxydjy");
		String id = request.getParameter("id");
		String parentNodeId = request.getParameter("parentNodeId");
		String text = request.getParameter("text");
		String zgy = request.getParameter("zgy");
		if (null != zgy && !"".equals(zgy)) {
			if ("是".equals(zgy)) {
				zgy = "1";
			} else {
				zgy = "0";
			}
		} else {
			zgy = "0";
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try {
			String newsid = null;
			String level = null;
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(FXZX_BEFOR_ADD_SQL);
			ps.setString(1, parentNodeId);
			res = ps.executeQuery();
			if (res.next()) {
				String sid = res.getString("sid");
				level = res.getString("level");
				int lev = Integer.parseInt(level);
				String sid_ = sid.substring(0, lev * 2 + 1 - 2);
				String _sid = sid.substring(lev * 2 + 1 - 2, lev * 2 + 1);
				int n = Integer.parseInt(_sid) + 1;
				newsid = sid_ + (n < 10 ? "0" + n : n);
			} else {
				ps = conn.prepareStatement(FXZX_BEFOR_ADD_SQL2);
				ps.setString(1, parentNodeId);
				res = ps.executeQuery();
				if (res.next()) {
					String sid = res.getString("sid");
					level = Integer.parseInt(res.getString("level")) + 1 + "";
					newsid = sid + "01";
				}
			}
			if (null != newsid && null != level) {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(FXZX_ADD_SQL);
				ps.setString(1, text);
				ps.setString(2, parentNodeId);
				ps.setString(3, newsid);
				ps.setString(4, fxydjy);
				ps.setString(5, "1");
				ps.setString(6, "0");
				ps.setString(7, level);
				ps.setString(8, zgy);
				int r = ps.executeUpdate();
				if (r > 0) {
					ps = conn.prepareStatement(FXZX_ADD_UPDATE_PID_SQL);
					ps.setString(1, parentNodeId);
					r = ps.executeUpdate();
					if (r > 0) {
						conn.commit();
						TreeQuanXian = null;
						return getTaskTreeQuanXian(request);
					} else {
						conn.rollback();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if (null != conn) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			DbConnectionManager.close(conn, ps, null, null);
		}
		return TreeQuanXian;
	}

	private static String FXZB_BEFOR_REMOVE_SQL = "select count(*) from tasks where fxzb_dm=?";
	private static String FXZB_BEFOR_REMOVE_SQL2 = "select count(*) from dm_fxzb where pid=?";
	private static String FXZB_BEFOR_STATUS_SQL = "select count(*) from dm_fxzb where pid=?";
	private static String FXZB_REMOVE_SQL = "DELETE from dm_fxzb where sid=? and pid=?";

	/**
	 * 删除风险指标
	 * 
	 * @param request
	 * @return
	 */
	private String TaskTreeQuan_Remove(HttpServletRequest request) {
		String fxydjy = request.getParameter("fxydjy");
		String id = request.getParameter("id");
		String parentNodeId = request.getParameter("parentNodeId");
		String text = request.getParameter("text");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try {
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(FXZB_BEFOR_REMOVE_SQL);// 判断此风险指标可以删除不,不可以就设置status为1
			ps.setString(1, id);
			res = ps.executeQuery();
			boolean b = false;
			while (res.next()) {
				int r = res.getInt(1);
				b = r > 0 ? false : true;
			}
			ps = conn.prepareStatement(FXZB_BEFOR_REMOVE_SQL2);// 判断此风险指标可以删除不,不可以就设置status为1
			ps.setString(1, id);
			res = ps.executeQuery();
			while (res.next()) {
				int r = res.getInt(1);
				b = r > 0 ? false : true;
			}
			if (b) {
				ps = conn.prepareStatement(FXZB_REMOVE_SQL);
				ps.setString(1, id);
				ps.setString(2, parentNodeId);
				int r = ps.executeUpdate();
				if (r > 0) {
					ps = conn
							.prepareStatement("select count(*) from dm_fxzb where pid=?");
					ps.setString(1, parentNodeId);
					res = ps.executeQuery();
					if (res.next()) {
						r = res.getInt(1);
						if (r == 0) {
							ps = conn
									.prepareStatement("update dm_fxzb set leaf=1 where sid=?");
							ps.setString(1, parentNodeId);
							ps.executeUpdate();
						}
					}
					TreeQuanXian = null;
					return getTaskTreeQuanXian(request);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(conn, ps, null, null);
		}
		return TreeQuanXian;
	}

	private static String UPDATE_TASK_TREE = "UPDATE dm_fxzb set fxzb=?,fxydjy=?,zgy=? where sid=? and pid=?";
	private static String UPDATE_TASKS = "UPDATE tasks set fxzb=?,fxydcs=? where fxzb_dm=? and status<>1";
	private static String UPDATE_TASKS_TEMP = "UPDATE tasks_temp set fxzb=?,fxydcs=? where fxzb_dm=? and status<>1";

	/**
	 * 更新风险指标信息
	 * 
	 * @param request
	 * @return
	 */
	private String TaskTreeQuan_Update(HttpServletRequest request) {
		String fxydjy = request.getParameter("fxydjy");
		String id = request.getParameter("id");
		String parentNodeId = request.getParameter("parentNodeId");
		String text = request.getParameter("text");
		String zgy = request.getParameter("zgy");
		// System.out.println(zgy);
		if (null != zgy && !"".equals(zgy)) {
			if ("是".equals(zgy)) {
				zgy = "1";
			} else {
				zgy = "0";
			}
		} else {
			zgy = "0";
		}
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(UPDATE_TASK_TREE);
			ps.setString(1, text);
			ps.setString(2, fxydjy);
			ps.setString(3, zgy);
			ps.setString(4, id);
			ps.setString(5, parentNodeId);
			int r = ps.executeUpdate();
			if (r > 0) {
				ps = conn.prepareStatement(UPDATE_TASKS);
				ps.setString(1, text);
				ps.setString(2, fxydjy);
				ps.setString(3, id);
				ps.executeUpdate();
				ps = conn.prepareStatement(UPDATE_TASKS_TEMP);
				ps.setString(1, text);
				ps.setString(2, fxydjy);
				ps.setString(3, id);
				ps.executeUpdate();
				TreeQuanXian = null;
				return getTaskTreeQuanXian(request);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(conn, ps, null, null);
		}
		return TreeQuanXian;
	}

	public static String TreeQuanXian = null;
	public static Map<String, String> ZGY_FXZB = null; // 专管员直属LIST
	public static List<JSONObject> ALL_FXZB = null;

	private synchronized static String getTaskTreeQuanXian(
			HttpServletRequest request) {
		if (null != TreeQuanXian) {
			return TreeQuanXian;
		}
		ZGY_FXZB = new HashMap<String, String>();
		ALL_FXZB = new ArrayList<JSONObject>();
		StringBuffer sJson = new StringBuffer("[");
		// 循环数据库取任务
		Connection conn = null;
		PreparedStatement sTask = null;
		ResultSet rsTask = null;
		String temppid = "";
		String[] tempidArray = null;

		try {
			conn = DbConnectionManager.getConnection();
			sTask = conn
					.prepareStatement("select * from dm_fxzb where status = 0  order by sid");
			rsTask = sTask.executeQuery();
			int snum = 0;
			while (rsTask.next()) {
				snum++;
				String id, pid, sid, currentpid, zgy;
				boolean leaf, zgy_bool;
				tempidArray = temppid.split(",");
				currentpid = tempidArray[tempidArray.length - 1];
				id = rsTask.getString("id").trim();
				sid = rsTask.getString("sid").trim();
				pid = rsTask.getString("pid").trim();
				leaf = rsTask.getBoolean("leaf");
				zgy_bool = rsTask.getBoolean("zgy");
				String fxzb = rsTask.getString("fxzb").trim();
				String fxydjy = rsTask.getString("fxydjy") == null ? ""
						: rsTask.getString("fxydjy");

				zgy = zgy_bool ? "'是'" : "'否'";
				JSONObject json = new JSONObject();
				try {
					json.put("id", id);
					json.put("sid", sid);
					json.put("pid", pid);
					json.put("fxzb", fxzb);
					json.put("fxydjy", fxydjy);
					json.put("leaf", leaf);
					json.put("zgy", zgy_bool);
					ALL_FXZB.add(json);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (snum == 1) {
					pid = "";
				} else {
					pid = pid.trim();
				}
				if (zgy_bool) {// 专管员直属
					ZGY_FXZB.put(sid, fxzb);
				}
				while (!pid.equals(currentpid)) {
					sJson.deleteCharAt(sJson.length() - 1);
					sJson.append("]},");
					temppid = temppid.substring(0, temppid.length() - 1);
					temppid = temppid
							.substring(0, temppid.lastIndexOf(",") + 1);
					tempidArray = temppid.split(",");
					currentpid = tempidArray[tempidArray.length - 1];
				}
				if (pid.equals(currentpid)) {
					sJson.append("{id:'");
					sJson.append(sid);
					sJson.append("',text:'");
					sJson.append(fxzb);
					sJson.append("',fxydjy:'");
					sJson.append(fxydjy);
					sJson.append("',");
					if (!leaf) {
						temppid += sid + ",";
						if ("2140000".equals(sid)) {
							sJson.append("expanded:true,");
						}
						sJson.append("children:[");
					} else {
						sJson.append("zgy:");
						sJson.append(zgy);
						sJson.append(",leaf:true},");
					}
				}
			}
			sJson.deleteCharAt(sJson.length() - 1);
			for (int j = 0; j <= tempidArray.length; j++) {
				sJson.append("]}");
			}
			sJson.deleteCharAt(sJson.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
			sJson = null;
		} finally {
			try {
				sTask.close();
				rsTask.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}
		TreeQuanXian = sJson == null ? null : sJson.toString();
		return TreeQuanXian;
	}

	/**
	 * 判断风险指标是否直接派发专管员
	 * 
	 * @param fxzb_dm
	 * @return
	 */
	public static boolean hasZgy_fxzb(String fxzb_dm) {
		if (null == TreeQuanXian || null == ZGY_FXZB) {
			getTaskTreeQuanXian(null);
		}
		return ZGY_FXZB.containsKey(fxzb_dm);
	}

	/**
	 * 判断风险指标是否直接派发专管员
	 * 
	 * @param fxzb_dm
	 * @return
	 */
	public static JSONObject GetFxzbJSONObject(String key, String value) {
		if (null == ALL_FXZB || ALL_FXZB == null) {
			getTaskTreeQuanXian(null);
		}
		int l = ALL_FXZB.size();
		try {
			for (int i = 0; i < l; i++) {
				JSONObject o = ALL_FXZB.get(i);
				if (o.getString(key).equals(value)) {
					return o;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */

	/**
	 * 用于加载数据 d
	 */
	private String show(HttpServletRequest request) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String strWhere = request.getParameter("strWhere");

		String sJson = "";
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;

		if (strWhere == null || strWhere.equals("")) {
			strWhere = "usercode<>'admin'";
		} else {
			strWhere = "usercode<>'admin' and " + strWhere;
		}

		// userType用来存放所属辖区
		// System.out.println(Integer.toBinaryString(14));
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForShow("WLUSER",
				"userid,usercode,username,userquan,userstatus,telnum",
				"userid", pageSize, index, true, strWhere);
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
		String userName = request.getParameter("username");// 用户名;
		String userPass = request.getParameter("uerPass1");// 用户密码;
		String userrole = request.getParameter("userrole");
		String usertype = request.getParameter("usertype");

		WLUser user = null;
		try {
			if (addtype == 1) {
				// 新文档保存
				// 检查是否存在该用户名
				if (SysFunction.getCnt("WLUSER", "usercode = '" + userCode
						+ "'") > 0) {
					sRetHTML = "{success:false,because:'该用户名已存在!'}";
				} else {
					// 相关部门的默认kebe设置为00
					user = WLUserHome.create(userCode, userName, userPass, "",
							"", "00", 1, usertype, "");
					if (userrole != null && !userrole.equals("")) {
						user.setRoles(userrole, userCode);
					}
					sRetHTML = "{success:true}";
				}
			} else {
				user = WLUserHome.findById(Integer.parseInt(id));
				sRetHTML = "{success:true}";
				if (null != user) {
					// userCode不允许修改；
					// user.setUsercode(userCode);
					user.setUsername(userName);
					// userPass 如果为空,则不传值
					if (userPass != null && !userPass.equals(""))
						user.setUserpass(userPass);
					user.setTelnum("");
					user.setUserquan("");
					user.update();
					user.setRoles(userrole, user.getUsercode().trim());
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
		String usercode = request.getParameter("usercode");// 所属辖区
		String username = request.getParameter("username");// 用户名;

		String strWhere = "1=1 ";
		if (usercode != null && !"".equals(usercode.trim())) {
			strWhere += "and usercode like '%" + usercode + "%' ";
		}

		if (username != null && !"".equals(username.trim())) {
			strWhere += "and username like '%" + username + "%' ";
		}

		// System.out.println(strWhere);
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
		sJson = odata.ResultSetToJsonForCombox("WLuser",
				"userid,usercode,username,usertype,telnum,userstatus,userquan",
				"userid=" + id, 0, "data");

		// 添加用户角色
		WLUser employee = null;
		// 该系统一个用户只能属于一个角色
		try {
			employee = WLUserHome.findById(Integer.parseInt(id));
			sJson = sJson.substring(0, sJson.length() - 3) + ",userrole:'"
					+ (employee.getRoles() == 0 ? "" : employee.getRoles())
					+ "'}]}";
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
		sJson = odata.ResultSetToJsonForCombox("SYS_ROLES", "id,name", "", 0,
				"data");
		return sJson;
	}

	public static Date toDate(String s) {
		Date rtn = null;
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
	 * 用于加载系统通知公告数据 d
	 */
	private String noticeInfo(HttpServletRequest request) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String sJson = "";
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;

		// System.out.println("strWhere" + strWhere);
		DataCenter odata = new DataCenter();

		sJson = odata.ResultSetToJsonForShow("taskman",
				"id,tasktitle,taskstatus", "id", pageSize, index, true, "");
		return sJson;
	}

	/**
	 * 用于编辑和修改系统通知公告数据 d
	 */
	private String addNoticeNew(HttpServletRequest request, int addtype) {
		String sRetHTML = "";
		String id = request.getParameter("id");
		String title = request.getParameter("tasktitle");
		int taskstatus = Integer.parseInt(request.getParameter("taskstatus"));
		String taskdetail = request.getParameter("taskdetail");
		String taxno = request.getParameter("taxno");
		String taxname = request.getParameter("taxname");
		TaskMan at = null;
		try {
			if (addtype == 1) {
				// 新文档保存
				at = TaskManHome.create(title);
				id = String.valueOf(at.getId());
				// 第一个1表示成功
				sRetHTML = "{success:true}";
			} else {
				at = TaskManHome.findById(Integer.parseInt(id));
				sRetHTML = "{success:true}";
			}
			// System.out.println(null != at);

			if (null != at) {

				at.setTasktitle(title);
				at.setTaskstatus(taskstatus);
				at.setTaskdetail(taskdetail);
				at.setTaxno(taxno);
				at.setTaxname(taxname);
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
	 * 用于删除系统通知公告数据 d
	 */
	private String delNoticeInfo(HttpServletRequest request) {
		String sRetHTML = "";
		String[] selectitems = request.getParameter("delid").split("-");

		try {
			sRetHTML = String.valueOf(TaskManHome.getTaskManCount("1=1"));
			for (int i = 0; i < selectitems.length; i++) {
				TaskManHome.remove(Integer.parseInt(selectitems[i]));
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
	 * 用于获取系统通知公告数据 d
	 */
	private String getNoticeInfo(HttpServletRequest request) {
		String id = request.getParameter("id");

		String sJson = "";
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForCombox("taskman",
				"id, tasktitle, taskstatus, taskdetail, taxno, taxname", "id="
						+ id, 0, "data");

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
			sMenus = conn
					.prepareStatement("select id,name from sys_roles where id>"
							+ nowuser.getRoles());
			rsMenus = sMenus.executeQuery();
			// System.out.println(rsMenus.next());
			while (rsMenus.next()) {
				int roleid;
				String rolename;
				roleid = rsMenus.getInt("id");
				rolename = rsMenus.getString("name").trim();
				sJson += "{text:'" + rolename
						+ "',iconCls:'file-jz',menu: {items: [";

				// 加入人员显示
				// 显示人员
				uMenus = conn
						.prepareStatement("select userid,username from wluser where usercode in (select username from sys_userrole where roleid = "
								+ roleid + ")");
				rsuMenus = uMenus.executeQuery();
				while (rsuMenus.next()) {
					sJson += "{text:'"
							+ rsuMenus.getString("username").trim()
							+ "',userid:'"
							+ rsuMenus.getInt("userid")
							+ "',checked:false, group: 'theme',iconCls:'file-fj',handler: GRDS.onUserCheck},";
				}
				sJson = sJson.substring(0, sJson.length() - 1);
				sJson += "]}},";
			}
			if (!sJson.equals("["))
				sJson = sJson.substring(0, sJson.length() - 1);
			sJson += "]";
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

	public static Map<String, String> ALL_SWJG_MAP_NAME_DM = new HashMap<String, String>();
	/**
	 * 根据税务机关名称查询税务机关代码
	 * 
	 * @param dm
	 * @return
	 */
	public static String GetSwjgDmByName(String name) {
		if (ALL_SWJG_MAP.containsKey(name)) {
			return ALL_SWJG_MAP.get(name);
		}
		Connection conn = null;
		PreparedStatement sTask = null;
		ResultSet res = null;
		conn = DbConnectionManager.getConnection();
		try {
			sTask = conn
					.prepareStatement("select top 1   SWJG_DM, SWJGMC from dm_gy_swjg where SWJGMC ='"
							+ name + "' order by swjg_dm");
			res = sTask.executeQuery();
			while (res.next()) {
				String swjg_dm = res.getString("swjg_dm");
				String swjg_mc = res.getString("SWJGMC");
				ALL_SWJG_MAP.put(swjg_mc, swjg_dm);
				return swjg_dm;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(conn, sTask, null, res);
		}
		return null;
	}

	public static Map<String, String> ALL_SWJG_MAP = new HashMap<String, String>();
	/**
	 * 根据税务机关代码查询税务机关名称
	 * 
	 * @param dm
	 * @return
	 */
	public static String GetSwjgNameByDm(String dm) {
		if (ALL_SWJG_MAP.containsKey(dm)) {
			return ALL_SWJG_MAP.get(dm);
		}
		Connection conn = null;
		PreparedStatement sTask = null;
		ResultSet res = null;
		conn = DbConnectionManager.getConnection();
		try {
			sTask = conn
					.prepareStatement("select top 1   SWJG_DM, SWJGMC from dm_gy_swjg where swjg_dm ='"
							+ dm + "' order by swjg_dm");
			res = sTask.executeQuery();
			while (res.next()) {
				String swjg_dm = res.getString("swjg_dm");
				String swjg_mc = res.getString("SWJGMC");
				ALL_SWJG_MAP.put(swjg_dm, swjg_mc);
				return swjg_mc;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(conn, sTask, null, res);
		}
		return null;
	}

	public static Map<String, String> SWJG_MAP = new HashMap<String, String>();

	/**
	 * 税务机关树的维护
	 * 
	 * @param request
	 * @return
	 */
	private String getTaskTreeSwjg(HttpServletRequest request) {
		String now_user_swjg = nowuser.getUserkebe();
		String swjg_like = TasksData.getSwjg_Like_Dm(now_user_swjg,true);
		if(swjg_like.length()%2==0){
			swjg_like += "0";
		}
//System.out.println(swjg_like);
		if (SWJG_MAP.containsKey(swjg_like)) {
			return SWJG_MAP.get(swjg_like);
		}
		StringBuffer sJson = new StringBuffer("[");
		// 循环数据库取任务
		Connection conn = null;
		PreparedStatement sTask = null;
		ResultSet rsTask = null;
		ResultSet rsTasksub = null;
		String temppid = "";
		String[] tempidArray = null;

		try {
			conn = DbConnectionManager.getConnection();
			sTask = conn
					.prepareStatement("select * from dm_gy_swjg where swjg_dm like '"
							+ swjg_like + "%' order by swjg_dm");
			rsTask = sTask.executeQuery();
			int snum = 0;

			while (rsTask.next()) {
				snum++;
				String mpid, mid, isLeaf, currentpid;
				tempidArray = temppid.split(",");
				currentpid = tempidArray[tempidArray.length - 1];
				mid = rsTask.getString("swjg_dm").trim();
				mpid = rsTask.getString("sjswjg_dm").trim();

				if (snum == 1) {
					mpid = "";
				} else {
					mpid = mpid.trim();
				}
				String entryName = rsTask.getString("swjgmc").trim();
				String txdm = rsTask.getString("swjg_dm").trim();

				if (!mpid.equals(currentpid)) {
					sJson.deleteCharAt(sJson.length() - 1);
					sJson.append("]},");
					temppid = temppid.substring(0, temppid.length() - 1);
					temppid = temppid
							.substring(0, temppid.lastIndexOf(",") + 1);
					tempidArray = temppid.split(",");
					currentpid = tempidArray[tempidArray.length - 1];
				}

				sTask = conn
						.prepareStatement("SELECT swjg_dm FROM dm_gy_swjg WHERE  sjswjg_dm = '"
								+ mid + "'");
				rsTasksub = sTask.executeQuery();
				boolean hassub = rsTasksub.next();

				if (mpid.equals(currentpid)) {
					if (hassub) {
						temppid += mid + ",";
						sJson.append("{id:'");
						sJson.append(txdm);
						sJson.append("',text:'");
						sJson.append(entryName);
						sJson.append("',children:[");
					} else if(snum == 1){
						sJson.append("{id:'");
						sJson.append(txdm);
						sJson.append("',text:'");
						sJson.append(entryName);
						sJson.append("',leaf:true");
						sJson.append(",children:[}");
					} else {
						sJson.append("{id:'");
						sJson.append(txdm);
						sJson.append("',text:'");
						sJson.append(entryName);
						sJson.append("',leaf:true},");
					}
				}
				/*
				 * sTasksub = conn.prepareStatement(
				 * "select * from dm_gy_swjg where sjswjg_dm = ?");
				 * sTasksub.setString(1, rsTask.getString("swjg_dm")); rsTasksub
				 * = sTasksub.executeQuery();
				 * 
				 * if(!rsTasksub.next()) { sJson += "{id:'" +
				 * rsTask.getString("swjg_dm") + "',text:'" +
				 * rsTask.getString("swjgmc") + "',checked:false,leaf:true},"; }
				 * else { sJson += "{id:'" + rsTask.getString("swjg_dm") +
				 * "',text:'" + rsTask.getString("swjgmc") + "',children:["; }
				 */
				// sJson +=
				// "{id:'"+rsTask.getInt("id")+"',text:'"+rsTask.getString("tasktitle")+"',checked:false,leaf:true},";
				// sJson +=
				// "{id:'"+rsTask.getString("swjg_dm")+"',text:'"+rsTask.getString("swjgmc")+"',checked:false,leaf:true},";

			}
			sJson.deleteCharAt(sJson.length() - 1);
			for (int j = 0; j <= tempidArray.length; j++) {
				sJson.append("]}");
			}
			sJson.deleteCharAt(sJson.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} finally {
			try {
				rsTasksub.close();
				sTask.close();
				rsTask.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}
		String res = sJson.toString();
		SWJG_MAP.put(swjg_like, res);
		return res;
	}

	private static JSONArray SWJG_JSON_LIST = new JSONArray();

	public static String getSwjg_SJ(String swjg_dm) {
		String s = swjg_dm.trim();
		int n = SWJG_JSON_LIST.length();
		try {
			for (int i = 0; i < n; i++) {
				JSONObject o = SWJG_JSON_LIST.getJSONObject(i);
				if (o.getString("swjg_dm").equals(s)) {
					return o.getString("sjswjg_dm");
				}
			}
			Connection conn = null;
			PreparedStatement sTask = null;
			ResultSet rsTask = null;
			try {
				conn = DbConnectionManager.getConnection();
				sTask = conn
						.prepareStatement("select * from dm_gy_swjg where swjg_dm=?");
				sTask.setString(1, s);
				rsTask = sTask.executeQuery();
				while (rsTask.next()) {
					JSONObject o = new JSONObject();
					o.put("swjg_dm", rsTask.getString("swjg_dm"));
					o.put("swjg_mc", rsTask.getString("swjgmc"));
					o.put("sjswjg_dm", rsTask.getString("sjswjg_dm"));
					SWJG_JSON_LIST.put(o);
					return o.getString("sjswjg_dm");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DbConnectionManager.close(conn, sTask, null, rsTask);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
