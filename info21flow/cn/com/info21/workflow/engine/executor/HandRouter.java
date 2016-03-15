/*
 * 创建日期 2005-9-16
 */

package cn.com.info21.workflow.engine.executor;
import java.util.*;
import cn.com.info21.org.*;
import cn.com.info21.workflow.*;

/**
 * @author lkh
 */

public class HandRouter {
    public HandRouter() {
    }
	public StringBuffer expXML(int taskid, User user) {
	    StringBuffer bf = new StringBuffer();
	    try {
	        String tmpstr = "";
	        Activity curact = null;
	        RamusIterator ri = null;
	        Task curtask = TaskHome.findById(taskid);
	        if (null != curtask) {
	            if (null != curact) {
	                ri = curact.getRamus();
	                Router router = curact.getRouter();
	                tmpstr = "curactid =\"" + curact.getId() + "\""
	                		+ " taskid=\"" + curtask.getId() + "\""
	                		+ " runmode=\"" + router.getRunMode() + "\"";
	            }
	        }
	        
	        bf.append("<source" + tmpstr + ">\n");
	        bf.append("</target>\n");
	        
	        if (null != ri) {
	            Ramus ra = null;
	            while(ri.hasNext()) {
	                ra = (Ramus) ri.next();
	                bf.append(HandRouter.expRamusToXML(curtask.getFlowId(), ra, user));
	            }
	        }
	        
	        
	    } catch (Exception e) {
	        bf = new StringBuffer();
	        e.printStackTrace();
	    }
	    return bf;
	}
	
	public static StringBuffer expRamusToXML(int flowid, Ramus ra, User user) {
	    StringBuffer bf = new StringBuffer();
	    try {
	        Activity act = ActivityHome.findById(ra.getNextActId());
	        if (null != act) {
	            Router router = act.getRouter();
	            if (null != router) {
	                bf.append("\t<activity id=\"" + act.getId() + "\""
		                    + " name=\"" + act.getName() + "\""
		                    + " runmode=\"" + router.getRunMode() + "\""
		                    + " ramusid=\"" + "\"" + ">\n");
	                bf.append(HandRouter.expPeronsToXML(flowid, ra, user));
	                bf.append("\t</activity>\n");
	            }
	        }
	            
	    } catch (Exception e) {
	        bf = new StringBuffer();
	        e.printStackTrace();
	    }
	    return bf;
	}
	
	public static StringBuffer expPeronsToXML(int flowid, Ramus ra, User user) {
	    StringBuffer bf = new StringBuffer();
	    try {
	        Assign an = null;
	        switch (ra.getChooseMode()) {
	        	case 0:
	        	    an = new ConditionAssign(ra.getRuleXml(), user.getDeptID());
	        	    break;
	        	case 2:
	        	    an = new ActAssign(ra.getRuleXml(), flowid);
	        	    break;
	        	default:
	        }
	        Vector persons = new Vector();
	        User tmpuser = null;
	        Dept dept = null;
	        if (null != an) {
	            persons = an.getPersons();
	            if (persons.isEmpty()) {
	                for (int i=0; i<persons.size(); i++) {
	                    tmpuser = (User)persons.elementAt(i);
	                    bf.append("\t\t\t<user id=\"" + user.getId() + "\"");
	                    bf.append(" name=\"" + user.getCNName() + "\"");
	                    bf.append(" deptid=\"" + user.getDeptID() + "\"");
	                    bf.append(" dept=\"" + dept.getDeptName() + "\"");
	                    bf.append(" username=\"" + user.getUserName() + "\"");
	                    bf.append("\"/>\n");
	                }
	            }
	        }
	    } catch (Exception e) {
	        bf = new StringBuffer();
	        e.printStackTrace();
	    }
	    return bf;
	}
}
