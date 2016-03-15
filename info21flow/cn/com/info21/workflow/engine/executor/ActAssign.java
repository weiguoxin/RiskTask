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

public class ActAssign extends Assign {
    private String xmlstr = null;
    private int flowid = 0;
    public ActAssign() {
    }
    public ActAssign(String xmlstr, int flowid) {
        this.xmlstr = xmlstr;
        this.flowid = flowid;
    }
    public Vector getPersons() {
        Vector persons = new Vector();
        try {
            int actid = Tools.getIntValueFromXML(this.xmlstr, "actid");
            if ((actid > 0) && (flowid > 0)) {
                TaskIterator ti = TaskHome.findByCondition(
                        " actid = " + actid + " and "
                        + "flowid = " + flowid);
                if (ti.hasNext()) {
                    Task task = (Task) ti.next();
                    User user = new User(task.getAuthor());
                    if (null != user && task.getDeptId() > 0) {
                        user.setDeptID(task.getDeptId());
                    }
                    persons.addElement(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persons;
    }
}
