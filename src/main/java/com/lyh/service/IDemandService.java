package com.lyh.service;

import com.lyh.entity.DemandList;

import java.util.List;

public interface IDemandService {
    public boolean saveDemandList(DemandList list);
    boolean updateDemandList (DemandList list, boolean onlyUpdateState);
    List<DemandList> getSelfDemand (int memberId);
}
