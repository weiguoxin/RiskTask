<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
import="cn.com.shasha.services.TasksData,com.wgx.util.json.*,cn.com.shasha.sys.WLUser"%>
<%
	WLUser nowuser = ((WLUser)request.getSession().getAttribute("Session_User"));
	JSONObject o = new JSONObject(TasksData.count_Tasks_SWJG_DM(request,nowuser,response));
	JSONArray ja = o.getJSONArray("topic");
	System.out.println(ja.toString());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>　</title>
    <style type="text/css">
    	body{
    		margin:0px;
    		padding:0px;
    		text-align:center;
    	}
    	table{
    		margin: 00 auto 0 auto;
	  		border-collapse: collapse;
	        border: none;
    	}
    	body table td{
    		text-align:center;
    		border: solid black 1px;
    		height:24px;
    		margin:0px;
    		padding:0px;
    	}
    </style>
    <script type="text/javascript"> 
function go() {       
	 window.print();
	 window.opener=null;
	 window.close();
} /*[ 'id', 'zxr_dm', 'a', 'b', 'c', 'username' ],*/
</script>  
  </head>
  <body onload="go()">
	<table>
  	<thead></thead>
  	<tr>
  		<td width='100'>序号</td>
  		<td width='100'>执行人姓名</td>
  		<td width='160'>所有任务</td>
  		<td width='100'>未完成任务</td>
  		<td width='100'>已完成任务</td>
  		<td width='100'>完成百分比</td>
  	</tr>
  	<%
  		int all = ja.length();
  	try {
  		for(int i=0;i<all;i++){
  			JSONObject j = ja.getJSONObject(i);
  			float a = Float.parseFloat(j.getString("a"));
  			float c = Float.parseFloat(j.getString("c"));
  	%>
  	<tr>
  		<td><%=j.getString("id") %></td>
  		<td><%=j.getString("username") %></td>
  		<td><%=j.getString("a") %></td>
  		<td><%=j.getString("b") %></td>
  		<td><%=j.getString("c") %></td>
  		<td><%=(Math.round(c / a * 10000) / 100.00 + "%") %></td>
  	</tr>
  	<%}}catch (Exception e) {
		e.printStackTrace();
	}  %>
  </table>
  </body>
</html>
