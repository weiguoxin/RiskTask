/*
 * Created on 2004-5-10
 *
 */

package cn.com.info21.org;
import java.sql.*;

import cn.com.info21.database.*;
import cn.com.info21.system.*;
/**
 * @author Hu Guohua
 */
public class OrgEntry {
	public static final String TABLENAME = "V_ORG_ORGENTRY";
	private static final String LOAD_BY_ID =
		"SELECT * FROM " + TABLENAME + " WHERE ID=?";

	private String errorStr;

	private int id0 = 0;
	private String id;
	private int etype = 0;
	public static final String[] TBS = {"org_user", "org_dept"};
	private String tbname;
	private int status;
	private String name;
	private String cnname;
	private String email;
	private int parentid;
	private Object obj = null;
	//private String[] childids;

	/**
	 * 默认构造函数
	 */
	public OrgEntry() {
	}
	/**
	 * 构造函数
	 * @param id 传入的tbname+"_"+etype+ID0
	 */
	public OrgEntry(String id) {
		try {
			int ipos1 = id.lastIndexOf("_");
			int id0 = Integer.parseInt(id.substring(ipos1 + 1));
			int ipos2 = id.lastIndexOf("_", ipos1 - 1);
			int etype = Integer.parseInt(id.substring(ipos2 + 1, ipos1));
			init(etype, id0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 构造函数
	 * @param id0 传入的ID0
	 * @param type 类型
	 */
	public OrgEntry(int type, int id0) {
		init(type, id0);
	}

	/**
	 * @param id0 传入的ID0
	 * @param type 类型
	 */
	private void init(int type, int id0) {
    	java.sql.Connection con = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_BY_ID);
			pstmt.setString(1, TBS[type] + "_" + type + "_" + id0);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.tbname = rs.getString("tbname");
				this.etype = rs.getInt("entrytype");
				this.id0 = rs.getInt("ID0");
				this.id = rs.getString("ID");
				this.name = rs.getString("name");
				this.cnname = rs.getString("cnname");
				this.email = rs.getString("email");
				this.parentid = rs.getInt("parentid");
				this.status = rs.getInt("status");
				if (type == 0) {
					this.obj = new User(id0);
				} else if (type == 1) {
					this.obj = new Dept(id0);
				}
			}
		} catch (SQLException sqle) {
			errorStr = "SQLException in Dept:constructor(int)-" + sqle;
			System.err.println(errorStr);
		} catch (Exception exception) {
			errorStr = "Exception in Dept:constructor(int)-" + exception;
			System.err.println(errorStr);
		} finally {
			DbConnectionManager.close(con, pstmt, null, rs);
		}
	}
	/**
	 * @return 当前entry的entry角色
	 */
	public Role getRole() {
		Role rtn = null;
		String conStr = "kindOf=1 and name='" + getId() + "'";
		try {
			RoleIterator iter = new RoleIterator(conStr);
			if (iter.hasNext()) {
				rtn = (Role) iter.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	/**
	 * @return innerXML
	 */
	public StringBuffer getInnerXML() {
		StringBuffer bf = new StringBuffer();
		bf.append("	<id>" + this.id + "</id>\n");
		bf.append("	<name>" + this.name + "</name>\n");
		bf.append("	<cnname>" + this.cnname + "</cnname>\n");
		bf.append("	<id0>" + this.id0 + "</id0>\n");
		bf.append("	<etype>" + this.etype + "</etype>\n");
		bf.append("	<tbname>" + this.tbname + "</tbname>\n");
		bf.append("	<status>" + this.status + "</status>\n");
		bf.append("	<email>" + this.email + "</email>\n");
		bf.append("	<pid>" + this.parentid + "</pid>\n");
		return bf;
	}
	/**
	 * @return 返回Proc XML
	 */
	public StringBuffer getTarget() {
		StringBuffer xmlbuf = new StringBuffer();
		if (this.etype == 0) {
			xmlbuf.append("<user id=\"" + this.id0 + "\">\n");
			xmlbuf.append(getInnerXML().toString());
			xmlbuf.append("</user>\n");
		}
//		int size = childids.length;
//		for (int i = 0; i < size; i++) {
//			xmlbuf.append(new OrgEntry(childids[i]).getTarget());
//		}
		return xmlbuf;
	}
	/**
	 * @return 返回XML
	 */
	public StringBuffer getXML() {
		StringBuffer bf = new StringBuffer();
		bf.append("<entry>\n");
		bf.append(this.getInnerXML().toString());
		try {
			OrgEntryIterator iter = getChildren();
			while (iter != null && iter.hasNext()) {
				OrgEntry entry = (OrgEntry) iter.next();
				bf.append(entry.getXML().toString());
			}
		} catch (SystemException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		bf.append("	</entry>\n");
		return bf;
	}
	/**
	 * @return 返回XML on Current Level
	 */
	public StringBuffer getXML2() {
		StringBuffer bf = new StringBuffer();
		bf.append("<entry>\n");
		bf.append(this.getInnerXML().toString());

		bf.append("	</entry>\n");
		return bf;
	}
	/**
	 * 获取下级组织
	 * @return 下级组织条目
	 * @throws SystemException 系统异常
	 */
	public OrgEntryIterator getChildren() throws SystemException {
		if (this.etype == 0) {
			return null;
		} else {
			return OrgEntryHome.findByCondition("parentid=" + this.id0);
		}
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return Returns the deptName.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Returns the cnName.
	 */
	public String getCname() {
		return cnname;
	}


	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId0() {
		return id0;
	}
	/**
	 * @param id0 The id to set.
	 */
	public void setId0(int id0) {
		this.id0 = id0;
	}
	/**
	 * @return Returns the parentid.
	 */
	public int getParentid() {
		return parentid;
	}
	/**
	 * @param parentid The parentid to set.
	 */
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return Returns the etype.
	 */
	public int getEtype() {
		return etype;
	}
	/**
	 * @return Returns the obj.
	 */
	public Object getObj() {
		return obj;
	}
}