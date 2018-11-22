package com.lyh.dao.imp;

import com.lyh.dao.IMemberDao;
import com.lyh.entity.Member;
import com.lyh.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("memberDao")
public class MemberDaoImpl implements IMemberDao {
    @Autowired
    private IMemberDao memberDao;
    @Override
    public int insertUser(Member member) {
        return memberDao.insertUser(member);
    }

    @Override
    public List<Member> queryMemberAllOrSth(Page page, Member member) {
        return memberDao.queryMemberAllOrSth(page, member);
    }

    @Override
    public int countMember(Page page, Member member) {
        return memberDao.countMember(page, member);
    }

    @Override
    public String getImageHeader(int memberId) {
        return memberDao.getImageHeader(memberId);
    }

    @Override
    public Member checkMember(Member member) {
        return memberDao.checkMember(member);
    }

    @Override
    public void editMember(Member member, String str) {
        memberDao.editMember(member, str);
    }

    @Override
    public void refreshDate(String lastLoginTime, int id) {
        memberDao.refreshDate(lastLoginTime, id);
    }

    @Override
    public void delMember(int id) {
        memberDao.delMember(id);
    }
}
