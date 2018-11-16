package com.lyh.controller;

import com.lyh.entity.File;
import com.lyh.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {
    @Autowired
    @Qualifier("fileServiceImpl")
    private IFileService fileServiceImpl;
    @RequestMapping("/getAllFiles")
    public Map<String, Object> getAllFiles(){
        List<Map<String, Object>> list = fileServiceImpl.getAllFiles();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("count", list.size());
        result.put("msg", "");
        result.put("code", 0);
        return result;
    }
    @RequestMapping("/delFile")
    public Map<String, Object> delFile (int id){
        Map<String, Object> result = new HashMap<String, Object>();
        String fileName = fileServiceImpl.queryFileName(id);
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
}
