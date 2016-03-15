<%@page errorPage="error.jsp"%>
<%@page import="java.io.*, java.text.*, java.util.*"%>
<%@page import="cn.com.info21.system.*, cn.com.info21.auth.*, cn.com.info21.util.*,cn.com.info21.org.*,cn.com.shasha.sys.*"%>
<%
	PagePublic pub = new PagePublic(request, response);
%>
<%!
class PagePublic {
	public int loglevel = 0;
	
	//路径
	public String sysPath = null;
//	public String imgPath = null;
	public String themePath = null;

	//表单变量
	public String pageFile = null;
	public String pageForm = null;
	public String pageSave = null;
	public String pageList = null;
	public String pageDel = null;
	public String pageName = "";
	public String pageMeta = "";

	//系统变量
	public String reqestMethod = null;
	public java.sql.Date today = null;
	public SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	public SimpleDateFormat dtFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());;
	public SimpleDateFormat dtShortFormatter = new SimpleDateFormat("yyMMdd HH:mm", Locale.getDefault());
	public SimpleDateFormat dtNo = new SimpleDateFormat("yyMMdd", Locale.getDefault());
	public NumberFormat moneyformatter = NumberFormat.getCurrencyInstance();
	AuthToken authToken = null;
	public String curUser = null;
	public long curUserID = 0;
	public String curUserCnname = null;
	boolean isSuperAdmin = true;
	public String charset = "";
	public PagePublic(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma","No-cache"); 
		response.setHeader("Cache-Control","no-cache"); 
		response.setDateHeader("Expires", 0); 
		charset = "GBK";
	}
}
%>