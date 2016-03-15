/*
 * �������� 2005-7-25
 */

package cn.com.info21.workflow.engine;
import cn.com.info21.workflow.*;
import cn.com.info21.workflow.engine.executor.*;

/**
 * @author lkh
 */

public class FlowEngine {
	private FlowRequest flowRequest;
	private FlowExecutor executor;
	private Source src;
	private Flow flow = null;
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public FlowEngine() {
	}
	/**
	 * ��������
	 * @param fr ��������
	 */
	public FlowEngine(FlowRequest fr) {
	    try {
		    this.flowRequest = fr;
		    this.src = Tools.getSource(fr.getPar());
		    if (this.src.getFlowId() > 0) {
	            flow = new Flow(this.src.getFlowId());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * ������������
	 * @return boolean
	 */
	public boolean run() {
	    boolean flag = false;
	    try {
	        FlowExecutor fe = null;
	        if (null != this.flow) {
	            fe = this.getExecutor(this.flow.getRunMode());
	        } else {
	            fe = this.getExecutor(0);
	        }
	        if (null != fe) {
	            fe.run();
	            this.flow = fe.getFlow();
	            flag = true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return flag;
	}
	/**
	 * ��ȡ����ִ����
	 * @param runmode ����ʵ����תģʽ������:��������ģʽ�����̹���ģʽ
	 * @return FlowExecutor
	 */
	private FlowExecutor getExecutor(int runmode) {
	    FlowExecutor fe = null;
	    try {
		    if (runmode == 0) {
		        //ȱʡ���̹���ģʽ,����������תģʽ,��ʼ��ִ����.
	            //��ȡ��ǰ�
	            Activity act = new Activity(this.src.getActId());
	            Router router = null;
	            if (null != act) {
	                router = act.getRouter();
	                if (null != router) {
	                    switch (router.getRunMode()) {
	                    	case 1:
	                    	    //�����񼤻���תģ��
	                    	    fe = new SingleActivationExecutor(this.flowRequest);
	                    	    break;
	                    	case 2:
	                    	    //ѡ�񼤻���תģ��
	                    	    fe = new ChoiceActivationExecutor(this.flowRequest);
	                    	    break;
	                    	case 3:
	                    	    //ѡ����תģ��
	                    	    fe = new ChoiceExecutor(this.flowRequest);
	                    	    break;
	                    	case 4:
	                    	    //��ɢ��תģ��
	                    	    fe = new DisperseExecutor(this.flowRequest);
	                    	    break;
	                    	case 5:
	                    	    //��ѭ����תģ��
	                    	    fe = new SelfCycleExecutor(this.flowRequest);
	                    	    break;
	                    	case 6:
	                    	    //������תģ��
	                    	    fe = new ParallelExecutor(this.flowRequest);
	                    	    break;
	                    	case 7:
	                    	    //�򵥾ۺ���תģ��
	                    	    fe = new SimpleMergeExecutor(this.flowRequest);
	                    	    break;
	                    	case 8:
	                    	    //������תģ��
	                    	    break;
	                    	case 9:
	                    	    //������תģ��
	                    	    break;
	                    	case 10:
	                    	    //�Զ���������תģ��
	                    	    break;
	                    	case 11:
	                    	    //������������תģ��
	                    	    break;
	                    	case 12:
	                    	    //�ع���������תģ��
	                    	    break;
	                    	case 13:
	                    	    //���̽�����תģ��
	                    	    fe = new FinishExecutor(this.flowRequest);
	                    	    break;
	                    	default:
	                    }
	                }
	            }
	        } else {
	            //�û���������ģʽ,��ʼ������ִ����
	            fe = new SequenceExecutor(this.flowRequest);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return fe;
	}
	/**
	 * @return ���̶���
	 */
	public Flow getFlow() {
	    return this.flow;
	}
}