package com.lyh.dao;

import com.lyh.entity.listenHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface IListenHistoryDao {
    @Select("<script>select * from listenHistory where 1 = 1 <if test=\"lh.id != 0\"> and id = #{lh.id}</if></script>")
    List<listenHistory> getHistory (@Param("lh") listenHistory lh);
    @Insert("insert into listenhistory(listenTime, memberId, listenType, targetFile) values (#{history.listenTime}, #{history.memberId}" +
            ", #{history.listenType}, #{history.targetFile})")
    int saveListen (@Param("history") listenHistory history);
    @Delete("<script>delete from listenhistory where id in " +
                "<foreach collection=\"ids\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" +
                     "#{id}" +
                "</foreach>" +
            "</script>")
    int delSelfHistory (@Param("ids") String[] ids);
}
