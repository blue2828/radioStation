package com.lyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.Page;
import com.lyh.entity.Station;
import com.lyh.service.IStationService;
import com.lyh.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
public class StationController {
    @Autowired
    private IStationService stationService;
    @Autowired
    private StringUtil stringUtil;
    @RequestMapping("/getStation")
    @ResponseBody
    public JSONObject getStation (Page page) {
        JSONObject jsonObject = new JSONObject();
        List<Station> list = stationService.queryStation(new Page(page.getPage() == 0 ? 1 : page.getPage(),
                page.getLimit() == 0 ? 30 : page.getLimit()));
        JSONArray array = null;
        if (list.size() != 0)
            array = stringUtil.formatListToJson(list);
        else
            array = null;
        jsonObject.put("msg", "");
        jsonObject.put("code", 0);
        jsonObject.put("data", array);
        jsonObject.put("count", stationService.countStation(page));
        return jsonObject;
    }
    @RequestMapping("/updateStationState")
    @ResponseBody
    public Map<String, Object> updateStationState (String time, Station station) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(time))
            station.setLastTimeBroadcast(new StringUtil().formatStrTimeToDate(time, "yyyy-MM-dd HH:mm:ss"));
        else {
            Date lastTime = stationService.getLastTime(station.getLastTimeMemberId());
            station.setLastTimeBroadcast(lastTime);
        }
        boolean flag = stationService.updateStationState(station);
        resultMap.put("success", flag ? true : false);
        return resultMap;
    }
    @RequestMapping("/getCurrentCastInfo")
    @ResponseBody
    public JSONObject getCurrentCastInfo() {
        JSONObject jsonObject = new JSONObject();
        List<Station> info = stationService.getCurrentStation(true);
        JSONArray array = info.size() > 0 ? stringUtil.formatListToJson(info) : null;
        jsonObject.put("result", array);
        return jsonObject;
    }
}

