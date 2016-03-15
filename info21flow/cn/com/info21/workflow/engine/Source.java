/*
 * �������� 2005-7-26
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
     * Ĭ�Ϲ�����
     */
    public Source() {
    }
    /**
     * ��������
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
     * ��ȡ����ID
     * @return int
     */
    public int getProcId() {
        return this.procid;
    }
    /**
     * ���ù���ID
     * @param procid ����ID
     */
    public void setProcId(int procid) {
        this.procid = procid;
    }
    /**
     * ��ȡ�ID
     * @return int
     */
    public int getActId() {
        return this.actid;
    }
    /**
     * ���ûID
     * @param actid �ID
     */
    public void setActId(int actid) {
        this.actid = actid;
    }
    /**
     * ��ȡ�Ƿ�Ϊ������
     * @return int
     */
    public int getIsNewFlow() {
        return this.isnewflow;
    }
    /**
     * �����Ƿ�Ϊ������
     * @param isnewflow �Ƿ�Ϊ�����̱�־
     */
    public void setIsNewFlow(int isnewflow) {
        this.isnewflow = isnewflow;
    }
    /**
     * ��ȡ����ID
     * @return int
     */
    public int getFlowId() {
        return this.flowid;
    }
    /**
     * ��������ID
     * @param flowid ����ID
     */
    public void setFlowId(int flowid) {
        this.flowid = flowid;
    }
    /**
     * ��ȡ����ID
     * @return int
     */
    public int getTaskId() {
        return this.taskid;
    }
    /**
     * ��������ID
     * @param taskid ����ID
     */
    public void setTaskId(int taskid) {
        this.taskid = taskid;
    }
    /**
     * ��ȡ�û�ID
     * @return int
     */
    public int getUserId() {
        return this.userid;
    }
    /**
     * �����û�ID
     * @param userid the userid to set
     */
    public void setUserId(int userid) {
        this.userid = userid;
    }
    /**
     * ��ȡ����ID
     * @return int
     */
    public int getDeptId() {
        return this.deptid;
    }
    /**
     * ���ò���ID
     * @param deptid the deptid to set
     */
    public void setDeptId(int deptid) {
        this.deptid = deptid;
    }
}
