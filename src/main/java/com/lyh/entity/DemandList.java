package com.lyh.entity;

import java.util.Date;

public class DemandList implements java.io.Serializable{
    private int id, state, memberId, fileId;
    private Date demandTime;
    private String toSb, email, musicName, demandDesc;
    private UserFile userFile;
    public DemandList(int memberId, int fileId) {
        this.memberId = memberId;
        this.fileId = fileId;
    }

    public DemandList(int id, int state, int fileId, Date demandTime, String toSb, String email, String musicName, String demandDesc) {
        this.id = id;
        this.state = state;
        this.fileId = fileId;
        this.demandTime = demandTime;
        this.toSb = toSb;
        this.email = email;
        this.musicName = musicName;
        this.demandDesc = demandDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public Date getDemandTime() {
        return demandTime;
    }

    public void setDemandTime(Date demandTime) {
        this.demandTime = demandTime;
    }

    public String getToSb() {
        return toSb;
    }

    public void setToSb(String toSb) {
        this.toSb = toSb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getDemandDesc() {
        return demandDesc;
    }

    public void setDemandDesc(String demandDesc) {
        this.demandDesc = demandDesc;
    }

    public UserFile getUserFile() {
        return userFile;
    }

    public void setUserFile(UserFile userFile) {
        this.userFile = userFile;
    }

    public DemandList() {
    }

}
