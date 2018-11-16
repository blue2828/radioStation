package com.lyh.dao;

import com.lyh.entity.Member;
import com.lyh.entity.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IMemberDao {
    @Insert("insert into member values (null, #{member.userName}, #{member.pwd}, #{member.birthday}, #{member.sex}, #{member.phone}," +
            " #{member.email}, #{member.imageHeaderAddr}, #{member.nickName}, #{member.label}, #{member.roleId})")
    int insertUser (@Param("member") Member member);
    @Select("<script>select * from member where 1 = 1 <if test=\"member.userName != null\">and userName like " +
            "concat('%', #{member.userName}, '%')</if>" +
            "<if test=\"member.nickName != null\"> and nickName like concat('%', #{member.nickName}, '%')</if>" +
            "<if test=\"member.id != 0\"> and id like concat('%', #{member.id}, '%')</if> limit #{page.start}, #{page.limit}</script>")
    List<Member> queryMemberAllOrSth (@Param("page") Page page, @Param("member")Member member);
    @Select("<script>select count(*) as hh from member where 1 = 1 <if test=\"member.userName != null\">and userName like " +
            "concat('%', #{member.userName}, '%')</if>" +
            "<if test=\"member.nickName != null\"> and nickName like concat('%', #{member.nickName}, '%')</if>" +
            "<if test=\"member.id != 0\"> and id like concat('%', #{member.id}, '%')</if> limit #{page.start}, #{page.limit}</script>")
    int countMember (@Param("page") Page page, @Param("member")Member member);
}
