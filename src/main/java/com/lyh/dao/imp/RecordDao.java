package com.lyh.dao.imp;

import com.lyh.dao.IRecordDao;
import com.lyh.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RecordDao implements IRecordDao{
    @Autowired
    private IRecordDao recordDao;
    @Override
    public void saveRecord(Record record) {
        recordDao.saveRecord(record);
    }
}
