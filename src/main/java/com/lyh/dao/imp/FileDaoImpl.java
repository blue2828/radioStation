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
public class FileDaoImpl implements IFileDao {
    @Autowired
    private IFileDao fileDao;
    @Override
    public List<UserFile> getAllFiles(UserFile userFile, Page page) {
        return fileDao.getAllFiles(userFile, page);
    }

    @Override
    public int countFile(UserFile userFile, Page page) {
        return fileDao.countFile(userFile, page);
    }

    @Override
    public boolean delFile(int id) {
        return fileDao.delFile(id);
    }

    @Override
    public void saveFile(UserFile userFile) {
        fileDao.saveFile(userFile);
    }

    @Override
    public int selectMaxId() {
        return fileDao.selectMaxId();
    }

    @Override
    public void updateFilePlayUrl(UserFile userFile) {
        fileDao.updateFilePlayUrl(userFile);
    }

    @Override
    public UserFile getFileById(int fileId) {
        return fileDao.getFileById(fileId);
    }


}
