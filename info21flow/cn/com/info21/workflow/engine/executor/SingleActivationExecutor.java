/*
 * �������� 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;
import cn.com.info21.workflow.*;
import cn.com.info21.workflow.engine.*;

/**
 * @author lkh
 * ��һĿ��ִ����
 */

public class SingleActivationExecutor extends FlowExecutor {
    /**
     * Ĭ�Ϲ��캯��һ
     */
    public SingleActivationExecutor() {
    }
    /**
     * ���캯����
     * @param fr ��������
     */
    public SingleActivationExecutor(FlowRequest fr) {
        initPar(fr);
    }
    /**
     * ����ִ����ִ��
     * @return boolean
     */
    public boolean run() {
        boolean flag = false;
        try {
            int flowid = 0;
            int curtaskid = 0;
            Task targetTask = null;
            if (null != curAct) {
                //��������
                flow = FlowHome.create(String.valueOf(src.getProcId()));
                if (null != flow) {
                    flowid = flow.getId();
                    //�����������
                    curTask = TaskHome.create(String.valueOf(flowid));
                    if (null != curTask) {
                        curTask.setProcId(this.src.getProcId());
                        curTask.setActId(curAct.getId());
                        curTask.setAuthor(this.user.getId());
                        curTask.setDeptId(this.user.getDeptID());
                        curTask.setCreator(this.user.getId());
                        curTask.update();
                        curtaskid = curTask.getId();
                        //����������ʼ����
                        flow.setStartTaskId(curtaskid);
                        flow.setRunMode(Flow.STATUS_ACTIVATE);
                        flow.update();
                        //����Ŀ������
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
     * ����Ŀ������
     * @param pid ����ID
     * @param fid ����ID
     * @param actid �ID
     * @param tid ��һ����ID
     * @return Task ����������
     */
    private Task createTargetTask(int pid, int fid, int actid, int tid) {
        Task task = null;
        try {
            Ramus ramus = Tools.getFirstRamus(actid);
            if (null != ramus) {
                int tactid = ramus.getNextActId();
                tgtAct = ActivityHome.findById(tactid);
                if (null != tgtAct) {
					//����Ŀ��
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
                        //д����ߡ������б�
                        Tools.writeReaderToAcl(fid, this.user.getId());
                        Tools.writeAuthorToAcl(fid, this.user.getId());
                        //д�����̸�����Ϣ
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }
    /**
     * ��ȡ����ִ������Ϣ
     * @return String
     */
    public String getMessage() {
        String msgstr = null;
        return msgstr;
    }
}