package cn.com.shasha.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wgx.util.excel.ExcelUtil;
import com.wgx.util.json.JSONArray;
import com.wgx.util.json.JSONException;
import com.wgx.util.json.JSONObject;

import cn.com.info21.database.DbConnectionManager;
import cn.com.shasha.sys.WLUser;
import cn.com.shasha.utils.DataCenter;

import cn.com.shasha.services.TasksData;
public class XingZhengTasksServices   extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WLUser nowuser = null;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		nowuser = ((WLUser)request.getSession().getAttribute("Session_User"));
		String act = request.getParameter("act");
//System.out.println(act);
		String res = null;
		if("newXingZhengTask".equals(act)){
			res = newXingZhengTask( request,  response);
		}else if("show".equals(act)){
			res = showTasks(request, response);
		}else if("updateXingZhengTask".equals(act)){
			res = updateXingZhengTask(request, response);
		}else if("del".equals(act)){
			res = del(request, response);
		}else if("Count_By_SWJG".equals(act)){// 根据税务机关统计
			res = CountTasksBySWJG(request, response);
		}else if("Count_By_SWJG_DM".equals(act)){// 根据人头统计
			res = count_Tasks_SWJG_DM(request, response);
		}

		PrintWriter out = response.getWriter();
		out.print(res);
		out.close();
	}

	/**
	 * 统计
	 * @return
	 */
	private String CountTasksBySWJG(HttpServletRequest request, HttpServletResponse response) {
		String task_date_start = request.getParameter("task_date_start");
		String task_date_end = request.getParameter("task_date_end");
		String jieshouswjg = request.getParameter("jieshouswjg");
		String jieshourenid = request.getParameter("jieshourenid");
		String xiafaswjg = request.getParameter("xiafaswjg");
		String xiafarenid = request.getParameter("xiafarenid");
		String type = request.getParameter("type");
		String t = request.getParameter("t");
		StringBuffer strWhere = new StringBuffer();
		String userkebe = nowuser.getUserkebe();


		if(null != userkebe){// 判断查询人权限
			strWhere.append(" and jieshouswjg like '" +  TasksData.getSwjg_Like_Dm(userkebe,true) + "%' ");
		}
		if(null != xiafarenid && !"".equals(xiafarenid)){
			strWhere.append(" and xiafarenid = " +  xiafarenid + " ");
		}
		if(null != jieshourenid && !"".equals(jieshourenid)){
			strWhere.append(" and jieshourenid = '" +  jieshourenid + "' ");
		}
		if(null != xiafaswjg && !"".equals(xiafaswjg)){
			strWhere.append(" and xiafaswjg = '" + xiafaswjg +  "' ");
		}
		if(null != jieshouswjg && !"".equals(jieshouswjg)){
			strWhere.append(" and jieshouswjg like '" +  TasksData.getSwjg_Like_Dm(jieshouswjg,true) + "%' ");
		}
		if(null != task_date_start && !"".equals(task_date_start)){
			strWhere.append(" and begindate >= '" + task_date_start +  "' ");
		}
		if(null != task_date_end && !"".equals(task_date_end)){
			strWhere.append(" and begindate <= '" + task_date_end +  " 23:59' ");
		}
		//查询统计 - 精确到部门
		String sql = "select x.swjgmc as swjg_mc,c.swjg_dm,sum(a)a,sum(b)b,sum(c) c from ( "+
					"SELECT jieshouswjg as swjg_dm,count(jieshouswjg) as a ,0 as b,0 as c  "+
					"FROM xingzhengtasks where  status <>-1 strWhere group by jieshouswjg "+
					"union "+
					"SELECT jieshouswjg as swjg_dm,0 as a ,count(jieshouswjg) as b,0 as c  "+
					"FROM xingzhengtasks where  status = 0  strWhere group by jieshouswjg "+
					"union "+
					"SELECT jieshouswjg as swjg_dm,0 as a ,0 as b,count(jieshouswjg) as c  "+
					"FROM xingzhengtasks where  status = 1 strWhere group by jieshouswjg "+
					") c  left join dm_GY_SWJG x on c.swjg_dm=x.swjg_dm group by c.swjg_dm,x.swjgmc order by swjg_dm";
		//统计 全部
		String sql_all = "select sum(case when status<>-1 then 1 else 0 end)a,"+
							"sum(case when status=0 then 1  else 0 end)b,"+
							"sum(case when status=1 then 1 else 0  end)c from xingzhengtasks where 1=1 strWhere";
		// 统计  精确到分局
		String sql_swjg = "select t.swjg_dm,sum(a)a,sum(b)b,sum(c)c,t2.swjgmc swjg_mc from " +
				"	(select  (case when substring(jieshouswjg,8,11)<>'0000' then substring(jieshouswjg,0,8)+'0000' else jieshouswjg end) swjg_dm,"+ 
						"sum(case when status<>-1 then 1 else 0 end)a,"+
						"sum(case when status=0 then 1  else 0 end)b,"+
						"sum(case when status=1 then 1 else 0  end)c "+
						"from xingzhengtasks where 1=1 strWhere group by jieshouswjg)t left join dm_gy_swjg t2 on t.swjg_dm=t2.swjg_dm group by t.swjg_dm,t2.swjgmc";
		
		sql = sql.replace("strWhere", strWhere.toString());
		sql_all = sql_all.replace("strWhere", strWhere.toString());
		sql_swjg = sql_swjg.replace("strWhere", strWhere.toString());
//System.out.println(sql_swjg);
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
				oRes.put("swjg_dm", jieshouswjg);
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

	private String del(HttpServletRequest request, HttpServletResponse response) {
		String idList = request.getParameter("idList");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		con = DbConnectionManager.getConnection();
		int r = 0;
		JSONObject jRes = new JSONObject();
		try {
			ps = con.prepareStatement("delete from XingZhengTasks where id in("+idList+") and status =0");
			r = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				jRes.put("res", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}finally{
			DbConnectionManager.close(con, ps, null, res);
		}
		try {
			jRes.put("success", true);
			jRes.put("r", r);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jRes.toString();
	}

	private static String SQL_UPDATE = 		  "update xingzhengtasks set title=?,jieshouswjg=?,jieshourenid=?,content=?,articles=? where id=? and status = 0";
	private static String SQL_UPDATE_FINISH = "update xingzhengtasks set title=?,jieshouswjg=?,jieshourenid=?,content=?,articles=?,status=1,file2=?,enddate=getdate() where id=? and status = 0";
	
	/**
	 * 修改任务信息,下发后不可修改
	 * @param request
	 * @param response
	 * @return
	 */
	private String updateXingZhengTask(HttpServletRequest request,
			HttpServletResponse response) {

		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String jieshouswjg =request.getParameter("jieshouswjg");
		String jieshourenid =request.getParameter("jieshourenid");
		String content = request.getParameter("content");
		String articles = request.getParameter("articles");
		String type = request.getParameter("type");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		con = DbConnectionManager.getConnection();
		int r = 0;
		try {
			if("finish".equals(type)){
				String []fujianid = request.getParameterValues("fujianid");
				StringBuffer fujian = new StringBuffer();
				if(null != fujianid)
				for(int i=0;i<fujianid.length;i++){
					if(i==0){
						fujian.append(fujianid[i]);
					}else{
						fujian.append(",");
						fujian.append(fujianid[i]);
					}
				}
				ps = con.prepareStatement(SQL_UPDATE_FINISH);
				ps.setString(1, title);
				ps.setString(2, jieshouswjg);
				ps.setString(3, jieshourenid);
				ps.setString(4, content);
				ps.setString(5, articles);
				ps.setString(6, fujian.toString());
				ps.setString(7, id);
			}else{
				ps = con.prepareStatement(SQL_UPDATE);
				ps.setString(1, title);
				ps.setString(2, jieshouswjg);
				ps.setString(3, jieshourenid);
				ps.setString(4, content);
				ps.setString(5, articles);
				ps.setString(6, id);
			}
			
			r = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbConnectionManager.close(con, ps, null, res);
		}
		JSONObject jRes = new JSONObject();
		try {
			jRes.put("success", true);
			jRes.put("r", r);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jRes.toString();
	}


	public String showTasks(HttpServletRequest request, HttpServletResponse response){
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String dir = request.getParameter("dir");
		boolean dirBoolean = "DESC".equals(dir)?true:false;
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;
		String sort = request.getParameter("sort");

		if (null == sort || sort.equals("")) {
			sort = "id";
		}
		
		String title = request.getParameter("title");
		String begindate = request.getParameter("begindate");
		String enddate = request.getParameter("enddate");
		String jieshouswjg = request.getParameter("jieshouswjg");
		String jieshourenid = request.getParameter("jieshourenid");
		String status = request.getParameter("status");
		
		
		StringBuffer strWhere = new StringBuffer(" (xiafarenid="+nowuser.getUserid()+ " or jieshourenid="+nowuser.getUserid()+")");
		
		if (null != title && !title.equals("")) {
			strWhere.append(" and title like '%"+title+"%' ");
		}
		if (null != begindate && !begindate.equals("") && null != enddate && !enddate.equals("")) {
			strWhere.append(" and begindate between '"+begindate+"' and '"+enddate+" 23:59' ");
		}
		if (null != jieshouswjg && !jieshouswjg.equals("")) {
			strWhere.append(" and jieshouswjg like '"+TasksData.getSwjg_Like_Dm(jieshouswjg)+"%' ");
		}
		if (null != jieshourenid && !jieshourenid.equals("")) {
			strWhere.append(" and jieshourenid = '"+jieshourenid+"' ");
		}
		
		if (null != status && !"-2".equals(status) && !"".equals(status)) {
			strWhere.append(" and status = '"+status+"' ");
		}
		System.out.println(strWhere);
		DataCenter odata = new DataCenter();
		String sJson = odata.ResultSetToJsonForShow("View_XingzhengTasks",
				"  id, title, [content], begindate, enddate, xiafarenid, xiafaswjg, jieshourenid, jieshouswjg, articles, status, xiafarenmc, xiafarenswjgmc, jieshourenmc," 
                 +"jieshourenswjgmc", sort, pageSize, index,
				dirBoolean, strWhere.toString());
		return sJson;
	}
	
	private static String  SQL_NEW = "insert into XingZhengTasks" +
			"(title, [content], begindate, xiafarenid, xiafaswjg, jieshourenid, jieshouswjg,  status, file1)" +
			"  values(?,?,?,?,?,?,?,?,?)";
	/**
	 * 新建行政任务
	 * @param request
	 * @param response
	 * @return
	 */
	public String newXingZhengTask(HttpServletRequest request, HttpServletResponse response){
		String title = request.getParameter("title");
		String jieshouswjg =request.getParameter("jieshouswjg");
		String jieshourenid =request.getParameter("jieshourenid");
		String content = request.getParameter("content");
		String []fujianid = request.getParameterValues("fujianid");
		StringBuffer fujian = new StringBuffer();
		if(null != fujianid)
		for(int i=0;i<fujianid.length;i++){
			if(i==0){
				fujian.append(fujianid[i]);
			}else{
				fujian.append(",");
				fujian.append(fujianid[i]);
			}
		}
		int jieshourenid1 = -1;
		if(null == jieshourenid || "".equals(jieshourenid)){
			System.out.println("jieshourenid null:"+jieshourenid);
			jieshourenid1 = getUserIdBySwjg(jieshouswjg);
		}else{
			jieshourenid1 = Integer.parseInt(jieshourenid);
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		con = DbConnectionManager.getConnection();
		int r = 0;
		try {
			ps = con.prepareStatement(SQL_NEW);
			ps.setString(1, title);
			ps.setString(2, content);
			ps.setTimestamp(3, new Timestamp(new Date().getTime()));
			ps.setInt(4, nowuser.getUserid());
			ps.setString(5, nowuser.getUserkebe());
			ps.setInt(6, jieshourenid1);
			ps.setString(7, jieshouswjg);
			ps.setString(8, "0");//任务状态 0执行中,1为完成
			ps.setString(9, fujian.toString());
			r = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbConnectionManager.close(con, ps, null, res);
		}
		JSONObject jRes = new JSONObject();
		try {
			jRes.put("success", true);
			jRes.put("r", r);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jRes.toString();
	}
	
	/**
	 * 统计任务 - 按人头统计
	 * @param request
	 * @return
	 */
	public  String count_Tasks_SWJG_DM(HttpServletRequest request, HttpServletResponse response) {
		String task_date_start = request.getParameter("task_date_start");
		String task_date_end = request.getParameter("task_date_end");
		String jieshouswjg = request.getParameter("jieshouswjg");
		String jieshourenid = request.getParameter("jieshourenid");
		String xiafaswjg = request.getParameter("xiafaswjg");
		String xiafarenid = request.getParameter("xiafarenid");
		String type = request.getParameter("type");
		String t = request.getParameter("t");
		StringBuffer strWhere = new StringBuffer();
		String userkebe = nowuser.getUserkebe();


		if(null != userkebe){// 判断查询人权限
			strWhere.append(" and jieshouswjg like '" +  TasksData.getSwjg_Like_Dm(userkebe,true) + "%' ");
		}
		if(null != xiafarenid && !"".equals(xiafarenid)){
			strWhere.append(" and xiafarenid = " +  xiafarenid + " ");
		}
		if(null != jieshourenid && !"".equals(jieshourenid)){
			strWhere.append(" and jieshourenid = '" +  jieshourenid + "' ");
		}
		if(null != xiafaswjg && !"".equals(xiafaswjg)){
			strWhere.append(" and xiafaswjg = '" + xiafaswjg +  "' ");
		}
		if(null != jieshouswjg && !"".equals(jieshouswjg)){
			strWhere.append(" and jieshouswjg like '" +  TasksData.getSwjg_Like_Dm(jieshouswjg,true) + "%' ");
		}
		if(null != task_date_start && !"".equals(task_date_start)){
			strWhere.append(" and begindate >= '" + task_date_start +  "' ");
		}
		if(null != task_date_end && !"".equals(task_date_end)){
			strWhere.append(" and begindate <= '" + task_date_end +  " 23:59' ");
		}
		String sql = "select jieshourenid,y.username,"+
					"sum(case when status=-1 then 0 else  1 end) a,"+
					"sum(case when status=0 then 1 else  0 end) b,"+
					"sum(case when status=1 then 1 else  0 end) c  "+
					"from xingzhengtasks t left join  wluser y on t.jieshourenid=y.userid where 1=1 strWhere  "+
					"group by jieshourenid,y.username";
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
				oRes.put("zxr_dm", res.getString("jieshourenid"));
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
	
	private int getUserIdBySwjg(String swjgdm){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;

		con = DbConnectionManager.getConnection();
		try {
			ps = con.prepareStatement("SELECT * FROM WLuser WHERE (userkebe = ?) and CHARINDEX('4',usertype)>0 ");
			ps.setString(1, swjgdm);
			res = ps.executeQuery();
			if(res.next()){
				return res.getInt("userid");
			}else{
				ps = con.prepareStatement("SELECT * FROM WLuser WHERE (userkebe = ?) and CHARINDEX('3',usertype)>0 ");
				ps.setString(1, swjgdm);
				res = ps.executeQuery();
				if(res.next()){
					return res.getInt("userid");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbConnectionManager.close(con, ps, null, res);
		}
		return -1;
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	
}
