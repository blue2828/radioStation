package com.lyh.service.impl;

import com.lyh.dao.IListenHistoryDao;
import com.lyh.entity.listenHistory;
import com.lyh.service.IListenHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ListenHistoryService implements IListenHistoryService {
    @Autowired
    private IListenHistoryDao listenHistoryDao;
    @Override
    public List<listenHistory> getHistory(listenHistory lh) {
        List<listenHistory> list = null;
        try {
            list = listenHistoryDao.getHistory(lh);
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
}
