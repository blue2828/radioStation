package com.lyh.dao;

import com.lyh.entity.Member;
import com.lyh.entity.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IMemberDao {
    @Insert("<script>insert into member values (#{member.id}, #{member.userName}, <if test=\"member.pwd==null\">123</if><if test=\"member.pwd!=null\">#{member.pwd }</if>, #{member.birthday}, #{member.sex}, #{member.phone}," +
            " #{member.email}, #{member.imageHeaderAddr}, #{member.nickName}, #{member.label}, #{member.roleId}, null, #{member.openid}, #{member.session_key}, #{member.wechatNo})</script>")
    int insertUser (@Param("member") Member member);
    @Select("<script>select * from member where 1 = 1 <if test=\"member.userName != null\">and userName like " +
            "concat('%', #{member.userName}, '%')</if>" +
            "<if test=\"member.nickName != null\"> and nickName like concat('%', #{member.nickName}, '%')</if>" +
            "<if test=\"member.id != 0\"> and id = #{member.id}</if><if test=\"member.sex != -1\"> and sex = #{member.sex}</if> limit #{page.start}, #{page.limit}</script>")
    List<Member> queryMemberAllOrSth (@Param("page") Page page, @Param("member")Member member);
    @Select("<script>select count(*) as hh from member where 1 = 1 <if test=\"member.userName != null\">and userName like " +
            "concat('%', #{member.userName}, '%')</if>" +
            "<if test=\"member.nickName != null\"> and nickName like concat('%', #{member.nickName}, '%')</if>" +
            "<if test=\"member.id != 0\"> and id = #{member.id}</if><if test=\"member.sex != -1\"> and sex = #{member.sex}</if>" +
            "limit #{page.start}, #{page.limit}</script>")
    int countMember (@Param("page") Page page, @Param("member")Member member);
    @Select("select imageHeaderAddr from member where id = #{memberId} ")
    String getImageHeader (@Param("memberId") int memberId);
    @Select("<script><if test=\"isWx == true\"> select * from member where openid = #{member.openid}</if>" +
            "<if test=\"isWx == false\"> select * from member where id = #{member.id} and pwd = #{member.pwd}</if></script>")
    Member checkMember (@Param("member") Member member, @Param("isWx") boolean isWx);
    @Update("<script>update member set userName = #{member.userName}, " +
            "pwd = #{member.pwd}, " +
            "birthday = #{member.birthday}, " +
            "sex = #{member.sex}, " +
            "phone = #{member.phone}, " +
            "email = #{member.email}, " +
            "imageHeaderAddr = #{member.imageHeaderAddr}, " +
            "nickName = #{member.nickName}, " +
            "label = #{member.label}, " +
            "roleId = #{member.roleId}, " +
            "lastTimeLogin = #{date} <if test=\"isWx != true\">,openid = #{member.openid}</if><if test=\"isWx != true\">, session_key = #{member.session_key}</if> where id = #{member.id}</script>")
    void editMember (@Param("member") Member member, @Param("date") String date, @Param("isWx") boolean isWx);
    @Select("update member set lastTimeLogin = #{lastTimeLogin} where id = #{id}")
    void refreshDate (@Param("lastTimeLogin") String lastTimeLogin, @Param("id") int id );
    @Delete("delete from member where id = #{id}")
    void delMember (@Param("id") int id);
    @Delete("update member set sessionKey = NULL where id = #{id}")
    void delSessionKey (@Param("id") int id);
    @Select("SELECT MAX(id) FROM member")
    int selectMaxId ();
    @Select("select * from member where session_key = #{member.session_key}")
    Member selectMemberByKey (@Param("member") Member member);
    @Update("update member set session_key = #{member.session_key} where id = #{member.id}")
    void refreshKey (@Param("member") Member member);
    @Select("select count(*) from member")
    int countAllMember();
}
