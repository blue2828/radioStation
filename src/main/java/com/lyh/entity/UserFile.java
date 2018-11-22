package com.lyh.entity;

import java.util.Date;

public class UserFile implements java.io.Serializable {
    private int id, uploadMemId, type;
    private String storeAddr, fileName, fileDesc;
    private Date uploadDate;

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

    public UserFile(int id) {
        this.id = id;
    }
}
