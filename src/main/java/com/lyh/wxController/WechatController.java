package com.lyh.wxController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.*;
import com.lyh.service.*;
import com.lyh.service.message.JmsProducer;
import com.lyh.util.EnumUtil;
import com.lyh.util.StringUtil;
import com.lyh.util.WxControllerUtil;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Destination;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

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
    @Autowired
    private IDemandService demandService;
    @Autowired
    @Qualifier("fileService")
    private IFileService fileServiceImpl;
    @Autowired
    @Qualifier("stationService")
    private IStationService stationService;
    @Autowired
    @Qualifier("jmsProducer")
    private JmsProducer jmsProducer;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String mailUserName;
    @RequestMapping("/onWxLogin")
    @ResponseBody
    public Map<String, Object> onLogin (String code, HttpSession session, String remoteKey) {
        logger.info("===执行登陆==");
        session.setMaxInactiveInterval(604800);
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
                    session.setAttribute("currentWxMember", currentWxMember);
                    session.setAttribute(key, session_key);
                    map.put("success", true);
                    map.put("session_key", session_key);
                    currentWxMember.setSession_key(null);
                    currentWxMember.setOpenid(null);
                    map.put("cookie_id", session.getId());
                    map.put("currentWxMember", currentWxMember);
                }else {
                    currentWxMember.setSession_key(session_key);
                    int flag = memberService.editMember(currentWxMember, stringUtil.getCurrentTimeStr(), true);
                    logger.info(flag <= 0 ? "重新登录失败" : "重新登录成功");
                    switch (flag) {
                        case 1 :
                            session.setAttribute("currentWxMember", currentWxMember);
                            session.setAttribute(key, session_key);
                            map.put("success", true);
                            map.put("session_key", session_key);
                            currentWxMember.setSession_key(null);
                            currentWxMember.setOpenid(null);
                            map.put("cookie_id", session.getId());
                            map.put("currentWxMember", currentWxMember);
                            break;
                        default :
                            map.put("success", false);
                            map.put("errMsg", "登录授权失败");

                    }

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
                    map.put("cookie_id", session.getId());
                    map.put("currentWxMember", thisMem);
                }else {
                    map.put("success", false);
                    map.put("errMsg", "服务器出错，注册失败，请稍后重试");
                    logger.error("添加用户失败");
                }
            } else {
                map.put("success", true);
                session.setAttribute("currentWxMember", result);
                map.put("cookie_id", session.getId());
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
            int fileId = fileServiceImpl.selectMaxId() + 1;
            UserFile imgFile = new UserFile(fileId, member.getId(), 2, "d:/fileUpload".concat(File.separator).concat(fileName),
                    fileName, stringUtil.formatStrTimeToDate(stringUtil.getCurrentTimeStr(), "yyyy-MM-dd HH:mm:ss"));
            fileServiceImpl.saveFile(imgFile);
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
    @RequestMapping("/wxUploadFile")
    @ResponseBody
    public Map<String, Object> wxUploadFile (@RequestParam(value = "file") MultipartFile multipartFile, String type, int memberId) {
        Map<String, Object> map = new HashMap<String, Object> ();
        String fileName = multipartFile.getName();
        fileName = StringUtil.mkFileName(fileName, type);
        String upperPath = "";
        boolean isWindows = StringUtil.isOsWindows();
        upperPath = isWindows ? "d:/fileUpload/" : "/usr/local/fileUpload/";
        String storeAddr = new StringBuffer(upperPath).append(fileName).toString();
        File storeFile = new File (storeAddr);
        boolean storeFlag = false;
        int fileId = fileServiceImpl.selectMaxId() + 1;
        UserFile userFile = new UserFile (fileId, memberId, storeAddr,
                stringUtil.formatStrTimeToDate(stringUtil.getCurrentTimeStr(), "yyyy-MM-dd HH:mm:ss"));
        userFile.setType(StringUtil.formatStrFileTypeToInt(type));
        boolean saveFileToDataSource = fileServiceImpl.saveFile(userFile);
        DemandList list = new DemandList(memberId, fileId);
        boolean saveDemandToDataSource = demandService.saveDemandList(list);
        try {
            multipartFile.transferTo(storeFile);
            storeFlag = true;
        }catch (Exception e) {
            storeFlag = false;
            e.printStackTrace();
        }
        if (!storeFlag)
            logger.error("文件存入磁盘失败");
        if (!saveFileToDataSource)
            logger.error("文件信息保存到数据库失败");
        if (!saveDemandToDataSource)
            logger.error("点播列表添加记录失败");
        if (storeFlag && saveFileToDataSource && saveDemandToDataSource) {
            map.put("fileId", fileId);
            Destination destination = new ActiveMQQueue("pushUrl");
            jmsProducer.sendMessage(destination, "fileId:" + fileId + "&fileType=" + type + "&fileName=" + fileName);
            map.put("success", true);
        }
        else {
            map.put("success", false);
            map.put("errMsg", "上传文件失败");
        }
        return map;
    }
    @RequestMapping("/getCurrentStation")
    @ResponseBody
    public JSONObject getCurrentStation () {
        JSONObject jsonObject = new JSONObject();
        List<Station> list = stationService.getCurrentStation(true);
        jsonObject.put("currentStation", list.size() > 0 ? list.get(0) : null);
        return jsonObject;
    }
    @RequestMapping("/saveDemand")
    @ResponseBody
    public Map<String, Object> saveDemand (DemandList list, String type, String play_url, HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean saveDemand = false;
        boolean saveToFileDb = false;
        list.setDemandTime(stringUtil.formatStrTimeToDate(stringUtil.getCurrentTimeStr(), "yyyy-MM-dd HH:mm:ss"));
        if (list.getFileId() != 0) {
            saveDemand = demandService.updateDemandList(list, false);
            UserFile userFile = new UserFile(play_url);
            userFile.setId(list.getFileId());
            userFile.setFileName(list.getMusicName());
            saveToFileDb = fileServiceImpl.updateFilePlayUrl(userFile);
        }else {
            int fileId = fileServiceImpl.selectMaxId() + 1;
            Member currentWxMember = (Member) session.getAttribute("currentWxMember");
            UserFile userFile = new UserFile (fileId, currentWxMember.getId(), null,
                    stringUtil.formatStrTimeToDate(stringUtil.getCurrentTimeStr(), "yyyy-MM-dd HH:mm:ss"));
            userFile.setType(Integer.parseInt(type));
            userFile.setPlay_url(play_url);
            userFile.setFileName(list.getMusicName());
            saveToFileDb = fileServiceImpl.saveFile(userFile);
            list.setMemberId(currentWxMember.getId());
            list.setFileId(fileId);
            saveDemand = demandService.saveDemandList(list);
        }
        if (!saveDemand)
            logger.error("保存到demandlist表失败");
        if (!saveToFileDb)
            logger.error("保存到userFile表失败");
        if (saveDemand && saveToFileDb)
            map.put("success", true);
        else {
            map.put("success", false);
            map.put("errMsg", "投稿失败，服务器出错");
        }
        return map;
    }
    @RequestMapping("/getSelfDemand")
    @ResponseBody
    public JSONObject getSelfDemand (HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        Member currentWxMember = (Member) session.getAttribute("currentWxMember");
        List<DemandList> list = demandService.getSelfDemand(currentWxMember.getId());
        JSONArray array = stringUtil.formatListToJson(list);
        jsonObject.put("demandList", array);
        List<Station> stationList = stationService.getCurrentStation(false);
        jsonObject.put("thisStation", stationList.size() > 0 ? stringUtil.formatListToJson(stationList) : null);
        return jsonObject;
    }
    @RequestMapping("/getFileIdByAddr")
    @ResponseBody
    public JSONObject getFileIdByAddr (String storeAddr) {
        JSONObject jsonObject = new JSONObject();
        UserFile tempUser = new UserFile(-1, -1, -1, storeAddr);
        List<UserFile> list = fileServiceImpl.getAllFiles(tempUser, new Page(1, 30));
        jsonObject.put("url", null != list && list.size() > 0 ? list.get(0).getId() : "");
        return jsonObject;
    }
    @RequestMapping("/wechat/saveListen")
    @ResponseBody
    public Map<String, Object> saveListen (listenHistory history) {
        Date listenTime = stringUtil.formatStrTimeToDate(stringUtil.getCurrentTimeStr(), "yyyy-MM-dd HH:mm:ss");
        history.setListenTime(listenTime);
        Map<String, Object> map = new HashMap<String, Object>();
        int flag = listenHistoryService.saveListen(history);
        switch (flag) {
            case 0 :
                map.put("success", false);
                logger.info("保存收听历史到数据库失败");
                break;
            default :
                map.put("success", true);
        }
        return map;
    }
    @RequestMapping("/wechat/delSelfHistory")
    @ResponseBody
    public Map<String, Object> delSelfHistory (String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        int flag = listenHistoryService.delSelfHistory(id);
        map.put("success", flag > 0 ? true : false);
        return map;
    }
    @RequestMapping("/app/updateImgHeader")
    @ResponseBody
    public Map<String, Object> updateImgHeader (@RequestParam(value = "imgHeader") MultipartFile multipartFile, int memberId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String fileName = multipartFile.getOriginalFilename();
        String type = fileName.substring(fileName.lastIndexOf("."));
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        String imageHeaderAddr = StringUtil.mkFileName(fileName, type);
        Member m = new Member();
        m.setId(memberId);
        imageHeaderAddr = StringUtil.isOsWindows() ? "d:/fileUpload/" + imageHeaderAddr : "/usr/local/fileUpload";
        m.setImageHeaderAddr(imageHeaderAddr);
        int flag = memberService.onlyRefreshImg(m);
        boolean toDisk = false;
        try {
            multipartFile.transferTo(new File(imageHeaderAddr));
            toDisk = true;
        }catch (Exception e) {
            toDisk =false;
            e.printStackTrace();
        }
        if (flag > 0 && toDisk)
            map.put("success", true);
        else
            map.put("success", false);
        if (flag == 0)
            logger.error("头像文件保存到数据库失败");
        if (!toDisk)
            logger.error("投降文件保存到磁盘失败");
        return map;
    }
    @RequestMapping("/app/updateMemberByCondition")
    @ResponseBody
    public Map<String, Object> updateMemberByCondition (Member member, String sex) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtil.isEmpty(sex))
            member.setSex(-1);
        int flat = memberService.updateMemberByCondition(member);
        map.put("success", flat > 0 ? true : false);
        return map;
    }
    @RequestMapping("/app/onlyUpdateStateOnDemand")
    @ResponseBody
    public Map<String, Object> onlyUpdateStateOnDemand (DemandList list) {
        Map<String, Object> map = new HashMap<String, Object>();
        int checkState = demandService.getStateById(list.getId());
        if (checkState == 1) {
            map.put("success", true);
            return map;
        }
        list.setState(1);
        boolean flag = demandService.updateDemandList(list, true);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailUserName);
            message.setSubject("学院广播台通知");
            Member m = new Member(list.getMemberId(), -1);
            List<Member> memberList = memberService.queryMemberAllOrSth(new Page(1, 30), m);
            UserFile userFile = fileServiceImpl.getFileById(list.getFileId());
            StringBuffer buffer = new StringBuffer();
            String text = buffer.append(memberList.get(0).getUserName()).append(EnumUtil.formatIntSexToStr(memberList.get(0).getSex())).append("士")
                    .append("给你分享了一").append(userFile.getType() == 0 ? "首".concat("" + (list.getMusicName() == null ? "歌" : "《".concat(list.getMusicName()).concat("》"))) : "篇文章")
                    .append("，请到微信搜索学院广播电台小程序收听").toString();
            message.setTo(list.getEmail());
            message.setText(text);
            javaMailSender.send(message);
            SimpleMailMessage message1 = new SimpleMailMessage();
            message1.setFrom(mailUserName);
            message1.setTo(memberList.get(0).getEmail());
            buffer = new StringBuffer();
            text = buffer.append("你分享给").append(list.getToSb()).append(userFile.getType() == 0 ? "的歌" : "的文章").
                    append("已经在播，请前往小程序收听").toString();
            message1.setSubject("学院广播台通知");
            message1.setText(text);
            javaMailSender.send(message1);
        }catch (Exception e) {
            e.printStackTrace();
        }
        map.put("success", flag);
        return map;
    }
}

