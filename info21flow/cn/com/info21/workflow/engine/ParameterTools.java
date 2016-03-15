/*
 * Created on 2004-7-29
 */

package cn.com.info21.workflow.engine;
import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.util.*;
import cn.com.info21.workflow.*;

/**
 * @author liukh
 * 参数分解工具
 */

public class ParameterTools {
	/**
	 * 默认构造函数
	 */
	public ParameterTools() {
	}
	/**
	 * 获取当前活动ID
	 * @param par 请求参数
	 * @return int
	 */
	public static int getCurActivityID(String par) {
		int curactid = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("actid");
			Node tmpnode = usernodes.item(0);
			if (tmpnode.hasChildNodes()) {
				curactid = Integer.parseInt(tmpnode.getFirstChild().getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curactid;
	}
	/**
	 * 获取当前任务ID
	 * @param par 请求参数
	 * @return 当前任务ID
	 */
	public static int getCurTaskID(String par) {
		int curtaskid = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("taskid");
			Node tmpnode = usernodes.item(0);
			if (tmpnode.hasChildNodes()) {
				curtaskid = Integer.parseInt(tmpnode.getFirstChild().getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curtaskid;
	}

	/**
	 * 获取第一个用户ID
	 * @param par 请求参数
	 * @return ID
	 */
	public static int getFirstUserID(String par) {
		int userid = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("user");
			Node tmpnode = usernodes.item(0);
			if (tmpnode.hasAttributes()) {
				userid = Integer.parseInt(
						tmpnode.getAttributes().getNamedItem("id").getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userid;
	}
	/**
	 * 获取第一个用户部门ID
	 * @param par 请求参数
	 * @return DeptID
	 */
	public static int getFirstUserDeptID(String par) {
		int deptid = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("user");
			Node tmpnode = usernodes.item(0);
			if (tmpnode.hasAttributes()) {
				deptid = Integer.parseInt(
						tmpnode.getAttributes().getNamedItem("deptid").getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptid;
	}
	/**
	 * 获取所有用户ID
	 * @param par 请求参数
	 * @return Vector用户ID集合
	 */
	public static Vector getAllUserIDs(String par) {
		Vector userids = new Vector();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("user");
			Node tmpnode;
			String id = null;
			for (int i = 0; i < usernodes.getLength(); i++) {
				tmpnode = usernodes.item(i);
				if (tmpnode.hasAttributes()) {
					id = tmpnode.getAttributes().getNamedItem("id").getNodeValue();
				} else {
					id = null;
				}
				if (null != id && !"".equals(id)) {
					userids.addElement(id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userids;
	}
	/**
	 * 获取所有用户部门ID集合
	 * @param par 请求参数
	 * @return Vector 部门ID集合
	 */
	public static Vector getAllDeptIDs(String par) {
		Vector deptids = new Vector();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("user");
			Node tmpnode;
			String deptid = null;
			for (int i = 0; i < usernodes.getLength(); i++) {
				tmpnode = usernodes.item(i);
				if (tmpnode.hasAttributes()) {
					deptid = tmpnode.getAttributes().
							getNamedItem("deptid").getNodeValue();
				} else {
					deptid = null;
				}
				if (null != deptid && !"".equals(deptid)) {
					deptids.addElement(deptid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptids;
	}
	/**
	 * 获取目标活动ID
	 * @param par 请求参数
	 * @return int
	 */
	public static int getFirstTargetId(String par) {
		int targetid = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("target");
			Node tmpnode = usernodes.item(0);
			if (tmpnode.hasAttributes()) {
				targetid = Integer.parseInt(
							tmpnode.getAttributes().getNamedItem("id").getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetid;
	}
	/**
	 * 获取目标活动ID
	 * @param par 参数
	 * @return id
	 */
	public static String getTargetId(String par) {
		String id = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList targetnodes = doc.getElementsByTagName("target");
			Node tmpnode = targetnodes.item(0);
			if (null != tmpnode) {
				if (tmpnode.hasAttributes()) {
					id = tmpnode.getAttributes().getNamedItem("id").getNodeValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	/**
	 * 获取当前流程ID
	 * @param par 请求参数
	 * @return int 流程ID
	 */
	public static int getCurFlowID(String par) {
		int flowid = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("flowid");
			Node tmpnode = usernodes.item(0);
			if (tmpnode.hasChildNodes()) {
				flowid = Integer.parseInt(tmpnode.getFirstChild().getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowid;
	}
	/**
	 * 获取当前过程结束活动ID
	 * @param proid 过程ID
	 * @return int
	 */
	public static int getEndActivityID(int proid) {
		int id = 0;
		try {
			String sql = " processid = " + proid + "and type = 2";
			ActivityIterator ai = ActivityHome.findByCondition(sql);
			Activity act = null;
			if (ai.hasNext()) {
				act = (Activity) ai.next();
				if (null != act) {
					id = act.getId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	/**
	 * 创建目标节点
	 * @param targetid 目标活动ID
	 * @return node
	 */
	public Node createTargetNode(int targetid) {
		Element node = null;
		try {
			DocumentBuilderFactory dbf = null;
			DocumentBuilder db = null;
			Document doc = null;
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			node = doc.createElement("target");
			node.setAttribute("id", String.valueOf(targetid));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return node;
	}
	/**
	 * 获取当前用户ID
	 * @param par 参数
	 * @return int
	 */
	public static int getCurUserID(String par) {
		int curuserid = 0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList usernodes = doc.getElementsByTagName("userid");
			Node tmpnode = usernodes.item(0);
			if (tmpnode.hasChildNodes()) {
				curuserid = Integer.parseInt(tmpnode.getFirstChild().getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curuserid;
	}
	/**
	 * 获取所有目标ID
	 * @return Vector
	 */
	public static Vector getAllTargetIDs(String par) {
		Vector targetids = new Vector();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			NodeList targetnodes = doc.getElementsByTagName("target");
			Node tmpnode;
			String id = null;
			for (int i = 0; i < targetnodes.getLength(); i++) {
				tmpnode = targetnodes.item(i);
				if (tmpnode.hasAttributes()) {
					id = tmpnode.getAttributes().getNamedItem("id").getNodeValue();
				} else {
					id = null;
				}
				if (null != id && !"".equals(id)) {
					targetids.addElement(id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetids;
	}
	/**
	 * 获取目标节点中user对象的属性，目前主要是用于获取人员的id和deptid
	 * @param targets 目标目录树
	 * @param tid 目标ID
	 * @param att 属性
	 * @return 属性值集合
	 */
	public static Vector getUserAttByTargetID(NodeList targets, int tid, String att) {
		Vector userids = null;
		try {
			Node tmpnode;
			NodeList chnl;
			Node chnode;
			int tmpid = 0;
			userids = new Vector();
			for (int i = 0; i < targets.getLength(); i++) {
				tmpnode = targets.item(i);
				if (tmpnode.hasAttributes()) {
					tmpid = Integer.parseInt(tmpnode.getAttributes().getNamedItem("id").getNodeValue());
				} else {
					tmpid = 0;
				}
				if (tid == tmpid) {
					chnl = tmpnode.getChildNodes();
					for(int j = 0;j<chnl.getLength();j++) {
						chnode = chnl.item(j);
						if (chnode.hasAttributes()) {
							userids.addElement(chnode.getAttributes().getNamedItem(att).getNodeValue());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userids;
	}
	/**
	 * 获取目标节点目录树
	 * @param par xml字符串
	 * @return 节点列表
	 */
	public static NodeList getTargetNodes(String par) {
		NodeList targetnodes = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(new InputSource(new StringReader(par)));
			targetnodes = doc.getElementsByTagName("target");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetnodes;
	}
}
