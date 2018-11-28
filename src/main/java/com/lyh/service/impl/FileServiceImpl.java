package com.lyh.service.impl;

import com.lyh.dao.IFileDao;
import com.lyh.entity.Page;
import com.lyh.entity.UserFile;
import com.lyh.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service("fileService")
public class FileServiceImpl implements IFileService {
    @Autowired
    @Qualifier("fileDao")
    private IFileDao fileDao;
    @Override
    public List<UserFile> getAllFiles(UserFile userFile, Page page){
        List<UserFile> mapList = null;
        try{
            mapList = fileDao.getAllFiles(userFile, page);
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
    public int countFile(UserFile userFile, Page page) {
        int count = 0;
        try {
            count = fileDao.countFile(userFile, page);
        }catch (Exception e) {
            count = 0;
            e.printStackTrace();
        }
        return count;
    }


}
