package com.lyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.Member;
import com.lyh.entity.Page;
import com.lyh.entity.Record;
import com.lyh.entity.Station;
import com.lyh.service.IRecordService;
import com.lyh.service.IStationService;
import com.lyh.service.message.JmsProducer;
import com.lyh.util.StringUtil;
import com.lyh.util.mediaUtil;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.net.util.IPAddressUtil;

import javax.jms.Destination;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
@CrossOrigin
@Scope("prototype")
public class RecordController {
    @Autowired
    @Qualifier("jmsProducer")
    private JmsProducer jmsProducer;
    @Autowired
    private IRecordService recordService;
    @Autowired
    private IStationService stationService;
    @Autowired
    private StringUtil stringUtil;

    private static Logger logger = LoggerFactory.getLogger(RecordController.class);
    @RequestMapping("/recordUpload")
    @ResponseBody
    public Map<String, Object> recordUpload (@RequestParam(value="file", required = false) MultipartFile multipartFile, HttpSession session) {
        String name = "";
        String fileName = "";
        String fileType = "";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (null != multipartFile) {
            fileName = multipartFile.getOriginalFilename();
            fileType = fileName.substring(fileName.lastIndexOf("."));
            fileName = StringUtil.mkRecordName("录音", fileType);
            String storeAddr = "d:/fileUpload/".concat(fileName);
            File file = new File(storeAddr);
            Member member = (Member) session.getAttribute("currentAppMember");
            List<Station> info = stationService.queryStation(new Page(1, 30));
            Date time = stringUtil.formatStrTimeToDate(stringUtil.getCurrentTimeStr(), "yyyy-MM-dd HH:mm:ss");
            Record record = new Record(info.get(0).getId(), member.getId(), fileName, storeAddr, time);
            boolean flag = recordService.saveRecord(record);
            try {
                multipartFile.transferTo(file);
                if (flag) {
                    resultMap.put("success", true);
                    Map<String, String> ids = (Map<String, String>) session.getAttribute("currentRecordDemands");
                    int thisId = recordService.getIdByStoreAddr(storeAddr);
                    StringBuffer strIds = new StringBuffer();
                    if (ids != null) {
                        Iterator it = ids.keySet().iterator();
                        int count = 0;
                        while (it.hasNext()) {
                            String key = (String) it.next();
                            if (count == ids.size() - 1)
                                strIds.append(ids.get(key));
                            else {
                                strIds.append(ids.get(key).concat(","));
                            }
                            count ++;
                        }
                        boolean updateDemandIds = recordService.updateDemandIds(thisId, strIds.toString());
                        if (updateDemandIds)
                            logger.info("更改点播id成功");
                        else
                            logger.info("更改点播id失败");
                    }
                    logger.info("录音文件地址及信息存入数据库成功");
                }else
                    logger.error("录音文件地址及信息存入数据库失败");
                Destination wx = new ActiveMQQueue("pushUrl");
                jmsProducer.sendMessage(wx, storeAddr);
                Destination destination = new ActiveMQQueue("broadcastMsg");
                jmsProducer.sendMessage(destination, storeAddr);
            }catch (Exception e) {
                e.printStackTrace();
                resultMap.put("success", false);
                resultMap.put("success", "录音上传失败");
                logger.error("录音文件存入磁盘失败");
            }
        }else {
            resultMap.put("success", false);
            resultMap.put("success", "录音文件未上传成功，没有上传的文件");
        }
        return resultMap;
    }
    @RequestMapping("/pushRecordStreamByName")
    public void pushRecordStreamByName (String fileName, HttpServletResponse response) {
        BufferedOutputStream bout = null;
        BufferedInputStream bin = null;
        OutputStream out = null;
        InputStream in = null;
        File file = null;
        try {
            String targetPath = fileName.replace("amr", "mp3");
            File temp = new File (targetPath);
            if (!temp.exists() || temp == null)
                mediaUtil.changeToMp3(fileName, targetPath);
            file = StringUtil.isEmpty(targetPath) ? null : new File(targetPath);
            long length = file.length();
            //response.addHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Type", "audio/mpeg");
            in = file == null ? null : new FileInputStream(file);
            bin = in == null ? null : new BufferedInputStream(in);
            out = response.getOutputStream();
            bout = new BufferedOutputStream(out);
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = bin.read(bytes)) != -1) {
                bout.write(bytes, 0, len);
            }
        }catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
                try {
                    if (null != in)
                        in.close();
                    if (null != bin)
                        bin.close();
                    if (null != out)
                        out.close();
                    if (null != bout)
                        bout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    @RequestMapping("/getAllRecordList")
    @ResponseBody
    public JSONObject getAllRecordList (String memberId) {
        JSONObject jsonObject = new JSONObject ();
        int id = 0;
        if (StringUtil.isEmpty(memberId))
            id = 0;
        else
            id = Integer.parseInt(memberId);
        List list = recordService.getAllRecordList(id);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ArrayList<JSONArray> list2 = (ArrayList<JSONArray>) it.next();
            for (JSONArray temp : list2) {
                jsonObject.put("recordList", temp);
            }
        }
        if (jsonObject.size() == 0)
            jsonObject.put("recordList", null);
        return jsonObject;
    }
}
