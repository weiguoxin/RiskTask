package cn.com.shasha.sys;


import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

import java.sql.*;

public class TaskMan {
	private static String tableName = "TASKMAN";
	private static String LOAD_INFO_TASKMAN_BY_ID =
		"SELECT * FROM TASKMAN WHERE ID=?";
	private static String INSERT_INFO_TASKMAN =
		"INSERT INTO TASKMAN(TASKTITLE) VALUES(?)";
	private static String UPDATE_INFO_TASKMAN =
		"UPDATE TASKMAN SET TASKTITLE=?, TASKSTATUS=?, TASKDETAIL=?, TAXNO=?, TAXNAME=? WHERE ID=?";
	private int id = 0;
	private String tasktitle = "";
	private String taskdetail = "";
	private int taskstatus = 0; //0表示不是通知；1表示是通知。
	private String taxno = "";
    private String taxname = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTasktitle() {
        return tasktitle;
    }

    public void setTasktitle(String tasktitle) {
        this.tasktitle = tasktitle;
    }

    public String getTaskdetail() {
        return taskdetail;
    }

    public void setTaskdetail(String taskdetail) {
        this.taskdetail = taskdetail;
    }

    public int getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(int taskstatus) {
        this.taskstatus = taskstatus;
    }

    public String getTaxno() {
        return taxno;
    }

    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    public String getTaxname() {
        return taxname;
    }

    public void setTaxname(String taxname) {
        this.taxname = taxname;
    }

    /**
	* 构造方法一：根据文档id号，从数据库中读取文档信息。
	* 创建日期：(2003-6-7 18:45:35)
	* @param id int 文档编号。
	* @throws cn.com.info21.system.SystemException。
	*/
	protected TaskMan(int id) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_INFO_TASKMAN_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id; //文档编号
				this.tasktitle = rs.getString("tasktitle"); //文档标题
				this.taskstatus = rs.getInt("taskstatus"); //内容
				this.taskdetail = rs.getString("taskdetail"); //状态
				this.taxno = rs.getString("taxno"); //创建时间
                this.taxname = rs.getString("taxname");
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
	* @throws cn.com.info21.system.SystemException。
	*/
	protected TaskMan(String title) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			this.tasktitle = title;
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_INFO_TASKMAN, PreparedStatement.RETURN_GENERATED_KEYS);
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
			pstmt = con.prepareStatement(UPDATE_INFO_TASKMAN);
			pstmt.setString(1, this.tasktitle);
			pstmt.setInt(2, this.taskstatus);
			pstmt.setString(3, this.taskdetail);
            pstmt.setString(4, this.taxno);
            pstmt.setString(5, this.taxname);
			pstmt.setInt(6, this.id);
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

}