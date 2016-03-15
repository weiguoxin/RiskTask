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
 * �����ֽ⹤��
 */

public class ParameterTools {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public ParameterTools() {
	}
	/**
	 * ��ȡ��ǰ�ID
	 * @param par �������
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
	 * ��ȡ��ǰ����ID
	 * @param par �������
	 * @return ��ǰ����ID
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
	 * ��ȡ��һ���û�ID
	 * @param par �������
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
	 * ��ȡ��һ���û�����ID
	 * @param par �������
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
	 * ��ȡ�����û�ID
	 * @param par �������
	 * @return Vector�û�ID����
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
	 * ��ȡ�����û�����ID����
	 * @param par �������
	 * @return Vector ����ID����
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
	 * ��ȡĿ��ID
	 * @param par �������
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
	 * ��ȡĿ��ID
	 * @param par ����
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
	 * ��ȡ��ǰ����ID
	 * @param par �������
	 * @return int ����ID
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
	 * ��ȡ��ǰ���̽����ID
	 * @param proid ����ID
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
	 * ����Ŀ��ڵ�
	 * @param targetid Ŀ��ID
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
	 * ��ȡ��ǰ�û�ID
	 * @param par ����
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
	 * ��ȡ����Ŀ��ID
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
	 * ��ȡĿ��ڵ���user��������ԣ�Ŀǰ��Ҫ�����ڻ�ȡ��Ա��id��deptid
	 * @param targets Ŀ��Ŀ¼��
	 * @param tid Ŀ��ID
	 * @param att ����
	 * @return ����ֵ����
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
	 * ��ȡĿ��ڵ�Ŀ¼��
	 * @param par xml�ַ���
	 * @return �ڵ��б�
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
