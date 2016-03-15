package cn.com.wgx.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.wgx.util.json.JSONObject;

import cn.com.info21.database.DbConnectionManager;
import cn.com.shasha.services.Tasks;

public class DoTaskLaterRunner extends Thread{

	private Tasks task = null;
	private Long sleep = 1000l;
	
	public void run(){
		if(null == task){
			return;
		}
		try {
			this.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		JSONObject o = new JSONObject();
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement("update tasks set status=?,begin_time=? where id in("+task.getId()+") and status=? and (zxr_dm is not null and zxr_dm <>'' and zxr_dm<>-1)");
			pstmt.setString(1, "0");
			pstmt.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(3, "-1");
			int res = pstmt.executeUpdate();
			if(res == 0){
			}else{
				System.out.println("下发任务成功!");
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
		
	}
	
	public void setTask(Tasks task){
		this.task = task;
	}
	
	public void setSleep(Long sleep){
		this.sleep = sleep;
	}
	
}
