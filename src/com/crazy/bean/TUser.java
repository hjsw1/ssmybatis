package com.crazy.bean;

import java.util.Date;

public class TUser {
    private Integer uid;

    private String uname;

    private String upwd;

    private String umessage;

    private Date utime;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd == null ? null : upwd.trim();
    }

    public String getUmessage() {
        return umessage;
    }

    public void setUmessage(String umessage) {
        this.umessage = umessage == null ? null : umessage.trim();
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }
}