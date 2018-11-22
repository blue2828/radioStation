package com.lyh.dao.imp;

import com.lyh.dao.IFileDao;
import com.lyh.entity.Page;
import com.lyh.entity.UserFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository("fileDao")
@Transactional
public class FileDaoImpl implements IFileDao {
    @Autowired
    private IFileDao fileDao;
    @Override
    public List<UserFile> getAllFiles(UserFile userFile, Page page) {
        return fileDao.getAllFiles(userFile, page);
    }

    @Override
    public boolean delFile(int id) {
        /*String sql = "delete from t_file where id  =  ? ";
        int num = jdbcTemplate.update(sql, id);
        if(num>0) return true;
        else return false;*/
        return false;
    }


}