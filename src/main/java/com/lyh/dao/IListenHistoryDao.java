package com.lyh.dao;

import com.lyh.entity.listenHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface IListenHistoryDao {
    @Select("<script>select * from listenHistory where 1 = 1 <if test=\"lh.id != 0\"> and id = #{lh.id}</if></script>")
    List<listenHistory> getHistory (@Param("lh") listenHistory lh);
}
