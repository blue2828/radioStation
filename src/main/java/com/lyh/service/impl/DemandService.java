package com.lyh.service.impl;

import com.lyh.dao.IDemandDao;
import com.lyh.entity.DemandList;
import com.lyh.service.IDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DemandService implements IDemandService {
    @Autowired
    private IDemandDao demandDao;
    @Override
    @Transactional
    public boolean saveDemandList(DemandList list) {
        boolean flag = false;
        try {
            demandDao.saveDemandList(list);
            flag = true;
        }catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    @Transactional
    public boolean updateDemandList(DemandList list, boolean onlyUpdateState) {
        boolean flag = false;
        try {
            demandDao.updateDemandList(list, onlyUpdateState);
            flag = true;
        }catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<DemandList> getSelfDemand(int memberId) {
        List<DemandList> list = null;
        try {
            list = demandDao.getSelfDemand(memberId);
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Object> getAllNotBroadcastDemandList() {
        List<Object> list = new ArrayList<Object>();
        try {
            list = demandDao.getAllNotBroadcastDemandList();
        }catch (Exception e) {
            list.clear();
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int getStateById(int id) {
        int flag = -1;
        try {
            flag = demandDao.getStateById(id);
        }catch (Exception e) {
            flag = -1;
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public List getDemandToMe(int memberId) {
        List list = new ArrayList();
        try {
            list = demandDao.getDemandToMe(memberId);
        }catch (Exception e) {
            list.clear();
            e.printStackTrace();
        }
        return list;
    }

}
