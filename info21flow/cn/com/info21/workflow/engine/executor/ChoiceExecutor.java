/*
 * 创建日期 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;
import java.util.Vector;
import cn.com.info21.org.User;
import cn.com.info21.workflow.*;
import cn.com.info21.workflow.engine.*;

/**
 * @author lkh
 * 显示选择执行器
 */

public class ChoiceExecutor extends FlowExecutor {
    /**
     * 默认构造函数一
     *
     */
    public ChoiceExecutor() {
    }
    /**
     * 构造函数二
     * @param fr 流程请求
     */
    public ChoiceExecutor(FlowRequest fr) {
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
            if (null != target) {
                //初始化开始活动节点
                if (null != curAct && null != curTask && null != tgtAct) {
                    if (createTargetTask(src, target)) {
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
     * 创建目标任务
     * @param src 流程数据源参数
     * @param tgt 目标参数
     * @return boolean 创建目标任务是否成功
     */
    private boolean createTargetTask(Source src, Target tgt) {
        boolean flag = false;
        try {
            Task task = null;
            Vector users = tgt.getUsers();
            User tmpuser = null;
            User proxyuser = null;
            int conid = 0;
            if (users.isEmpty()) {
                for (int i = 0; i < users.size(); i++) {
                    tmpuser = (User) users.elementAt(i);
                    proxyuser = tmpuser.getProxyUser();
                    if (null != proxyuser) {
                        conid = tmpuser.getId();
                        tmpuser = proxyuser;
                    }
                    task = TaskHome.create(String.valueOf(src.getProcId()));
                    task.setFlowId(src.getFlowId());
                    task.setActId(tgtAct.getId());
                    task.setAuthor(tmpuser.getId());
                    task.setDeptId(tmpuser.getDeptID());
                    task.setCreator(user.getId());
                    task.setPreTaskId(curTask.getId());
                    task.setStatus(Task.PROCESSING);
                    task.setConsignorId(conid);
                    task.update();
                    //写入存取控制
                    Tools.writeReaderToAcl(src.getProcId(), tmpuser.getId());
                    Tools.writeAuthorToAcl(src.getFlowId(), tmpuser.getId());
                    conid = 0;
                }
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
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
