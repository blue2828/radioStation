package com.lyh.entity;

import java.util.Date;

public class Record implements java.io.Serializable {
    private int id, stationId, memberId;
    private String name, storeAddr;
    private Date createTime;

    public Record(int stationId, int memberId, String name, String storeAddr, Date createTime) {
        this.stationId = stationId;
        this.memberId = memberId;
        this.name = name;
        this.storeAddr = storeAddr;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreAddr() {
        return storeAddr;
    }

    public void setStoreAddr(String storeAddr) {
        this.storeAddr = storeAddr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Record() {
    }
}
