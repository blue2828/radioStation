package com.lyh.dao.imp;

import com.lyh.dao.IDemandDao;
import com.lyh.entity.DemandList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("demandDao")
public class DemandDao implements IDemandDao {
    @Autowired
    private IDemandDao demandDao;
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
}
