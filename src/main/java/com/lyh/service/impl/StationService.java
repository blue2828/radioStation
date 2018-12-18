package com.lyh.service.impl;

import com.lyh.dao.IStationDao;
import com.lyh.entity.Page;
import com.lyh.entity.Station;
import com.lyh.service.IStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
public class StationService implements IStationService{
    @Autowired
    private IStationDao stationDao;
    @Override
    public List<Station> queryStation(Page page) {
        List<Station> list = null;
        try {
            list = stationDao.queryStation(page);
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countStation(Page page) {
        int result = 0;
        try {
            result = stationDao.countStation(page);
        }catch (Exception e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateStationState(Station station) {
        boolean flag = false;
        try {
            stationDao.updateStationState(station);
            flag = true;
        }catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public Date getLastTime(int id) {
        Date date = null;
        try {
            date = stationDao.getLastTime(id);
        }catch (Exception e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public  List<Station> getCurrentStation(boolean isOnlyQueryBroadcast) {
        List<Station> result = null;
        try {
            result = stationDao.getCurrentStation(isOnlyQueryBroadcast);
        }catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }
}
