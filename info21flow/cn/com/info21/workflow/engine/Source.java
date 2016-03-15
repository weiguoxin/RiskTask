/*
 * 创建日期 2005-7-26
 */

package cn.com.info21.workflow.engine;

import cn.com.info21.workflow.engine.executor.Tools;

/**
 * @author lkh
 */

public class Source {
    private int procid;
    private int actid;
    private int isnewflow;
    private int flowid;
    private int taskid;
    private int userid;
    private int deptid;
    /**
     * 默认构造器
     */
    public Source() {
    }
    /**
     * 构造器二
     * @param xml String
     */
    public Source(String xml) {
        try {
            this.procid = Tools.getIntValueFromXML(xml, "procid");
   		    this.actid = Tools.getIntValueFromXML(xml, "actid");
   		    this.isnewflow = Tools.getIntValueFromXML(xml, "isnewflow");
   		    this.flowid = Tools.getIntValueFromXML(xml, "flowid");
   		    this.taskid = Tools.getIntValueFromXML(xml, "taskid");
   		    this.userid = Tools.getIntValueFromXML(xml, "userid");
   		    this.deptid = Tools.getIntValueFromXML(xml, "deptid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取过程ID
     * @return int
     */
    public int getProcId() {
        return this.procid;
    }
    /**
     * 设置过程ID
     * @param procid 过程ID
     */
    public void setProcId(int procid) {
        this.procid = procid;
    }
    /**
     * 获取活动ID
     * @return int
     */
    public int getActId() {
        return this.actid;
    }
    /**
     * 设置活动ID
     * @param actid 活动ID
     */
    public void setActId(int actid) {
        this.actid = actid;
    }
    /**
     * 获取是否为新流程
     * @return int
     */
    public int getIsNewFlow() {
        return this.isnewflow;
    }
    /**
     * 设置是否为新流程
     * @param isnewflow 是否为新流程标志
     */
    public void setIsNewFlow(int isnewflow) {
        this.isnewflow = isnewflow;
    }
    /**
     * 获取流程ID
     * @return int
     */
    public int getFlowId() {
        return this.flowid;
    }
    /**
     * 设置流程ID
     * @param flowid 流程ID
     */
    public void setFlowId(int flowid) {
        this.flowid = flowid;
    }
    /**
     * 获取任务ID
     * @return int
     */
    public int getTaskId() {
        return this.taskid;
    }
    /**
     * 设置任务ID
     * @param taskid 任务ID
     */
    public void setTaskId(int taskid) {
        this.taskid = taskid;
    }
    /**
     * 获取用户ID
     * @return int
     */
    public int getUserId() {
        return this.userid;
    }
    /**
     * 设置用户ID
     * @param userid the userid to set
     */
    public void setUserId(int userid) {
        this.userid = userid;
    }
    /**
     * 获取部门ID
     * @return int
     */
    public int getDeptId() {
        return this.deptid;
    }
    /**
     * 设置部门ID
     * @param deptid the deptid to set
     */
    public void setDeptId(int deptid) {
        this.deptid = deptid;
    }
}
