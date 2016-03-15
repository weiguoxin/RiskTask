/*
 * 创建日期 2005-9-2
 */

package cn.com.info21.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SystemException;

/**
 * @author lkh
 */

public class FlowTaskMapping {
    private int id;
    private int procid;
    private int flowid;
    private int status;
    private int taskstatus;
    private int author;
    private int appid;
    //应用序列号
    private int asid;
	/*定义数据库操作************************/
	public static final String TABLENAME = "v_wf_mapping";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE id=?";
	/***********************************/
	private String errorStr = null; 
    public FlowTaskMapping() {
    }
    public FlowTaskMapping(int id) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
			    this.id = rs.getInt("id");
				this.procid = rs.getInt("procid");
				this.appid = rs.getInt("appid");
				this.asid = rs.getInt("asid");
				this.author = rs.getInt("author");
				this.flowid = rs.getInt("flowid");
				this.status = rs.getInt("status");
				this.taskstatus = rs.getInt("taskstatus");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in FlowTaskMapping:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in FlowTaskMapping:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
    public int getId() {
        return this.id;
    }
    public int getProcId() {
        return this.procid;
    }
    public int getAppId() {
        return this.appid;
    }
    public int getAsId() {
        return this.asid;
    }
    public int getAuthor() {
        return this.author;
    }
    public int getFlowId() {
        return this.flowid;
    }
    public int getStatus() {
        return this.status;
    }
    public int getTaskStatus() {
        return this.taskstatus;
    }
}