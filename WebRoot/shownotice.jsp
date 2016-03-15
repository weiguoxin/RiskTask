<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.shasha.sys.*"%>
<%
String id= request.getParameter("id");
Article article = ArticleHome.findById(Integer.valueOf(id));
%>
<html>
<body style="background:transparent;" width=450> 
<h3 align="center"><%=article.getTitle()%></h3>
<div style="line-height:22px;letter-spacing:1px;font-size:14px;">
<%=article.getDoccontent()%>
</div>
</body>
</html>