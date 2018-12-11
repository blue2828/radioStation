package com.lyh.controller;

import com.lyh.entity.Page;
import com.lyh.entity.UserFile;
import com.lyh.service.IFileService;
import com.lyh.service.message.JmsProducer;
import com.lyh.util.StringUtil;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileController {

    @Autowired
    @Qualifier("fileService")
    private IFileService fileServiceImpl;
    @Autowired
    private StringUtil stringUtil;
    private static Logger logger = LoggerFactory.getLogger(FileController.class);
    @RequestMapping("/getAllFiles")
    @ResponseBody
    /**
     * selectVersion 0:默认查询全部；1:条件查
     */
    public Map<String, Object> getAllFiles(UserFile userFile, Page page, int selectVersion){
        switch (selectVersion) {
            case 0 :
                userFile.setId(-1);
                userFile.setType(-1);
                userFile.setUploadMemId(-1);
                break;
        }
        List<UserFile> list = fileServiceImpl.getAllFiles(userFile, page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", stringUtil.formatListToJson(list));
        result.put("count", fileServiceImpl.countFile(userFile, page));
        result.put("msg", "");
        result.put("code", 0);
        return result;
    }
    @RequestMapping("/delFile")
    @ResponseBody
    public Map<String, Object> delFile (int id){
        Map<String, Object> result = new HashMap<String, Object>();
        UserFile userFile = new UserFile();
        userFile.setType(-1);
        userFile.setUploadMemId(-1);
        userFile.setId(id);
        String storeAddr = fileServiceImpl.getAllFiles(userFile, new Page(1, 30)).get(0).getStoreAddr();
        boolean delFlag = fileServiceImpl.delFile(id);
        java.io.File file = new java.io.File(storeAddr);
        if(delFlag){
            if(!file.exists() || file == null){
                result.put("success", false);
                result.put("msg", "文件不存在");
                logger.error("文件不存在，但在数据库的当前文件信息已成功删除");
            }else{
                boolean  localFlag = false;
                try{
                    localFlag = file.delete();
                }catch (Exception e){
                    e.printStackTrace();
                    localFlag = false;
                }
                if(localFlag){
                    result.put("success", true);
                }else{
                    result.put("success", false);
                    result.put("msg", "本地文件删除失败");
                }
            }
        }else{
            if ( !file.exists() || file == null){
                result.put("success", false);
                result.put("msg", "文件不存在");
                logger.error("文件不存在，在数据库的当前文件信息删除失败");
            }else{
                boolean  localFlag = false;
                try{
                    localFlag = file.delete();
                }catch (Exception e){
                    e.printStackTrace();
                    localFlag = false;
                }
                if (localFlag){
                    result.put("success", false);
                    result.put("msg", "本地文件删除成功, 但数据未删除成功");
                    logger.error("数据库信息删除失败，但是本地文件删除成功");
                }else{
                    result.put("success", false);
                    result.put("errMsg", "本地文件删除失败");
                    logger.error("数据库信息删除失败，本地文件删除失败");
                }
            }
        }
        return result;
    }
    @RequestMapping("/previewFile")
    public void previewFile (HttpServletRequest request, Map<String, Object> map, HttpServletResponse response, int fileId, boolean isMusic, boolean isTxt) {
        BufferedInputStream bufferedInputStream = null;
        FileInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        OutputStream out = null;
        UserFile userFile = new UserFile();
        userFile.setId(fileId);
        userFile.setUploadMemId(-1);
        userFile.setType(-1);
        List<UserFile> list = fileServiceImpl.getAllFiles(userFile, new Page(1, 30));
        //response.setContentType("multipart/form-data");
       // response.setHeader("Content-Disposition", "attachment;fileName=" + list.get(0).getStoreAddr().substring(list.get(0).getStoreAddr().indexOf("_") + 1));
        File file = new File(list.get(0).getStoreAddr());
        if (isMusic) {
            long length = file.length();
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", length + "");
            response.addHeader("Content-Type", "audio/mpeg;charset=UTF-8");
        }
        try {
            inputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream((inputStream));
            if (!isTxt){
                out = response.getOutputStream();
                outputStream = new BufferedOutputStream(out);
                int temp = 0;
                byte[] buffer = new byte[1024];
                while ((temp = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, temp);
                    outputStream.flush();
                }
            }
            else {
                BufferedReader reader = null;
                InputStreamReader inputStreamReader = null;
                response.setContentType("text/html;charset=utf-8");
                PrintWriter writer = null;
                try {
                    inputStreamReader = new InputStreamReader(inputStream, "gb2312");
                    reader = new BufferedReader(inputStreamReader);
                    char[] results = new char[1024];
                    reader.read(results);
                    String responseTxt = new String(results);
                    writer = response.getWriter();
                    if (responseTxt.endsWith("\u0000"))
                        responseTxt = responseTxt.substring(0, responseTxt.indexOf("\u0000"));
                    writer.println(responseTxt);
                    writer.flush();
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (null != writer) writer.close();
                    if (null != inputStreamReader)
                        inputStreamReader.close();
                }
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
    @RequestMapping("/getImgResolution")
    @ResponseBody
    public Map<String, Object> getImgResolution (int fileId) {
        Map<String, Object> result = new HashMap<String, Object> ();
        UserFile userFile = new UserFile();
        userFile.setId(fileId);
        userFile.setUploadMemId(-1);
        userFile.setType(-1);
        List<UserFile> list = fileServiceImpl.getAllFiles(userFile, new Page(1, 30));
        File file = new File(list.get(0).getStoreAddr());
        if (list.get(0).getType() == 2) {
            result.put("result", stringUtil.getImgResolution(file));
        }
        return result;
    }

}
