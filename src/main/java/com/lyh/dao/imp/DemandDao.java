package com.lyh.dao.imp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.dao.IDemandDao;
import com.lyh.entity.DemandList;
import com.lyh.entity.Member;
import com.lyh.entity.UserFile;
import com.lyh.entity.listenHistory;
import com.lyh.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("demandDao")
public class DemandDao implements IDemandDao {
    @Autowired
    private IDemandDao demandDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringUtil stringUtil;
    @Override
    public void saveDemandList(DemandList list) {
        demandDao.saveDemandList(list);
    }

    @Override
    public void updateDemandList(DemandList list, boolean onlyUpdateState) {
        demandDao.updateDemandList(list, onlyUpdateState);
    }

    @Override
    public List<DemandList> getSelfDemand(int memberId) {
        return demandDao.getSelfDemand(memberId);
    }
    @Override
    public List getAllNotBroadcastDemandList () {
        List resultSets = jdbcTemplate.query("SELECT dl.id AS dl_id, dl.`fileId` AS dl_fileId, u.storeAddr, u.fileName, u.uploadDate, u.type, u.play_url, m.id AS m_id, m.`userName`,\n" +
                "m.birthday, m.sex, m.phone, m.email as m_email, m.imageHeaderAddr, m.nickName, m.label, dl.state, dl.demandTime, dl.toSb, dl.email, dl.musicName, dl.demandDesc FROM  demandlist dl JOIN \n" +
                "userfile u  ON  u.id = dl.fileId AND dl.state = 0 JOIN member m ON u.`uploadMemId` = m.id", new RowMapper<Object>() {

            @Nullable
            @Override
            public List<JSONArray> mapRow(ResultSet resultSet, int i) throws SQLException {
                JSONArray array = stringUtil.formatObToJson(resultSet);
                List<JSONArray> list = new ArrayList<>();
                list.add(array);
                return list;
            }
        });
        return resultSets;
    }

    @Override
    public int getStateById(int id) {
        return demandDao.getStateById(id);
    }

}
