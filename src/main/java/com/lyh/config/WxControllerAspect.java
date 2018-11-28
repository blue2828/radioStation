package com.lyh.config;

import com.lyh.entity.Member;
import com.lyh.service.IMemberService;
import com.lyh.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class WxControllerAspect {
    private static  Logger logger = LoggerFactory.getLogger(WxControllerAspect.class);
    @Autowired
    @Qualifier("memberService")
    private IMemberService memberService;
    @Pointcut("execution(public * com.lyh.wxController.*.*(..))")
    public void delSessionKey () {

    }
    @Before("delSessionKey()")
    public void beforeDel (JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession();
            Member currentMember = (Member) session.getAttribute("currentWxMember");
            String memberId = request.getParameter("memberId");
            if (currentMember == null && !StringUtil.isEmpty(memberId)) {
                int result = memberService.delSessionKey(Integer.parseInt(memberId));
                logger.info(result > 0 ? "成功重置sessionkey" : "重置sessionkey失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
