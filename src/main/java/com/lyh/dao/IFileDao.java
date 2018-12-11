package com.lyh.dao;

import com.lyh.entity.Page;
import com.lyh.entity.UserFile;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
@Mapper
public interface IFileDao {

    @Select("<script>select * from userfile where 1 = 1 <if test=\"userFile.fileName != null\"> and fileName like " +
            "concat('%', #{userFile.fileName}, '%')</if><if test=\"userFile.uploadMemId != -1\"> and uploadMemId = #{userFile.uploadMemId}</if>" +
            "<if test=\"userFile.type != -1\"> and type = #{userFile.type}</if> <if test=\"userFile.id != -1\"> and id = #{userFile.id}</if>" +
            "limit #{page.start}, #{page.limit}</script>")
    public List<UserFile> getAllFiles(@Param("userFile") UserFile userFile, @Param("page") Page page);
    @Select("<script>select count(*) as num from userfile where 1 = 1 <if test=\"userFile.fileName != null\"> and fileName like " +
            "concat('%', #{userFile.fileName}, '%')</if><if test=\"userFile.uploadMemId != -1\"> and uploadMemId = #{userFile.uploadMemId}</if>" +
            "<if test=\"userFile.type != -1\"> and type = #{userFile.type}</if> <if test=\"userFile.id != -1\"> and id = #{userFile.id}</if>" +
            "limit #{page.start}, #{page.limit}</script>")
    public int countFile (@Param("userFile") UserFile userFile, @Param("page") Page page);
    @Delete("delete from userfile where id = #{id}")
    public boolean delFile (@Param("id") int id);
    @Insert("insert into userfile(id, storeAddr, fileName, uploadMemId, uploadDate, type, fileDesc) values (" +
            "null, #{userFile.storeAddr}, #{userFile.fileName}, #{userFile.uploadMemId}, #{userFile.uploadDate}, #{userFile.type}, #{userFile.fileDesc})")
    public void saveFile (@Param("userFile") UserFile userFile);
}
