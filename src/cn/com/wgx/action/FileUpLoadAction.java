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
	// action����
	private static String ACT_UPLOAD_FILE = "uploadfile";
	private static String ACT_GETFILELIST = "getFileList";
	private static String ACT_GETFILELIST_NEW = "getFileList_new";
	private static String ACT_DELETE = "delete";
	// action����

	private static String FilePath = "uploadfile/"; // �ļ��洢·��
	private static String TempPath = "uploadtemp/"; // ��ʱ�ļ�·��
	private static String FileRealPath = "/"; // �ļ��洢·��
	private static String TempRealPath = "/"; // ��ʱ�ļ�·��
	private static long FileMaxLong = (1024 * 1024 * 100); // �����ļ���󳤶�
	private static DiskFileItemFactory Factory;

	/**
	 * ��ʼ��
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
		Factory = new DiskFileItemFactory(); // ����һ������Ӳ�̵�FileItem����
		//Factory.setSizeThreshold(4 * 1024); // ������Ӳ��д����ʱ���õĻ������Ĵ�С���˴�Ϊ4K
		Factory.setRepository(new File(TempRealPath)); // ������ʱĿ¼

		System.out.println("tempFilePath:" + TempRealPath);
		System.out.println("filePath:" + FileRealPath);
	}

	public JSONObject jRes = new JSONObject();
	public WLUser nowuser = null;
	/**
	 * �ļ��ϴ�/�������
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
					jRes.put("res", "���ͱ�����");
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
	 * ��������ID��ȡ�����б�
	 * @param request
	 * @param response
	 * @return
	 * ��������,�������,����ID,���񸽼�����
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
	 * �ϴ���������������
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String UpLoadFileFactory(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// ����һ���ļ��ϴ�������
			ServletFileUpload upload = new ServletFileUpload(Factory);
			// ���������ϴ����ļ������ߴ�
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
					// ȡ���ϴ��ļ����ļ�����
					String fullname = item.getName();
					// ȡ���ļ���׺
					String fileSuffix = fullname.substring(
							fullname.lastIndexOf('.'), fullname.length());
					//�洢�ļ�����
					String saveName = nowuser.getUsercode()+"_"+new Date().getTime()+fileSuffix;
					// �ϴ��ļ��Ժ�Ĵ洢·��
					String path = FileRealPath
							+ File.separatorChar + saveName;
					// �ϴ��ļ�
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
				if("upLoadTaskPackage".equals(act)){//�ϴ������
					System.out.println("�����ϴ������.");
					r = TaskPackageServices.saveTaskPackage(FileRealPath+ File.separatorChar + jRes.getString("saveName"),nowuser,TaskPackageServices.newTaskPackage(nowuser,params.get("name")));
					jRes.put("tipMsg", "�ɹ��ϴ�"+r+"������.");
				}else{
					jRes.put("r_save", SaveUpLoadfileInfo(list, taskId, nowuser.getUserid(), nowuser.getUserkebe()));
				}
			}else{
				jRes.put("res", "����������");
			}
			jRes.put("r", r);
			jRes.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				jRes.put("res", "�ļ����󣬲��ɳ���100M��");
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
	 * �����ϴ��ļ��������Ϣ�����ݿ�(��ַ,�ļ���,�ϴ���ID,�ϴ�˰�����)
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
						out.print("{success:\"true\"" + ",tipMsg:\"�ļ����ݵ���ɹ�"
								+ jres.getInt("r") + "��!\"}");
						out.flush();
						out.close();
					} else {
						out.print("{success:\"true\""
								+ ",tipMsg:\"�ļ����ݲ��ֵ���ʧ��!ԭ��:"
								+ jres.getString("res") + "\"}");
						out.flush();
						out.close();
					}

				}
			}

		} catch (Exception ex) {
			out.print("{success:\"true\"" + ",tipMsg:\"�ļ����ݵ���ʧ��!\"}");
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
