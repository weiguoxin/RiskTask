/*
 * �������� 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;

import cn.com.info21.workflow.engine.FlowRequest;

/**
 * @author lkh
 * �򵥾ۺ�ִ����
 */

public class SimpleMergeExecutor extends FlowExecutor {
    /**
     * Ĭ�Ϲ��캯��һ
     *
     */
    public SimpleMergeExecutor() {
    }
    /**
     * ���캯����
     * @param fr ��������
     */
    public SimpleMergeExecutor(FlowRequest fr) {
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
