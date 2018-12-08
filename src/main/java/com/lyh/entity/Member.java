package com.lyh.entity;

import java.util.Date;

public class Member implements java.io.Serializable{
    private int id, sex, roleId;
    private String userName, pwd, phone, email, imageHeaderAddr, nickName, label, openid, session_key, wechatNo;
    private Date birthday, lastTimeLogin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageHeaderAddr() {
        return imageHeaderAddr;
    }

    public void setImageHeaderAddr(String imageHeaderAddr) {
        this.imageHeaderAddr = imageHeaderAddr;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public Member() {
    }

    public Member(int id) {
        this.id = id;
    }

    public Member(int id, int sex) {
        this.id = id;
        this.sex = sex;
    }

    public Member(int sex, int roleId, String userName, String pwd, String phone, String email, String imageHeaderAddr, String nickName, String label, Date birthday, Date lastTimeLogin) {
        this.sex = sex;
        this.roleId = roleId;
        this.userName = userName;
        this.pwd = pwd;
        this.phone = phone;
        this.email = email;
        this.imageHeaderAddr = imageHeaderAddr;
        this.nickName = nickName;
        this.label = label;
        this.birthday = birthday;
        this.lastTimeLogin = lastTimeLogin;
    }

    public Member(int sex, int roleId, String userName, String pwd, String phone, String email, String imageHeaderAddr, String nickName, String label, String openid, String session_key, Date birthday, Date lastTimeLogin, String wechatNo) {
        this.sex = sex;
        this.roleId = roleId;
        this.userName = userName;
        this.pwd = pwd;
        this.phone = phone;
        this.email = email;
        this.imageHeaderAddr = imageHeaderAddr;
        this.nickName = nickName;
        this.label = label;
        this.openid = openid;
        this.session_key = session_key;
        this.birthday = birthday;
        this.lastTimeLogin = lastTimeLogin;
        this.wechatNo = wechatNo;
    }

    public Member(String openid, String session_key) {
        this.openid = openid;
        this.session_key = session_key;
    }

    public Date getLastTimeLogin() {
        return lastTimeLogin;
    }

    public void setLastTimeLogin(Date lastTimeLogin) {
        this.lastTimeLogin = lastTimeLogin;
    }

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo;
    }
}
