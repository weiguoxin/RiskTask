/*
 * 创建日期 2005-7-28
 */

package cn.com.info21.workflow.engine.executor;

import cn.com.info21.workflow.engine.FlowRequest;

/**
 * @author lkh
 * 
 */

public class DisperseExecutor extends FlowExecutor {
    /**
     * 默认构造函数一
     * 发散执行器
     */
    public DisperseExecutor() {
    }
    /**
     * 构造函数二
     * @param fr 流程请求
     */
    public DisperseExecutor(FlowRequest fr) {
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
