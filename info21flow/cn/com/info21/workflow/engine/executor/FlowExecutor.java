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
	 * ������������
	 * @param fr ��������
	 */
	public void setFlowRequest(FlowRequest fr) {
		this.flowrequest = fr;
	}
	/**
	 * ��ȡ��������
	 * @return ��������
	 */
	public FlowRequest getFlowRequest() {
		return this.flowrequest;
	}
	/**
	 * ��ʼ������
	 * @param fr ��������
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
	 * ִ������
	 * @return boolean �Ƿ�ִ�гɹ�
	 */
	public abstract boolean run();
	/**
	 * ��ȡ��Ϣ
	 * @return int
	 */
	public abstract String getMessage();
	/** 
	 * @return ���̶���
	 */
	public Flow getFlow() {
	    return this.flow;
	}
}
