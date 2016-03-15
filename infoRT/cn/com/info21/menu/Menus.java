package cn.com.info21.menu;

/**
 * 此处插入类型说明。
 * 创建日期：(01-7-12 9:42:01)
 * @author：chenzhonghui
 */
import java.sql.*;
import javax.servlet.http.*;

import cn.com.info21.database.*;

public class Menus {
	private String type;
	private static String MENUS_SQL =
		"SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM SYS_MENUS WHERE ISENABLE = 'Y' AND TYPE = ? AND ID IN (SELECT MENUID FROM SYS_ROLEMENUS WHERE ROLEID IN (SELECT ROLEID FROM SYS_USERROLE WHERE USERNAME = ?)) ORDER BY ID";

	private static String MENUS_SQL_ALL =
		"SELECT ID, NAME, PID, MLEVEL, MEXPLAIN, ISENABLE, ISLEAF , HREF  FROM SYS_MENUS WHERE ISENABLE = 'Y' AND TYPE = ? ORDER BY ID";

	public Menus() {
		super();
	}

	public Menus(String type) {
		this.type = type;
	}
	/**
		 * 构造树型菜单
		 * 创建日期：(01-7-5 15:39:03)
		 * @return String menusHTML
		 */
	public String getMenusHTML(
		HttpServletRequest request,
		HttpServletResponse response) {
		String menusHTML =
			"<HTML><HEAD><TITLE>menu</TITLE>"
				+ "\n"
				+ "<META content=\"text/html; charset=gb2312\" http-equiv=Content-Type><!--字体大小和颜色可以改变，其余不要改变-->"
				+ "\n"
				+ "<STYLE id=JoustStyles type=text/css>"
				+ "\n"
				+ ".menuItem {"
				+ "\n"
				+ "LEFT: 0px; POSITION: absolute; VISIBILITY: hidden"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".node {"
				+ "\n"
				+ "COLOR: #FFFFFF; FONT-FAMILY: \"宋体\"; FONT-SIZE: 9pt"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".node A:link {"
				+ "\n"
				+ "COLOR: #FFFFFF; TEXT-DECORATION: none"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".node A:visited {"
				+ "\n"
				+ "COLOR: #FFFFFF; TEXT-DECORATION: none"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".node A:active {"
				+ "\n"
				+ "COLOR: #FFF200; TEXT-DECORATION: none"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".node A:hover {"
				+ "\n"
				+ "COLOR: #FFFFFF; TEXT-DECORATION: none"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".leaf {"
				+ "\n"
				+ "COLOR: #FFFFFF; FONT-FAMILY: \"宋体\"; FONT-SIZE: 9pt"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".leaf A:link {"
				+ "\n"
				+ "COLOR: #FFFFFF; TEXT-DECORATION: none"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".leaf A:visited {"
				+ "\n"
				+ "COLOR: #FFFFFF; TEXT-DECORATION: none"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".leaf A:active {"
				+ "\n"
				+ "COLOR: #FFF200; TEXT-DECORATION: none"
				+ "\n"
				+ "}"
				+ "\n"
				+ ".leaf A:hover {"
				+ "\n"
				+ "COLOR: #FFFFFF; TEXT-DECORATION: none"
				+ "\n"
				+ "}"
				+ "\n"
				+ "</STYLE>"
			//+ "\n"
		//+ "<SCRIPT language=javascript src='/yofc/js/menuDef.js'></SCRIPT>"
	+"\n"
		+ "<SCRIPT language=JavaScript>"
		+ "\n"
		+ "var guageWin;"
		+ "\n"
		+ "parent.init();"
		+ "\n"
		+ "var theMenuRef = \"parent.theMenu\";"
		+ "\n"
		+ "var theMenu = eval(theMenuRef);"
		+ "\n"
		+ "var theBrowser = parent.theBrowser;"
		+ "\n"
		+ "var menuStart = 0;"
			//+ "\n"
		//	+ "function initMenu() {"
	+"\n"
		+ "if (parent.theBrowser) {"
		+ "\n"
		+ "if (parent.theBrowser.canOnError) {window.onerror = parent.defOnError;}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "if (theMenu) {"
		+ "\n"
		+ "theMenu.amBusy = true;"
		+ "\n"
		+ "if (theBrowser.hasDHTML) {"
		+ "\n"
		+ "if (document.layers) {"
		+ "\n"
		+ "document.ids.menuTop.position = \"absolute\";"
		+ "\n"
		+ "document.ids.menuBottom.position = \"absolute\";"
		+ "\n"
		+ "document.ids.menuBottom.visibility = \"hidden\";"
		+ "\n"
		+ "document.ids.statusMsgDiv.position = \"absolute\";"
		+ "\n"
		+ "} else {"
		+ "\n"
		+ "if (document.all) {"
		+ "\n"
		+ "with (document.styleSheets[\"JoustStyles\"]) {"
		+ "\n"
		+ "addRule (\"#menuTop\", \"position:absolute\");"
		+ "\n"
		+ "addRule (\"#menuBottom\", \"position:absolute\");"
		+ "\n"
		+ "addRule (\"#menuBottom\", \"visibility:hidden\");"
		+ "\n"
		+ "addRule (\"#statusMsgDiv\", \"position:absolute\");"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
			//+ "}"
		//+ "\n"
	+"function openGuage () {       "
		+ "\n"
		+ "guageWin = window.open('/yofc/image/progress.html','进度条','width=220,height=25,pageXOffset=300,pageYOffset=200,location=no,menubar=no,status=no,toolbar=no,center=yes,resizable=no,scrollbars=no');"
		+ "\n"
		+ "guageWin.moveTo(300,200)"
		+ "\n"
		+ "guageWin.resizeTo(220,25)"
		+ "\n"
		+ "return false;"
		+ "\n"
		+ " } "
		+ "\n"
		+ "function closeGuage () {       "
		+ "\n"
		+ "if (guageWin != null) "
		+ "guageWin.close();"
		+ "\n"
		+ "return false;"
		+ "\n"
		+ " } "
		+ "\n"
		+ "function doIt() {       "
		+ "\n"
		+ "window.parent.frames(\"content\").location.reload()"
		+ "\n"
		+ " } "
		+ "\n"
		+ "function getDHTMLObj(objName) {"
		+ "\n"
		+ "return eval('document' + theBrowser.DHTMLRange + '.' + objName + theBrowser.DHTMLStyleObj);"
		+ "\n"
		+ "}"
		+ "\n"
		+ "function getDHTMLObjHeight(objName) {"
		+ "\n"
		+ "return eval('document' + theBrowser.DHTMLRange + '.' + objName + theBrowser.DHTMLDivHeight);"
		+ "\n"
		+ "}"
		+ "\n"
		+ "function getDHTMLObjTop(theObj) {return (theBrowser.code == \"MSIE\") ? theObj.pixelTop + 0 : theObj.top;}"
		+ "\n"
		+ "function myVoid() { ; }"
		+ "\n"
		+ "function setMenuHeight(theHeight) {"
		+ "\n"
		+ "getDHTMLObj('menuBottom').top = theHeight;"
		+ "\n"
		+ "}"
		+ "\n"
		+ "function drawStatusMsg() {"
		+ "\n"
		+ "if (document.layers) {"
		+ "\n"
		+ "document.ids.statusMsgDiv.top = menuStart;"
		+ "\n"
		+ "} else{"
		+ "\n"
		+ "if (document.all) {"
		+ "\n"
		+ "document.styleSheets[\"JoustStyles\"].addRule (\"#statusMsgDiv\", \"top:\" + menuStart);"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "document.writeln('<DIV ID=\"statusMsgDiv\"><CENTER><font size=\"2\">初始化，请等待...</font></CENTER></DIV>');"
		+ "\n"
		+ "}"
		+ "\n"
		+ "function drawLimitMarker() {"
		+ "\n"
		+ "var b = theBrowser;"
		+ "\n"
		+ "if (theMenu && b.hasDHTML && b.needLM) {"
		+ "\n"
		+ "var limitPos = theMenu.maxHeight + menuStart + getDHTMLObjHeight('menuBottom');"
		+ "\n"
		+ "if (b.code == 'NS') {"
		+ "\n"
		+ "document.ids.limitMarker.position = \"absolute\";"
		+ "\n"
		+ "document.ids.limitMarker.visibility = \"hidden\";"
		+ "\n"
		+ "document.ids.limitMarker.top = limitPos;"
		+ "\n"
		+ "}"
		+ "\n"
		+ "if (b.code == 'MSIE') {"
		+ "\n"
		+ "with (document.styleSheets[\"JoustStyles\"]) {"
		+ "\n"
		+ "addRule (\"#limitMarker\", \"position:absolute\");"
		+ "\n"
		+ "addRule (\"#limitMarker\", \"visibility:hidden\");"
		+ "\n"
		+ "addRule (\"#limitMarker\", \"top:\" + limitPos + \"px\");"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "document.writeln('<DIV ID=\"limitMarker\">&nbsp;</DIV>');"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "function setTop() {"
		+ "\n"
		+ "if (theMenu && theBrowser.hasDHTML) {"
		+ "\n"
		+ "if (getDHTMLObj('menuTop')) {"
		+ "\n"
		+ "//modify the offset of menu to top here"
		+ "\n"
		+ "menuStart = getDHTMLObjHeight(\"menuTop\") + 30;"
		+ "\n"
		+ "drawStatusMsg();"
		+ "\n"
		+ "} else {"
		+ "\n"
		+ "theBrowser.hasDHTML = false;"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "function setBottom() {"
		+ "\n"
		+ "if (theMenu) {"
		+ "\n"
		+ "if (theBrowser.hasDHTML) {"
		+ "\n"
		+ "drawLimitMarker();"
		+ "\n"
		+ "getDHTMLObj(\"statusMsgDiv\").visibility = 'hidden';"
		+ "\n"
		+ "theMenu.refreshDHTML();"
		+ "\n"
		+ "getDHTMLObj('menuBottom').visibility = 'visible';"
		+ "\n"
		+ "}"
		+ "\n"
		+ "theMenu.amBusy = false;"
		+ "\n"
		+ "}"
		+ "\n"
		+ "}"
		+ "\n"
		+ "function frameResized() {if (theBrowser.hasDHTML) {theMenu.refreshDHTML();}}"
		+ "\n"
		+ "/**"
		+ "\n"
		+ " * Initialize the menu here"
		+ "\n"
		+ " */"
		+ "\n"
		+ "function initialise() {"
		+ "\n"
		+ "//Accordint to the javascipt, i have to add the menu root using addEntry() at fisrt,"
		+ "\n"
		+ "//and then add menu items using addChild() level by level;"
		+ "\n"
		+ "//Set up parameters to control menu behaviour"
		+ "\n"
		+ "if (theMenu.firstEntry == 0) return;"
		+ "\n"
		+ "theMenu.autoScrolling = true;"
		+ "\n"
		+ "theMenu.modalFolders = false;"
		+ "\n"
		+ "theMenu.linkOnExpand = false;"
		+ "\n"
		+ "theMenu.toggleOnLink = false;"
		+ "\n"
		+ "theMenu.showAllAsLinks = false;"
		+ "\n"
		+ "theMenu.savePage = true;"
		+ "\n"
		+ "theMenu.tipText = \"status\";"
		+ "\n"
		+ "theMenu.selectParents = false;"
		+ "\n"
		+ "theMenu.name = \"theMenu\";"
		+ "\n"
		+ "theMenu.container = \"self.menu\";"
		+ "\n"
		+ "theMenu.reverseRef = \"parent\";"
		+ "\n"
		+ "theMenu.contentFrame = \"content\";"
		+ "\n"
		+ "theMenu.defaultTarget = \"content\";"
		+ "\n"
		+ "//Initialise all the icons"
		+ "\n"
		+ "parent.initOutlineIcons(theMenu.imgStore);"
		+ "\n";
		Connection conn = null;
		PreparedStatement sMenus = null;
		ResultSet rsMenus = null;
		try {
			String tempStr = "";
			conn = DbConnectionManager.getConnection();
			sMenus = conn.prepareStatement(MENUS_SQL_ALL);
			sMenus.setString(1, type);

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
				String entryExplain = rsMenus.getString("MEXPLAIN");
				if (entryExplain == null)
					entryExplain = "";
				if (mlevel > 0) {
					mpid = rsMenus.getString("PID");
					if (rsMenus.getString("ISLEAF").equals("N")) {
						menuType = "Folder";
					} else {
						menuType = "Document";
					}
					if (mlevel == 1) {

						menusHTML =
							menusHTML
								+ "m"
								+ mid
								+ "id = theMenu.addEntry(-1,\""
								+ menuType
								+ "\",\""
								+ entryName
								+ "\",\""
								+ entryHref
								+ "\",\""
								+ entryExplain
								+ "\");"
								+ "\n"
								+ "theMenu.entry[m"
								+ mid
								+ "id].isopen = true;\n";
					} else {

						menusHTML =
							menusHTML
								+ "m"
								+ mid
								+ "id = theMenu.addChild(m"
								+ mpid
								+ "id, \""
								+ menuType
								+ "\", \""
								+ entryName
								+ "\", \""
								+ entryHref
								+ "\", \""
								+ entryExplain
								+ "\", \"\", \"content\");\n";

					}
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

		menusHTML =
			menusHTML
				+ "\n"
				+ "}"
				+ "\n"
				+ "self.defaultStatus = \"\";"
				+ "\n"
				+ "</SCRIPT>"
				+ "\n"
				+ "<SCRIPT language=JavaScript>"
				+ "\n"
				+ "if (theMenu) "
				+ "\n"
				+ "{"
				+ "\n"
				+ "initialise();"
				+ "\n"
				+ "} else {"
				+ "\n"
				+ "document.writeln(\"<p>菜单初始化错误！</p>\");"
				+ "\n"
				+ "}"
				+ "\n"
				+ "</SCRIPT>"
				+ "\n"
				+ "<META content=\"MSHTML 5.00.2920.0\" name=GENERATOR></HEAD>"
				+ "\n"
			//+ "<BODY background='/yofc/image/leftBG.gif' onresize=frameResized(); "
	+"<BODY bgcolor='3177C0' onresize=frameResized(); "
		+ "\n"
		+ "topMargin=0 leftMargin=0>"
		+ "<form name='form1'>"
		+ "\n"
		+ "<DIV id=menuTop><!-- keep this id undeleted --></DIV>"
		+ "\n"
		+ "<P>"
		+ "\n"
		+ "<SCRIPT language=JavaScript>"
		+ "\n"
		+ "setTop();"
		+ "\n"
		+ "</SCRIPT>"
		+ "\n"
		+ "<!--这些字体，颜色等参数应用于后面的菜单 -->"
		+ "\n"
		+ "<FONT face=宋体>"
		+ "\n"
		+ "<SCRIPT language=JavaScript>"
		+ "\n"
		+ "if (theMenu) {"
		+ "\n"
		+ "parent.DrawMenu(theMenu);"
		+ "\n"
		+ "}"
		+ "\n"
		+ "</SCRIPT>"
		+ "\n"
		+ "</FONT>"
		+ "\n"
		+ "<DIV id=menuBottom><!-- keep this id undeleted --></DIV>"
		+ "\n"
		+ "<SCRIPT language=JavaScript>"
		+ "\n"
		+ "setBottom();"
		+ "\n"
		+ "</SCRIPT>"
		+ "\n"
		+ "</form>"
		+ "\n"
		+ "</BODY></HTML>";
		return menusHTML;
	}


	public String showMenu(boolean isAdmin, String userName) {
		Connection conn = null;
		PreparedStatement sMenus = null;
		ResultSet rsMenus = null;
		String menuStr = "";
		try {
			String tempStr = "";
			conn = DbConnectionManager.getConnection();
			if (isAdmin) {
				sMenus = conn.prepareStatement(MENUS_SQL_ALL);
				sMenus.setString(1, type);
			} else {
				sMenus = conn.prepareStatement(MENUS_SQL);
				sMenus.setString(1, type);
				sMenus.setString(2, userName);
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
				String entryExplain = rsMenus.getString("MEXPLAIN");
				if (entryExplain == null)
					entryExplain = "";
				if (mlevel > 0) {
					mpid = rsMenus.getString("PID");
					if (rsMenus.getString("ISLEAF").equals("N")) {
						menuType = "Folder";
					} else {
						menuType = "Document";
					}
					if (mlevel == 1) {

						menuStr =
							menuStr
								+ "m"
								+ mid
								+ "id = theMenu.addEntry(-1,\""
								+ menuType
								+ "\",\""
								+ entryName
								+ "\",\""
								+ entryHref
								+ "\",\""
								+ entryExplain
								+ "\");"
								+ "\n"
								+ "theMenu.entry[m"
								+ mid
								+ "id].isopen = true;\n";
					} else {

						menuStr =
							menuStr
								+ "m"
								+ mid
								+ "id = theMenu.addChild(m"
								+ mpid
								+ "id, \""
								+ menuType
								+ "\", \""
								+ entryName
								+ "\", \""
								+ entryHref
								+ "\", \""
								+ entryExplain
								+ "\", \"\", \"content\");\n";

					}
				}

			}
		} catch (Exception e) {
			//Log.error(MENUS_SQL);
			//Log.error("*****" + e.getMessage());
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
