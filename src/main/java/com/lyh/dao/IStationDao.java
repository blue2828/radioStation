package com.lyh.dao;

import com.lyh.entity.Member;
import com.lyh.entity.Page;
import com.lyh.entity.Station;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
@Mapper
public interface IStationDao {
    @Select("select * from station limit #{page.start}, #{page.limit}")
    List<Station> queryStation (@Param("page") Page page);
    @Select("select count(*) from station limit #{page.start}, #{page.limit}")
    int countStation (@Param("page") Page page);
    @Update("update station set stationState = #{station.stationState}, lastTimeBroadcast = #{station.lastTimeBroadcast}," +
            " lastTimeMemberId = #{station.lastTimeMemberId}")
    void updateStationState (@Param("station") Station station);
    @Select("select lastTimeBroadcast from station where lastTimeMemberId = #{id}")
    Date getLastTime(@Param("id") int id);
    @Select("select * from station where stationState = 0")
    List<Station> getCurrentStation (); //获取直播内容
}
