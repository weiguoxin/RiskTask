/*
 * 创建日期 2005-8-9
 */

package cn.com.info21.workflow.engine.executor;
import java.util.*;
import cn.com.info21.org.*;
import cn.com.info21.workflow.*;

/**
 * @author lkh
 */

public class SequenceAssign extends Assign {
    private int actid = 0;
    private int taskid = 0;
    /**
     * 默认构造函数
     */
    public SequenceAssign() {
    }
    /**
     * 构造函数二
     * @param actid 活动ID
     * @param taskid 任务ID
     */
    public SequenceAssign(int actid, int taskid) {
        this.actid = actid;
        this.taskid = taskid;
    }
    /**
     * 获取人员
     * @return Vector
     */
    public Vector getPersons() {
        Vector persons = new Vector();
        try {
            User user = null;
            User puser = null;
            Task task = null;
            Task tgttask = null;
            int flowid = 0;
            int userid = 0;
            int deptid = 0;
            TaskIterator ti = TaskHome.findByCondition("id = " + taskid);
            if (ti.hasNext()) {
                task = (Task) ti.next();
                flowid = task.getFlowId();
            }
            if (flowid > 0) {
                ti = TaskHome.findByCondition("flowid = "
                        + flowid + " and actid = " + actid);
                if (ti.hasNext()) {
                    tgttask = (Task) ti.next();
                    userid = tgttask.getAuthor();
                    deptid = tgttask.getDeptId();
                    user = new User(userid);
                    user.setDeptID(deptid);
                    puser = Tools.getProxyUser(userid);
                    if (null != puser) {
                        user.setProxyUser(puser);
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
