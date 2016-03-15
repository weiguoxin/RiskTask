/*
 * 创建日期 2005-7-26
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
     * 初始化源
     * @param xmlstr 输入参数字符串
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
     * 获取第一个目标
     * @param xmlstr XML参数
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
     * 获取目标数据结构
     * @param xmlstr 请求参数
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
     * 根据XML文件初始化目标用户
     * @param node 用户节点
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
     * 从XML文本中获取指定标签数据
     * @param xmlstr XML文本
     * @param label 标签
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
     * 获取XML字符串中指定标签的值
     * @param xmlstr XML 字符串
     * @param label 标签
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
     * 获取活动第一个分支
     * @param actid 活动ID
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
     * 将读者写入存取控制列表
     * @param flowid 流程ID
     * @param userid 用户ID
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
     * 将作者写入到存取控制列表
     * @param flowid 流程ID
     * @param userid 用户ID
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
     * 创建应用与程序之间的映射
     * @param flowid 流程ID
     */
    public static void createAppFlowMapping(int flowid) {
        try {
            //创建应用与流程之间的映色关联
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从存取控制列表中删除作者字段
     * @param flowid 流程ID
     * @param userid 用户ID
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
	 * 写入流程跟踪信息
	 * @param flowid 流程ID
	 * @param user 当前用户
	 * @param actname 执行操作名称
	 * @param deptid 部门ID
	 * @param taskid 任务ID
	 * @param 任务ID
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
			//判断是否为代理他人处理任务
			User cuser = Tools.getConsignorFromTask(taskid);
			if (null != cuser) {
			    remark = "代办任务，委托人:" + cuser.getCNName();
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
	 * 获取指定人员的代办人员
	 * @param userid 用户ID
	 * @return User 代理用户
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
	 * 从任务中获取任务的委托人
	 * @param taskid 任务ID
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
	 * 判断当前活动与流程对应的所有任务是否办理完毕
	 * @param actid 活动ID
	 * @param flowid 流程ID
	 * @return boolean 如果没有任务集，则已办理完毕，否则没有办理完毕。
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
	 * 返回一个活动的第一个分支第一个用户(符合查询规则)
	 * @param src 源参数
	 * @return User
	 */
	public static User getFirstRamusUser(Source src) {
	    User user = null;
	    try {
	        Vector users = null;
	        //获取第一个分支
            Ramus ramus = Tools.getFirstRamus(src.getActId());
            if (null != ramus) {
                //获取选择用户模式
                int choosemode = ramus.getChooseMode();
                //获取选择用户规则
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
