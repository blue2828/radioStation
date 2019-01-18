package com.lyh.dao;

import com.lyh.entity.Record;
import com.lyh.entity.listenHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IRecordDao {
    @Insert("insert into record(id, name, storeAddr, stationId, memberId, createTime) values" +
            "(null, #{record.name}, #{record.storeAddr}, #{record.stationId}, #{record.memberId}, #{record.createTime}) ")
    void saveRecord (@Param("record") Record record);
    public List<Record> getAllRecordList (int memberId);
    @Update("update record set demandIds = #{ids} where id = #{recordId}")
    public void updateDemandIds (@Param("recordId") int recordId, @Param("ids") String ids);
    @Select ("select demandIds from record where id = #{id}")
    public String getDemandIdsById (@Param("id") int id);
    @Select("select id from record where storeAddr = #{storeAddr}")
    int getIdByStoreAddr (@Param("storeAddr") String storeAddr);
}
