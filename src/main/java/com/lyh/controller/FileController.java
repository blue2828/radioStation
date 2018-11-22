package com.lyh.controller;

import com.lyh.entity.Page;
import com.lyh.entity.UserFile;
import com.lyh.service.IFileService;
import com.lyh.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
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
        result.put("data", new StringUtil().formatListToJson(list));
        result.put("count", list.size());
        result.put("msg", "");
        result.put("code", 0);
        return result;
    }
    @RequestMapping("/delFile")
    public Map<String, Object> delFile (int id){
        Map<String, Object> result = new HashMap<String, Object>();
        UserFile userFile = new UserFile();
        userFile.setType(-1);
        userFile.setUploadMemId(-1);
        userFile.setId(id);
        String fileName = fileServiceImpl.getAllFiles(userFile, new Page(1, 30)).get(0).getFileName();
        boolean delFlag = fileServiceImpl.delFile(id);
        java.io.File file = new java.io.File(fileName);
        if(delFlag){
            if(!file.exists()||file == null){
                result.put("success", false);
                result.put("msg", "文件不存在");
                System.err.println("文件不存在，但在数据库的当前文件信息已成功删除");
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
                System.err.println("文件不存在，在数据库的当前文件信息删除失败");
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
                    System.err.println("数据库信息删除失败，但是本地文件删除成功");
                }else{
                    result.put("success", false);
                    result.put("errMsg", "本地文件删除失败");
                    System.err.println("数据库信息删除失败，本地文件删除失败");
                }
            }
        }
        return result;
    }
    @RequestMapping("/previewPdf")
    public void previewDoc (HttpServletRequest request, Map<String, Object> map, HttpServletResponse response) {
        BufferedInputStream bufferedInputStream = null;
        FileInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        OutputStream out = null;
        response.setHeader("Content-Disposition", "attachment;fileName=test.pdf");
        response.setContentType("multipart/form-data");
        try {
            out = response.getOutputStream();
            inputStream = new FileInputStream(new File("d:/test.pdf"));
            bufferedInputStream = new BufferedInputStream((inputStream));
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
