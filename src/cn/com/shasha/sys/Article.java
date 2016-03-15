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
	private String notice = "0"; //0��ʾ����֪ͨ��1��ʾ��֪ͨ��
	private java.sql.Timestamp createdate = null;


	/**
	* ���췽��һ�������ĵ�id�ţ������ݿ��ж�ȡ�ĵ���Ϣ��
	* �������ڣ�(2003-6-7 18:45:35)
	* @param id int �ĵ���š�
	* @throws SystemException��
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
				this.id = id; //�ĵ����
				this.title = rs.getString("TITLE"); //�ĵ�����
				this.doccontent = rs.getString("DOCCONTENT"); //����
				this.notice = rs.getString("notice"); //״̬
				this.createdate = rs.getTimestamp("CREATEDATE"); //����ʱ��
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
	* ���췽�����������ĵ�id�źͱ��⣬�����ݿ��д���һ���ĵ���¼��
	* �������ڣ�(2003-6-7 19:06:55)
	* @param id int �ĵ���š�
	* @param title String �ĵ����⡣
	* @throws SystemException��
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