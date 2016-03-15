/*
 * 创建日期 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;
import java.sql.Timestamp;
import cn.com.info21.workflow.*;
import cn.com.info21.org.*;
import cn.com.info21.workflow.engine.*;

/**
 * @author lkh
 * 自循环执行器
 */

public class SelfCycleExecutor extends FlowExecutor {
    /**
     * 默认构造函数一
     */
    public SelfCycleExecutor() {
    }
    /**
     * 构造函数二
     * @param fr 流程请求
     */
    public SelfCycleExecutor(FlowRequest fr) {
        initPar(fr);
    }
    /**
     * 流程执行器执行
     * @return boolean
     */
    public boolean run() {
        boolean flag = false;
        try {
            Task targetTask = null;
            Router router = null;
            if (null != target) {
                curTask.setStatus(Task.DONE);
                curTask.update();
                //获取路由器
                router = curAct.getRouter();
                //判断当前活动的所有用户是否全部办理完毕
                if (Tools.isAllTaskDone(src.getActId(), src.getFlowId())) {
                    //全部办理完毕,流程进入下一活动
                    if (router.getIsInteract() == 0) {
                        //交互路由方式
                        targetTask = createTaskByFrontChoose();
                    } else {
                        //后台选择方式
                        targetTask = createTaskByBackground();
                    }
                    if (null != targetTask) {
                        //结束当前任务
                        curTask.setStatus(Task.DONE);
                        curTask.update();
                        //删除当前用户的流程编辑权限
                        Tools.delAuthorFormAcl(src.getFlowId(), user.getId());
                        //添加流程跟踪信息
                        Tools.writeTrack(src.getFlowId(),
                                user,
                                curAct.getAction(),
                                this.src.getDeptId(),
                                src.getTaskId());
                        flag = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * 在前台选择模式下创建任务
     * @return Task
     */
    private Task createTaskByFrontChoose() {
        Task task = null;
        try {
            int flowid = curTask.getFlowId();
            int curtaskid = curTask.getId();
            int tgtactid = target.getActId();
            int conid = 0;
            User proxyuser = null;
            User tmpuser = target.getFirstUser();
            proxyuser = tmpuser.getProxyUser();
            if (null != proxyuser) {
                conid = proxyuser.getId();
            }
            if (flowid > 0 && curtaskid > 0 && tgtactid > 0) {
                task = TaskHome.create(String.valueOf(src.getProcId()));
                if (null != task) {
                    task.setFlowId(flowid);
                    task.setActId(tgtactid);
                    task.setPreTaskId(curtaskid);
                    task.setCreator(src.getUserId());
                    task.setAuthor(tmpuser.getId());
                    task.setDeptId(tmpuser.getDeptID());
                    task.setConsignorId(conid);
                    java.sql.Date today = new java.sql.Date(
                            (new java.util.Date()).getTime());
        			Timestamp time = new Timestamp(today.getTime());
        			task.setCreateTime(time);
                    task.update();
                    //写入存取控制
                    Tools.writeReaderToAcl(src.getProcId(), tmpuser.getId());
                    Tools.writeAuthorToAcl(src.getFlowId(), tmpuser.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }
    /**
     * 在后台选择模式下创建任务
     * @return Task
     */
    private Task createTaskByBackground() {
        Task task = null;
        try {
            //获取第一个目标分支
            Ramus ramus = Tools.getFirstRamus(src.getActId());
            if (null != ramus) {
                //获取符合条件的一个人员
                int conid = 0;
                User user = Tools.getFirstRamusUser(src);
                if (null != user.getProxyUser()) {
                    conid = user.getProxyUser().getId();
                }
                if (null != user) {
                    //创建后台任务
                    task = TaskHome.create(String.valueOf(src.getFlowId()));
                    if (null != task) {
                        task.setActId(src.getActId());
                        task.setPreTaskId(curTask.getId());
                        task.setCreator(src.getUserId());
                        task.setAuthor(user.getId());
                        task.setDeptId(user.getDeptID());
                        task.setConsignorId(conid);
                        java.sql.Date today = new java.sql.Date(
                                (new java.util.Date()).getTime());
            			Timestamp time = new Timestamp(today.getTime());
            			task.setCreateTime(time);
                        task.update();
                        //写入存取控制
                        Tools.writeReaderToAcl(src.getProcId(), user.getId());
                        Tools.writeAuthorToAcl(src.getFlowId(), user.getId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }
    /**
     * 获取流程执行器消息
     * @return String
     */
    public String getMessage() {
        String msgstr = null;
        return msgstr;
    }
}
