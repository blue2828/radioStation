package com.lyh.dao.imp;

import com.lyh.dao.IStationDao;
import com.lyh.entity.Page;
import com.lyh.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class StationDao implements IStationDao {
    @Autowired
    private IStationDao stationDao;
    @Override
    public List<Station> queryStation(Page page) {
        return stationDao.queryStation(page);
    }

    @Override
    public int countStation(Page page) {
        return stationDao.countStation(page);
    }
}
