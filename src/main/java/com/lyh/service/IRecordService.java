package com.lyh.service;

import com.lyh.entity.Record;

import java.util.List;

public interface IRecordService {
    boolean saveRecord(Record record);
    List getAllRecordList(int memberId);
    public boolean updateDemandIds(int recordId, String ids);
    public String getDemandIdsById(int id);
    int getIdByStoreAddr (String addr);
}
