package com.lyh.util;

import com.lyh.entity.Member;
import com.lyh.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
@Component
public class WxControllerUtil {
    @Autowired
    @Qualifier("memberService")
    private IMemberService memberService;
    public boolean isLogined (HttpSession session, Member member, String key) {
        boolean flag = false;
        Member currentMember = (Member) session.getAttribute("currentWxMember");
        String session_key = (String) session.getAttribute(key);
        Member m = memberService.checkMember(member, true);
        if (currentMember != null && m != null )
            flag = m.getSession_key().equals(session_key) ? true : false;
        else
            flag = false;
        return flag;
    }
}
