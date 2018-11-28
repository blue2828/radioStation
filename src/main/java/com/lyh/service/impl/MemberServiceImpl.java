package com.lyh.service.impl;

import com.lyh.dao.IMemberDao;
import com.lyh.entity.Member;
import com.lyh.entity.Page;
import com.lyh.service.IMemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("memberService")
public class MemberServiceImpl implements IMemberService {
    @Autowired
    @Qualifier("memberDao")
    private IMemberDao memberDao;
    @Override
    @Transactional
    public int insertUser(Member member) {
        int flag = 0;
        try {
            flag = memberDao.insertUser(member);
        }catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        }
        return flag;
    }

    @Override
    public List<Member> queryMemberAllOrSth(Page page, Member member) {
        List<Member> list = null;
        try {
            list = memberDao.queryMemberAllOrSth(page, member);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countMember(Page page, Member member) {
        int num = 0;
        try {
            num = memberDao.countMember(page, member);
        }catch (Exception e) {
            e.printStackTrace();
            num = 0;
        }
        return num;
    }

    @Override
    public String getImageHeader(int memberId) {
        String imgAddr = "";
        try {
            imgAddr = memberDao.getImageHeader(memberId);
        }catch (Exception e) {
            e.printStackTrace();
            imgAddr = "";
        }
        return imgAddr;
    }

    @Override
    public Member checkMember(Member member, boolean isWx) {
        Member mem = null;
        try {
            mem = memberDao.checkMember(member, isWx);
        }catch (Exception e) {
            mem = null;
            e.printStackTrace();
        }
        return mem;
    }

    @Override
    @Transactional
    public int editMember(Member member, String date) {
        int flag = 0;
        try {
            memberDao.editMember(member, date);
            flag = 1;
        }catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        }
        return flag;
    }

    @Override
    public void refreshDate(String lastLoginTime, int id) {
        try {
            memberDao.refreshDate(lastLoginTime, id);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public int delMember(int id) {
        int flag = 0;
        try {
            memberDao.delMember(id);
            flag = 1;
        }catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        }
        return flag;
    }

    @Override
    public int delSessionKey(int id) {
        int flag = 0;
        try {
            memberDao.delSessionKey(id);
            flag = 1;
        }catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        }
        return flag;
    }
    @Override
    public int selectMaxId() {
        int result = 0;
        try {
            result = memberDao.selectMaxId();
        }catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    @Override
    public Member selectMemberByKey(Member member) {
        Member result = null;
        try {
            result = memberDao.selectMemberByKey(member);
        }catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int refreshKey(Member member) {
        int flag = 0;
        try {
            memberDao.refreshKey(member);
            flag = 1;
        }catch (Exception e) {
            flag = 0;
            e.printStackTrace();
        }
        return flag;
    }
}
