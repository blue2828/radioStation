package com.lyh.service;


import com.lyh.entity.Member;
import com.lyh.entity.Page;

import java.util.List;

public interface IMemberService {
    int insertUser (Member member);
    List<Member> queryMemberAllOrSth (Page page, Member member);
    int countMember (Page page, Member member);
    String getImageHeader (int memberId);
    Member checkMember (Member member);
    int editMember(Member member, String date);
    void refreshDate(String lastLoginTime, int id);
    int delMember (int id);
}
