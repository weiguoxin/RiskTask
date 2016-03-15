package cn.com.shasha.sys;


import java.sql.*;
import java.io.*;

import cn.com.info21.system.*;
import cn.com.info21.database.*;
import cn.com.info21.util.*;

public class Article {
	private static String tableName = "SYSNOTICE";
	private static String LOAD_INFO_ARTICLE_BY_ID =
		"SELECT * FROM SYSNOTICE WHERE ID=?";
	private static String INSERT_INFO_ARTICLE =
		"INSERT INTO SYSNOTICE(TITLE) VALUES(?)";
	private static String UPDATE_INFO_ARTICLE =
		"UPDATE SYSNOTICE SET TITLE=?, DOCCONTENT=?, NOTICE=? WHERE ID=?";
	private int id = 0;
	private String title = "";
	private String doccontent = "";
	private String notice = "0"; //0表示不是通知；1表示是通知。
	private java.sql.Timestamp createdate = null;


	/**
	* 构造方法一：根据文档id号，从数据库中读取文档信息。
	* 创建日期：(2003-6-7 18:45:35)
	* @param id int 文档编号。
	* @throws SystemException。
	*/
	protected Article(int id) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_INFO_ARTICLE_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id; //文档编号
				this.title = rs.getString("TITLE"); //文档标题
				this.doccontent = rs.getString("DOCCONTENT"); //内容
				this.notice = rs.getString("notice"); //状态
				this.createdate = rs.getTimestamp("CREATEDATE"); //创建时间
			} else {
				throw new SystemException("");
			}
		} catch (SQLException sqle) {
				sqle.printStackTrace();
		} catch (Exception exception) {
				exception.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	* 构造方法二：给出文档id号和标题，在数据库中创建一条文档记录。
	* 创建日期：(2003-6-7 19:06:55)
	* @param id int 文档编号。
	* @param title String 文档标题。
	* @throws SystemException。
	*/
	protected Article(String title) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			this.title = title;
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_INFO_ARTICLE, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, title);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs.next())
			{
				this.id = rs.getInt("id");
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}			
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_INFO_ARTICLE);
			pstmt.setString(1, this.title);
			pstmt.setString(2, this.doccontent);
			pstmt.setString(3, this.notice);
			pstmt.setInt(4, this.id);
			pstmt.executeUpdate();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void remove() throws SystemException {
		SysFunction.delRecord(tableName, this.id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDoccontent() {
		return doccontent;
	}

	public void setDoccontent(String doccontent) {
		this.doccontent = doccontent;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public java.sql.Timestamp getCreatedate() {
		return createdate;
	}

	public void setCreatedate(java.sql.Timestamp createdate) {
		this.createdate = createdate;
	}
}