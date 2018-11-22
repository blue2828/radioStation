package com.lyh.service;

import com.lyh.entity.Page;
import com.lyh.entity.UserFile;

import java.util.List;
import java.util.Map;

public interface IFileService {
    public List<UserFile> getAllFiles(UserFile file, Page page);
    public boolean delFile(int id);
}