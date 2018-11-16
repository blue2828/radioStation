package com.lyh.dao.imp;

import com.lyh.dao.IFileDao;
import com.lyh.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
@Repository
@Transactional
public class FileDaoImpl implements IFileDao {
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<Map<String, Object>> getAllFiles() {
        String sql = "select * from t_file";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for(Map<String, Object> map : list){
            Set<Entry<String, Object>> set = map.entrySet();
            if(set != null){
                Iterator<Entry<String, Object>> it = set.iterator();
                while (it.hasNext()){
                    Entry<String, Object> entry = it.next();
                    System.out.println("key = "+entry.getKey()+", value = "+entry.getValue());
                }
            }
        }
        return list;
    }

    @Override
    public boolean delFile(int id) {
        String sql = "delete from t_file where id  =  ? ";
        int num = jdbcTemplate.update(sql, id);
        if(num>0) return true;
        else return false;
    }

    @Override
    public String queryFileName(int id) {
        String sql = "select fileName from t_file where id  =  ? ";
        Object[] args = new Object[]{id};
        Object file = jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(File.class));
        return ((File) file).getFileName();
    }

}
