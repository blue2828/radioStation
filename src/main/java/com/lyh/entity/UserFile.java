package com.lyh.entity;

import java.util.Date;

public class UserFile implements java.io.Serializable {
    private int id, uploadMemId, type;
    private String storeAddr, fileName, fileDesc, play_url;
    private Date uploadDate;

    public UserFile(int id, int uploadMemId, String storeAddr, Date uploadDate) {
        this.id = id;
        this.uploadMemId = uploadMemId;
        this.storeAddr = storeAddr;
        this.uploadDate = uploadDate;
    }

    public UserFile(int id, int uploadMemId, int type, String storeAddr, String fileName, Date uploadDate) {
        this.id = id;
        this.uploadMemId = uploadMemId;
        this.type = type;
        this.storeAddr = storeAddr;
        this.fileName = fileName;
        this.uploadDate = uploadDate;
    }

    public UserFile(int id, int uploadMemId, int type, String storeAddr) {
        this.id = id;
        this.uploadMemId = uploadMemId;
        this.type = type;
        this.storeAddr = storeAddr;
    }

    public UserFile(int type, String storeAddr, String fileName, String play_url, Date uploadDate) {
        this.type = type;
        this.storeAddr = storeAddr;
        this.fileName = fileName;
        this.play_url = play_url;
        this.uploadDate = uploadDate;
    }

    public UserFile(String play_url) {
        this.play_url = play_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUploadMemId() {
        return uploadMemId;
    }

    public void setUploadMemId(int uploadMemId) {
        this.uploadMemId = uploadMemId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStoreAddr() {
        return storeAddr;
    }

    public void setStoreAddr(String storeAddr) {
        this.storeAddr = storeAddr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public UserFile() {
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public UserFile(int id) {
        this.id = id;
    }
}
