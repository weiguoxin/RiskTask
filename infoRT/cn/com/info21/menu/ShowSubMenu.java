package cn.com.info21.menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import cn.com.info21.database.DbConnectionManager;
public class ShowSubMenu {
    private String type;

    //private static String MENUS_SQL = "SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM SYS_MENUS WHERE ISENABLE = 'Y' AND TYPE = ? ORDER BY ID";
	private static String MENUS_SQL = "SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF FROM SYS_MENUS WHERE ISUSERMENU=1 AND ISENABLE = 'Y' AND TYPE = ? AND ID IN (SELECT MENUID FROM SYS_ROLEMENUS WHERE ROLEID IN (SELECT ROLEID FROM org_user_role WHERE userid = ?)) ORDER BY ID";

    private static String MENUS_SQL_ALL = "SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM SYS_MENUS WHERE ISUSERMENU=1 AND ISENABLE = 'Y' AND TYPE = ? ORDER BY ID";

    public ShowSubMenu() {
        super();
    }

    public ShowSubMenu(String type) {
        this.type = type;
    }

    /**
     * 构造树型菜单
     * 
     * @return String menuStr
     */

    public String showDownmenu(boolean isAdmin, int userid) {
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
                mpid = rsMenus.getString("PID");
                if("".equals(mpid) || mpid == null){
                	continue;
                }
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
                menuStr += "<tr><td class=\"flag\">■</td><td><a href=\""+entryHref+"\" target=\"listcontent\">"+entryName+"</a></td></tr>";
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
