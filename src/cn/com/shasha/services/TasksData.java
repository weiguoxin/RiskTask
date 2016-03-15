package cn.com.shasha.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wgx.util.excel.ExcelUtil;
import com.wgx.util.json.JSONArray;
import com.wgx.util.json.JSONException;
import com.wgx.util.json.JSONObject;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;
import cn.com.info21.util.ParameterUtils;
import cn.com.shasha.sys.ArticleHome;
import cn.com.shasha.sys.TaskManData;
import cn.com.shasha.sys.WLUser;
import cn.com.shasha.sys.WLUserHome;
import cn.com.shasha.utils.DataCenter;
import cn.com.shasha.utils.DateTime;
import cn.com.wgx.service.DoTaskLaterRunner;

public class TasksData  extends HttpServlet {
	public static int changeHOUR = 24; //修改执行人的时间限制
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
		response.setCharacterEncoding("utf-8");
		nowuser = ((WLUser)request.getSession().getAttribute("Session_User"));
		String sReturnStr = "";
		String sAction = request.getParameter("action");
		if (sAction.equalsIgnoreCase("new")) {
			//sReturnStr = addNew(request);
		}else if (sAction.equalsIgnoreCase("show")) {
			sReturnStr = showInfo(request, response);
		} else if (sAction.equalsIgnoreCase("getFile")) {
			sReturnStr = getFileById(request);
		} else if (sAction.equalsIgnoreCase("upDateStatus")) { //修改任务状态 ( 推送任务)
			sReturnStr = upDateStatus(request);
		}else if (sAction.equalsIgnoreCase("updateTask")) {//更新任务
			sReturnStr = updateTask(request);
		}else if (sAction.equalsIgnoreCase("updateTask_finish")) {//任务完成
			sReturnStr = updateTask_finish(request);
		}else if (sAction.equalsIgnoreCase("Count_By_SWJG")) {//统计数据,根据税务机关
			sReturnStr = count_Tasks_SWJG(request,response);
		}else if (sAction.equalsIgnoreCase("Count_By_SWJG_DM")) { //统计部门人员任务数
			sReturnStr = count_Tasks_SWJG_DM(request, nowuser, response);
		}else if (sAction.equalsIgnoreCase("Count_By_Fxzb_Dm")) {//根据风险指标统计
			sReturnStr = Count_By_Fxzb_Dm(request);
		}else if (sAction.equalsIgnoreCase("newTask")) { //新建任务
			sReturnStr = newTask(request);
		}else if (sAction.equalsIgnoreCase("del")) { //删除任务
			sReturnStr = delTasks(request);
		}else if (sAction.equalsIgnoreCase("check")) { //审核任务
			sReturnStr = checked(request);
		}else if (sAction.equalsIgnoreCase("change_zxr")) { //审核任务
			sReturnStr = change_zxr(request);
		}
		PrintWriter out = response.getWriter();
//System.out.println(sReturnStr);
		out.print(sReturnStr);
		out.flush();
		out.close();
	}
	/**
	 * 申请修改执行人
	 * @param request
	 * @return
	 */
	private String change_zxr(HttpServletRequest request) {
		String id = request.getParameter("id");
		String limit_time = request.getParameter("limit_time");
		String zgy_mc = request.getParameter("zgy_mc");
		String zxr_dm = request.getParameter("zxr_dm");
		String zgy_dm = request.getParameter("zgy_dm");
		String fxydcs = request.getParameter("fxydcs");
		String swjg_mc = request.getParameter("swjg_mc");
		String fxms = request.getParameter("fxms");
		String status = request.getParameter("status");
		String zx_swjg_dm = request.getParameter("zx_swjg_dm");
		String type = request.getParameter("type");
		Connection con = DbConnectionManager.getConnection();;
		PreparedStatement ps = null;
		JSONObject o = new JSONObject();
		try {
			if("1".equals(type)){
				ps = con.prepareStatement("update tasks set change_zxr = 0,begin_time=getdate() where id = ?");
			}else{
				ps = con.prepareStatement("update tasks set change_zxr = 1 where id = ?");
			}
			ps.setString(1, id);
			int res = ps.executeUpdate();
			if(res == 0){
				o.put("res", "申请失败,数据错误.");
			}else{
				o.put("res", "申请成功,请等待数据处理组审核.");
			}
			o.put("r", res);
			o.put("success", true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			DbConnectionManager.close(con, ps, null, null);
		}
		return o.toString();
	}
	/**
	 * 审核任务
	 * @param request
	 * @return
	 */
	private String checked(HttpServletRequest request) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String jjyy = request.getParameter("jjyy");
		String userkebe = nowuser.getUserkebe();
		String update_check_sql = "update tasks set checked=1 where id=? strWhere ";
		if("1".equals(type)){ //type为1为拒绝审核,status归0
			update_check_sql = "update tasks set status=0,begin_time=getDate(),end_time=NULL,rwhk=NULL,jjyy='" + jjyy + "' where id=? strWhere ";
		}
		StringBuffer strWhere = new StringBuffer();
		if(null != userkebe){
			strWhere.append(" and zx_swjg_dm like '" +  getSwjg_Like_Dm(userkebe,true) + "%' ");
		}
		update_check_sql = update_check_sql.replace("strWhere", strWhere.toString());
		
		Connection con = null;
		PreparedStatement pstmt = null;
		JSONObject o = new JSONObject();
		try {
			int r = 0;
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(update_check_sql);
			pstmt.setString(1, id);
			r = pstmt.executeUpdate();
			o.put("res", "权限不足或数据错误.");
			o.put("r", r);
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
	/**
	 * 删除任务
	 * @param request
	 * @return
	 */
	private String delTasks(HttpServletRequest request) {
		String idList = request.getParameter("idList");
		String userkebe = nowuser.getUserkebe();
		String del_sql = "delete tasks where status=-1 strWhere ";
		StringBuffer strWhere = new StringBuffer();
		int r = -1;
		if(null != userkebe){
			strWhere.append(" and (zx_swjg_dm like '" +  getSwjg_Like_Dm(userkebe,true) + "%' or swjg_dm like '" +  getSwjg_Like_Dm(userkebe,true) + "%')");
		}
		if(null != idList){
			strWhere.append(" and id in (" +  idList + ") ");
			r = 0;
		}
		del_sql = del_sql.replace("strWhere", strWhere.toString());
		Connection con = null;
		PreparedStatement pstmt = null;
		JSONObject o = new JSONObject();
		try {
			if(r == 0){
				con = DbConnectionManager.getConnection();
				pstmt = con.prepareStatement(del_sql);
				r = pstmt.executeUpdate();
			}
			if(r < 1){
				o.put("res", "已推送的任务不可删除.");
			}
			o.put("r", r);
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
	/**
	 * 统计部门 各种任务类别情况
	 * @param request
	 * @return
	 */
	private String Count_By_Fxzb_Dm(HttpServletRequest request) {
		String task_date_start = request.getParameter("task_date_start");
		String task_date_end = request.getParameter("task_date_end");
		String swjg_dm = request.getParameter("sw_dm");
		String fxzb_dm = request.getParameter("fxzb_dm");
		String imp_userid = request.getParameter("imp_userid");
		StringBuffer strWhere = new StringBuffer();
		String userkebe = nowuser.getUserkebe();
		if(null != imp_userid && !"".equals(imp_userid)){
			strWhere.append(" and imp_userid = " +  imp_userid + " ");
		}
		if(null != fxzb_dm){
			strWhere.append(" and fxzb_dm like '" +  getFxzb_Dm_Like(fxzb_dm) + "%' ");
		}
		if(null != userkebe){
			strWhere.append(" and swjg_dm like '" +  getSwjg_Like_Dm(userkebe,true) + "%' ");
		}
		if(null != swjg_dm && !"".equals(swjg_dm) && !"-1".equals(swjg_dm)){
			strWhere.append(" and swjg_dm like '" +  getSwjg_Like_Dm(swjg_dm) + "%' ");
		}
		if(null != task_date_start && !"".equals(task_date_start)){
			strWhere.append(" and begin_time >= '" + task_date_start +  "' ");
		}
		if(null != task_date_end && !"".equals(task_date_end)){
			strWhere.append(" and begin_time <= '" + task_date_end +  " 23:59' ");
		}
		//市级查询
		String sql = "select t.swjg_dm," +
"sum(case when fxzb_dm ='21401' then a else 0 end)a,"+
"sum(case when fxzb_dm ='21402' then a else 0 end)b,"+
"sum(case when fxzb_dm ='21404' then a else 0  end)c,"+
"sum(case when fxzb_dm ='21405' then a else 0  end)d, "+
"sum(case when fxzb_dm ='21406' then a else 0  end)e, "+
"sum(case when fxzb_dm ='21407' then a else 0  end)f, "+
"t2.swjgmc swjg_mc from "+
"(select (case when substring(swjg_dm,8,11)<>'0000' then substring(swjg_dm,0,8)+'0000' else swjg_dm end) swjg_dm,"+
		"substring(fxzb_dm,1,5)fxzb_dm,"+
		"sum(case when status<>-1 then 1 else 0 end)a,"+
		"sum(case when status=0 then 1  else 0 end)b "+
 "from tasks where 1=1 strWhere group by swjg_dm,fxzb_dm)t "+
"left join dm_gy_swjg t2 on t.swjg_dm=t2.swjg_dm  group by t.swjg_dm,t2.swjgmc order by t.swjg_dm";
		//县级分局
		String sql_next = "select t.swjg_dm," +
	"sum(case when fxzb_dm ='21401' then a else 0 end)a,"+
	"sum(case when fxzb_dm ='21402' then a else 0 end)b,"+
	"sum(case when fxzb_dm ='21404' then a else 0  end)c,"+
	"sum(case when fxzb_dm ='21405' then a else 0  end)d, "+
	"sum(case when fxzb_dm ='21406' then a else 0  end)e, "+
	"sum(case when fxzb_dm ='21407' then a else 0  end)f, "+
	"t2.swjgmc swjg_mc from "+
	"(select  (case when substring(swjg_dm,10,11)<>'00' then substring(swjg_dm,0,10)+'00' else swjg_dm end) swjg_dm,"+
	"substring(fxzb_dm,1,5) fxzb_dm,"+
			"sum(case when status<>-1 then 1 else 0 end)a,"+
			"sum(case when status=0 then 1  else 0 end)b "+
	 "from tasks where 1=1 strWhere group by swjg_dm,fxzb_dm)t "+
	"left join dm_gy_swjg t2 on t.swjg_dm=t2.swjg_dm  group by t.swjg_dm,t2.swjgmc order by t.swjg_dm";
		if(swjg_dm == null || swjg_dm.equals("") || swjg_dm.equals("21409000000")){
			sql = sql.replace("strWhere", strWhere.toString());
		}else{
			sql = sql_next.replace("strWhere", strWhere.toString());
		}
//System.out.println(sql);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		JSONObject o = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			int i = 0;
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			res = pstmt.executeQuery();
			while(res.next()){
				JSONObject oRes = new JSONObject();
				oRes.put("id", ++i);
				oRes.put("swjg_mc", res.getString("swjg_mc"));
				oRes.put("swjg_dm", res.getString("swjg_dm"));
				oRes.put("a", res.getString("a"));
				oRes.put("b", res.getString("b"));
				oRes.put("c", res.getString("c"));
				oRes.put("d", res.getString("d"));
				oRes.put("e", res.getString("e"));
				oRes.put("f", res.getString("f"));
				ja.put(oRes);
			}
			//统计全部数量
			int a=0,b=0,c=0,d=0,e=0,f=0,n=ja.length();
			for(int j=0;j<n;j++){
				JSONObject oo = ja.getJSONObject(j);
				a += Integer.parseInt(oo.getString("a"));
				b += Integer.parseInt(oo.getString("b"));
				c += Integer.parseInt(oo.getString("c"));
				d += Integer.parseInt(oo.getString("d"));
				e += Integer.parseInt(oo.getString("e"));
				f += Integer.parseInt(oo.getString("f"));
			}
			JSONObject all_count = new JSONObject();
			all_count.put("a", a+"");
			all_count.put("b", b+"");
			all_count.put("c", c+"");
			all_count.put("d", d+"");
			all_count.put("e", e+"");
			all_count.put("f", f+"");
			all_count.put("id", ++i);
			all_count.put("swjg_mc", "全部统计");
			all_count.put("swjg_dm", "-1");
			ja.put(all_count);
			o.put("topic", ja);
			o.put("topcount", ja.length());
			o.put("success", true);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, res);
		}
		return o.toString();
	}
	/**
	 * 新建任务
	 * @param request
	 * @return
	 */
	private String newTask(HttpServletRequest request) {
		String nsrsbh = request.getParameter("nsrsbh");
		String nsrmc = request.getParameter("nsrmc");
		String swjg_dm = request.getParameter("swjg_dm");
		String zx_swjg_dm = request.getParameter("zx_swjg_dm");
		String swjg_mc = request.getParameter("swjg_mc");
		String zgy_dm = request.getParameter("zgy_dm");
		String zgy_mc = request.getParameter("zgy_mc");
		String zxr_dm = request.getParameter("zxr_dm");
		String limit_date = request.getParameter("limit_time");
		String fxzb = request.getParameter("fxzb");
		String fxzb_dm = request.getParameter("fxzb_dm");
		String fxms = request.getParameter("fxms");
		String fxydcs = request.getParameter("fxydcs");
		JSONObject o = new JSONObject();
		Tasks ps = new Tasks();
//		nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, limit_time, end_time ,
//		fxydcs
		ps.setNsrsbh(nsrsbh);
		ps.setNsrmc(nsrmc);
		WLUser zgy = null;
		if(null != zgy_dm && !"".equals(zgy_dm)&& !"-1".equals(zgy_dm)){
			try {
				zgy = new WLUser(Integer.parseInt(zgy_dm));
				ps.setZgy_mc(zgy.getUsername());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		}else{
			ps.setZgy_mc(zgy_mc);
		}
		
		ps.setFxzb(fxzb);
		ps.setFxms(fxms);
		ps.setSwjg_dm(swjg_dm);
		ps.setSwjg_mc(swjg_mc);
		ps.setZx_swjg_dm(zx_swjg_dm);
		ps.setZgy_dm(zgy_dm); 
		ps.setFxzb_dm(fxzb_dm);
		if(null != zxr_dm && !"".equals(zxr_dm)&& !"-1".equals(zxr_dm)){
			ps.setZxr_dm(Integer.parseInt(zxr_dm));
		}else if(TaskManData.hasZgy_fxzb(fxzb_dm)){
			ps.setZxr_dm(zgy.getUserid());
		}
		
		ps.setLimit_time(new Timestamp(DateTime.strToDate(limit_date).getTime()));
		ps.setFxydcs(fxydcs);
		ps.setImp_userid(nowuser.getUserid());
		ps.setImp_date(new Timestamp(new Date().getTime()));
		try {
			int r = ps.insertSelfTask();
			try {
				if(r == 1){
					o.put("r", r);
					o.put("success", true);
					DoTaskLaterRunner doTask = new DoTaskLaterRunner(); //1小时后自动下发任务
					doTask.setTask(ps);
					doTask.setSleep(1000l * 60 * 60);
					doTask.start();
				}else{
					o.put("res", "数据错误");
					o.put("r", r);
					o.put("success", true);
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return o.toString();
	}
	/**
	 * 统计任务 - 部门人员
	 * @param request
	 * @return
	 */
	public static  String count_Tasks_SWJG_DM(HttpServletRequest request, WLUser nowuser, HttpServletResponse response) {
		String task_date_start = request.getParameter("task_date_start");
		String task_date_end = request.getParameter("task_date_end");
		String swjg_dm = request.getParameter("sw_dm");
		String fxzb_dm = request.getParameter("fxzb_dm");
		String type = request.getParameter("type");
		String imp_userid = request.getParameter("imp_userid");
		StringBuffer strWhere = new StringBuffer();
		String userkebe = nowuser.getUserkebe();
		if(null != imp_userid && !"".equals(imp_userid)){
			strWhere.append(" and imp_userid = " +  imp_userid + " ");
		}
		if(null != fxzb_dm){
			strWhere.append(" and fxzb_dm like '" +  getFxzb_Dm_Like(fxzb_dm) + "%' ");
		}
		if(null != userkebe){
			strWhere.append(" and swjg_dm like '" +  getSwjg_Like_Dm(userkebe,true) + "%' ");
		}
		if(null != swjg_dm && !"".equals(swjg_dm)){
			strWhere.append(" and swjg_dm like '" +  getSwjg_Like_Dm(swjg_dm) + "%' ");
		}
		if(null != task_date_start && !"".equals(task_date_start)){
			strWhere.append(" and begin_time >= '" + task_date_start +  "' ");
		}
		if(null != task_date_end && !"".equals(task_date_end)){
			strWhere.append(" and begin_time <= '" + task_date_end +  " 23:59' ");
		}
		String sql = "select zxr_dm,y.username,"+
					"sum(case when status=-1 then 0 else  1 end) a,"+
					"sum(case when status=0 then 1 else  0 end) b,"+
					"sum(case when status=1 then 1 else  0 end) c  "+
					"from tasks t left join  wluser y on t.zxr_dm=y.userid where 1=1 strWhere  "+
					"group by zxr_dm,y.username";
		sql = sql.replace("strWhere", strWhere.toString());
//System.out.println(sql);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		JSONObject o = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			res = pstmt.executeQuery();
			int i = 0;
			while(res.next()){
				JSONObject oRes = new JSONObject();
				oRes.put("id", ++i);
				oRes.put("zxr_dm", res.getString("zxr_dm"));
				oRes.put("a", res.getString("a"));
				oRes.put("b", res.getString("b"));
				oRes.put("c", res.getString("c"));
				oRes.put("username", res.getString("username"));
	  			float a = Float.parseFloat(oRes.getString("a"));
	  			float c = Float.parseFloat(oRes.getString("c"));
	  			oRes.put("d", (Math.round(c / a * 10000) / 100.00 + "%"));
				ja.put(oRes);
			}
			int a=0,b=0,c=0;
			for(int j=0;j<ja.length();j++){
				JSONObject oo = ja.getJSONObject(j);
				a += Integer.parseInt(oo.getString("a"));
				b += Integer.parseInt(oo.getString("b"));
				c += Integer.parseInt(oo.getString("c"));
			}
			JSONObject all_count = new JSONObject();
			all_count.put("id", ++i);
			all_count.put("zxr_dm", "-1");
			all_count.put("a", a+"");
			all_count.put("b", b+"");
			all_count.put("c", c+"");
			all_count.put("username", "全部统计");
  			float aa = Float.parseFloat(all_count.getString("a"));
  			float cc = Float.parseFloat(all_count.getString("c"));
  			all_count.put("d", (Math.round(cc / aa * 10000) / 100.00 + "%"));
			ja.put(all_count);
			
			o.put("topic", ja);
			o.put("topcount", ja.length());
			o.put("success", true);
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, res);
		}
		if("excel".equals(type)){
			File file = ExcelUtil.Excel(ja, ExcelUtil.Count_Ren_mapKey);
			try {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition","attachment; filename="+file.getName());
				response.sendRedirect("temp/"+file.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return o.toString();
	}
	/**
	 * 统计任务完成数 - 部门  - 树结果 -暂时不用
	 * @param request
	 * @return
	 */
	private String count_Tasks_SWJG_Tree(HttpServletRequest request) {
		String task_date_start = request.getParameter("task_date_start");
		String task_date_end = request.getParameter("task_date_end");
		String swjg_dm = request.getParameter("swjg_dm");
		String fxzb_dm = request.getParameter("fxzb_dm");
		String zxr_dm = request.getParameter("zxr_dm");
		String zxr_mc = request.getParameter("zxr_mc");
		StringBuffer strWhere = new StringBuffer();
		String userkebe = nowuser.getUserkebe();

		if(null != fxzb_dm){
			strWhere.append(" and fxzb_dm like '" +  getFxzb_Dm_Like(fxzb_dm) + "%' ");
		}
		if(null != zxr_dm && !"".equals(zxr_dm)){
			strWhere.append(" and zxr_dm = '" +  zxr_dm + "' ");
		}
		if(null != zxr_mc && !"".equals(zxr_mc)){
			strWhere.append(" and zxr_dm in (select userid from wluser where username like '%" + zxr_mc +  "%') ");
		}
		if(null != userkebe){
			strWhere.append(" and swjg_dm like '" +  getSwjg_Like_Dm(userkebe,true) + "%' ");
		}
		if(null != swjg_dm && !"".equals(swjg_dm)){
			strWhere.append(" and swjg_dm like '" +  getSwjg_Like_Dm(swjg_dm) + "%' ");
		}
		if(null != task_date_start && !"".equals(task_date_start)){
			strWhere.append(" and begin_time >= '" + task_date_start +  "' ");
		}
		if(null != task_date_end && !"".equals(task_date_end)){
			strWhere.append(" and begin_time <= '" + task_date_end +  " 23:59' ");
		}
		//查询统计 - 精确到部门
		String sql = "select c.swjg_mc,c.swjg_dm,sum(c.a)a,sum(c.b)b,sum(c.c)c,d.sjswjg_dm from ( "+
					"SELECT swjg_mc,swjg_dm,count(swjg_mc) as a ,0 as b,0 as c "+
					"FROM         tasks where  status <>-1 strWhere group by swjg_dm,swjg_mc "+
					"union "+
					"SELECT swjg_mc,swjg_dm,0 as a ,count(swjg_mc) as b,0 as c "+
					"FROM         tasks where  status = 0  strWhere group by swjg_dm,swjg_mc "+
					"union "+
					"SELECT swjg_mc,swjg_dm,0 as a ,0 as b,count(swjg_mc) as c "+
					"FROM         tasks where  status = 1 strWhere group by swjg_dm,swjg_mc "+
					") c,DM_GY_SWJG d where c.swjg_dm=d.swjg_dm group by c.swjg_dm,c.swjg_mc,d.sjswjg_dm order by swjg_dm";
		//统计 全部
		String sql_all = "select sum(case when status<>-1 then 1 else 0 end)a,"+
							"sum(case when status=0 then 1  else 0 end)b,"+
							"sum(case when status=1 then 1 else 0  end)c from tasks where 1=1 strWhere";
		// 统计  精确到分局
		String sql_swjg = "select t.swjg_dm,sum(a)a,sum(b)b,sum(c)c,t2.swjgmc swjg_mc from " +
				"	(select  (case when substring(swjg_dm,10,11)<>'00' then substring(swjg_dm,0,10)+'00' else swjg_dm end) swjg_dm,"+ 
						"sum(case when status<>-1 then 1 else 0 end)a,"+
						"sum(case when status=0 then 1  else 0 end)b,"+
						"sum(case when status=1 then 1 else 0  end)c "+
						"from tasks where 1=1 strWhere group by swjg_dm)t left join dm_gy_swjg t2 on t.swjg_dm=t2.swjg_dm group by t.swjg_dm,t2.swjgmc";
		
		sql = sql.replace("strWhere", strWhere.toString());
		sql_all = sql_all.replace("strWhere", strWhere.toString());
		sql_swjg = sql_swjg.replace("strWhere", strWhere.toString());
//System.out.println(sql);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		JSONObject o = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql_all);
			res = pstmt.executeQuery();
			int i = -1;
			if(res.next()){
				JSONObject oRes = new JSONObject();
				oRes.put("id", i);
				oRes.put("swjg_mc", "全部统计");
				oRes.put("a", res.getInt("a"));
				oRes.put("b", res.getInt("b"));
				oRes.put("c", res.getInt("c"));
				oRes.put("swjg_dm", "-1");
				oRes.put("expanded", true);
				oRes.put("children", new JSONArray());
				ja.put(oRes);
				i++;
			}
			if(i != -1){
				pstmt = con.prepareStatement(sql_swjg);
				res = pstmt.executeQuery();
				JSONArray jaa = ja.getJSONObject(0).getJSONArray("children");
				while(res.next()){
					JSONObject oRes = new JSONObject();
					oRes.put("id", ++i);
					oRes.put("swjg_mc", res.getString("swjg_mc"));
					oRes.put("a", res.getString("a"));
					oRes.put("b", res.getString("b"));
					oRes.put("c", res.getString("c"));
					oRes.put("swjg_dm", res.getString("swjg_dm"));
					oRes.put("children", new JSONArray());
					oRes.put("leaf", true);
					jaa.put(oRes);
				}
				pstmt = con.prepareStatement(sql);
				res = pstmt.executeQuery();
				while(res.next()){
					JSONObject oRes = new JSONObject();
					oRes.put("id", ++i);
					oRes.put("swjg_mc", res.getString("swjg_mc"));
					oRes.put("a", res.getString("a"));
					oRes.put("b", res.getString("b"));
					oRes.put("c", res.getString("c"));
					oRes.put("swjg_dm", res.getString("swjg_dm"));
					oRes.put("sjswjg_dm", res.getString("sjswjg_dm"));
					oRes.put("leaf", true);
					int jaaLength = jaa.length();
					for(int n=0;n<jaaLength;n++){
						String sjswjg_dm = oRes.getString("sjswjg_dm");
						if(sjswjg_dm.endsWith(jaa.getJSONObject(n).getString("swjg_dm"))){
							jaa.getJSONObject(n).getJSONArray("children").put(oRes);
							break;
						}
					}
				}
			}
			o.put("topic", ja);
			o.put("topcount", ja.length());
			o.put("success", true);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, res);
		}
		return ja.toString();
	}
	/**
	 * 统计任务完成数 - 部门
	 * @param request
	 * @return
	 */
	private String count_Tasks_SWJG(HttpServletRequest request, HttpServletResponse response) {
		String task_date_start = request.getParameter("task_date_start");
		String task_date_end = request.getParameter("task_date_end");
		String swjg_dm = request.getParameter("swjg_dm");
		String fxzb_dm = request.getParameter("fxzb_dm");
		String zxr_dm = request.getParameter("zxr_dm");
		String zxr_mc = request.getParameter("zxr_mc");
		String type = request.getParameter("type");
		String t = request.getParameter("t");
		String imp_userid = request.getParameter("imp_userid");
		StringBuffer strWhere = new StringBuffer();
		String userkebe = nowuser.getUserkebe();

		if(null != fxzb_dm){
			strWhere.append(" and fxzb_dm like '" +  getFxzb_Dm_Like(fxzb_dm) + "%' ");
		}
		if(null != imp_userid && !"".equals(imp_userid)){
			strWhere.append(" and imp_userid = " +  imp_userid + " ");
		}
		if(null != zxr_dm && !"".equals(zxr_dm)){
			strWhere.append(" and zxr_dm = '" +  zxr_dm + "' ");
		}
		if(null != zxr_mc && !"".equals(zxr_mc)){
			strWhere.append(" and zxr_dm in (select userid from wluser where username like '%" + zxr_mc +  "%') ");
		}
		if(null != userkebe){
			strWhere.append(" and swjg_dm like '" +  getSwjg_Like_Dm(userkebe,true) + "%' ");
		}
		if(null != swjg_dm && !"".equals(swjg_dm)){
			strWhere.append(" and swjg_dm like '" +  getSwjg_Like_Dm(swjg_dm) + "%' ");
		}
		if(null != task_date_start && !"".equals(task_date_start)){
			strWhere.append(" and begin_time >= '" + task_date_start +  "' ");
		}
		if(null != task_date_end && !"".equals(task_date_end)){
			strWhere.append(" and begin_time <= '" + task_date_end +  " 23:59' ");
		}
		//查询统计 - 精确到部门
		String sql = "select swjg_mc,swjg_dm,sum(a)a,sum(b)b,sum(c) c from ( "+
					"SELECT swjg_mc,swjg_dm,count(swjg_mc) as a ,0 as b,0 as c "+
					"FROM         tasks where  status <>-1 strWhere group by swjg_dm,swjg_mc "+
					"union "+
					"SELECT swjg_mc,swjg_dm,0 as a ,count(swjg_mc) as b,0 as c "+
					"FROM         tasks where  status = 0  strWhere group by swjg_dm,swjg_mc "+
					"union "+
					"SELECT swjg_mc,swjg_dm,0 as a ,0 as b,count(swjg_mc) as c "+
					"FROM         tasks where  status = 1 strWhere group by swjg_dm,swjg_mc "+
					") c group by swjg_dm,swjg_mc order by swjg_dm";
		//统计 全部
		String sql_all = "select sum(case when status<>-1 then 1 else 0 end)a,"+
							"sum(case when status=0 then 1  else 0 end)b,"+
							"sum(case when status=1 then 1 else 0  end)c from tasks where 1=1 strWhere";
		// 统计  精确到分局
		String sql_swjg = "select t.swjg_dm,sum(a)a,sum(b)b,sum(c)c,t2.swjgmc swjg_mc from " +
				"	(select  (case when substring(swjg_dm,8,11)<>'0000' then substring(swjg_dm,0,8)+'0000' else swjg_dm end) swjg_dm,"+ 
						"sum(case when status<>-1 then 1 else 0 end)a,"+
						"sum(case when status=0 then 1  else 0 end)b,"+
						"sum(case when status=1 then 1 else 0  end)c "+
						"from tasks where 1=1 strWhere group by swjg_dm)t left join dm_gy_swjg t2 on t.swjg_dm=t2.swjg_dm group by t.swjg_dm,t2.swjgmc";
		
		sql = sql.replace("strWhere", strWhere.toString());
		sql_all = sql_all.replace("strWhere", strWhere.toString());
		sql_swjg = sql_swjg.replace("strWhere", strWhere.toString());
//System.out.println(sql);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		JSONObject o = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			con = DbConnectionManager.getConnection();
			int i = -1;
			pstmt = con.prepareStatement(sql_all);
			res = pstmt.executeQuery();
			if(res.next()){
				JSONObject oRes = new JSONObject();
				oRes.put("id", "");
				oRes.put("swjg_mc", "全部统计");
				oRes.put("a", res.getInt("a"));
				oRes.put("b", res.getInt("b"));
				oRes.put("c", res.getInt("c"));
				oRes.put("swjg_dm", swjg_dm);
	  			float a = Float.parseFloat(oRes.getString("a"));
	  			float c = Float.parseFloat(oRes.getString("c"));
	  			oRes.put("d", (Math.round(c / a * 10000) / 100.00 + "%"));
				ja.put(oRes);
				i++;
			}
			if("nextSwjg".equals(type) && i != -1){//统计科室
				pstmt = con.prepareStatement(sql);
				res = pstmt.executeQuery();
			}else if(i != -1){
				pstmt = con.prepareStatement(sql_swjg);
				res = pstmt.executeQuery();
			}
			while(null!= res && res.next()){
				JSONObject oRes = new JSONObject();
				oRes.put("id", ++i);
				oRes.put("swjg_mc", res.getString("swjg_mc"));
				oRes.put("a", res.getString("a"));
				oRes.put("b", res.getString("b"));
				oRes.put("c", res.getString("c"));
				oRes.put("swjg_dm", res.getString("swjg_dm"));
	  			float a = Float.parseFloat(oRes.getString("a"));
	  			float c = Float.parseFloat(oRes.getString("c"));
	  			oRes.put("d", (Math.round(c / a * 10000) / 100.00 + "%"));
				ja.put(oRes);
			}
			o.put("topic", ja);
			o.put("topcount", ja.length());
			o.put("success", true);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, res);
		}
		if("excel".equals(t)){
			File file = ExcelUtil.Excel(ja, ExcelUtil.Count_SWJG_mapKey);
			try {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition","attachment; filename="+file.getName());
				response.sendRedirect("temp/"+file.getName());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return o.toString();
	}
	/**
	 *   执行人 - 任务完成
	 * @param request
	 * @return
	 */
	private String updateTask_finish(HttpServletRequest request) {
		String idList = request.getParameter("id");
		String statusNew = request.getParameter("status");
		String rwhk = request.getParameter("rwhk");
		Connection con = null;
		PreparedStatement pstmt = null;
		JSONObject o = new JSONObject();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement("update tasks set status=?,end_time=?,rwhk=? where zxr_dm = ? and id in("+idList+") and status<>-1");
			pstmt.setString(1, statusNew);
			pstmt.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(3, rwhk);
			pstmt.setInt(4, nowuser.getUserid());
			int res = pstmt.executeUpdate();
			if(res == 0){
				o.put("res", "任务只能由执行人完成.");
			}
			o.put("r", res);
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
	
	/**
	 * 修改任务 - 主要是执行人
	 * @param request
	 * @return
	 */
	private String updateTask(HttpServletRequest request) {
		String id = request.getParameter("id");
		String limit_time = request.getParameter("limit_time");
		String zgy_mc = request.getParameter("zgy_mc");
		String zxr_dm = request.getParameter("zxr_dm");
		String zgy_dm = request.getParameter("zgy_dm");
		String fxydcs = request.getParameter("fxydcs");
		String swjg_mc = request.getParameter("swjg_mc");
		String fxms = request.getParameter("fxms");
		String status = request.getParameter("status");
		String zx_swjg_dm = request.getParameter("zx_swjg_dm");
		String type = request.getParameter("type");
		if(null == limit_time || "".equals(limit_time)){
			limit_time = new java.sql.Date(new java.util.Date().getTime()).toString();
		}
		WLUser zgy = null;
		if(null == zxr_dm || "".equals(zxr_dm) || "-1".equals(zxr_dm)){ //如果执行人为空,则为专管员
			if(null != zgy_dm && !"".equals(zgy_dm)&& !"-1".equals(zgy_dm)){
				try {
					zgy = new WLUser(Integer.parseInt(zgy_dm));
					zxr_dm = zgy.getUserid()+"";
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (SystemException e1) {
					e1.printStackTrace();
				}
			}
		}
		if((null == zx_swjg_dm || "".equals(zx_swjg_dm)) && null != zgy){ //如果执行机关为空,则为专管员机关
			zx_swjg_dm = zgy.getUserkebe();
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		JSONObject o = new JSONObject();
		try {
			con = DbConnectionManager.getConnection();
			if("2".equals(type)){
				pstmt = con.prepareStatement("update tasks set zxr_dm=?,limit_time=? where  id in("+id+") and (((begin_time is null or datediff(hour,begin_time,getdate())<=" +changeHOUR+") and status<1) or status=-1)");
				pstmt.setString(1, zxr_dm);
				pstmt.setString(2, limit_time);
			}else{
				pstmt = con.prepareStatement("update tasks set zx_swjg_dm=?,zxr_dm=?,limit_time=?,fxms=? where  id in("+id+") and (((begin_time is null or datediff(hour,begin_time,getdate())<="+changeHOUR+") and status<1) or status=-1)");
				pstmt.setString(1, zx_swjg_dm);
				pstmt.setString(2, zxr_dm);
				pstmt.setString(3, limit_time);
				pstmt.setString(4, fxms);
			}
			int res = pstmt.executeUpdate();
			if("0".equals(status)){// 任务开始后修改执行人,重新设置任务起始时间
				pstmt = con.prepareStatement("update tasks set status=?,begin_time=? where id in("+id+") and status=?");
				pstmt.setInt(1, 0);
				pstmt.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				pstmt.setInt(3, -1);
				int res2 = pstmt.executeUpdate();
			}
			if("-1".equals(status)){ //下发任务前修改执行人,任务起始时间归0
				pstmt = con.prepareStatement("update tasks set status=?,begin_time=NULL where id in("+id+") and status=?");
				pstmt.setString(1, status);
				pstmt.setInt(2, 0);
				int res2 = pstmt.executeUpdate();
			}
			if(res == 0){
				o.put("res", "任务发布超过"+changeHOUR+"小时,不可更换执行人.如遇特殊情况可申请数据管理组审核通过再修改.");
			}else{
				o.put("r", res);
				o.put("success", true);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
		return o.toString();
	}
	/**
	 * 批量下发任务
	 * @param request
	 * @return
	 */
	private String upDateStatus(HttpServletRequest request) {
		String idList = request.getParameter("idList");
		String statusNew = request.getParameter("statusNew");
		String statusOld = request.getParameter("statusOld");
		String allTask = request.getParameter("allTask");
		Connection con = null;
		PreparedStatement pstmt = null;
		JSONObject o = new JSONObject();
		try {
			con = DbConnectionManager.getConnection();
			if("yes".equals(allTask)){
				pstmt = con.prepareStatement("update tasks set status=?,begin_time=? where imp_userid="+nowuser.getUserid()+" and status=? and (zxr_dm is not null and zxr_dm <>'' and zxr_dm<>-1) and (zx_swjg_dm is not null and zx_swjg_dm<>'')");
			}else{
				pstmt = con.prepareStatement("update tasks set status=?,begin_time=? where id in("+idList+") and status=? and (zxr_dm is not null and zxr_dm <>'' and zxr_dm<>-1) and (zx_swjg_dm is not null and zx_swjg_dm<>'')");
			}
			pstmt.setString(1, statusNew);
			pstmt.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(3, statusOld);
			int res = pstmt.executeUpdate();
			if(res == 0){
				o.put("res","任务状态不正确或无任务可推送.");
			}
			o.put("r", res);
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
	 * 查询任务计划
	 * @param request
	 * @param response
	 * @return
	 */
	private String showInfo(HttpServletRequest request,HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String nsrmc = request.getParameter("nsrmc");
		String nsrsbh = request.getParameter("nsrsbh");
		String zgy_mc = request.getParameter("zgy_mc");
		String zxr_mc = request.getParameter("zxr_mc");
		String begin_time_start = request.getParameter("begin_time_start");
		String begin_time_end = request.getParameter("begin_time_end");
		String dir = request.getParameter("dir");
		String sort = request.getParameter("sort");

		String fxzb_dm = request.getParameter("fxzb_dm");
		String swjg_dm = request.getParameter("swjg_dm");
		String zx_swjg_dm = request.getParameter("zx_swjg_dm");
		String fxms = request.getParameter("fxms");
		String checked = request.getParameter("checked");
		String change_zxr = request.getParameter("change_zxr");
		String rwhk = request.getParameter("rwhk");
		String limit_time_s = request.getParameter("limit_time_s");
		String limit_time_e = request.getParameter("limit_time_e");
		String end_time_s = request.getParameter("end_time_s");
		String end_time_e = request.getParameter("end_time_e");
		String imp_swjg_dm = request.getParameter("imp_swjg_dm");
		String imp_userid = request.getParameter("imp_userid");
		String t = request.getParameter("t");
		int shijuxiafa = ParameterUtils.getIntParameter(request, "shijuxiafa", -2);	
		int status = ParameterUtils.getIntParameter(request, "status", -1);	
		int type = ParameterUtils.getIntParameter(request, "type", 0);
		
		boolean dirBoolean = "DESC".equals(dir)?true:false;
		sort = (null!=sort&&!"".equals(sort))?sort:"id";
		
		String sJson = "";
		StringBuffer strWhere = new StringBuffer(" 1=1 ");
		String userkebe = nowuser.getUserkebe();
		
		if(2 == type){//如果是办事员就按照userid查询任务
			strWhere.append(" and status<>-1 and (zxr_dm='"+nowuser.getUserid()+"' or zgy_dm='"+nowuser.getUserid()+"') ");
		}else if(3 == type){ //组长查询
			strWhere.append(" and status<>-1 and zx_swjg_dm='"+nowuser.getUserkebe()+"' ");
		}else{
			if(null != userkebe){
				strWhere.append(" and (imp_userid = " + nowuser.getUserid() + " or zx_swjg_dm like '" +  getSwjg_Like_Dm(userkebe,true) + "%' or swjg_dm like '"+getSwjg_Like_Dm(userkebe,true)+"%')");
			}
		}
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;

		if (shijuxiafa != -2) {
			if(shijuxiafa == 1){
				strWhere.append(" and shijuxiafa <> 0");
			}else{
				strWhere.append(" and shijuxiafa =0 ");
			}
		}
		if (null != nsrmc && !nsrmc.equals("")) {
			strWhere.append(" and nsrmc like '%"+nsrmc+"%' ");
		}
		if (null != imp_swjg_dm && !imp_swjg_dm.equals("")) {
			strWhere.append(" and imp_swjg_dm like '"+TasksData.getSwjg_Like_Dm(imp_swjg_dm)+"%' ");
		}
		if (null != imp_userid && !imp_userid.equals("")) {
			strWhere.append(" and imp_userid ='"+imp_userid+"' ");
		}
		if (null != nsrsbh && !nsrsbh.equals("")) {
			strWhere.append(" and nsrsbh like '"+nsrsbh+"%' ");
		}
		if (null != zgy_mc && !zgy_mc.equals("")) {
			strWhere.append(" and zgy_mc like '%"+zgy_mc+"%' ");
		}
		if (null != zxr_mc && !zxr_mc.equals("")) {
			strWhere.append(" and zxr_dm in (select userid from wluser where username like '%"+zxr_mc+"%') ");
		}
		if (null != begin_time_start && !begin_time_start.equals("") && null != begin_time_end && !begin_time_end.equals("")) {
			strWhere.append(" and (begin_time between '"+begin_time_start+"' and '"+begin_time_end+" 23:59' or status<0) ");
		}
		if (-2 != status) {
			strWhere.append(" and status = "+status+" ");
		}
//		String fxzb_dm = request.getParameter("fxzb_dm");
//		String swjg_dm = request.getParameter("swjg_dm");
//		String zx_swjg_dm = request.getParameter("zx_swjg_dm");
//		String fxms = request.getParameter("fxms");
//		String checked = request.getParameter("checked");
//		String change_zxr = request.getParameter("change_zxr");
//		String rwhk = request.getParameter("rwhk");
//		String limit_time_s = request.getParameter("limit_time_s");
//		String limit_time_e = request.getParameter("limit_time_e");
//		String end_time_s = request.getParameter("end_time_s");
//		String end_time_e = request.getParameter("end_time_e");
		if (null != fxzb_dm && !"".equals(fxzb_dm)) {
			strWhere.append(" and fxzb_dm like '"+getFxzb_Dm_Like(fxzb_dm)+"%' ");
		}
		if (null != swjg_dm && !"".equals(swjg_dm)) {
			strWhere.append(" and swjg_dm like '"+ getSwjg_Like_Dm(swjg_dm)+"%' ");
		}
		if (null != zx_swjg_dm && !"".equals(zx_swjg_dm)) {
			strWhere.append(" and zx_swjg_dm like '"+getSwjg_Like_Dm(zx_swjg_dm)+"%' ");
		}
		if (null != fxms && !"".equals(fxms)) {
			strWhere.append(" and fxms like '%"+fxms+"%' ");
		}
		if (null != checked && !"".equals(checked) && !"-1".equals(checked)) {
			strWhere.append(" and checked = "+checked);
		}
		if (null != change_zxr && !"".equals(change_zxr) && !"-1".equals(change_zxr)) {
			strWhere.append(" and change_zxr = "+change_zxr);
		}
		if (null != rwhk && !"".equals(rwhk)) {
			strWhere.append(" and rwhk like '%"+rwhk+"%' ");
		}
		if (null != limit_time_s && !limit_time_s.equals("") && null != limit_time_e && !limit_time_e.equals("")) {
			strWhere.append(" and (limit_time between '"+limit_time_s+"' and '"+limit_time_e+" 23:59') ");
		}
		if (null != end_time_s && !end_time_s.equals("") && null != end_time_e && !end_time_e.equals("")) {
			strWhere.append(" and (end_time between '"+end_time_s+"' and '"+end_time_e+" 23:59') ");
		}
//System.out.println("strWhere---"+strWhere );
		//userType用来存放所属辖区
		//System.out.println(Integer.toBinaryString(14));
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForShow("view_tasks",
				"  id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, limit_time, " +
				"fxydcs, status, swjg_dm, zgy_dm, task_man,imp_date,imp_userid,zxr_dm,rwhk,zxr_mc,[checked]," +
				"zx_swjg_dm,zx_swjg_mc,fxzb_dm,change_zxr,jjyy,shijuxiafa,jieshouren, jieshoudate,jieshourenmc,jieshourenswjg,imp_username,imp_swjg_mc", sort, pageSize, index,
				dirBoolean, strWhere.toString());
		if("excel".equals(t)){
			try {
				JSONObject o = new JSONObject(sJson);
				JSONArray ja = o.getJSONArray("topics");
				File file = ExcelUtil.Excel(ja, ExcelUtil.Tasks_Column_mapKey);
				try {
					response.setContentType("application/octet-stream");
					response.setHeader("Content-disposition","attachment; filename="+file.getName());
					response.sendRedirect("temp/"+file.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				PrintWriter out;
				try {
					out = response.getWriter();
					//System.out.println(sReturnStr);
					out.print(sJson);
					out.flush();
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return sJson;
	}
	/**
	 * 获取机关代码去末尾0
	 * @param swjg_dm
	 * @param sj 是否只要上级代码
	 * @return
	 */
	public static String getSwjg_Like_Dm(String swjg_dm, boolean sj){
		if(null == swjg_dm){
			return null;
		}
		int n = swjg_dm.length();
		for(int i=(n-2),j=n;i>=0;i--,i--,j--,j--){
			if(!swjg_dm.substring(i, j).equals("00")){
				if(j == n && sj){
					return getSwjg_Like_Dm(TaskManData.getSwjg_SJ(swjg_dm),false);
				}
				return swjg_dm.substring(0, j);
			}
		}
		return swjg_dm;
	}
	public static String getSwjg_Like_Dm(String swjg_dm){
		if(null == swjg_dm){
			return null;
		}
		int n = swjg_dm.length();
		for(int i=(n-2),j=n;i>=0;i--,i--,j--,j--){
			if(!swjg_dm.substring(i, j).equals("00")){
//				if(j == (n-2)){
//					return getSwjg_Like_Dm(TaskManData.getSwjg_SJ(swjg_dm));
//				}
//System.out.println(swjg_dm.substring(0, j));
				return swjg_dm.substring(0, j);
			}
		}
		return swjg_dm;
	}
	/**
	 * 获取风险指标代码去末尾0
	 * @param swjg_dm
	 * @return
	 */
	public static String getFxzb_Dm_Like(String fxzb_dm){
		if(null == fxzb_dm){
			return null;
		}
		int n = fxzb_dm.length();
		for(int i=(n-1);i>=0;i--){
			if(fxzb_dm.charAt(i) != '0'){
				n = i+1;
				return fxzb_dm.substring(0, n);
			}
		}
		return fxzb_dm;
	}
	/**
	 * 根据id返回file信息sjon格式的
	 * 
	 */
	private String getFileById(HttpServletRequest request) {
		String id = request.getParameter("fileid");
		String sJson = "";
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForCombox(Tasks.TABLENAME, "  id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, limit_time, fxydcs, status, swjg_dm, zgy_dm, task_man", "id=" + id, 0,
				"data");
		return sJson;
	}	
}
