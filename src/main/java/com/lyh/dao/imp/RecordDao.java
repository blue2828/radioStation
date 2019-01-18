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
    public List getAllRecordList(int memberId) {
        StringBuffer stringBuffer = new StringBuffer("select r.id as r_id, r.name, r.storeAddr, r.stationId, r.memberId, r.createTime, m.userName, " +
                "s.name as s_name from record r join member m on r.memberId = m.id inner join station s on r.stationId = s.id");
        if (memberId != 0)
            stringBuffer.append(" where memberId = ?");
        List recordList = null;
        if (memberId != 0) {
             recordList = jdbcTemplate.query(stringBuffer.toString(), new Object[] {memberId}, new RowMapper<Object>() {
                @Nullable
                @Override
                public List<JSONArray> mapRow(ResultSet resultSet, int i) throws SQLException {
                    JSONArray array = stringUtil.formatObToJson(resultSet);
                    List<JSONArray> list = new ArrayList<>();
                    list.add(array);
                    return list;
                }
            });
        }else {
            recordList = jdbcTemplate.query(stringBuffer.toString(), new RowMapper<Object>() {

                @Nullable
                @Override
                public List<JSONArray> mapRow(ResultSet resultSet, int i) throws SQLException {
                    JSONArray array = stringUtil.formatObToJson(resultSet);
                    List<JSONArray> list = new ArrayList<>();
                    list.add(array);
                    return list;
                }
            });
        }
        return recordList;
    }

    @Override
    public void updateDemandIds(int recordId, String ids) {
        recordDao.updateDemandIds(recordId, ids);
    }

    @Override
    public String getDemandIdsById(int id) {
        return recordDao.getDemandIdsById(id);
    }

    @Override
    public int getIdByStoreAddr(String storeAdr) {
        return recordDao.getIdByStoreAddr(storeAdr);
    }


}
