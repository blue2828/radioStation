package com.lyh.service;


import com.lyh.entity.Member;
import com.lyh.entity.Page;

import java.util.List;

public interface IMemberService {
    int insertUser (Member member);
    List<Member> queryMemberAllOrSth (Page page, Member member);
    int countMember (Page page, Member member);
}
