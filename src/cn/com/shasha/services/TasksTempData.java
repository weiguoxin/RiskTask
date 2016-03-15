package cn.com.shasha.services;

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
/**
 * 预处理任务 操作服务类
 * @author _wgx
 *
 */
public class TasksTempData  extends HttpServlet {
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
			sReturnStr = showInfo(request);
		} else if (sAction.equalsIgnoreCase("getFile")) {
			sReturnStr = getFileById(request);
		} else if (sAction.equalsIgnoreCase("upDateStatus")) { //修改任务状态
			sReturnStr = upDateStatus(request);
		}else if (sAction.equalsIgnoreCase("updateTask")) {//更新任务
			sReturnStr = updateTask(request);
		}else if (sAction.equalsIgnoreCase("newTask")) { //新建任务
			sReturnStr = newTask(request);
		}else if (sAction.equalsIgnoreCase("del")) { //删除任务
			sReturnStr = delTasks(request);
		}else if (sAction.equalsIgnoreCase("turnTasks")) { //批量转入任务
			sReturnStr = turnTasks(request);
		}
		PrintWriter out = response.getWriter();
//System.out.println(sReturnStr);
		out.print(sReturnStr);
		out.flush();
		out.close();
	}
	
	String TURN_SQL = "insert into tasks(nsrsbh, nsrmc, swjg_mc,zgy_mc, fxzb, fxms, limit_time, "+
				" fxydcs, swjg_dm, zgy_dm,fxzb_dm, imp_date, imp_userid, zxr_dm, zx_swjg_dm"+
				" ) select nsrsbh, nsrmc, swjg_mc,zgy_mc, fxzb, fxms, limit_time, "+
				" fxydcs, swjg_dm, zgy_dm,fxzb_dm, imp_date, imp_userid, zxr_dm, zx_swjg_dm from tasks_temp "+
				" where imp_userid = ? and status =-1 and nsrmc is not null and swjg_mc is not null and zgy_mc is not null and  fxzb is not null and fxms is not null and "+
				" limit_time is not null and fxydcs is not null and swjg_dm is not null and fxzb_dm is not null and "+
				"  zx_swjg_dm is not null and nsrmc <>'' and swjg_mc  <>'' and zgy_mc  <>'' and  fxzb  <>'' and fxms  <>'' and "+
				" limit_time  <>'' and fxydcs  <>'' and swjg_dm  <>'' and fxzb_dm  <>''"+
				"  and zx_swjg_dm  <>'' ";
	String UPDATE_TURN_SQL = "update a set status=0 from tasks_temp a"+
				" where imp_userid = ? and status =-1 and  nsrmc is not null and swjg_mc is not null and zgy_mc is not null and  fxzb is not null and fxms is not null and "+
				" limit_time is not null and fxydcs is not null and swjg_dm is not null and fxzb_dm is not null and "+
				"  zx_swjg_dm is not null and nsrmc <>'' and swjg_mc  <>'' and zgy_mc  <>'' and  fxzb  <>'' and fxms  <>'' and "+
				" limit_time  <>'' and fxydcs  <>'' and swjg_dm  <>'' and fxzb_dm  <>''  "+
				"  and zx_swjg_dm  <>'' ";
	private String turnTasks(HttpServletRequest request) {
	
		Connection con = null;
		PreparedStatement pstmt = null;
		con = DbConnectionManager.getConnection();
		JSONObject o = new JSONObject();
		try {
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(TURN_SQL);
			pstmt.setInt(1, nowuser.getUserid());
			int r = pstmt.executeUpdate();
			pstmt = con.prepareStatement(UPDATE_TURN_SQL);
			pstmt.setInt(1, nowuser.getUserid());
			int rr = pstmt.executeUpdate();
			con.commit();
			o.put("insert_r", r);
			o.put("update_r", rr);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}finally{
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
		String del_sql = "delete tasks_temp where status=-1 strWhere ";
		StringBuffer strWhere = new StringBuffer();
		int r = -1;
		if(null != userkebe){
			strWhere.append(" and (swjg_dm like '" +  TasksData.getSwjg_Like_Dm(userkebe,true) + "%' or swjg_dm is null or imp_userid="+nowuser.getUserid()+") ");
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
		ps.setSwjg_mc(swjg_mc);
		WLUser zgy = null;
		try {
			zgy = new WLUser(Integer.parseInt(zgy_dm));
			ps.setZgy_mc(zgy.getUsername());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		ps.setFxzb(fxzb);
		ps.setFxzb_dm(fxzb_dm);
		ps.setFxms(fxms);
		ps.setSwjg_dm(swjg_dm);
		ps.setZx_swjg_dm(zx_swjg_dm);
		ps.setZgy_dm(zgy_dm); 
		if(null != zxr_dm && !"".equals(zxr_dm)){
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
	 * 修改任务
	 * @param request
	 * @return
	 */
	private String updateTask(HttpServletRequest request) {
		String id = request.getParameter("id");
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
		TasksTemp ps = new TasksTemp();
		ps.setId(Integer.parseInt(id));
		ps.setNsrsbh(nsrsbh);
		ps.setNsrmc(nsrmc);
		ps.setSwjg_mc(swjg_mc);
		ps.setFxzb(fxzb);
		ps.setFxzb_dm(fxzb_dm);
		ps.setFxms(fxms);
		ps.setSwjg_dm(swjg_dm);
		ps.setFxydcs(fxydcs);
		if(null != limit_date && !"".equals(limit_date)){
			ps.setLimit_time(new Timestamp(DateTime.strToDate(limit_date).getTime()));
		}
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
		if(null != zxr_dm && !"".equals(zxr_dm) && !"-1".equals(zxr_dm)){ //如果执行人为空,则为专管员
			ps.setZxr_dm(Integer.parseInt(zxr_dm));
		}else if(TaskManData.hasZgy_fxzb(fxzb_dm) && null != zgy){
			ps.setZxr_dm(zgy.getUserid());
		}
		if(null != zx_swjg_dm && !"".equals(zx_swjg_dm)){ //如果执行机关为空,则为专管员机关
			ps.setZx_swjg_dm(zx_swjg_dm);
		}else if(TaskManData.hasZgy_fxzb(fxzb_dm) && null != zgy){
			ps.setZx_swjg_dm(zgy.getUserkebe());
		}
		int r = ps.upDateSelf(null);
		JSONObject o = new JSONObject();
		try {
			o.put("r", r);
			o.put("res", "数据错误.");
		} catch (JSONException e) {
			e.printStackTrace();
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
		Connection con = null;
		PreparedStatement pstmt = null;
		JSONObject o = new JSONObject();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement("update tasks_temp set status=?,begin_time=? where id in("+idList+") and status=? and (zxr_dm is not null and zxr_dm <>'' and zxr_dm<>-1)");
			pstmt.setString(1, statusNew);
			pstmt.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(3, statusOld);
			int res = pstmt.executeUpdate();
			if(res == 0){
				o.put("res","未指定执行人或任务状态不正确.");
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
	 * 加载所有任务
	 * d
	 */
	private String showInfo(HttpServletRequest request) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String nsrmc = request.getParameter("nsrmc");
		String nsrsbh = request.getParameter("nsrsbh");
		String zgy_mc = request.getParameter("zgy_mc");
		String zxr_mc = request.getParameter("zxr_mc");
		String shijuxiafa = request.getParameter("shijuxiafa");
		String begin_time_start = request.getParameter("begin_time_start");
		String begin_time_end = request.getParameter("begin_time_end");
		String zx_swjg_dm = request.getParameter("zx_swjg_dm");
		String jieshou_time_start = request.getParameter("jieshou_time_start");
		String jieshou_time_end = request.getParameter("jieshou_time_end");
		String jieshou_status = request.getParameter("jieshou_status");
		
		int status = ParameterUtils.getIntParameter(request, "status", -1);	
		int type = ParameterUtils.getIntParameter(request, "type", 0);	
		
		String sJson = "";
		StringBuffer strWhere = new StringBuffer(" 1=1 ");
		String userkebe = nowuser.getUserkebe();
		
		if(4 == type){ //查询未准备好的数据
			strWhere.append("  and imp_userid="+nowuser.getUserid());
		}
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;

		if (null != shijuxiafa && !shijuxiafa.equals("")) {
			strWhere.append(" and shijuxiafa = "+shijuxiafa+" ");
		}
		if (null != nsrmc && !nsrmc.equals("")) {
			strWhere.append(" and nsrmc like '%"+nsrmc+"%' ");
		}
		if (null != nsrsbh && !nsrsbh.equals("")) {
			strWhere.append(" and nsrsbh like '"+nsrsbh+"%' ");
		}
		if (null != jieshou_time_start && !jieshou_time_start.equals("")) {
			strWhere.append(" and jieshoudate > '"+jieshou_time_start+"' ");
		}
		if (null != jieshou_time_end && !jieshou_time_end.equals("")) {
			strWhere.append(" and jieshou_time_end < '"+jieshou_time_end+"' ");
		}
		if (null != zx_swjg_dm && !zx_swjg_dm.equals("")) {
			strWhere.append(" and zx_swjg_dm like '"+ TasksData.getSwjg_Like_Dm(zx_swjg_dm,false)+"%' ");
		}
		if (null != zgy_mc && !zgy_mc.equals("")) {
			strWhere.append(" and zgy_mc like '%"+zgy_mc+"%' ");
		}
		if (null != zxr_mc && !zxr_mc.equals("")) {
			strWhere.append(" and zxr_dm in (select userid from wluser where username like '%"+zxr_mc+"%') ");
		}
		if (null != begin_time_start && !begin_time_start.equals("") && null != begin_time_end && !begin_time_end.equals("")) {
			strWhere.append(" and imp_date between '"+begin_time_start+"' and '"+begin_time_end+" 23:59' ");
		}
		if (-2 != status) {
			strWhere.append(" and status = "+status+" ");
		}if (null != jieshou_status && !"".equals(jieshou_status) && !"-2".equals(jieshou_status)) {
			if("-1".equals(jieshou_status)){
				strWhere.append(" and jieshouren is not null and jieshoudate is not null ");
			}else{
				strWhere.append(" and (jieshouren is null or jieshouren='') and (jieshoudate is null or jieshoudate ='') ");
			}
		}
		
//System.out.println("strWhere---"+strWhere );
		//userType用来存放所属辖区
		//System.out.println(Integer.toBinaryString(14));
		DataCenter odata = new DataCenter();
		sJson = odata.ResultSetToJsonForShow("view_tasks_temp",
				"  id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, limit_time, " +
				"fxydcs, status, swjg_dm, zgy_dm, task_man,imp_date,imp_userid,zxr_dm,rwhk,zxr_mc,[checked]," +
				"zx_swjg_dm,zx_swjg_mc,fxzb_dm,shijuxiafa,jieshouren, jieshoudate,jieshourenmc,jieshourenswjg,imp_username,imp_swjg_mc", "id", pageSize, index,
				false, strWhere.toString());
//sJson = sJson.replace("limit_time", "l_time");
//System.out.println("User show json--"+sJson);
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
		for(int i=(n-1);i>=0;i--){
			if(swjg_dm.charAt(i) != '0'){
				if(i == 10 && sj){
					return getSwjg_Like_Dm(TaskManData.getSwjg_SJ(swjg_dm),false);
				}
				n = i+1;
				return swjg_dm.substring(0, n);
			}
		}
		return swjg_dm;
	}
	public static String getSwjg_Like_Dm(String swjg_dm){
		if(null == swjg_dm){
			return null;
		}
		int n = swjg_dm.length();
		for(int i=(n-1);i>=0;i--){
			if(swjg_dm.charAt(i) != '0'){
				if(i == 10){
					return getSwjg_Like_Dm(TaskManData.getSwjg_SJ(swjg_dm));
				}
				n = i+1;
				return swjg_dm.substring(0, n);
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
		sJson = odata.ResultSetToJsonForCombox(TasksTemp.TABLENAME, "  id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, limit_time, fxydcs, status, swjg_dm, zgy_dm, task_man", "id=" + id, 0,
				"data");
		return sJson;
	}	
}
