/*
 * �������� 2005-8-9
 */

package cn.com.info21.workflow.engine.executor;
import java.util.*;
import cn.com.info21.workflow.*;
import cn.com.info21.org.*;

/**
 * @author lkh
 */

public class ReturnAssign extends Assign {
    private int taskid;
    /**
     * ���캯��
     */
    public ReturnAssign() {
    }
    /**
     * ���캯��
     * @param taskid ����ID
     */
    public ReturnAssign(int taskid) {
        this.taskid = taskid;
    }
    /**
     * ��ȡ���Ϲ������Ա����
     * @return Vector
     */
    public Vector getPersons() {
        Vector persons = new Vector();
        try {
            User user = null;
            User puser = null;
            Task task = null;
            Task pretask = null;
            int userid = 0;
            int deptid = 0;
            TaskIterator ti = TaskHome.findByCondition("id = " + taskid);
            int pid = 0;
            if (ti.hasNext()) {
                task = (Task) ti.next();
                pid = task.getPreTaskId();
            }
            if (pid > 0) {
                ti = TaskHome.findByCondition("id = " + pid);
                if (ti.hasNext()) {
                    pretask = (Task) ti.next();
                    userid = pretask.getAuthor();
                    deptid = pretask.getDeptId();
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