package com.lyh.dao.imp;

import com.lyh.dao.IStationDao;
import com.lyh.entity.Page;
import com.lyh.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    @Override
    public void updateStationState(Station station) {
        stationDao.updateStationState(station);
    }

    @Override
    public Date getLastTime(int id) {
        return stationDao.getLastTime(id);
    }

    @Override
    public  List<Station> getCurrentStation(boolean isOnlyQueryBroadcast) {
        return stationDao.getCurrentStation(isOnlyQueryBroadcast);
    }

    @Override
    public void onlyUpdateState(Station station) {
        stationDao.onlyUpdateState(station);
    }
}
