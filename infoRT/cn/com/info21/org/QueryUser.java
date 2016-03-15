/*
 * Created on 2004-7-29
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package cn.com.info21.org;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import cn.com.info21.util.regexp.RE;
import cn.com.info21.system.*;
import cn.com.info21.system.SystemException;
/**
 * @author Hu Guohua
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class QueryUser {
	private String initial = "com.sun.jndi.ldap.LdapCtxFactory";	//初始化类名
	private String host = "ldap://oa.winowner.com:389";
	private String searchBase = "o=winowner";
	private String filter = "";
	private DirContext ctx = null;								//上下文驱动
	private SearchControls sc = new SearchControls();
	private Hashtable env = new Hashtable();					//哈唏表邦定
	private int rootOrg = 0;
	private StringBuffer xml;
	/**
	 * 测试用
	 * @param args args
	 */
	public static void main(String[] args) {
		QueryUser qu = new QueryUser();
	}

	/**
	 * 构造函数
	 */
	public QueryUser() {
		rootOrg = Integer.parseInt(Info21Config.getProperty("ldap.rootOrg"));
		host = "ldap://10.128.0.168:389";
		searchBase = "OU=中南电力,DC=info21,DC=net";

		String filterUser = "(objectclass=person)";
		filterUser = "(|" + filterUser + "(objectclass=user))";
		filterUser = "(|" + filterUser + "(objectclass=organizationalPerson))";
		filterUser = "(|" + filterUser + "(objectclass=inetOrgPerson))";

		filterUser = "(&(cn=*)" + filterUser + ")";
		String filterOU = "(ou=*)";

		filter = filterUser;
		filter = "(|" + filterOU + filterUser + ")";

		sc = new SearchControls();			//新建搜索器
		sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);	//指定搜索范围
		//sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

		env.put(Context.INITIAL_CONTEXT_FACTORY, initial);         //初始化上下文
		env.put(Context.PROVIDER_URL, host);                       //ldap服务器端口
		env.put(Context.SECURITY_AUTHENTICATION, "simple");        //安全协议
		env.put(Context.SECURITY_PRINCIPAL, "Administrator@info21.net");	//用户名
		env.put(Context.SECURITY_CREDENTIALS, "password");           // 密码

		try {
			ctx = new InitialDirContext(env);					//绑定到上下文驱动
			xml = getXML(rootOrg, searchBase);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ctx.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param pid pid
	 * @param searchBase ldap的searchbase
	 * @return xml格式的查询结果
	 */
	private StringBuffer getXML(int pid, String searchBase) {
		StringBuffer bf = new StringBuffer();
		TreeMap tmap = new TreeMap();
		NamingEnumeration results = null;					//查询结果
		try {
			results = ctx.search(searchBase, filter, sc);
		} catch (NamingException ne) {
			ne.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (results != null && results.hasMoreElements()) {
			try {
				String value = null;
				SearchResult sr = (SearchResult) results.next();
				String dn = "";		//得到DN
				if (sr.getName().equals("")) {
					dn = searchBase;
				} else {
					dn = sr.getName() + "," + searchBase;
				}
				Object[] tmp = getNodeByResult(pid, dn);
				tmap.put((String) tmp[0], tmp);
			} catch (NamingException ne) {
				ne.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		Iterator iter = tmap.entrySet().iterator();
		while (iter.hasNext()) {
			try {
				Map.Entry entry = (Map.Entry) iter.next();
				Object[] tmp = (Object[]) entry.getValue();
				StringBuffer bftmp = (StringBuffer) tmp[3];
				bf.append(bftmp.toString());
				if (((Boolean) tmp[1]).booleanValue()) {
					bf.append(getXML(((Integer) tmp[4]).intValue(), (String) tmp[2]).toString());
				}
				bf.append("</entry>\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bf;
	}



	/**
	 * @param pid pid
	 * @param dn dn
	 * @return object数组
	 */
	private Object[] getNodeByResult(int pid, String dn) {
		Object[] rtn = new Object[5];
		StringBuffer bf = new StringBuffer();
		String key = "";			//当前entry的key
		boolean bOrg = false;
		int did = -1;
		try {
			String txt = dn.substring(0, dn.indexOf(","));
			txt = txt.substring(txt.indexOf("=") + 1);

			bf.append("<entry");
			bf.append(" dn=\"" + dn + "\"");
			bf.append(" text=\"" + txt + "\"");
			bf.append(" value=\"" + dn + "\"");
			String exist = "0";
			if (dn.startsWith("CN=")) {
				String[] attname = {"sid", "sAMAccountName"};
				Attributes attr = ctx.getAttributes(dn, attname);      //获得属性
				String uid = (String) ((Attribute) (attr.getAll().nextElement()))
						.getAll().nextElement();
				if (chkUser(uid)) {
					exist = "1";
				}
				bf.append(" uid=\"" + uid + "\"");
				bf.append(" exist=\"" + exist + "\"");
				bf.append(" org=\"0\"");
				key = new String(("1|" + dn).getBytes("GBK"), "ISO-8859-1");
			} else {
				if (pid > 0) {
					DeptIterator iter = DeptHome.findByCondition("parentid=" + pid
							+ " and deptname='" + txt + "'");
					if (iter.hasNext()) {
						Dept dept = (Dept) iter.next();
						did = dept.getId();
					}
				}
				if (chkDept(pid, txt)) {
					exist = "1";
				}
				bf.append(" exist=\"" + exist + "\"");
				bf.append(" org=\"1\"");
				key = new String(("0|" + dn).getBytes("GBK"), "ISO-8859-1");
				bOrg = true;
			}
			bf.append(">\n");
		} catch (Exception e) {
			key = "";
			e.printStackTrace();
		}
		rtn[0] = key;
		rtn[1] = new Boolean(bOrg);
		rtn[2] = dn;
		rtn[3] = bf;
		rtn[4] = new Integer(did);
		return rtn;
	}


/**
 * @param pid pid
 * @param isOrg 部门标志
 * @param name 名称
 * @param dn ldap dn
 * @param mode 模式
 * @return 执行信息
 */
//FROM DATABASE
	public static String importOrgEntry(int pid, String isOrg,
			String name, String dn, String mode) {
		String rtn = "";
		String txt = dn;
		if (txt.indexOf(",") != -1) {
			txt = txt.substring(0, txt.indexOf(","));
		}
		txt = txt.substring(txt.indexOf("=") + 1);
		try {
			if (mode.equals("0")) {		//导入到当前部门
				if (isOrg.equals("0")) {
					if (chkUser(name)) {
						rtn += "<li>用户" + dn + "已存在！</li>\n";
					} else {
						User user = UserHome.create(name);
						user.setCNName(txt);
						user.update();
						int[] depts = {pid};
						int[] dutys = {pid};
						user.setDepts(depts,dutys);
						rtn += "<li>用户" + dn + "已成功导入！</li>\n";
					}
				} else {
					if (chkDept(pid, name)) {
						rtn += "<li>部门" + dn + "已存在！</li>\n";
					} else {
						Dept dept = DeptHome.create(name);
						dept.setParentid(pid);
						dept.update();
						rtn += "<li>部门" + dn + "已成功导入！</li>\n";
					}
				}
			} else {
				String tmp = dn;
				int rootOrg = Integer.parseInt(Info21Config.getProperty("ldap.rootOrg"));
				String base = Info21Config.getProperty("ldap.base");
				int tmpi = tmp.lastIndexOf(base);
				if (tmpi > 0 && tmpi + base.length() == tmp.length()) {
					tmp = tmp.substring(0, tmpi - 1);
				}

				int pid2 = rootOrg;
				while (!tmp.equals("")) {
					String tmp2 = tmp.substring(tmp.lastIndexOf(",") + 1);
					String txt2 = tmp2.substring(tmp2.lastIndexOf("=") + 1);
					if (tmp2.startsWith("CN=")) {
						rtn += importOrgEntry(pid2, "0", name, tmp2, "0");
					} else {
						rtn += importOrgEntry(pid2, "1", txt2, tmp2, "0");
						DeptIterator iter = DeptHome.findByCondition("parentid=" + pid2
								+ " and deptname='" + txt2 + "'");
						pid2 = 0;
						if (iter.hasNext()) {
							pid2 = ((Dept) iter.next()).getId();
						}
					}
					tmp = tmp.substring(0, tmp.length() - tmp2.length());
					if (tmp.endsWith(",")) {
						tmp = tmp.substring(0, tmp.length() - 1);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			rtn = "";
		}
		return rtn;
	}


	/**
	 * @param username 用户名
	 * @return 检查用户是否存在
	 */
	public static boolean chkUser(String username) {
		boolean rtn = false;
        try {
        	rtn = UserHome.getUserCount("username='" + username + "'") > 0;
        } catch (Exception e) {
        	rtn = false;
        	e.printStackTrace();
        }
		return rtn;
	}
	/**
	 * @param pid pid
	 * @param deptname 部门名称
	 * @return 检查部门是否存在
	 */
	public static boolean chkDept(int pid, String deptname) {
		boolean rtn = false;
        try {
        	int count = DutyHome.getDutyCount("parentid=" + pid
					+ " and deptname='" + deptname + "'");
			rtn = count > 0;
        } catch (Exception e) {
        	rtn = false;
        	e.printStackTrace();
        }
		return rtn;
	}

	/**
	 * @param rootid pid
	 * @param pPath 部门path
	 * @return 查询xml
	 */
	public static StringBuffer getDeptListByParent(int rootid, String pPath) {
		StringBuffer bf = new StringBuffer();
		String curPath = "";
		try {
			OrgEntryIterator iter = OrgEntryHome.findByCondition("entrytype=1"
					+ " and parentid=" + rootid);
			if (iter.hasNext()) {
				while (iter != null && iter.hasNext()) {
					OrgEntry echild = (OrgEntry) iter.next();
					if (pPath == null || pPath.equals("")) {
						curPath = echild.getName();
					} else {
						curPath = pPath + "@" + echild.getName();
					}
					bf.append("<entry text=\"" + echild.getName() + "\""
						+ " value=\"" + echild.getId0() + "\""
						+ " path=\"" + curPath + "\""
						+ ">");
					bf.append("</entry>");
					bf.append(getDeptListByParent(echild.getId0(), curPath).toString());
				}
			}
		} catch (SystemException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bf;
	}

	/**
	 * @param rootid pid
	 * @return 查询的xml
	 */
	public static StringBuffer getDeptByParent(int rootid) {
		StringBuffer bf = new StringBuffer();
		try {
			OrgEntryIterator iter = OrgEntryHome.findByCondition("entrytype=1"
					+ " and parentid=" + rootid);
			if (iter.hasNext()) {
				while (iter != null && iter.hasNext()) {
					OrgEntry echild = (OrgEntry) iter.next();
					bf.append("<entry text=\"" + echild.getName() + "\""
						+ " value=\"" + echild.getId0() + "\">");
					bf.append(getDeptByParent(echild.getId0()).toString());
					bf.append("</entry>");
				}
			}
		} catch (SystemException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bf;
	}
	/**
	 * @param rootid pid
	 * @return 查询的xml
	 */
	public static StringBuffer getUserByParent(int rootid) {
		StringBuffer bf = new StringBuffer();
		try {
			//OrgEntry entry = OrgEntryHome.findById("WF_dept_1_" + rootid);
			//bf.append(entry.getXML2());
			try {
				OrgEntryIterator iter = OrgEntryHome.findByCondition("parentid="
						+ rootid);
				//OrgEntryIterator iter = entry.getChildren();
				if (iter.hasNext()) {
					//bf.append("<entry>");
					while (iter != null && iter.hasNext()) {
						OrgEntry echild = (OrgEntry) iter.next();
						//bf.append(getTreeEntryInner(echild, src, rootid));
						if (echild.getEtype() == 0) {
							bf.append("<entry text=\"" + echild.getName() + "\""
								+ " eid=\"" + echild.getId0() + "\""
								+ " value=\"" + echild.getId0() + "\">");
						} else {
							bf.append("<entry text=\"" + echild.getName() + "\""
								+ " eid=\"" + echild.getId0() + "\"" + ">");
							bf.append(getUserByParent(echild.getId0()).toString());
						}
						bf.append("</entry>");
					}
					//bf.append("</entry>");
				}
			} catch (SystemException se) {
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//String sql = "select * from " + OrgEntry.TABLENAME + " where parentid=" + pid;
		return bf;
	}
	/**
	 * @param rootid pid
	 * @param src getchild src
	 * @return 查询xml
	 */
	public static StringBuffer getUserByDeptChild(int rootid, String src) {
		StringBuffer bf = new StringBuffer();
		try {
			//OrgEntry entry = OrgEntryHome.findById("WF_dept_1_" + rootid);
			//bf.append(entry.getXML2());
			try {
				OrgEntryIterator iter = OrgEntryHome.findByCondition("parentid="
						+ rootid);
				//OrgEntryIterator iter = entry.getChildren();
				bf.append("<entry>");
				while (iter != null && iter.hasNext()) {
					OrgEntry echild = (OrgEntry) iter.next();
					bf.append(getTreeEntry(echild, src, rootid).toString());
				}
				bf.append("</entry>");
			} catch (SystemException se) {
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//String sql = "select * from " + OrgEntry.TABLENAME + " where parentid=" + pid;
		return bf;
	}

	/**
	 * @param entry orgentry
	 * @param src load child src
	 * @param pid pid
	 * @return 查询xml
	 */
	public static StringBuffer getTreeEntry(OrgEntry entry, String src, int pid) {
		StringBuffer bf = new StringBuffer();
		Dept pentry = null;
		String deptstr = "";
		String deptval = "";
		RE re = null;
		try {
			pentry = DeptHome.findById(pid);
			if (entry.getEtype() == 0) {
				deptstr = "@" + pentry.getDeptName();
				deptval = "@" + pentry.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		bf.append("<entry text=\"" + entry.getName() + deptstr + "\"");
		if (entry.getEtype() == 0) {
			bf.append(" value=\"" + entry.getId0() + deptval + "\"");
		} else {
			re = new RE("~id~");
			if (re.match(src)) {
				bf.append(" src=\"" + re.subst(src,
						String.valueOf(entry.getId0())) + "\"");
			} else {
				bf.append(" src=\"" + src + "\"");
			}
		}
		bf.append(">");
		bf.append("</entry>");
		return bf;
	}


	/**
	 * @return Returns the xml.
	 */
	public StringBuffer getXml() {
		return xml;
	}
}
