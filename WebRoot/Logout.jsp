<%@ page import="cn.com.info21.util.*"%>
<%//@ page contentType="text/html; charset=gb2312"%>
<%//@ page errorPage="/Error.jsp"%>
<%	
  	session.invalidate();
	
	//SkinUtils.removeUserAuthorization(request,response);
	response.sendRedirect("LoginForm.jsp");
	//out.println(sysPath);
%>