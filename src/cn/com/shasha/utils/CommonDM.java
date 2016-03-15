package cn.com.shasha.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

public class CommonDM {
	/**
	 * 默认构造函数
	 */
	public CommonDM() {
	}
	/**
	 * 根据kebeid从dm902取kebename
	 */		
	public String getKebeName(String kebe) throws SystemException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		String kebename = "";
		try {
			con = DbConnectionManager.getConnection();
			sql = "select name from coal.dbo.dm902 where txdm='"+ kebe +"'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				kebename = rs.getString("name").trim();
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in Commondm kebename(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in Commondm kebename(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}	
		return kebename;
	}	
	/**
	 * 根据dsdm从allgh取dwmc
	 */		
	public String getallghDwmc(String dsdm) throws SystemException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		String dwmc = "";
		try {
			con = DbConnectionManager.getConnection();
			sql = "select rtrim(dwmc) as dwmc from coal.dbo.allgh where rtrim(dsdm)='"+ dsdm +"'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dwmc = rs.getString("dwmc").trim();
			}
		} catch (SQLException sqle) {
			System.err.println("SQLException in Commondm getallghDwmc(String): " + sqle);
		} catch (Exception e) {
			System.err.println("Exception in Commondm getallghDwmc(String): " + e);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}	
		return dwmc;
	}		
}
