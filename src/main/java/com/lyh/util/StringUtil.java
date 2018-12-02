package com.lyh.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.*;
import com.lyh.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
@Component
public class StringUtil {
    @Autowired
    @Qualifier("memberService")
    private IMemberService memberService;

    public static boolean isEmpty(String str){
        if(str == null || "".equals(str))
            return true;
        else
            return false;
    }

    public JSONArray formatListToJson (List<?> list) {
        JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i ++) {
                JSONObject tempJb = new JSONObject();
                String[] fieldNames = this.getFieldName(list.get(i));
                for (String fieldName : fieldNames) {
                    if (fieldName.equals("state"))
                        tempJb.put("state", EnumUtil.formatIntStateToStr(((DemandList) list.get(i)).getState()));
                    else if (fieldName.equals("demandTime"))
                        tempJb.put("demandTime", this.formatTime(((DemandList) list.get(i)).getDemandTime(), "yyyy-MM-dd HH:mm:ss"));
                        else if (fieldName.equals("birthday"))
                            tempJb.put("birthday", this.formatTime(((Member) list.get(i)).getBirthday(), "yyyy-MM-dd"));
                        else if (fieldName.equals("sex"))
                            tempJb.put("sex", EnumUtil.formatIntSexToStr(((Member) list.get(i)).getSex()));
                        else if (fieldName.equals("createTime"))
                            tempJb.put("createTime", this.formatTime(((Record) list.get(i)).getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                        else if (fieldName.equals("stationState"))
                            tempJb.put("stationState", EnumUtil.formatIntStationStateToStr(((Station) list.get(i)).getStationState()));
                        else if (fieldName.equals("lastTimeBroadcast"))
                            tempJb.put("lastTimeBroadcast", this.formatTime(((Station) list.get(i)).getLastTimeBroadcast(), "yyyy-MM-dd HH:mm:ss"));
                        else if (fieldName.equals("uploadDate"))
                            tempJb.put("uploadDate", this.formatTime(((UserFile) list.get(i)).getUploadDate(), "yyyy-MM-dd HH:mm:ss"));
                        else if (fieldName.equals("roleId") && (list.get(i).getClass() == Member.class))
                            tempJb.put("roleId", EnumUtil.formatIntRoleToStr(((Member) list.get(i)).getRoleId()));
                        else if (fieldName.equals("type"))
                            tempJb.put("type", EnumUtil.formatIntTypeToStr(((UserFile) list.get(i)).getType()));
                        else if (fieldName.equals("lastTimeMemberId"))
                            tempJb.put("lastTimeMemberId", memberService.queryMemberAllOrSth(new Page(1, 30), new Member(((Station)list.get(i)).getLastTimeMemberId())).get(0).getUserName());
                            else
                                tempJb.put(fieldName, this.getFieldValueByName(fieldName, list.get(i)));
                }
                array.add(tempJb);
            }
        return array;
    }
    public Date formatStrTimeToDate (String date, String formatStr) {
        SimpleDateFormat spdf = new SimpleDateFormat(formatStr);
        Date result = null;
        try {
            if (formatStr != null) {
                result = spdf.parse(date);
            }
        }catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }
    public String formatTime (Date date, String formatStr) {
        String str = "";
        try {
            SimpleDateFormat spdf = new SimpleDateFormat(formatStr);
            if (date != null)
                str = spdf.format(date);
            else
                str = "";
        }catch (Exception e) {
            e.printStackTrace();
            str = "";
        }
        return str;
    }

    public String[] getFieldName (Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i ++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    public Object getFieldValueByName (String fieldName, Object o) {
        Object value = null;
        try {
            String firstLetter = fieldName.substring(0, 1);
            String getter = "get" + firstLetter.toUpperCase() + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            value = method.invoke(o, new Object[] {});
        }catch (Exception e) {
            value = null;
            e.printStackTrace();
        }
        return value;
    }
    public String getCurrentTimeStr () {
        String str = null;
        Calendar calendar = Calendar.getInstance();
        str = "" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        return str;
    }
    public String getImgResolution (File file) {
        String str = null;
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
            str = bufferedImage.getWidth() + "*" + bufferedImage.getHeight();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String trimRight (String arg) {
        String str = "";
        if (arg.matches("^[\\t\\n\\r\\s]")) {
            str = arg.substring(0, arg.indexOf(arg.trim().substring(0, 1)) + arg.trim().length());
        }else
            str = arg.trim();
        return str;
    }
    public String getRandomStr () {
        String result = "";
        String willSelected = "随意搞几个就行了，哈哈";
        char[] chars = willSelected.toCharArray();
        Random random = new Random();
        for (int i = 0; i < 4; i ++) {
            result += willSelected.charAt(random.nextInt(chars.length));
        }
        return result;
    }
    public static String mkFileName (String fileName, String type) {
        return fileName = fileName.concat("_").concat(UUID.randomUUID().toString().replace("-", "_")).concat(type);
    }
}
