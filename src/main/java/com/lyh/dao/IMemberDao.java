package com.lyh.dao;

import com.lyh.entity.Member;
import com.lyh.entity.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IMemberDao {
    @Insert("insert into member values (null, #{member.userName}, #{member.pwd}, #{member.birthday}, #{member.sex}, #{member.phone}," +
            " #{member.email}, #{member.imageHeaderAddr}, #{member.nickName}, #{member.label}, #{member.roleId}), null")
    int insertUser (@Param("member") Member member);
    @Select("<script>select * from member where 1 = 1 <if test=\"member.userName != null\">and userName like " +
            "concat('%', #{member.userName}, '%')</if>" +
            "<if test=\"member.nickName != null\"> and nickName like concat('%', #{member.nickName}, '%')</if>" +
            "<if test=\"member.id != 0\"> and id = #{member.id}</if> limit #{page.start}, #{page.limit}</script>")
    List<Member> queryMemberAllOrSth (@Param("page") Page page, @Param("member")Member member);
    @Select("<script>select count(*) as hh from member where 1 = 1 <if test=\"member.userName != null\">and userName like " +
            "concat('%', #{member.userName}, '%')</if>" +
            "<if test=\"member.nickName != null\"> and nickName like concat('%', #{member.nickName}, '%')</if>" +
            "<if test=\"member.id != 0\"> and id = #{member.id}</if> limit #{page.start}, #{page.limit}</script>")
    int countMember (@Param("page") Page page, @Param("member")Member member);
    @Select("select imageHeaderAddr from member where id = #{memberId} ")
    String getImageHeader (@Param("memberId") int memberId);
    @Select("select * from member where id = #{member.id} and pwd = #{member.pwd}")
    Member checkMember (@Param("member") Member member);
    @Update("update member set userName = #{member.userName}, " +
            "pwd = #{member.pwd}, " +
            "birthday = #{member.birthday}, " +
            "sex = #{member.sex}, " +
            "phone = #{member.phone}, " +
            "email = #{member.email}, " +
            "imageHeaderAddr = #{member.imageHeaderAddr}, " +
            "nickName = #{member.nickName}, " +
            "label = #{member.label}, " +
            "roleId = #{member.roleId}, " +
            "lastTimeLogin = #{date} where id = #{member.id} ")
    void editMember (@Param("member") Member member, @Param("date") String date);
    @Select("update Member set lastTimeLogin = #{lastTimeLogin} where id = #{id}")
    void refreshDate (@Param("lastTimeLogin") String lastTimeLogin, @Param("id") int id );
    @Delete("delete from Member where id = #{id}")
    void delMember (@Param("id") int id);
}
