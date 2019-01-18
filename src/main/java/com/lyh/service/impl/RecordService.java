package com.lyh.service.impl;

import com.lyh.dao.IRecordDao;
import com.lyh.entity.Record;
import com.lyh.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService implements IRecordService {
    @Autowired
    private IRecordDao recordDao;
    @Override
    public boolean saveRecord(Record record) {
        boolean flag = false;
        try {
            recordDao.saveRecord(record);
            flag = true;
        }catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public List getAllRecordList(int memberId) {
        List<Record> list = new ArrayList<>();
        try {
            list = recordDao.getAllRecordList(memberId);
        }catch (Exception e) {
            list.clear();
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateDemandIds(int recordId, String ids) {
        boolean flag = false;
        try {
            recordDao.updateDemandIds(recordId, ids);
            flag = true;
        }catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public String getDemandIdsById(int id) {
        String ids = "";
        try {
            ids = recordDao.getDemandIdsById(id);
        }catch (Exception e) {
            ids = "";
            e.printStackTrace();
        }
        return ids;
    }

    @Override
    public int getIdByStoreAddr(String addr) {
        int id = -1;
        try {
            id = recordDao.getIdByStoreAddr(addr);
        }catch (Exception e) {
            id = -1;
            e.printStackTrace();
        }
        return id;
    }
}
