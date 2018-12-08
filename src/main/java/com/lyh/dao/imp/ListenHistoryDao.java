package com.lyh.dao.imp;

import com.lyh.dao.IListenHistoryDao;
import com.lyh.entity.listenHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ListenHistoryDao implements IListenHistoryDao {
    @Autowired
    private IListenHistoryDao listenHistoryDao;
    @Override
    public List<listenHistory> getHistory(listenHistory lh) {
        return listenHistoryDao.getHistory(lh);
    }
}
