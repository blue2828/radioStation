package com.lyh.dao;

import com.lyh.entity.DemandList;
import com.lyh.entity.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IDemandDao {
    @Insert("insert into demandlist(id, state, memberId, demandTime, fileId, toSb, email, musicName, demandDesc) values (null, #{list.state}, #{list.memberId}," +
            " #{list.demandTime}, #{list.fileId}, #{list.toSb}, #{list.email}, #{list.musicName}, #{list.demandDesc})")
    void saveDemandList (@Param("list") DemandList list);
    @Update("<script>update demandlist <if test=\"onlyUpdateState\">set state = #{demandList.state}</if> <if test=\"!onlyUpdateState\"> " +
            "set `demandTime` = #{demandList.demandTime}, musicName = #{demandList.musicName}, toSb = #{demandList.toSb}" +
            ",email = #{demandList.email}, demandDesc = #{demandList.demandDesc}</if> where `fileId` = #{demandList.fileId}</script>")
    void updateDemandList (@Param("demandList") DemandList demandList, @Param("onlyUpdateState") boolean onlyUpdateState); //0 不更改点播状态 1 更改
    @Select("select * from demandlist where memberId = #{memberId}")
    @Results({
            //查询关联对象
            @Result(property = "userFile",
                    column = "fileId",
                    one = @One(select = "com.lyh.dao.IFileDao.getFileById"))
    })
    List<DemandList> getSelfDemand (@Param("memberId") int memberId);

}
