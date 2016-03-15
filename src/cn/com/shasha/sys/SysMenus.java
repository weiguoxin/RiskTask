package cn.com.shasha.sys;

import java.io.IOException;



import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cn.com.info21.database.DbConnectionManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SysMenus extends HttpServlet {
	private static String MENUS_SQL_ =
		"SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM " +
		"SYS_MENUS WHERE ISENABLE = 'Y' AND ISLINK<>'Y' AND TYPE = 3 AND ID IN (SELECT MENUID FROM SYS_ROLEMENUS WHERE ROLEID IN (" ;
	private static String _MENUS_SQL	= ")) ORDER BY ID";

	private static String MENUS_SQL_ALL =
		"SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM SYS_MENUS WHERE ISENABLE = 'Y' AND ISLINK<>'Y' AND TYPE = 3 ORDER BY ID";	
	public void init(ServletConfig config) throws ServletException {
	   }
	   
	   /** Handles the HTTP <code>GET</code> method.
	    * @param request servlet request
	    * @param response servlet response
	    */
	   protected void doGet(HttpServletRequest request, HttpServletResponse response)
	   throws ServletException, IOException {
			Connection conn = null;
			PreparedStatement sMenus = null;
			ResultSet rsMenus = null;
			String sJson="[";
			String temppid="";
			String[] tempidArray =null;
			
			boolean isSuperAdmin = false;
			isSuperAdmin = ((Boolean)request.getSession().getAttribute("Session_isSuperAdmin")).booleanValue();
			WLUser nowuser = ((WLUser)request.getSession().getAttribute("Session_User"));
			try {
				conn = DbConnectionManager.getConnection();
				if (isSuperAdmin) {
					sMenus = conn.prepareStatement(MENUS_SQL_ALL);
				} else {
					sMenus = conn.prepareStatement(MENUS_SQL_+nowuser.getUsertype()+_MENUS_SQL);
				}
				
				rsMenus = sMenus.executeQuery();
				//System.out.println("dfdfdfd---"+rsMenus.next());
				while (rsMenus.next()) {
					//System.out.println(i++);
					String mpid, mid,isLeaf,currentpid;
					//tempidArray用来存放所有的pid,currentpid用来取当前的pid的值
					tempidArray =temppid.split(",");
					currentpid = tempidArray[tempidArray.length-1];
					mid = rsMenus.getString("ID").trim();
					mpid = rsMenus.getString("PID");
					//System.out.println("mid"+mid+"mpid"+mpid);
					if (mpid == null) {
						mpid = "";
					} else {
						mpid = mpid.trim();
					}
					String entryName = rsMenus.getString("NAME");
					if (entryName == null) {
						entryName = "";
					} else {
						entryName = entryName.trim();
					}
					String entryHref = rsMenus.getString("HREF");
					if (entryHref == null) {
						entryHref = "";
					} else {
						entryHref = entryHref.trim();
					}
					if (!"".equals(entryHref)) {
						if (entryHref.indexOf("?") > 0) {
							entryHref = entryHref + "&random=" + Math.floor(Math.random()*100000);
						} else {
							entryHref = entryHref + "?random=" + Math.floor(Math.random()*100000);
						}
					}
					String entryExplain = rsMenus.getString("MEXPLAIN");
					if (entryExplain == null) {
						entryExplain = "";
					} else {
						entryExplain = entryExplain.trim();
					}
					isLeaf=rsMenus.getString("ISLEAF").trim();
					/*
					System.out.println("---"+mpid+"***'");
					System.out.println("currentpid---"+currentpid+"***'");
					System.out.println( mpid.equals(currentpid));
					System.out.println("是否等于当前pid--"+mpid.equals(currentpid));
					*/
					//若当前的mpid不等currentpid,则表示到了下一级了,进行封闭 ]}
					if(!mpid.equals(currentpid)){
						//System.out.println("换父结点了---"+mid);
						sJson = sJson.substring(0, sJson.length() - 1);
						sJson += "]},";
						temppid = temppid.substring(0, temppid.length() - 1);
						temppid = temppid.substring(0, temppid.lastIndexOf(",")+1);
						//System.out.println(temppid);
						tempidArray =temppid.split(",");
						currentpid = tempidArray[tempidArray.length-1];						
					}
					//若mpid等于currentpid并且不为页子节点,证明它有下级子节点,否则没有
					if(isLeaf.equals("N")&& mpid.equals(currentpid)) {
						//System.out.println("我有子节点呀哎呀---"+mid);
						temppid += mid+",";
						sJson += "{id:'"+mid+"',text:'"+entryName+"',children:["; 
					}
					if(isLeaf.equals("Y")&& mpid.equals(currentpid)) {
						//System.out.println("我最终了---"+mid);
						sJson += "{id:'"+mid+"',text:'"+entryName+"',href:'"+entryHref+"',leaf:true},"; 
					}															
				}
				sJson = sJson.substring(0, sJson.length() - 1);
				for(int j=0;j<=tempidArray.length;j++)
				{
					sJson += "]}";	
				}
				sJson = sJson.substring(0, sJson.length() - 1);
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
				//System.out.println("combox--"+sJson);
			   response.setContentType("appplication/x-json;charset=gbk");
			   //response.setHeader("Cache-Control", "no-cache");
			   response.getWriter().print(sJson);
			   //response.getWriter().print("{success:true,rows:[{'id':'1','zzmm':'你好'},{'id':'2','zzmm':'b'},{'id':'3','zzmm':'c'},{'id':'4','zzmm':'d'}]}");			   
			   }		     
	   /** Handles the HTTP <code>POST</code> method.
	    * @param request servlet request
	    * @param response servlet response
	    */
	   protected void doPost(HttpServletRequest request, HttpServletResponse response)
	   throws ServletException, IOException {
	       doGet(request, response);
	   }
}
