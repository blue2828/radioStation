package com.lyh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.Member;
import com.lyh.entity.Page;
import com.lyh.service.IMemberService;
import com.lyh.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Scope("prototype")
@CrossOrigin
@Controller
public class MemberController {
    @Autowired
    @Qualifier("memberService")
    private IMemberService memberService;
    @Autowired
    private StringUtil stringUtil;
    @RequestMapping("/checkLogin")
    @ResponseBody
    public Map<String, Object> checkLogin (Member member, HttpSession session, int isApp) {
        Map<String, Object> map = new HashMap<String, Object>();
        Member result = memberService.checkMember(member, false);
        if (isApp == 1) {
            if (result != null) {
                map.put("success", true);
                result.setPwd(null);
                result.setOpenid(null);
                result.setSession_key(null);
                map.put("currentMember", result);
                session.setAttribute("currentMember", result);
                return map;
            }
        }
        if (null != result) {
            if (result.getRoleId() != 2) {
                map.put("success", false);
                map.put("errMsg", "非管理员不能登录");
            }else {
                map.put("success", true);
                session.setAttribute("currentMember", result);
            }
        }else {
            map.put("success", false);
            map.put("errMsg", "账号或密码错误");
        }
        return map;
    }

    @RequestMapping("/getMemberInfo")
    @ResponseBody
    public JSONObject queryMemberAllOrSth (Page page, Member member, String s_sex) {
        if (!StringUtil.isEmpty(s_sex))
            member.setSex(Integer.parseInt(s_sex));
        else
            member.setSex(-1);
        page = page.getPage() == 0 && page.getLimit() == 0 ? new Page(1, 30) : page;
        JSONObject jb = new JSONObject();
        jb.put("code", 0);
        jb.put("msg", "");
        jb.put("data", stringUtil.formatListToJson(memberService.queryMemberAllOrSth(page, member)));
        jb.put("count", memberService.countMember(page, member));
        return jb;
    }

