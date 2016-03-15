<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="com.wgx.util.json.JSONException"%>
<%@ page import="com.wgx.util.json.JSONObject"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="cn.com.shasha.utils.*"%>
<% 

	
	final long MAX_SIZE = 20 * 1024 * 1024;
	DiskFileItemFactory dfif = new DiskFileItemFactory();
	dfif.setSizeThreshold(32 * 1024);
	String savedir = request.getSession().getServletContext().getRealPath("/") + "UploadTemp";
	java.io.File d = new java.io.File(savedir);
	if (!d.exists()) {
		d.mkdirs();
	}
	dfif.setRepository(d);
	ServletFileUpload sfu = new ServletFileUpload(dfif);
	sfu.setSizeMax(MAX_SIZE);
	try {
		List fileList = sfu.parseRequest(request);
		Iterator fileIterator = fileList.iterator();
		while (fileIterator.hasNext()) {
			FileItem fileItem = (FileItem) fileIterator.next();
			if (fileItem == null || fileItem.isFormField()) {
				continue;
				//break;
			}
			String path = fileItem.getName();
			long size = fileItem.getSize();
			if (path.equals("") || size == 0) {
			} else {
				String t_name = path
						.substring(path.lastIndexOf("\\") + 1);
				String t_ext = t_name
						.substring(t_name.lastIndexOf(".") + 1);
				String prefix = String.valueOf(System
						.currentTimeMillis());
				String u_name = request.getSession().getServletContext().getRealPath("/") + "excelUploaded/"
						+ prefix + "." + t_ext;
				File uploadFile = new File(u_name);
				String updirpath = request.getSession().getServletContext().getRealPath("/")
						+ "excelUploaded";
				java.io.File updir = new java.io.File(updirpath);
				if (!updir.exists()) {
					updir.mkdirs();
				}	
				fileItem.write(uploadFile);
				JSONObject jres = null;
				try {
					jres = JavaOperateExcel.readExcel(u_name,0,"",session);
				} catch (Exception e) {
					   e.printStackTrace();
				}
				uploadFile.delete();   
				if(jres.getInt("r")>0)
				{
					out.print("{success:\"true\""
							+ ",tipMsg:\"文件数据导入成功"+jres.getInt("r")+"条!\"}");
					out.flush();
					out.close();					
				}else{
					out.print("{success:\"true\""
							+ ",tipMsg:\"文件数据部分导入失败!原因:"+jres.getString("res")+"\"}");
					out.flush();
					out.close();						
				}
					 
			}
		}
		
	} catch (Exception ex) {
		out.print("{success:\"true\""
				+ ",tipMsg:\"文件数据导入失败!\"}");
		out.flush();
		out.close();			
		   ex.printStackTrace();
	}
	     
%>