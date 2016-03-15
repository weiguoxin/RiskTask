/*
 * �������� 2005-7-26
 */

package cn.com.info21.workflow.engine.executor;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.util.*;
import cn.com.info21.org.*;
import cn.com.info21.workflow.*;
import cn.com.info21.workflow.engine.Source;
import cn.com.info21.workflow.engine.Target;

import java.sql.*;

/**
 * @author lkh
 */

public class Tools {
    /**
     * ��ʼ��Դ
     * @param xmlstr ��������ַ���
     * @return Source
     */
    public static Source getSource(String xmlstr) {
        Source source = new Source();
        try {
            source.setProcId(getIntValueFromXML(xmlstr, "procid"));
   		    source.setActId(getIntValueFromXML(xmlstr, "actid"));
   		    source.setFlowId(getIntValueFromXML(xmlstr, "flowid"));
   		    source.setTaskId(getIntValueFromXML(xmlstr, "taskid"));
   		    source.setUserId(Tools.getIntValueFromXML(xmlstr, "userid"));
   		    source.setDeptId(Tools.getIntValueFromXML(xmlstr, "deptid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }
    /**
     * ��ȡ��һ��Ŀ��
     * @param xmlstr XML����
     * @return Target
     */
    public static Target getFirstTarget(String xmlstr) {
        Target target = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = null;
    		doc = db.parse(new InputSource(new StringReader(xmlstr)));
    		NodeList nodes = null;
    		Node tmpnode = null;
    		String tmpstr = null;
    		Vector tmpusers = null;
    		int id = 0;
    		nodes = doc.getElementsByTagName("ramus");
    		tmpnode = nodes.item(0);
    		if (null != tmpnode) {
    		    tmpstr = tmpnode.getAttributes().getNamedItem("id").getNodeValue();
    		    if (null != tmpstr) {
    		        id = Integer.parseInt(tmpstr);
    		        if (id > 0) {
    		            target.setActId(id);
    		            tmpusers = initTargetUser(tmpnode);
    		            target.setUsers(tmpusers);
    		        }
    		    }
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }
    /**
     * ��ȡĿ�����ݽṹ
     * @param xmlstr �������
     * @return Vector
     */
    public static Vector getAllTargets(String xmlstr) {
        Vector targets = new Vector();
        try {
            Target tmptarget = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = null;
    		doc = db.parse(new InputSource(new StringReader(xmlstr)));
    		NodeList nodes = null;
    		Node tmpnode = null;
    		String tmpstr = null;
    		int tmpint = 0;
    		Vector tmpusers = null;
    		nodes = doc.getElementsByTagName("ramus");
    		for (int i = 0; i < nodes.getLength(); i++) {
    		    tmptarget = new Target();
    		    tmpnode = nodes.item(i);
    		    tmpstr = tmpnode.getAttributes().getNamedItem("id").getNodeValue();
    		    if (null != tmpstr) {
    		        tmpint = Integer.parseInt(tmpstr);
    		        if (tmpint > 0) {
        		        tmptarget.setActId(tmpint);
        		        tmpusers = initTargetUser(tmpnode);
        		        tmptarget.setUsers(tmpusers);
        		        targets.addElement(tmptarget);
        		    }
    		    }
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targets;
    }
    /**
     * ����XML�ļ���ʼ��Ŀ���û�
     * @param node �û��ڵ�
     * @return Vector
     */
    public static Vector initTargetUser(Node node) {
        Vector tmpusers = new Vector();
        try {
            User tmpuser = null;
            String tmpstr = null;
            int userid = 0;
            int deptid = 0;
            Node tmpnode = node.getFirstChild();
            User puser = null;
            while (null != tmpnode) {
                tmpstr = tmpnode.getAttributes().getNamedItem("id").getNodeValue();
    		    if (null != tmpstr) {
    		        userid = Integer.parseInt(tmpstr);
    		    }
    		    tmpstr = tmpnode.getAttributes().getNamedItem("deptid").getNodeValue();
    		    if (null != tmpstr) {
    		        deptid = Integer.parseInt(tmpstr);
    		    }
    		    if (userid > 0) {
    		        tmpuser = new User(userid);
    		        if ((null != tmpuser) && (deptid > 0)) {
    		            tmpuser.setDeptID(deptid);
    		            puser = Tools.getProxyUser(userid);
    		            if (null != puser) {
    		                tmpuser.setProxyUser(puser);
    		            }
    		            tmpusers.addElement(tmpuser);
    		        }
    		    }
                tmpnode = node.getNextSibling();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpusers;
    }
    /**
     * ��XML�ı��л�ȡָ����ǩ����
     * @param xmlstr XML�ı�
     * @param label ��ǩ
     * @return int
     */
    public static int getIntValueFromXML(String xmlstr, String label) {
        int value = 0;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = null;
    		doc = db.parse(new InputSource(new StringReader(xmlstr)));
    		String tmpstr = null;
    		NodeList nodes = null;
    		Node tmpnode = null;
    		nodes = doc.getElementsByTagName(label);
    		tmpnode = nodes.item(0);
    		if (tmpnode.hasChildNodes()) {
    		    tmpstr = tmpnode.getFirstChild().getNodeValue();
    		    if (null != tmpstr) {
    		        value = Integer.parseInt(tmpstr);
    		    }
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    /**
     * ��ȡXML�ַ�����ָ����ǩ��ֵ
     * @param xmlstr XML �ַ���
     * @param label ��ǩ
     * @return Vector
     */
    public static Vector getValuesFromXML(String xmlstr, String label) {
        Vector v = new Vector();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = null;
    		doc = db.parse(new InputSource(new StringReader(xmlstr)));
    		String tmpstr = null;
    		NodeList nodes = null;
    		Node tmpnode = null;
    		nodes = doc.getElementsByTagName(label);
    		for (int i = 0; i < nodes.getLength(); i++) {
    		    tmpnode = nodes.item(i);
    		    tmpstr = tmpnode.getFirstChild().getNodeValue();
    		    if (null != tmpstr) {
    		        v.addElement(tmpstr);
    		    }
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
    /**
     * ��ȡ���һ����֧
     * @param actid �ID
     * @return Ramus
     */
    public static Ramus getFirstRamus(int actid) {
        Ramus ramus = null;
        try {
            String sql = "actid = " + actid;
            RamusIterator ri = RamusHome.findByCondition(sql);
            if (null != ri) {
                if (ri.hasNext()) {
                    ramus = (Ramus) ri.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ramus;
    }
    /**
     * ������д���ȡ�����б�
     * @param flowid ����ID
     * @param userid �û�ID
     */
    public static void writeReaderToAcl(int flowid, int userid) {
        try {
            AclIterator ai = AclHome.findByCondition("userid = " + userid);
            if (!ai.hasNext()) {
                Acl acl = new Acl(String.valueOf(flowid));
                if (null != acl) {
                    acl.setType(Acl.READER_TYPE);
                    acl.setUserId(userid);
                    acl.update();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ������д�뵽��ȡ�����б�
     * @param flowid ����ID
     * @param userid �û�ID
     */
    public static void writeAuthorToAcl(int flowid, int userid) {
        try {
            AclIterator ai = AclHome.findByCondition("userid = " + userid);
            if (!ai.hasNext()) {
                Acl acl = new Acl(String.valueOf(flowid));
                if (null != acl) {
                    acl.setType(Acl.AUTHOR_TYPE);
                    acl.setUserId(userid);
                    acl.update();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ����Ӧ�������֮���ӳ��
     * @param flowid ����ID
     */
    public static void createAppFlowMapping(int flowid) {
        try {
            //����Ӧ��������֮���ӳɫ����
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * �Ӵ�ȡ�����б���ɾ�������ֶ�
     * @param flowid ����ID
     * @param userid �û�ID
     */
    public static void delAuthorFormAcl(int flowid, int userid) {
        try {
            String sql = "flowid = " + flowid + " and userid =" + userid;
            AclIterator ai = AclHome.findByCondition(sql);
            if (ai.hasNext()) {
                int id = ((Acl) ai.next()).getId();
                AclHome.remove(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * д�����̸�����Ϣ
	 * @param flowid ����ID
	 * @param user ��ǰ�û�
	 * @param actname ִ�в�������
	 * @param deptid ����ID
	 * @param taskid ����ID
	 * @param ����ID
	 */
	public static void writeTrack(int flowid, User user, String actname,
	        int deptid, int taskid) {
		try {
			String username = user.getCNName();
			String deptname = null;
			String remark = null;
			Dept dept = DeptHome.findById(deptid);
			deptname = dept.getDeptName();
			if (null == deptname) {
				deptname = "";
			}
			//�ж��Ƿ�Ϊ�������˴�������
			User cuser = Tools.getConsignorFromTask(taskid);
			if (null != cuser) {
			    remark = "��������ί����:" + cuser.getCNName();
			}
			java.sql.Date today = new java.sql.Date((new java.util.Date()).getTime());
			Timestamp time = new Timestamp(today.getTime());
			Track track = TrackHome.create(String.valueOf(flowid));
			if (null != track) {
			    track.setUserName(username);
			    track.setActName(actname);
			    track.setDeptName(deptname);
			    track.setTaskId(taskid);
			    track.setOtime(time);
			    track.setRemark(remark);
			    track.update();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡָ����Ա�Ĵ�����Ա
	 * @param userid �û�ID
	 * @return User �����û�
	 */
	public static User getProxyUser(int userid) {
	    User puser = null;
	    try {
	        ProxyIterator pi = ProxyHome.findByCondition(
	                "consignorid = " + userid + " and returnflag = 0");
	        if (pi.hasNext()) {
	            Proxy proxy = (Proxy) pi.next();
	            int aid = proxy.getAttorneyId();
	            puser = new User(aid);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return puser;
	}
	/**
	 * �������л�ȡ�����ί����
	 * @param taskid ����ID
	 * @return User
	 */
	public static User getConsignorFromTask(int taskid) {
	    User cuser = null;
	    try {
	        TaskIterator ti = TaskHome.findByCondition("id = " + taskid);
	        if (ti.hasNext()) {
	            Task task = (Task) ti.next();
	            if (task.getConsignorId() > 0) {
	                cuser = new User(task.getConsignorId());
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return cuser;
	}
	/**
	 * �жϵ�ǰ������̶�Ӧ�����������Ƿ�������
	 * @param actid �ID
	 * @param flowid ����ID
	 * @return boolean ���û�����񼯣����Ѱ�����ϣ�����û�а�����ϡ�
	 */
	public static boolean isAllTaskDone(int actid, int flowid) {
	    boolean flag = false;
	    try {
	        TaskIterator ti = TaskHome.findByCondition(
	                "actid= " + actid + " and flowid = "
	                + flowid + " and status = 0");
	        if (ti.hasNext()) {
	            flag = true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return flag;
	}
	/**
	 * ����һ����ĵ�һ����֧��һ���û�(���ϲ�ѯ����)
	 * @param src Դ����
	 * @return User
	 */
	public static User getFirstRamusUser(Source src) {
	    User user = null;
	    try {
	        Vector users = null;
	        //��ȡ��һ����֧
            Ramus ramus = Tools.getFirstRamus(src.getActId());
            if (null != ramus) {
                //��ȡѡ���û�ģʽ
                int choosemode = ramus.getChooseMode();
                //��ȡѡ���û�����
                String rulexml = ramus.getRuleXml();
                Assign assign = null;
                switch (choosemode) {
                	case 0:
                	    assign = new ConditionAssign(rulexml, src.getDeptId());
                	    break;
                	case 1:
                	    assign = new ReturnAssign(src.getTaskId());
                	    break;
                	case 2:
                	    assign = new SequenceAssign(src.getActId(), src.getTaskId());
                	    break;
                	default:
                	    break;
                }
                if (null != assign) {
                    users = assign.getPersons();
                    if (null != users && !users.isEmpty()) {
                        user = (User) users.elementAt(0);
                    }
                }
            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return user;
	}
}
