package com.lyh.service;

import com.lyh.entity.listenHistory;

import java.util.List;

public interface IListenHistoryService {
    List<listenHistory> getHistory(listenHistory lh);
    public int saveListen(listenHistory history);
    public int delSelfHistory(String ids);
}
