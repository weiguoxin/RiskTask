package cn.com.wgx.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.com.info21.database.DbConnectionManager;
import cn.com.shasha.services.TaskPackageServices;
import cn.com.shasha.sys.WLUser;
import cn.com.shasha.utils.JavaOperateExcel;

import com.wgx.util.json.JSONArray;
import com.wgx.util.json.JSONException;
import com.wgx.util.json.JSONObject;

public class FileUpLoadAction extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// action请求
	private static String ACT_UPLOAD_FILE = "uploadfile";
	private static String ACT_GETFILELIST = "getFileList";
	private static String ACT_GETFILELIST_NEW = "getFileList_new";
	private static String ACT_DELETE = "delete";
	// action结束

	private static String FilePath = "uploadfile/"; // 文件存储路径
	private static String TempPath = "uploadtemp/"; // 临时文件路径
	private static String FileRealPath = "/"; // 文件存储路径
	private static String TempRealPath = "/"; // 临时文件路径
	private static long FileMaxLong = (1024 * 1024 * 100); // 设置文件最大长度
	private static DiskFileItemFactory Factory;

	/**
	 * 初始化
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		FilePath = config.getInitParameter("filePath");
		TempPath = config.getInitParameter("tempPath");
		FileRealPath = this.getServletContext().getRealPath(FilePath);
		TempRealPath = this.getServletContext().getRealPath(TempPath);
		File f = new File(FileRealPath);
		if(!f.exists()){
			f.mkdirs(); 
		}
		File f2 = new File(TempRealPath);
		if(!f2.exists()){
			f2.mkdirs();
		}
		Factory = new DiskFileItemFactory(); // 创建一个基于硬盘的FileItem工厂
		//Factory.setSizeThreshold(4 * 1024); // 设置向硬盘写数据时所用的缓冲区的大小，此处为4K
		Factory.setRepository(new File(TempRealPath)); // 设置临时目录

		System.out.println("tempFilePath:" + TempRealPath);
		System.out.println("filePath:" + FileRealPath);
	}

	public JSONObject jRes = new JSONObject();
	public WLUser nowuser = null;
	/**
	 * 文件上传/操作入口
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		nowuser = ((WLUser)request.getSession().getAttribute("Session_User"));
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			UpLoadFileFactory(request, response);
		}else{
			String act = request.getParameter("act");
			if(ACT_GETFILELIST.equals(act)){
				String res = GetFileListByTaskId(request,response);
			}else if(ACT_GETFILELIST_NEW.equals(act)){
				String res = GetFileListByTaskId_New(request,response);
			}else if(ACT_DELETE.equals(act)){
				String res = DeleteById(request,response);
			}else{
				try {
					jRes.put("success", "false");
					jRes.put("res", "传送表单出错！");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		response.setContentType("text/html;charset=UTF-8");  
		response.setHeader("Cache-Control","no-cache");  
		PrintWriter out = response.getWriter();
		out.print(jRes.toString());
		out.flush();
		out.close();
//System.out.println(sReturnStr);
	}

	

	public static String SQL_GETFILEINFOBYTASKID = "select * from uploadfileinfo where taskid=?";
	private String GetFileListByTaskId(HttpServletRequest request,
			HttpServletResponse response) {
		String taskId = request.getParameter("taskid");
		JSONArray ja = new JSONArray();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_GETFILEINFOBYTASKID);
			ps.setString(1, taskId);
			rs = ps.executeQuery();
			while(rs.next()){
				JSONObject o = new JSONObject();
				o.put("id", rs.getInt("id"));
				o.put("fileName", rs.getString("originalName"));
				o.put("path", FilePath + rs.getString("newName"));
				o.put("upLoad_Time", rs.getString("upload_time"));
				o.put("upLoadUserId", rs.getString("upLoadUserId"));
				ja.put(o);
			}
			jRes.put("fileList", ja);
			jRes.put("success", true);
			jRes.put("r", ja.length());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbConnectionManager.close(con, ps, null, rs);
		}
		return null;
	}
	
	
	private final static String SQL_GETFILEINFOBYTASKID_NEW_BEGIN = "select a.* from uploadfileinfo a,";
	private final static String SQL_GETFILEINFOBYTASKID_NEW_2 =	" b where charindex (',' + cast(a.id as varchar(10)) + ',',','+";
	private final static String SQL_GETFILEINFOBYTASKID_NEW_END = "+',')>0 and b.id = ?";
	/**
	 * 根据任务ID获取附件列表
	 * @param request
	 * @param response
	 * @return
	 * 参数必须,任务表名,任务ID,任务附件列名
	 */
	private String GetFileListByTaskId_New(HttpServletRequest request,
			HttpServletResponse response) {
		String taskId = request.getParameter("taskid");
		String fujianField = request.getParameter("fujianField");
		String tbName = request.getParameter("tbName");
		JSONArray ja = new JSONArray();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			StringBuffer sqlSB = new StringBuffer();
			sqlSB.append(SQL_GETFILEINFOBYTASKID_NEW_BEGIN);
			sqlSB.append(tbName);
			sqlSB.append(SQL_GETFILEINFOBYTASKID_NEW_2);
			sqlSB.append("b."+fujianField);
			sqlSB.append(SQL_GETFILEINFOBYTASKID_NEW_END);
			ps = con.prepareStatement(sqlSB.toString());
			ps.setString(1, taskId);
			rs = ps.executeQuery();
			while(rs.next()){
				JSONObject o = new JSONObject();
				o.put("id", rs.getInt("id"));
				o.put("fileName", rs.getString("originalName"));
				o.put("path", FilePath + rs.getString("newName"));
				o.put("upLoad_Time", rs.getString("upload_time"));
				o.put("upLoadUserId", rs.getString("upLoadUserId"));
				ja.put(o);
			}
			jRes.put("fileList", ja);
			jRes.put("success", true);
			jRes.put("r", ja.length());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbConnectionManager.close(con, ps, null, rs);
		}
		return null;
	}
	
	public static String SQL_DELETE_BY_ID = "delete from uploadfileinfo where id=?";
	
	private String DeleteById(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_DELETE_BY_ID);
			ps.setString(1, id);
			int r = ps.executeUpdate();
			jRes.put("success", true);
			jRes.put("r", r);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbConnectionManager.close(con, ps, null, rs);
		}
		return null;
	}
	/**
	 * 上传附件，不限类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String UpLoadFileFactory(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// 创建一个文件上传处理器
			ServletFileUpload upload = new ServletFileUpload(Factory);
			// 设置允许上传的文件的最大尺寸
			upload.setSizeMax(FileMaxLong);
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			int r = 0;
			String taskId = null;
			JSONArray list = new JSONArray();
			String act = null;
			HashMap<String,String> params = new HashMap<String,String>();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					String v = item.getFieldName();
					if("id".equals(v)){
						taskId = item.getString();
					}else if("act".equals(v)){
						act = item.getString();;
					}else if("name".equals(v)){
						params.put("name", item.getString("gbk"));
						//System.out.println(item.getString("gbk"));
					}
				} else {
					// 取出上传文件的文件名称
					String fullname = item.getName();
					// 取得文件后缀
					String fileSuffix = fullname.substring(
							fullname.lastIndexOf('.'), fullname.length());
					//存储文件名称
					String saveName = nowuser.getUsercode()+"_"+new Date().getTime()+fileSuffix;
					// 上传文件以后的存储路径
					String path = FileRealPath
							+ File.separatorChar + saveName;
					// 上传文件
//System.out.println("fullName:"+fullname+"\nsaveName:"+saveName+"\npath:"+path);
					File uploaderFile = new File(path);
					if(uploaderFile.createNewFile()){
						item.write(uploaderFile);
						item.delete();
						JSONObject jo = new JSONObject();
						jo.put("fullName", fullname);
						jo.put("saveName", saveName);
						list.put(jo);
						r++;
						jRes.put("path", FilePath + saveName);
						jRes.put("fileName", fullname);
						jRes.put("saveName", saveName);
					}
				}
			}
			if(list.length()>0){
				if("upLoadTaskPackage".equals(act)){//上传任务包
					System.out.println("正在上传任务包.");
					r = TaskPackageServices.saveTaskPackage(FileRealPath+ File.separatorChar + jRes.getString("saveName"),nowuser,TaskPackageServices.newTaskPackage(nowuser,params.get("name")));
					jRes.put("tipMsg", "成功上传"+r+"条任务.");
				}else{
					jRes.put("r_save", SaveUpLoadfileInfo(list, taskId, nowuser.getUserid(), nowuser.getUserkebe()));
				}
			}else{
				jRes.put("res", "服务器错误。");
			}
			jRes.put("r", r);
			jRes.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				jRes.put("res", "文件过大，不可超过100M。");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
	public static String SQL_INSERT_UPLOADFILEINFO = "insert into [UpLoadFileInfo]"+
           "([originalName]"+
           ",[newName]"+
           ",[taskId]"+
           ",[upLoadUserId]"+
           ",[swjg_dm]) values(?,?,?,?,?)";
	/**
	 * 保存上传文件的相关信息到数据库(地址,文件名,上传人ID,上传税务机关)
	 * @param ja
	 * @param taskId
	 * @param userId
	 * @param swjg_dm
	 * @return
	 */
	public int SaveUpLoadfileInfo(JSONArray ja, String taskId, int userId, String swjg_dm){
		int len = ja.length();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int r = 0;
		try {
			con = DbConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_UPLOADFILEINFO, PreparedStatement.RETURN_GENERATED_KEYS);
			for(int i=0;i<len;i++){
				JSONObject o = ja.getJSONObject(i);
				String fullName = o.getString("fullName");
				String saveName = o.getString("saveName");
				ps.setString(1, fullName);
				ps.setString(2, saveName);
				ps.setString(3, taskId);
				ps.setInt(4, userId);
				ps.setString(5, swjg_dm);
				r += ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				while(rs.next()){
					jRes.put("id", rs.getInt(1));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DbConnectionManager.close(con, ps, null, null);
		}
		return r;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String UpLoadFileAndSave(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		HttpSession session = request.getSession();
		ServletOutputStream out = response.getOutputStream();
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(32 * 1024);
		String savedir = request.getSession().getServletContext()
				.getRealPath("/")
				+ "UploadTemp";
		java.io.File d = new java.io.File(savedir);
		if (!d.exists()) {
			d.mkdirs();
		}
		dfif.setRepository(d);
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		sfu.setSizeMax(FileMaxLong);
		try {
			List fileList = sfu.parseRequest(request);
			Iterator fileIterator = fileList.iterator();
			while (fileIterator.hasNext()) {
				FileItem fileItem = (FileItem) fileIterator.next();
				if (fileItem == null || fileItem.isFormField()) {
					continue;
					// break;
				}
				String path = fileItem.getName();
				long size = fileItem.getSize();
				if (path.equals("") || size == 0) {
				} else {
					String t_name = path.substring(path.lastIndexOf("\\") + 1);
					String t_ext = t_name
							.substring(t_name.lastIndexOf(".") + 1);
					String prefix = String.valueOf(System.currentTimeMillis());
					String u_name = request.getSession().getServletContext()
							.getRealPath("/")
							+ "excelUploaded/" + prefix + "." + t_ext;
					File uploadFile = new File(u_name);
					String updirpath = request.getSession().getServletContext()
							.getRealPath("/")
							+ "excelUploaded";
					java.io.File updir = new java.io.File(updirpath);
					if (!updir.exists()) {
						updir.mkdirs();
					}
					fileItem.write(uploadFile);
					JSONObject jres = null;
					try {
						jres = JavaOperateExcel.readExcel(u_name, 0, "",
								session);
					} catch (Exception e) {
						e.printStackTrace();
					}
					uploadFile.delete();
					if (jres.getInt("r") > 0) {
						out.print("{success:\"true\"" + ",tipMsg:\"文件数据导入成功"
								+ jres.getInt("r") + "条!\"}");
						out.flush();
						out.close();
					} else {
						out.print("{success:\"true\""
								+ ",tipMsg:\"文件数据部分导入失败!原因:"
								+ jres.getString("res") + "\"}");
						out.flush();
						out.close();
					}

				}
			}

		} catch (Exception ex) {
			out.print("{success:\"true\"" + ",tipMsg:\"文件数据导入失败!\"}");
			out.flush();
			out.close();
			ex.printStackTrace();
		}
		return null;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException  {
		this.doPost(request, response);
	}
}
