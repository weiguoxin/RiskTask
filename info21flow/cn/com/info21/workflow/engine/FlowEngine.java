/*
 * 创建日期 2005-7-25
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
	 * 默认构造函数
	 */
	public FlowEngine() {
	}
	/**
	 * 构造器二
	 * @param fr 流程请求
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
	 * 流程引擎运行
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
	 * 获取流程执行器
	 * @param runmode 流程实例运转模式分两类:流程序列模式和流程管制模式
	 * @return FlowExecutor
	 */
	private FlowExecutor getExecutor(int runmode) {
	    FlowExecutor fe = null;
	    try {
		    if (runmode == 0) {
		        //缺省流程管制模式,根据流程运转模式,初始化执行器.
	            //获取当前活动
	            Activity act = new Activity(this.src.getActId());
	            Router router = null;
	            if (null != act) {
	                router = act.getRouter();
	                if (null != router) {
	                    switch (router.getRunMode()) {
	                    	case 1:
	                    	    //单任务激活运转模型
	                    	    fe = new SingleActivationExecutor(this.flowRequest);
	                    	    break;
	                    	case 2:
	                    	    //选择激活运转模型
	                    	    fe = new ChoiceActivationExecutor(this.flowRequest);
	                    	    break;
	                    	case 3:
	                    	    //选择运转模型
	                    	    fe = new ChoiceExecutor(this.flowRequest);
	                    	    break;
	                    	case 4:
	                    	    //发散运转模型
	                    	    fe = new DisperseExecutor(this.flowRequest);
	                    	    break;
	                    	case 5:
	                    	    //自循环运转模型
	                    	    fe = new SelfCycleExecutor(this.flowRequest);
	                    	    break;
	                    	case 6:
	                    	    //并行运转模型
	                    	    fe = new ParallelExecutor(this.flowRequest);
	                    	    break;
	                    	case 7:
	                    	    //简单聚合运转模型
	                    	    fe = new SimpleMergeExecutor(this.flowRequest);
	                    	    break;
	                    	case 8:
	                    	    //抄送运转模型
	                    	    break;
	                    	case 9:
	                    	    //自由运转模型
	                    	    break;
	                    	case 10:
	                    	    //自定义序列运转模型
	                    	    break;
	                    	case 11:
	                    	    //激活子流程运转模型
	                    	    break;
	                    	case 12:
	                    	    //回归主流程运转模型
	                    	    break;
	                    	case 13:
	                    	    //流程结束运转模型
	                    	    fe = new FinishExecutor(this.flowRequest);
	                    	    break;
	                    	default:
	                    }
	                }
	            }
	        } else {
	            //用户定义序列模式,初始化序列执行器
	            fe = new SequenceExecutor(this.flowRequest);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return fe;
	}
	/**
	 * @return 流程对象
	 */
	public Flow getFlow() {
	    return this.flow;
	}
}