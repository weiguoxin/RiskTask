<%@ page contentType="text/html; charset=GBK" %>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="cn.com.info21.database.DbConnectionManager"%>
<%
Connection con = null;
PreparedStatement pstmt = null;
ResultSet rs = null;
try {
	con = DbConnectionManager.getConnection();
	pstmt = con.prepareStatement("select top 1 from zduser");
	rs = pstmt.executeQuery();	
	if (rs.next()) {
		System.out.println("username--"+rs.getString("username"));
	}
} catch (SQLException sqle) {
	System.err.println("SQLException in self_person(String): " + sqle);
} catch (Exception e) {
	System.err.println("Exception in self_person(String): " + e);
} finally {
	DbConnectionManager.close(con, pstmt, null, rs);
}	
%>