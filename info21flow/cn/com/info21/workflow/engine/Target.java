/*
 * 创建日期 2005-7-26
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
     * 默认构造函数
     */
    public Target() {
    }
    /**
     * 获取目标活动ID
     * @return int
     */
    public int getActId() {
        return this.actid;
    }
    /**
     * 设置目标活动ID
     * @param actid int
     */
    public void setActId(int actid) {
        this.actid = actid;
    }
    /**
     * 获取目标用户
     * @return Users
     */
    public Vector getUsers() {
        return this.users;
    }
    /**
     * 设置目标用户
     * @param users Users
     */
    public void setUsers(Vector users) {
        this.users = users;
    }
    /**
     * 获取目标分支第一个用户
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