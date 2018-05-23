package com.lyh.controller;

import com.lyh.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @RequestMapping("/login")
    public Map<String,List<User>> hello(){
        User u=new User(1,"stephy","123","66@qq.com");
        User u2=new User(2,"theresa","123","66@qq.com");
        List<User> userList=new ArrayList<User>();
        userList.add(u);
        userList.add(u2);
        Map<String,List<User>> map=new HashMap<String,List<User>>();
        map.put("data",userList);
        return map;
    }
}
