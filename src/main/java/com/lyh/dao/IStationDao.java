package com.lyh.dao;

import com.lyh.entity.Member;
import com.lyh.entity.Page;
import com.lyh.entity.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface IStationDao {
    @Select("select * from station limit #{page.start}, #{page.limit}")
    List<Station> queryStation (@Param("page") Page page);
    @Select("select count(*) from station limit #{page.start}, #{page.limit}")
    int countStation (@Param("page") Page page);
}
