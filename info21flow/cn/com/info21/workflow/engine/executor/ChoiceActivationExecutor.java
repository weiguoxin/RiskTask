/*
 * �������� 2005-8-1
 */

package cn.com.info21.workflow.engine.executor;

import cn.com.info21.workflow.engine.FlowRequest;

/**
 * @author lkh
 * ѡ�񼤻�ִ����
 */

public class ChoiceActivationExecutor extends FlowExecutor {
    /**
     * Ĭ�Ϲ��캯��һ
     */
    public ChoiceActivationExecutor() {
    }
    /**
     * ���캯����
     * @param fr ��������
     */
    public ChoiceActivationExecutor(FlowRequest fr) {
        this.setFlowRequest(fr);
    }
    /**
     * ����ִ����ִ��
     * @return boolean
     */
    public boolean run() {
        return false;
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