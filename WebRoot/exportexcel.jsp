<%@ page contentType="text/html;charset=GBK"%>
<% 
//System.out.println("ooooo");
//System.out.println(request.getParameter("exportContent"));
response.setCharacterEncoding("utf-8");
response.setHeader("Content-Type","application/force-download");
response.setHeader("Content-Type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment;filename=export.xls");
out.print(request.getParameter("exportContent")); 
%> 