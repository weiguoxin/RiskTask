/*
 * 创建日期 2005-8-9
 */

package cn.com.info21.workflow.engine.executor;
import java.sql.*;
import java.util.*;
import cn.com.info21.database.DbConnectionManager;
import cn.com.info21.org.*;

/**
 * @author lkh
 */

public class ConditionAssign extends Assign {
    private String xmlstr = null;
    private Vector roles = null;
    private Vector depts = null;
    private Vector dutys = null;
    private int curdept = 0;
    private int deptid = 0;
    /**
     * 默认构造器
     */
    public ConditionAssign() {
    }
    /**
     * 构造器二
     * @param xmlstr 人员选择规则xml字符串
     * @param deptid 部门ID
     */
    public ConditionAssign (String xmlstr, int deptid) {
        this.xmlstr = xmlstr;
        this.deptid = deptid;
    }
    /**
     * 获取人员
     * @return Vector 人员集合
     */
    public Vector getPersons() {
        Vector persons = null;
        Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
        try {
            User user = null;
            User puser = null;
            int uid = 0;
            String sqlstr = this.parseXml();
    		con = DbConnectionManager.getConnection();
    		pstmt = con.prepareStatement(sqlstr);
    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    		    uid = rs.getInt("userid");
    		    user = new User(uid);
    		    user.setDeptID(rs.getInt("deptid"));
    		    //获取代理人员
    		    puser = Tools.getProxyUser(uid);
    		    if (null != puser) {
    		        user.setProxyUser(puser);
    		    }
    		    persons.addElement(user);
    		    user = null;
    		    puser = null;
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persons;
    }
    /**
     * 解析人员选择规则字符串
     * @return String
     */
    private String parseXml() {
        String sql = null;
        try {
            roles = Tools.getValuesFromXML(xmlstr, "roleid");
            depts = Tools.getValuesFromXML(xmlstr, "deptid");
            dutys = Tools.getValuesFromXML(xmlstr, "dutyid");
            curdept = Tools.getIntValueFromXML(xmlstr, "curdept");
            int i = 0;
            if (null != roles && !roles.isEmpty()) {
                for (i = 0; i < roles.size(); i++) {
                    if (null == sql) {
                        sql = "roleid = " + (String) roles.elementAt(i);
                    } else {
                        sql = sql + " and " + "roleid = " + (String) roles.elementAt(i);
                    }
                }
            }
            if (null != depts && !depts.isEmpty()) {
                for (i = 0; i < depts.size(); i++) {
                    if (null == sql) {
                        sql = "deptid = " + (String) depts.elementAt(i);
                    } else {
                        sql = sql + " and " + "deptid = " + (String) depts.elementAt(i);
                    }
                }
            }
            if (null != dutys && !dutys.isEmpty()) {
                for (i = 0; i < dutys.size(); i++) {
                    if (null == sql) {
                        sql = "dutyid = " + (String) dutys.elementAt(i);
                    } else {
                        sql = sql + " and " + "dutyid = " + (String) dutys.elementAt(i);
                    }
                }
            }
            if (curdept > 0) {
                if (null == sql) {
                    sql = " deptid = " + deptid;
                } else {
                    sql = sql + " and " + "deptid = " + deptid;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }
}
