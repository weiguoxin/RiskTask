/*
 * �������� 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;
import java.sql.Timestamp;
import cn.com.info21.workflow.*;
import cn.com.info21.org.*;
import cn.com.info21.workflow.engine.*;

/**
 * @author lkh
 * ��ѭ��ִ����
 */

public class SelfCycleExecutor extends FlowExecutor {
    /**
     * Ĭ�Ϲ��캯��һ
     */
    public SelfCycleExecutor() {
    }
    /**
     * ���캯����
     * @param fr ��������
     */
    public SelfCycleExecutor(FlowRequest fr) {
        initPar(fr);
    }
    /**
     * ����ִ����ִ��
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
                //��ȡ·����
                router = curAct.getRouter();
                //�жϵ�ǰ��������û��Ƿ�ȫ���������
                if (Tools.isAllTaskDone(src.getActId(), src.getFlowId())) {
                    //ȫ���������,���̽�����һ�
                    if (router.getIsInteract() == 0) {
                        //����·�ɷ�ʽ
                        targetTask = createTaskByFrontChoose();
                    } else {
                        //��̨ѡ��ʽ
                        targetTask = createTaskByBackground();
                    }
                    if (null != targetTask) {
                        //������ǰ����
                        curTask.setStatus(Task.DONE);
                        curTask.update();
                        //ɾ����ǰ�û������̱༭Ȩ��
                        Tools.delAuthorFormAcl(src.getFlowId(), user.getId());
                        //������̸�����Ϣ
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
     * ��ǰ̨ѡ��ģʽ�´�������
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
                    //д���ȡ����
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
     * �ں�̨ѡ��ģʽ�´�������
     * @return Task
     */
    private Task createTaskByBackground() {
        Task task = null;
        try {
            //��ȡ��һ��Ŀ���֧
            Ramus ramus = Tools.getFirstRamus(src.getActId());
            if (null != ramus) {
                //��ȡ����������һ����Ա
                int conid = 0;
                User user = Tools.getFirstRamusUser(src);
                if (null != user.getProxyUser()) {
                    conid = user.getProxyUser().getId();
                }
                if (null != user) {
                    //������̨����
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
                        //д���ȡ����
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
     * ��ȡ����ִ������Ϣ
     * @return String
     */
    public String getMessage() {
        String msgstr = null;
        return msgstr;
    }
}
