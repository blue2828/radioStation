package com.lyh.service.impl;

import com.lyh.dao.IRecordDao;
import com.lyh.entity.Record;
import com.lyh.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
