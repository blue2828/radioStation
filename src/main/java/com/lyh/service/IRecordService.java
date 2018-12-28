package com.lyh.service;

import com.lyh.entity.Record;

import java.util.List;

public interface IRecordService {
    boolean saveRecord(Record record);
    List getAllRecordList();
}
