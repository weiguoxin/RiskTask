/*
 * Created on 2004-11-24
 *  
 */
package cn.com.info21.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cn.com.info21.database.DbConnectionManager;

public class Tree {
    private String type;

    //private static String MENUS_SQL = "SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM SYS_MENUS WHERE ISENABLE = 'Y' AND TYPE = ? ORDER BY ID";
	private static String MENUS_SQL = "SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF FROM SYS_MENUS WHERE ISUSERMENU=1 AND ISENABLE = 'Y' AND TYPE = ? AND ID IN (SELECT MENUID FROM SYS_ROLEMENUS WHERE ROLEID IN (SELECT ROLEID FROM org_user_role WHERE userid = ?)) ORDER BY ID";

    private static String MENUS_SQL_ALL = "SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM SYS_MENUS WHERE ISUSERMENU=1 AND ISENABLE = 'Y' AND TYPE = ? ORDER BY ID";

    public Tree() {
        super();
    }

    public Tree(String type) {
        this.type = type;
    }

    /**
     * 构造树型菜单
     * 
     * @return String menuStr
     */

    public String showTree(boolean isAdmin, int userid) {
        Connection conn = null;
        PreparedStatement sMenus = null;
        ResultSet rsMenus = null;
        String menuStr = "";
        java.util.Vector vTree = new java.util.Vector();
        try {
            String tempStr = "";
            conn = DbConnectionManager.getConnection();
            if (isAdmin) {
                sMenus = conn.prepareStatement(MENUS_SQL_ALL);
                sMenus.setString(1, type);
            } else {
                sMenus = conn.prepareStatement(MENUS_SQL);
                sMenus.setString(1, type);
                sMenus.setInt(2, userid);
            }

            rsMenus = sMenus.executeQuery();
            while (rsMenus.next()) {
                String menuType;
                String mpid, mid;
                mid = rsMenus.getString("ID");
                int mlevel = rsMenus.getInt("MLEVEL");
                String entryName = rsMenus.getString("NAME");
                if (entryName == null)
                    entryName = "";
                String entryHref = rsMenus.getString("HREF");
                if (entryHref == null)
                    entryHref = "";
                if (!"".equals(entryHref)) {
                    if (entryHref.indexOf("?") > 0) {
                        entryHref = entryHref + "&navMenu=" + mid;
                    } else {
                        entryHref = entryHref + "?navMenu=" + mid;
                    }
                }
                boolean ishref = false;
                if ("Y".equals(rsMenus.getString("ISLEAF").toUpperCase()) && !"".equals(entryHref)) {
                    ishref = true;
                }
                String entryExplain = rsMenus.getString("MEXPLAIN");
                if (entryExplain == null)
                    entryExplain = "";
                mpid = rsMenus.getString("PID");
                if (mpid == null || "".equals(mpid) || "0".equals(mpid)) {
                    menuStr += "var item" + mid + " = new WebFXTree('"
                            + entryName + "');\n" + "item" + mid
                            + ".setBehavior('classic');\n";
                    vTree.add("item" + mid);
                } else if (ishref) {
                    menuStr += "var item" + mid + " = new WebFXTreeItem('"
                            + entryName + "','" + entryHref+ "');\n";
                    menuStr += "item" + mpid + ".add(item" + mid + ");\n";
                } else {
                    menuStr += "var item" + mid + " = new WebFXTreeItem('"
                            + entryName + "');\n";
                    menuStr += "item" + mpid + ".add(item" + mid + ");\n";
                }

            }
            if (vTree != null && vTree.size() > 0) {
                for (int j = 0; j < vTree.size(); j++) {
                    menuStr += "document.write(" + (String) vTree.elementAt(j)
                            + ")\n";
                }
            }

        } catch (Exception e) {
            System.out.println(MENUS_SQL);
            System.out.println("*****" + e.getMessage());
        } finally {
            try {
                sMenus.close();
                rsMenus.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return menuStr;
    }
}