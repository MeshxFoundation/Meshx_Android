package com.pgy.meshim.bean;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 用户bean
 */

@Entity
public class UserInfo {
    public final static int LOGINED = 1;
    public final static int OFFLINE_LOGINED = -1;//离线登录
    public final static int UNLOGIN = 0;
    public final static int LAST_LOGIN = 2;//数据库中标记上一个登录的用户
    public final static int LOGOUT = 100;//数据库中标记上一个登出的用户

    @Id(autoincrement = true)
    private long _id;

    /**
     * 登录用户名
     */
    @NotNull
    private String userPassName;
    /**
     * 登录用户密码
     */
    private String userPassWord;
    /**
     * 昵称
     */
    private String userName;

    private int status = UNLOGIN;

    public UserInfo() {

    }

    public UserInfo(String userPassName, String userPassWord, String userName) {
        super();
        this.userPassName = userPassName;
        this.userPassWord = userPassWord;
        this.userName = userName;
    }

    @Generated(hash = 1773807456)
    public UserInfo(long _id, @NotNull String userPassName, String userPassWord,
                    String userName, int status) {
        this._id = _id;
        this.userPassName = userPassName;
        this.userPassWord = userPassWord;
        this.userName = userName;
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserInfo)) return false;
        UserInfo other = (UserInfo) obj;
        return TextUtils.equals(other.userPassName, getUserPassName());
    }

    public String getUserPassName() {
        return userPassName;
    }

    public void setUserPassName(String userPassName) {
        this.userPassName = userPassName;
    }

    public boolean isLogin() {
        return (getStatus() == LOGINED || getStatus() == OFFLINE_LOGINED) && !TextUtils.isEmpty(getUserPassName());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isOfflineLogin() {
        return getStatus() == OFFLINE_LOGINED && !TextUtils.isEmpty(getUserPassName());
    }

    public String getDisplayName() {
        if (TextUtils.isEmpty(userName)) return userPassName;
        else return userName;
    }
}
