package com.lyh.entity;

import java.util.Date;

public class listenHistory implements java.io.Serializable {
    private int id, memberId, listenType;
    private Date listenTime;
    private String targetFile;

    public listenHistory() {
    }

    public listenHistory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getListenType() {
        return listenType;
    }

    public void setListenType(int listenType) {
        this.listenType = listenType;
    }

    public Date getListenTime() {
        return listenTime;
    }

    public void setListenTime(Date listenTime) {
        this.listenTime = listenTime;
    }

    public String getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(String targetFile) {
        this.targetFile = targetFile;
    }
}
