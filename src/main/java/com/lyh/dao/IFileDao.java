package com.lyh.dao;

import com.lyh.entity.File;

import java.util.List;
import java.util.Map;

public interface IFileDao {
    public List<Map<String,Object>> getAllFiles();
    public boolean delFile (int id);
    public String queryFileName (int id);
}
