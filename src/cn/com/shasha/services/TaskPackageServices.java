package cn.com.shasha.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.com.info21.database.DbConnectionManager;
import cn.com.shasha.sys.TaskManData;
import cn.com.shasha.sys.WLUser;
import cn.com.shasha.sys.WLUserData;
import cn.com.shasha.utils.DataCenter;

import com.wgx.util.json.DateUtil;
import com.wgx.util.json.JSONArray;
import com.wgx.util.json.JSONException;
import com.wgx.util.json.JSONObject;
/**
 * 任务包服务类
 * @author _wgx
 *
 */
public class TaskPackageServices  extends HttpServlet{

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
		String sReturnStr = "";
		String sAction = request.getParameter("action");
		System.out.println(sAction);
		if("getTaskPackageList".equals(sAction)){
			sReturnStr = getTaskPackages(request,response);
		}else if("getTaskPackageListBySwjg".equals(sAction)){
			sReturnStr = getTaskPackages_CanGet(request,response);
		}else if("reveiveTaskPackageAll".equals(sAction)){
			sReturnStr = reveiveTaskPackageAll(request,response);
		}else if("delTaskPackage".equals(sAction)){
			sReturnStr = delTaskPackage(request,response);
		}
		
		
		PrintWriter out = response.getWriter();
		out.print(sReturnStr);
		out.close();
	}



	private static String SQL_SELECT_TASK_TaskPackage_ByID = "select count(*) from TaskPackage where id=? and get_count=0";
	private static String SQL_DELETE__TaskPackage = "delete from TaskPackage where id in(?)";
	private String delTaskPackage(HttpServletRequest request,
			HttpServletResponse response) {
		String idList = request.getParameter("idList");
		String idArray [] = idList.split(",");
		StringBuffer idSB = new StringBuffer();
		int r = 0;
		String errorStr = "";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		con = DbConnectionManager.getConnection();
		try {
			ps = con.prepareStatement(SQL_SELECT_TASK_TaskPackage_ByID);
			for(int i=0; i < idArray.length;i++){
				String id = idArray[i];
				ps.setString(1,id);
				res = ps.executeQuery();
				if(res.next()){
					if(res.getInt(1)>0){
						idSB.append(id+",");
					}
				}
			}
			if(idSB.length()>0){
				String idlist = idSB.substring(0,idSB.length()-1);
				con.setAutoCommit(false);
				ps = con.prepareStatement("delete from TaskPackage where id in("+idlist+")");
				r = ps.executeUpdate();
				if(r > 0){
					ps = con.prepareStatement("delete from tasks_temp where shijuxiafa in("+idlist+") and shijuxiafa<>0");
					ps.executeUpdate();
					con.commit();
				}else{
					errorStr = "数据库错误.";
				}
				if(r != idArray.length){
					errorStr = "部分任务包不可删除,已有任务被接收.";
				}
			}else{
				errorStr = "已选定的任务包有任务已接收,不可删除.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DbConnectionManager.close(con, ps, null, res);
		}
		JSONObject jRes = new JSONObject();
		try {
			jRes.put("r", r);
			jRes.put("success", true);
			jRes.put("res", errorStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jRes.toString();
	}



	public static  String INSERT_TASKPACKAGE = "insert into taskpackage([name],swjgdm,swjgmc,xfrdm,xfrmc,xfdate) " +
			"values(?,?,?,?,?,?)";
	/**
	 * 新建任务包
	 * @param user
	 * @param name
	 * @return
	 */
	public static int newTaskPackage(WLUser user,String name){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int r = -1;
		con = DbConnectionManager.getConnection();
		try {
			pstmt = con.prepareStatement(INSERT_TASKPACKAGE,PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, name);
			pstmt.setString(2, user.getUserkebe());
			pstmt.setString(3, user.getSwjg_mc());
			pstmt.setInt(4, user.getUserid());
			pstmt.setString(5, user.getUsername());
			pstmt.setTimestamp(6, new Timestamp(new Date().getTime()));
			if(pstmt.executeUpdate()>0){
				res= pstmt.getGeneratedKeys();
				if(res.next()){
					r = res.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DbConnectionManager.close(con, pstmt, null, res);
		}
		return r;
	}

	
	
	private static String  headArray[] = new String[] { "纳税人识别号", "纳税人名称", "主管税务机关", "专管员姓名","风险指标", "任务时限","风险描述"};
	private static int  headArray_len = headArray.length; //"风险指标", "风险描述","风险应对措施","税务机关代码","专管员代码"
	/**
	 * 上传数据包
	 * @param filepath
	 * @param user
	 * @return
	 */
	public static int saveTaskPackage(String filepath, WLUser user, int TaskPackageID){
		Timestamp ts = new Timestamp(new java.util.Date().getTime());
		JSONObject jres = new JSONObject();
		String errorStr = "";
		int r = 0;
		try {
			FileInputStream fis = new FileInputStream(filepath); // 根据excel文件路径创建文件流
			POIFSFileSystem fs = new POIFSFileSystem(fis); // 利用poi读取excel文件流
			HSSFWorkbook wb = new HSSFWorkbook(fs); // 读取excel工作簿
			HSSFSheet sheet = wb.getSheetAt(0); // 读取excel的sheet，0表示读取第一个、1表示第二个.....
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			// 先取excel的第一行信息,进行表头检查查看是否为正确模板文件
			HSSFRow headrow = sheet.getRow(0);
//System.out.println("headrow.getLastCellNum()---" + headrow.getLastCellNum());
			if (headrow != null) {
				if (headrow.getLastCellNum() >= headArray.length
						|| sheet.getPhysicalNumberOfRows() > 1) {
					for (int i = 0; i < headArray.length; i++) {
						HSSFCell cell = headrow.getCell((short) i);
						if (cell == null) {
							errorStr = "该Excel格式不对!1 "+i;
						} else {
							if (cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
								errorStr = "该Excel格式不对!2";
								break;
							}
						}
					}
					if (errorStr.equals("")) {
						// 检验表头是否正确,与数组进行匹配
						for (int i = 0; i < headrow.getLastCellNum()&&i<headArray_len; i++) {
							HSSFCell cell = headrow.getCell((short) i);
							if (!(cell.getStringCellValue().trim())
									.equals(headArray[i])) {
//System.out.println(cell.getStringCellValue().trim() + " " + headArray[i]);
								errorStr = "该Excel格式不对!3";
								break;
							}
						}
					}
				} else {
					errorStr = "该Excel格式不对!4";
				}
			} else {
				errorStr = "该Excel是空文档!";
			}
			// 若表头格式正确,则进行行的导入
//System.out.println("sheet.getPhysicalNumberOfRows()--"
//					+ sheet.getPhysicalNumberOfRows());
			if (errorStr.equals("")) {

				// 获取sheet中总共有多少行数据sheet.getPhysicalNumberOfRows()
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
					HSSFRow row = sheet.getRow(i); // 取出sheet中的某一行数据
					String lineError = "";
						// 无错，就写入数据库
						if (lineError.equals("")) {
							String nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb,limit_time, fxms, fxydcs=null, fxzb_dm=null, swjg_dm=null,zx_swjg_dm=null;
							boolean zgy_bool = false;
							int index = 0,zgy_dm=-1,zxr_dm=-1;
							nsrsbh = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							nsrmc = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							swjg_mc = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							zgy_mc = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							fxzb = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							limit_time = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							fxms = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							Date limit = DateUtil.getDate(limit_time, "yyyy-M-d");
							swjg_dm = TaskManData.GetSwjgDmByName(swjg_mc);
							zgy_dm = WLUserData.GetUserByNameAndSwjgDM(zgy_mc, swjg_dm);
							JSONObject fxzb_json = TaskManData.GetFxzbJSONObject("fxzb",fxzb);
							if(null != fxzb_json){
								fxydcs = fxzb_json.getString("fxydjy");
								fxzb_dm = fxzb_json.getString("sid");
								zgy_bool = fxzb_json.getBoolean("zgy");
								if(zgy_bool){
									zxr_dm = zgy_dm;
									zx_swjg_dm = swjg_dm;
								}
							}
							TasksTemp ps = new TasksTemp();
							ps.setNsrsbh(nsrsbh);
							ps.setNsrmc(nsrmc);
							ps.setSwjg_mc(swjg_mc);
							ps.setZgy_mc(zgy_mc);
							ps.setFxzb(fxzb);
							ps.setFxydcs(fxydcs);
							ps.setFxzb_dm(fxzb_dm);
							ps.setZgy_dm(zgy_dm+"");
							ps.setSwjg_dm(swjg_dm);
							ps.setZxr_dm(zxr_dm);
							ps.setZx_swjg_dm(zx_swjg_dm);
							if(null!=limit){
								ps.setLimit_time(new Timestamp(limit.getTime()));
							}
							ps.setImp_userid(user.getUserid());
							ps.setImp_date(ts);
							ps.setFxms(fxms);
							ps.setShijuxiafa(TaskPackageID);
							r += ps.insertSelfTask_TaskPackage();
//System.out.println("insert:" + i);
						}
						errorStr += lineError;
					}
				}else{
					System.out.println(errorStr);
			}
		} catch (Exception e) {
			errorStr = "导入时发生异常,请检查文件无误后重新导入!";
			e.printStackTrace();
		}
		updateTaskPackage("all_count", r+"", TaskPackageID);
		try {
			jres.put("r", r);
			jres.put("tipMsg", r);
			jres.put("res", errorStr);
		} catch (JSONException e) {
		}
		return r;
	}
	
	/**
	 * 查询任务包
	 * @param request
	 * @param response
	 * @return
	 */
	public String getTaskPackages(HttpServletRequest request, HttpServletResponse response){
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String dir = request.getParameter("dir");
		boolean dirBoolean = "DESC".equals(dir)?true:false;
		int pageSize = Integer.parseInt(limit);
		int index = (Integer.parseInt(start) + pageSize) / pageSize;
		String name = request.getParameter("name");
		String swjgdm = request.getParameter("swjgdm");
		String xfrdm = request.getParameter("xfrdm");
		String date_start = request.getParameter("date_start");
		String date_end = request.getParameter("date_end");
		String sort = request.getParameter("sort");
		
		StringBuffer strWhere = new StringBuffer(" xfrdm="+nowuser.getUserid()+ " ");
		
		if (null != name && !name.equals("")) {
			strWhere.append(" and name like '%"+name+"%' ");
		}
		if (null != swjgdm && !swjgdm.equals("")) {
			strWhere.append(" and swjgdm = '"+swjgdm+"' ");
		}
		if (null != xfrdm && !xfrdm.equals("")) {
			strWhere.append(" and xfrdm = '"+xfrdm+"' ");
		}
		if (null != date_start && !date_start.equals("") && null != date_end && !date_end.equals("")) {
			strWhere.append(" and xfdate between '"+date_start+"' and '"+date_end+" 23:59' ");
		}
		if (null == sort || sort.equals("")) {
			sort = "id";
		}
		
		DataCenter odata = new DataCenter();
		String sJson = odata.ResultSetToJsonForShow("TaskPackage",
				" id, name, swjgdm, swjgmc, xfrdm, xfrmc, xfdate, all_count, get_count, beizhu ", sort, pageSize, index,
				dirBoolean, strWhere.toString());
		return sJson;
	}
	
	
	private static String TURN_SQL = "insert into tasks(nsrsbh, nsrmc, swjg_mc,zgy_mc, fxzb, fxms, limit_time, "+
	" fxydcs, swjg_dm, zgy_dm,fxzb_dm, imp_date, imp_userid, zxr_dm, zx_swjg_dm, shijuxiafa, jieshouren, jieshoudate"+
	" ) select nsrsbh, nsrmc, swjg_mc,zgy_mc, fxzb, fxms, limit_time, "+
	" fxydcs, swjg_dm, zgy_dm,fxzb_dm, imp_date, imp_userid, zxr_dm, zx_swjg_dm,shijuxiafa, jieshouren, jieshoudate from tasks_temp "+
	" where swjg_dm like ? and shijuxiafa=? and jieshouren is null and status =-1 and nsrmc is not null and swjg_mc is not null and zgy_mc is not null and  fxzb is not null and fxms is not null and "+
	" limit_time is not null and fxydcs is not null and swjg_dm is not null and fxzb_dm is not null and "+
	"  zx_swjg_dm is not null and nsrmc <>'' and swjg_mc  <>'' and zgy_mc  <>'' and  fxzb  <>'' and fxms  <>'' and "+
	" limit_time  <>'' and fxydcs  <>'' and swjg_dm  <>'' and fxzb_dm  <>''"+
	"  and zx_swjg_dm  <>'' ";
	
	private static String SQL_UPDATE_TASKTEMP = 
	"update tasks_temp set jieshouren=?,jieshoudate=?,status =0 where " +
	"swjg_dm like ? and shijuxiafa=? and jieshouren is null"+
	" and status =-1 and nsrmc is not null and swjg_mc is not null and zgy_mc is not null and  fxzb is not null and fxms is not null and "+
	" limit_time is not null and fxydcs is not null and swjg_dm is not null and fxzb_dm is not null and "+
	"  zx_swjg_dm is not null and nsrmc <>'' and swjg_mc  <>'' and zgy_mc  <>'' and  fxzb  <>'' and fxms  <>'' and "+
	" limit_time  <>'' and fxydcs  <>'' and swjg_dm  <>'' and fxzb_dm  <>'' and zx_swjg_dm  <>'' ";
	
	private static String SQL_UPDATE_TASKPACKAGE = "update TaskPackage set get_count = get_count+";
	
	/**
	 * 下级接收市局下发任务,并更新任务包接收数量
	 * @param request
	 * @param response
	 * @return
	 */
	private String reveiveTaskPackageAll(HttpServletRequest request,
			HttpServletResponse response) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		con = DbConnectionManager.getConnection();
		int r_all = 0;
		int r_tasks = 0;
		try {
			ps = con.prepareStatement(SQL_CAN_GET_TASKPACKAGE);
			ps.setString(1, TasksData.getSwjg_Like_Dm(nowuser.getUserkebe(),true)+"%");
			res = ps.executeQuery();
			while(res.next()){
				int r = 0;
				int id =  res.getInt("id");
				con.setAutoCommit(false);
				ps = con.prepareStatement(TURN_SQL);
				ps.setString(1, TasksData.getSwjg_Like_Dm(nowuser.getUserkebe(),true)+"%");
				ps.setInt(2, id);
				r = ps.executeUpdate();
				if(r > 0){
					ps = con.prepareStatement(SQL_UPDATE_TASKTEMP);
					ps.setInt(1, nowuser.getUserid());
					ps.setTimestamp(2, new Timestamp(new Date().getTime()));
					ps.setString(3, TasksData.getSwjg_Like_Dm(nowuser.getUserkebe(),true)+"%");
					ps.setInt(4, id);
					int n = ps.executeUpdate();
					if(r == n){
						ps = con.prepareStatement("update TaskPackage set get_count = get_count+"+r+" where id=?");
						ps.setInt(1, id);
						r_all += ps.executeUpdate();
						r_tasks += r;
						con.commit();
					}else{
						con.rollback();
					}
				}
				
			}
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			DbConnectionManager.close(con, ps, null, res);
		}
		JSONObject jres = new JSONObject();
		try {
			jres.put("r_package", r_all);
			jres.put("r_tasks", r_tasks);
			jres.put("success", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jres.toString();
	}
	
	private static String SQL_CAN_GET_TASKPACKAGE = "select a.*,b.count as hasTasks from taskpackage a, "+
	"(select shijuxiafa,count(shijuxiafa)as count from tasks_temp where shijuxiafa <>0 and swjg_dm like ? and jieshouren is null group by shijuxiafa)b"+ 
	" where a.id = (b.shijuxiafa)";
	/**
	 * 下级查询任务包, (可接受任务数量)
	 * @param request
	 * @param response
	 * @return
	 */
	public String getTaskPackages_CanGet(HttpServletRequest request, HttpServletResponse response){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONObject jsonRes = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(SQL_CAN_GET_TASKPACKAGE);
			pstmt.setString(1, TasksData.getSwjg_Like_Dm(nowuser.getUserkebe(),true)+"%");
System.out.println(TasksData.getSwjg_Like_Dm(nowuser.getUserkebe(),true)+"%'");
			rs = pstmt.executeQuery();
			int i = 0;
			while(rs.next()){
				i++;
				JSONObject o = new JSONObject();
				o.put("id", i);
				o.put("name", rs.getString("name"));
				o.put("swjgdm", rs.getString("swjgdm"));
				o.put("swjgmc", rs.getString("swjgmc"));
				o.put("xfrmc", rs.getString("xfrmc"));
				o.put("xfdate", rs.getString("xfdate")== null? "": rs.getString("xfdate").substring(0, 19));
				o.put("beizhu", rs.getString("beizhu"));
				o.put("hasTasks", rs.getString("hasTasks"));
				ja.put(o);
			}
			jsonRes.put("topics", ja);
			jsonRes.put("success", true);
			jsonRes.put("totalCount", ja.length());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
		
		return jsonRes.toString();
	}
	public boolean delTaskPackage(){
		
		return false;
	}
	/**
	 * 更新指定字段
	 * @param field
	 * @param value
	 * @return
	 */
	public static boolean updateTaskPackage(String field,String value,int id){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		int r = 0;
		con = DbConnectionManager.getConnection();
		try {
			ps = con.prepareStatement("update taskpackage set " + field + " = '"+value+"' where id="+id);
			
			r = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DbConnectionManager.close(con, ps, null, res);
		}
		return r == 0?false:true;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
