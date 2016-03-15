/*
 * 创建日期 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;

import cn.com.info21.workflow.engine.FlowRequest;

/**
 * @author lkh
 * 序列执行器
 */

public class SequenceExecutor extends FlowExecutor {
    /**
     * 默认构造函数一
     *
     */
    public SequenceExecutor() {
    }
    /**
     * 构造函数二
     * @param fr 流程请求
     */
    public SequenceExecutor(FlowRequest fr) {
        this.setFlowRequest(fr);
    }
    /**
     * 流程执行器执行
     * @return boolean
     */
    public boolean run() {
        return false;
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
