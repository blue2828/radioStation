package com.lyh.service;

import com.lyh.entity.Page;
import com.lyh.entity.UserFile;

import java.util.List;
import java.util.Map;

public interface IFileService {
    public List<UserFile> getAllFiles(UserFile file, Page page);
    public boolean delFile(int id);
    public int countFile(UserFile userFile, Page page);
    public boolean saveFile(UserFile userFile);
    int selectMaxId ();
    boolean updateFilePlayUrl (UserFile userFile);
    public UserFile getFileById(int fileId);
}
