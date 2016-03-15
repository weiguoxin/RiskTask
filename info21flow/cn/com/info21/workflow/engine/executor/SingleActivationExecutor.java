/*
 * 创建日期 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;
import cn.com.info21.workflow.*;
import cn.com.info21.workflow.engine.*;

/**
 * @author lkh
 * 单一目标执行器
 */

public class SingleActivationExecutor extends FlowExecutor {
    /**
     * 默认构造函数一
     */
    public SingleActivationExecutor() {
    }
    /**
     * 构造函数二
     * @param fr 流程请求
     */
    public SingleActivationExecutor(FlowRequest fr) {
        initPar(fr);
    }
    /**
     * 流程执行器执行
     * @return boolean
     */
    public boolean run() {
        boolean flag = false;
        try {
            int flowid = 0;
            int curtaskid = 0;
            Task targetTask = null;
            if (null != curAct) {
                //创建流程
                flow = FlowHome.create(String.valueOf(src.getProcId()));
                if (null != flow) {
                    flowid = flow.getId();
                    //创建起点任务
                    curTask = TaskHome.create(String.valueOf(flowid));
                    if (null != curTask) {
                        curTask.setProcId(this.src.getProcId());
                        curTask.setActId(curAct.getId());
                        curTask.setAuthor(this.user.getId());
                        curTask.setDeptId(this.user.getDeptID());
                        curTask.setCreator(this.user.getId());
                        curTask.update();
                        curtaskid = curTask.getId();
                        //设置流程起始任务
                        flow.setStartTaskId(curtaskid);
                        flow.setRunMode(Flow.STATUS_ACTIVATE);
                        flow.update();
                        //创建目标任务
                        targetTask = createTargetTask(src.getProcId(),
                                		flowid,
                                		src.getActId(),
                                		curtaskid);
                        if (null != targetTask) {
                            curTask.setNextTaskId(targetTask.getId());
                            curTask.setStatus(Task.DONE);
                            curTask.update();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * 创建目标任务
     * @param pid 过程ID
     * @param fid 流程ID
     * @param actid 活动ID
     * @param tid 上一任务ID
     * @return Task 创建的任务
     */
    private Task createTargetTask(int pid, int fid, int actid, int tid) {
        Task task = null;
        try {
            Ramus ramus = Tools.getFirstRamus(actid);
            if (null != ramus) {
                int tactid = ramus.getNextActId();
                tgtAct = ActivityHome.findById(tactid);
                if (null != tgtAct) {
					//创建目标活动
                    task = TaskHome.create(String.valueOf(fid));
                    if (null != task) {
                        task.setProcId(pid);
                        task.setFlowId(fid);
                        task.setActId(tgtAct.getId());
                        task.setPreTaskId(tid);
                        task.setAuthor(this.user.getId());
                        task.setDeptId(this.user.getDeptID());
                        task.setCreator(this.user.getId());
                        task.setStatus(Task.PROCESSING);
                        task.update();
                        //写入读者、作者列表
                        Tools.writeReaderToAcl(fid, this.user.getId());
                        Tools.writeAuthorToAcl(fid, this.user.getId());
                        //写入流程跟踪信息
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