package com.lyh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.Member;
import com.lyh.entity.Page;
import com.lyh.service.IMemberService;
import com.lyh.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.Date;

@CrossOrigin
@RestController
public class MemberController {
    @Autowired
    @Qualifier("memberService")
    private IMemberService memberService;

    public void setUserService(IMemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping("/getAllMember")
    public JSONObject queryMemberAllOrSth (Page page, Member member) {
        JSONObject jb = new JSONObject();
        jb.put("code", 0);
        jb.put("msg", "");
        jb.put("data", new StringUtil().formatListToJson(memberService.queryMemberAllOrSth(page, member)));
        jb.put("count", memberService.countMember(page, member));
        return jb;
    }

    @RequestMapping("/userImg")
    public String getUserImage () {
        String base24Str = null;
        BufferedInputStream bufferedInputStream = null;
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File("D:/2.jpg"));
            bufferedInputStream = new BufferedInputStream((inputStream));
            outputStream = new ByteArrayOutputStream();
            int temp = 0;
            byte[] buffer = new byte[1024];
            while ((temp = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, temp);
                outputStream.flush();
            }
            byte[] result = outputStream.toByteArray();
            base24Str = JSON.toJSONString(result);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != bufferedInputStream)
                    bufferedInputStream.close();
                if (null != inputStream)
                    inputStream.close();
                if (null != outputStream)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base24Str;
    }
    @RequestMapping("/wxTest")
    public String testWx () {
        return "diao ni lao mu";
    }
    @Test
    public void testAdd () {
        Member member = new Member(0, 0, "Theresa", "123", "15678499723", "651991020@qq.com", "d:/uploadFile", "theresa", "哈哈", new Date());
        int i = memberService.insertUser(member);
    }
}
