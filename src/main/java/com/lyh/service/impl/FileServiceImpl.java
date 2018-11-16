package com.lyh.service.impl;

import com.lyh.dao.IFileDao;
import com.lyh.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements IFileService {
    @Autowired
    @Qualifier("fileDaoImpl")
    private IFileDao fileDao;
    public List<Map<String, Object>> getAllFiles(){
        List<Map<String, Object>> mapList = null;
        try{
            mapList = fileDao.getAllFiles();
        }catch (Exception e){
            e.printStackTrace();
            mapList = null;
        }
        return mapList;
    }

    @Override
    public boolean delFile(int id) {
        boolean isSuccess = false;
        try{
            isSuccess = fileDao.delFile(id);
        }catch (RuntimeException e){
            isSuccess = false;
            e.printStackTrace();
        }catch (Exception e){
            isSuccess = false;
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    public String queryFileName(int id) {
        String str = "";
        try{
            str = fileDao.queryFileName(id);
        }catch (Exception e){
            str = "";
            e.printStackTrace();
        }
        return str;
    }

}
