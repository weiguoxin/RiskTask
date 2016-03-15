package cn.com.info21.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.system.SysFunction;
import cn.com.info21.system.SystemException;

/**
 * @author Puhongtao
 */

public class Activity {
	public static final int TYPE_START = 1;
	public static final int TYPE_END = 2;
	/*定义属性*/
	//活动ID
	private int id;
	//过程ID
	private int procid;
	//活动名称
	private String name;
	//活动类型，开始|普通|汇聚|结束
	private int type;
	//是否为多任务活动
	private int ismutiltask;
	//活动操作
	private String action;
	//备注描述
	private String remark;

	/*定义数据库操作************/
	public static final String TABLENAME = "WF_ACTIVITY";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";
	private static final String INSERT_OBJ =
		"INSERT INTO " + TABLENAME + "(id,name) VALUES(?,?)";
	private static final String UPDATE_OBJ =
		 "UPDATE " + TABLENAME
		 + " SET procid=?,name=?,type=?,ismutiltask=?,action=?,"
		 + "remark=? WHERE ID=?";
	/***********************************/

	/*定义类运行全局变量*/
	private String errorStr;

	/**
	 * 构造函数一
	 */
	public Activity() {
	}
	/**
	 * 构造函数二
	 * @param name 活动名称
	 * @exception SystemException 系统异常
	 */
	public Activity(String name) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			this.name = name;
			this.id = (int) SysFunction.getMaxID(TABLENAME) + 1;
			con = DbConnectionManager.getConnection();
			if (this.id != -1) {
				pstmt = con.prepareStatement(INSERT_OBJ);
				pstmt.setInt(1, this.id);
				pstmt.setString(2, this.name);
				pstmt.executeUpdate();
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Activity:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Activity:constructor()-" + exception;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 * 构造函数三
	 * @param id 活动ID
	 * @throws SystemException 系统异常
	 */
	public Activity(int id) throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.id = id;
				this.name = rs.getString("name");
				this.type = rs.getInt("type");
				this.procid = rs.getInt("procid");
				this.ismutiltask = rs.getInt("ismutiltask");
				this.action = rs.getString("action");
				this.remark = rs.getString("remark");
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Activity:constructor()-" + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Activity:constructor()-" + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * 更新对象
	 * @throws SystemException 系统异常
	 */
	public void update() throws SystemException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_OBJ);
			pstmt.setInt(1, this.procid);
			pstmt.setString(2, this.name);
			pstmt.setInt(3, this.type);
			pstmt.setInt(4, this.ismutiltask);
			pstmt.setString(5, this.action);
			pstmt.setString(6, this.remark);
			pstmt.setInt(7, this.id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			errorStr = "SQLException in Activity.java:update(): " + sqle;
			throw new SystemException (this, errorStr);
		} catch (Exception e) {
			errorStr = "Exception in Activity.java:update(): " + e;
			throw new SystemException (this, errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, null);
		}
	}
	/**
	 *删除一条记录
	 */
	public void remove() {
		SysFunction.delRecord(TABLENAME, this.id);
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	private void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return Returns the procid.
	 */
	public int getProcId() {
		return procid;
	}
	/**
	 * @param procid The procid to set.
	 */
	public void setProcId(int procid) {
		this.procid = procid;
	}
	/**
	 * @return Returns the ismutiltask.
	 */
	public int getIsmutiltask() {
		return ismutiltask;
	}
	/**
	 * @param ismutiltask The ismutiltask to set.
	 */
	public void setIsmutiltask(int ismutiltask) {
		this.ismutiltask = ismutiltask;
	}
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return Returns the remark.
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark The remark to set.
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取活动分支
	 * @return RamusIterator
	 * @throws SystemException 系统异常
	 */
	public RamusIterator getRamus() throws SystemException {
	    return RamusHome.findByCondition(" actid = " + this.id);
	}
	/**
	 * 根据条件获取活动分支
	 * @return Router
	 * @throws SystemException 系统异常
	 */
	public Router getRouter() throws SystemException {
	    Router router = null;
	    try {
	        RouterIterator ri = RouterHome.findByCondition(" act_id = " + this.id);
	        if (ri.hasNext()) {
	            router = (Router) ri.next();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return router;
	}
}

