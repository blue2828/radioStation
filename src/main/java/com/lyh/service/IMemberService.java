package com.lyh.service;


import com.lyh.entity.Member;
import com.lyh.entity.Page;

import java.util.List;

public interface IMemberService {
    int insertUser (Member member);
    List<Member> queryMemberAllOrSth (Page page, Member member);
    int countMember (Page page, Member member);
    int countAllMember ();
    String getImageHeader (int memberId);
    Member checkMember (Member member, boolean isWx);
    int editMember(Member member, String date, boolean isWx);
    void refreshDate(String lastLoginTime, int id);
    int delMember (int id);
    int delSessionKey (int id);
    int selectMaxId();
    public Member selectMemberByKey(Member member);
    public int refreshKey(Member member);
}
