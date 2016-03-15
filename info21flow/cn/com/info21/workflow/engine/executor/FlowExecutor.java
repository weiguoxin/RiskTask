/*
 * Created on 2004-6-25
 */

package cn.com.info21.workflow.engine.executor;
import cn.com.info21.org.User;
import cn.com.info21.workflow.*;
import cn.com.info21.workflow.engine.*;

/**
 * @author lkh
 */

public abstract class FlowExecutor {
	private FlowRequest flowrequest = null;
	protected Flow flow = null;
	protected Activity curAct = null;
	protected Activity tgtAct = null;
	protected Task curTask = null;
	protected User user = null;
	protected Source src = null;
	protected Target target = null;
	/**
	 * 设置流程请求
	 * @param fr 流程请求
	 */
	public void setFlowRequest(FlowRequest fr) {
		this.flowrequest = fr;
	}
	/**
	 * 获取流程请求
	 * @return 流程请求
	 */
	public FlowRequest getFlowRequest() {
		return this.flowrequest;
	}
	/**
	 * 初始化参数
	 * @param fr 流程请求
	 */
	public void initPar(FlowRequest fr) {
	    try {
	        this.flowrequest = fr;
	        src = Tools.getSource(this.flowrequest.getPar());
	        target = Tools.getFirstTarget(this.flowrequest.getPar());
	        if (src.getUserId() > 0) {
	            user = new User(src.getUserId());
	        }
	        if (src.getActId() > 0) {
	            curAct = new Activity(src.getActId());
	        }
	        if (null != user) {
	            user.setDeptID(src.getDeptId());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * 执行事务
	 * @return boolean 是否执行成功
	 */
	public abstract boolean run();
	/**
	 * 获取消息
	 * @return int
	 */
	public abstract String getMessage();
	/** 
	 * @return 流程对象
	 */
	public Flow getFlow() {
	    return this.flow;
	}
}