    /**
     * ajax 获取头像
     * @param memberId
     * @return
     */
    @RequestMapping("/getMemberImg")
    @ResponseBody
    public String getUserImage (int memberId, HttpServletRequest request) {
        String base24Str = null;
        BufferedInputStream bufferedInputStream = null;
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] result = null;
        List<Member> currentMember = memberService.queryMemberAllOrSth(new Page(1, 30), new Member(memberId, -1));
        int sex = 0;
        if (currentMember.size() > 0) {
            sex = currentMember.get(0).getSex();
        }
        try {
            inputStream = new FileInputStream(new File(memberService.getImageHeader(memberId) == null ? "" : memberService.getImageHeader(memberId)));
            bufferedInputStream = new BufferedInputStream((inputStream));
            int temp = 0;
            byte[] buffer = new byte[1024];
            while ((temp = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, temp);
                outputStream.flush();
            }
            result = outputStream.toByteArray();
            base24Str = JSON.toJSONString(result);
        }catch(FileNotFoundException e) {
            String path = null;
            try {
                File file = ResourceUtils.getFile("classpath:static/images" + java.io.File.separator + (sex == 0 ? "manHeader.jpg" : "wmHeader.jpg"));
                inputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream((inputStream));
                int temp = 0;
                byte[] buffer = new byte[1024];
                while ((temp = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, temp);
                    outputStream.flush();
                }
                result = outputStream.toByteArray();
                base24Str = JSON.toJSONString(result);
            }catch (Exception ee) {
                ee.printStackTrace();
                base24Str = null;
            }
            e.printStackTrace();
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
    @RequestMapping("/getImgUrl")
    public void getImgUrl (String url, HttpServletResponse response) {
        FileInputStream fileInputStream = null;
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        OutputStream out = null;
        try {
            fileInputStream = new FileInputStream(new File("d:/fileUpload/f76bbac5_7cce_4eb6_9b7f_033be0fa6157_傅颖.jpg"));
            inputStream = new BufferedInputStream(fileInputStream);
            out = response.getOutputStream();
            outputStream = new BufferedOutputStream(out);
            int temp = 0;
            byte[] b = new byte[1024];
            while ((temp = fileInputStream.read(b)) != -1) {
                outputStream.write(b, 0, temp);
                outputStream.flush();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
                try {
                    if (null != out)
                        out.close();
                    if (null != outputStream)
                        outputStream.close();
                    if (null != fileInputStream)
                        fileInputStream.close();
                    if (null != inputStream)
                        inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @RequestMapping("/getCurrentMember")
    @ResponseBody
    public JSONObject getCurrentMember (HttpSession session) {
        Member currentMember = (Member) session.getAttribute("currentMember");
        JSONObject result = new JSONObject();
        if (currentMember == null) {
            result.put("currentMember", null);
            return result;
        }
        List<Member> list = memberService.queryMemberAllOrSth(new Page(1, 30), new Member(currentMember.getId(), -1));
        result.put("currentMember", stringUtil.formatListToJson(list));
        if (list.size() > 0) {
            if (null == list.get(0).getLastTimeLogin())
                result.put("lastTimeLogin", "此次为第一次登录");
            else
                result.put("lastTimeLogin", stringUtil.formatTime(list.get(0).getLastTimeLogin(), "yyyy-MM-dd HH:mm:ss"));
        }else
            result.put("currentMember", null);
        return result;
    }
    @RequestMapping("/logout")
    public String logout (HttpSession session, HttpServletResponse response) throws Exception {
        session.removeAttribute("currentMember");
        return "redirect:/page/login.html";
    }
    /*@RequestMapping("/editMember")
    @ResponseBody
    public Map<String, Object> editMember (Member member, String date) {
        Map<String, Object> map = new HashMap<String, Object>();
        int flag = memberService.editMember(member, date, false);
        switch (flag) {
            case 1 :
                map.put("success", true);
                break;
                default:
                    map.put("success", false);
                    map.put("errMsg", "编辑失败");
        }
        return map;
    }*/
    @RequestMapping("/refreshLoginTime")
    @ResponseBody
    public Map<String, Object> refreshLoginTime (HttpSession session) {
        Member m = (Member) session.getAttribute("currentMember");
        Map<String, Object> map = new HashMap<String, Object>();
        if (m == null) {
            map.put("complete", "等会会话已经过期");
            map.put("errMsg", true);
            return map;
        }
        memberService.refreshDate(stringUtil.getCurrentTimeStr(), m.getId());
        map.put("complete", true);
        return map;
    }
    @RequestMapping("/delMember")
    @ResponseBody
    public Map<String, Object> delMember (String idArr) {
        Map<String, Object> map = new HashMap<String, Object>();
        String[] ids = idArr.split(",");
        int count = 0;
        List<Object> list = new ArrayList<Object>();
        for (String id : ids) {
            if (!StringUtil.isEmpty(id)) {
                memberService.delMember(Integer.parseInt(id));
                count ++;
            } else {
                list.add(id);
                continue;
            }
        }
        if (count == ids.length)
            map.put("success", true);
        else {
            String items = "";
            for(int i = 0; i < list.size(); i ++) {
                if (i == list.size() - 1)
                    items = items.concat( "" + list.get(i));
                else
                    items = items.concat( list.get(i) + ",");
            }
            map.put("success", false);
            map.put("errMsg", "删除失败，失败条数：" + list.size() + "，删除失败的用户编号：" + items);
        }
        return map;
    }
    @RequestMapping("/saveMember")
    @ResponseBody
    public JSONObject saveMember (Member member, int isEdit, String birthDay) {
        JSONObject result = new JSONObject();
        int flag = 0;
        if (!StringUtil.isEmpty(birthDay))
            member.setBirthday(new StringUtil().formatStrTimeToDate(birthDay, "yyyy-MM-dd"));
        if (isEdit == 1) {
            Member temp = new Member(member.getId(), -1);
            Member currentMember = memberService.queryMemberAllOrSth(new Page(1, 30), temp).get(0);
            member.setImageHeaderAddr(currentMember.getImageHeaderAddr());
            member.setPwd(currentMember.getPwd());
            flag = memberService.editMember(member, null, false);
        }
        else
            flag = memberService.insertUser(member);
        switch (flag) {
            case 0 :
                result.put("success", false);
                result.put("errMsg", "保存失败");
                break;
            case 1 :
                result.put("success", true);
                break;
        }
        return result;
    }
}
