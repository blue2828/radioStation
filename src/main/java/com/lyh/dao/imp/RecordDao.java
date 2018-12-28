package com.lyh.dao.imp;

import com.alibaba.fastjson.JSONArray;
import com.lyh.dao.IRecordDao;
import com.lyh.entity.Record;
import com.lyh.entity.listenHistory;
import com.lyh.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RecordDao implements IRecordDao{
    @Autowired
    private IRecordDao recordDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringUtil stringUtil;
    @Override
    public void saveRecord(Record record) {
        recordDao.saveRecord(record);
    }

    @Override
    public List getAllRecordList() {
        List recordList = jdbcTemplate.query("select r.id as r_id, r.name, r.storeAddr, r.stationId, r.memberId, r.createTime, m.userName, " +
                "s.name as s_name from record r join member m on r.memberId = m.id inner join station s on r.stationId = s.id", new RowMapper<Object>() {

            @Nullable
            @Override
            public List<JSONArray> mapRow(ResultSet resultSet, int i) throws SQLException {
                JSONArray array = stringUtil.formatObToJson(resultSet);
                List<JSONArray> list = new ArrayList<>();
                list.add(array);
                return list;
            }
        });
        return recordList;
    }

}
