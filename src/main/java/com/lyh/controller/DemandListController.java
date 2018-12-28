package com.lyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.DemandList;
import com.lyh.service.IDemandService;
import com.lyh.service.IFileService;
import com.lyh.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@CrossOrigin
@Scope("prototype")
public class DemandListController {
    @Autowired
    IDemandService demandService;
    @Autowired
    IFileService fileService;
    @Autowired
    private StringUtil stringUtil;
    @RequestMapping("/getAllNotBroadcastDemandList")
    @ResponseBody
    public JSONObject getAllNotBroadcastDemandList () {
        JSONObject jsonObject = new JSONObject();
        List list = demandService.getAllNotBroadcastDemandList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ArrayList<JSONArray> list2 = (ArrayList<JSONArray>) it.next();
            for (JSONArray temp : list2) {
                jsonObject.put("demandList", temp);
            }
        }
        if (jsonObject.size() == 0 )
            jsonObject.put("demandList", new JSONArray());
        return jsonObject;
    }
}
