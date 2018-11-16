package com.lyh.service;

import com.lyh.entity.File;

import java.util.List;
import java.util.Map;

public interface IFileService {
    public List<Map<String,Object>> getAllFiles();
    public boolean delFile(int id);
    public String queryFileName(int id);
}
