package com.lyh.dao;

import com.lyh.entity.Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IRecordDao {
    @Insert("insert into record(id, name, storeAddr, stationId, memberId, createTime) values" +
            "(null, #{record.name}, #{record.storeAddr}, #{record.stationId}, #{record.memberId}, #{record.createTime}) ")
    void saveRecord (@Param("record") Record record);
}
