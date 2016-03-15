/*
 * 创建日期 2005-8-31
 */

package cn.com.info21.workflow.action;
import cn.com.info21.workflow.*;

/**
 * @author lkh
 */

public class FlowInitAction {
    public static String run(int flowid, int userid) {
        String xmlstr = null;
        try {
            //获取流程对应的第一个任务
            String sqlstr = 
                " flowid = "+ flowid
                + " and author = " + userid
                + " and status = 1";
            Task task = null;
            TaskIterator ti = TaskHome.findByCondition(sqlstr);
            if (ti.hasNext()) {
                task = (Task) ti.next();
                xmlstr = "<procid>" + task.getProcId() + "</procid>";
                xmlstr = xmlstr + "<actid>" + task.getActId() + "</actid>";
                xmlstr = xmlstr + "<flowid>" + task.getFlowId() + "</flowid>";
                xmlstr = xmlstr + "<taskid>" + task.getId() + "</taskid>";
            }
            if (null != xmlstr) {
                xmlstr = "<param>" + xmlstr + "</param>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlstr;
    }
}
