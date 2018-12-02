package com.lyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.Page;
import com.lyh.entity.Station;
import com.lyh.service.IStationService;
import com.lyh.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
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
}

