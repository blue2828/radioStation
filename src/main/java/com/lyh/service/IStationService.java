package com.lyh.service;

import com.lyh.entity.Page;
import com.lyh.entity.Station;

import java.util.List;

public interface IStationService {
    List<Station> queryStation (Page page);
    int countStation (Page page);
}
