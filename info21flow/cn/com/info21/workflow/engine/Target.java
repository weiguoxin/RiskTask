/*
 * �������� 2005-7-26
 */

package cn.com.info21.workflow.engine;
import java.util.*;
import cn.com.info21.org.*;

/**
 * @author lkh
 */

public class Target {
    private int actid;
    private Vector users = null;
    /**
     * Ĭ�Ϲ��캯��
     */
    public Target() {
    }
    /**
     * ��ȡĿ��ID
     * @return int
     */
    public int getActId() {
        return this.actid;
    }
    /**
     * ����Ŀ��ID
     * @param actid int
     */
    public void setActId(int actid) {
        this.actid = actid;
    }
    /**
     * ��ȡĿ���û�
     * @return Users
     */
    public Vector getUsers() {
        return this.users;
    }
    /**
     * ����Ŀ���û�
     * @param users Users
     */
    public void setUsers(Vector users) {
        this.users = users;
    }
    /**
     * ��ȡĿ���֧��һ���û�
     * @return User
     */
    public User getFirstUser() {
        User user = null;
        try {
            if (null != this.users) {
                if (!this.users.isEmpty()) {
                    user = (User) this.users.elementAt(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}