<%@ page contentType="text/html;charset=GBK"%> 
<%@ page import="cn.com.shasha.sys.*,cn.com.info21.util.StringUtils"%>
<%@ page import="java.io.PrintWriter"%>
<%
String id = request.getParameter("userid");
String oldpwd = request.getParameter("oldpwd");
String password =request.getParameter("uerPass1");//�û�����;
try {
	StringUtils.hash(oldpwd);
	WLUser nowuser = null;
	nowuser = ((WLUser)request.getSession().getAttribute("Session_User"));
	PrintWriter outp = response.getWriter();
	if (StringUtils.hash(oldpwd).equals(nowuser.getUserpass())){
		nowuser = WLUserHome.findById(nowuser.getUserid());
		nowuser.setUserpass(password);
		nowuser.update();
		outp.print("{success:true}");
	}else
	{
		outp.print("{success:false,because:'����ľ��û����룬����������!'}");
	}	
	outp.flush();
	outp.close();
} catch (Exception e) {
	PrintWriter outp = response.getWriter();
	outp.print("{success:false,because:''}");
	outp.flush();
	outp.close();	
	//out.print(e);
}

%>