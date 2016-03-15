/*
 * �������� 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;
import java.util.Vector;
import cn.com.info21.org.User;
import cn.com.info21.workflow.*;
import cn.com.info21.workflow.engine.*;

/**
 * @author lkh
 * ��ʾѡ��ִ����
 */

public class ChoiceExecutor extends FlowExecutor {
    /**
     * Ĭ�Ϲ��캯��һ
     *
     */
    public ChoiceExecutor() {
    }
    /**
     * ���캯����
     * @param fr ��������
     */
    public ChoiceExecutor(FlowRequest fr) {
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
            if (null != target) {
                //��ʼ����ʼ��ڵ�
                if (null != curAct && null != curTask && null != tgtAct) {
                    if (createTargetTask(src, target)) {
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
     * ����Ŀ������
     * @param src ��������Դ����
     * @param tgt Ŀ�����
     * @return boolean ����Ŀ�������Ƿ�ɹ�
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
                    //д���ȡ����
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
     * ��ȡ����ִ������Ϣ
     * @return String
     */
    public String getMessage() {
        String msgstr = null;
        return msgstr;
    }
}
