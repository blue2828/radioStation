package com.lyh.wxController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.Member;
import com.lyh.entity.Page;
import com.lyh.entity.listenHistory;
import com.lyh.service.IListenHistoryService;
import com.lyh.service.IMemberService;
import com.lyh.util.StringUtil;
import com.lyh.util.WxControllerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@Controller("wechatController")
@Scope("prototype")
public class WechatController {
    private static final String APPID = "wx20a96a20afe5cefb";
    private static final String SECRET = "c19fb131c57e25122cfff85013701cf1";
    private static Logger logger = LoggerFactory.getLogger(Logger.class);
    @Autowired
    @Qualifier("memberService")
    private IMemberService memberService;
    @Autowired
    @Qualifier("wxControllerUtil")
    private WxControllerUtil wxControllerUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private IListenHistoryService listenHistoryService;
    @RequestMapping("/onWxLogin")
    @ResponseBody
    public Map<String, Object> onLogin (String code, HttpSession session, String remoteKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=".concat(APPID).concat("&secret=").concat(SECRET).concat("&js_code=")
                .concat(code).concat("&grant_type=authorization_code");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity =  restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (entity != null && entity.getStatusCode() == HttpStatus.OK) {
            String data = entity.getBody();
            JSONObject tempJb = JSON.parseObject(data);
            String openid = tempJb.getString("openid");
            String session_key = tempJb.getString("session_key");
            Member member = new Member();
            member.setOpenid(openid);
            String key = new StringUtil().getRandomStr();
            Member currentWxMember = (Member) session.getAttribute("currentWxMember");
            if (currentWxMember != null) {
                if (wxControllerUtil.isLogined(session, currentWxMember, (String) session.getAttribute(remoteKey))) {
                    map.put("success", true);
                    currentWxMember.setOpenid(null);
                    currentWxMember.setSession_key(null);
                    map.put("currentWxMember", currentWxMember);
                }else {
                    map.put("errMsg", "登录授权失败");
                    map.put("success", false);
                }
                return map;
            }
            Member result = memberService.checkMember(member, true);
            if (result == null) {
                int maxId = memberService.selectMaxId();
                member.setNickName("电台用户_" + UUID.randomUUID().toString().substring(0, 10));
                member.setId(maxId + 1);
                member.setSession_key(session_key);
                int flag = memberService.insertUser(member);
                if (flag > 0) {
                    logger.info("成功添加用户");
                    Member thisMem = memberService.queryMemberAllOrSth(new Page(1, 30), new Member(maxId + 1, -1)).get(0);
                    session.setAttribute("currentWxMember", thisMem);
                    session.setAttribute(key, session_key);
                    map.put("success", true);
                    map.put("session_key", session_key);
                    thisMem.setSession_key(null);
                    thisMem.setOpenid(null);
                    map.put("currentWxMember", thisMem);
                }else {
                    map.put("success", false);
                    map.put("errMsg", "服务器出错，注册失败，请稍后重试");
                    logger.error("添加用户失败");
                }
            } else {
                map.put("success", true);
                session.setAttribute("currentWxMember", result);
                session.setAttribute(key, result.getSession_key());
                result.setOpenid(null);
                result.setSession_key(null);
                map.put("session_key", key);
                map.put("currentWxMember", result);
            }
        }else {
            map.put("errMsg", "授权登录失败");
        }
        return map;
    }
    @RequestMapping("/editWxMember")
    @ResponseBody
    public Map<String, Object> editWxMember (@RequestParam(value = "wxImg", required = false) MultipartFile wxImg, Member member, String birthDay, HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean fileFlag = false;
        if (wxImg != null) {
            String type = wxImg.getOriginalFilename().substring(wxImg.getOriginalFilename().lastIndexOf("."));
            String fileName = wxImg.getName();
            fileName = StringUtil.mkFileName(fileName, type);
            member.setImageHeaderAddr("d:/fileUpload".concat(File.separator).concat(fileName));
            try {
                wxImg.transferTo(new File("d:/fileUpload".concat(File.separator).concat(fileName)));
                fileFlag = true;
            }catch (Exception e) {
                e.printStackTrace();
                fileFlag = false;
            }
        }else {
            Member temp = new Member(member.getId(), -1);
            Member currentMember = memberService.queryMemberAllOrSth(new Page(1, 30), temp).get(0);
            member.setImageHeaderAddr(currentMember.getImageHeaderAddr());
        }
        member.setBirthday(new StringUtil().formatStrTimeToDate(birthDay, "yyyy-MM-dd"));
        int flag = memberService.editMember(member, null, true);
        if (wxImg != null) {
            if (!fileFlag)
                logger.info("头像上传到磁盘失败");
            if (flag <= 0)
                logger.info("保存到数据库失败");
            if (flag > 0 && fileFlag)
                map.put("isSuccessed", true);
            else
                map.put("isSuccessed", false);
        }else {
            if (flag > 0)
                map.put("isSuccessed", true);
            else {
                map.put("isSuccessed", false);
                logger.info("保存到数据库失败");
            }
        }
        return map;
    }
    @RequestMapping("/getListetnHistory")
    @ResponseBody
    public JSONObject getListetnHistory () {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = stringUtil.formatListToJson(listenHistoryService.getHistory(new listenHistory()));
        jsonObject.put("list", array);
        return jsonObject;
    }
    @RequestMapping("/readRecord")
    public void readRecord (HttpServletResponse response, int listenHistoryId) {
        BufferedInputStream bufferedInputStream = null;
        FileInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        OutputStream out = null;
        listenHistory lh = listenHistoryService.getHistory(new listenHistory(listenHistoryId)).get(0);
        File file = new File(lh.getTargetFile());
        long length = file.length();
        response.addHeader("Accept-Ranges", "bytes");
        response.addHeader("Content-Length", length + "");
        response.addHeader("Content-Type", "audio/mpeg;charset=UTF-8");
        try {
            inputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream((inputStream));
            out = response.getOutputStream();
            outputStream = new BufferedOutputStream(out);
            int temp = 0;
            byte[] buffer = new byte[1024];
            while ((temp = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, temp);
                outputStream.flush();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != bufferedInputStream)
                    bufferedInputStream.close();
                if (null != inputStream)
                    inputStream.close();
                if(null != out)
                    out.close();
                if (null != outputStream)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
