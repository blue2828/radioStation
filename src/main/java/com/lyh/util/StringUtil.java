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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
                    else if (fieldName.equals("userFile")) {
                            if (list.size() > 0) {
                                if (((DemandList)list.get(i)).getUserFile() != null) {
                                    tempJb.put("fileEntityId", ((DemandList)list.get(i)).getUserFile().getId());
                                    tempJb.put("storeAddr", !StringUtil.isEmpty((((DemandList)list.get(i)).getUserFile().getStoreAddr())) ? ((DemandList)list.get(i)).getUserFile().getStoreAddr() : "" );
                                    tempJb.put("play_url", (!StringUtil.isEmpty(((DemandList)list.get(i)).getUserFile().getPlay_url())) ? ((DemandList)list.get(i)).getUserFile().getPlay_url() : "");
                                    tempJb.put("type", EnumUtil.formatIntTypeToStr(((DemandList)list.get(i)).getUserFile().getType()));
                                }
                            }
                        }
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
                        else if (fieldName.equals("lastTimeMemberId")) {
                        List<Member> tempList = memberService.queryMemberAllOrSth(new Page(1, 30),
                                new Member(((Station)list.get(i)).getLastTimeMemberId(), -1));
                            tempJb.put("lastTimeMemberId", tempList.size() > 0 ? tempList.get(0).getUserName() : "");
                            tempJb.put("memberId", tempList.size() > 0 ? tempList.get(0).getId() : 0);
                         }else if (fieldName.equals("memberId") && (list.get(0).getClass()) == listenHistory.class) {
                            tempJb.put("memberId", memberService.queryMemberAllOrSth(new Page(1, 30),
                                new Member(((listenHistory)list.get(i)).getMemberId(), -1)).get(0).getUserName());
                         }else if (fieldName.equals("listenTime"))
                            tempJb.put("listenTime", this.formatTime(((listenHistory) list.get(i)).getListenTime(), "yyyy-MM-dd HH:mm:ss"));
                          else if (fieldName.equals("listenType"))
                            tempJb.put("listenType", EnumUtil.formatListenTypeToStr(((listenHistory) list.get(i)).getListenType()));
                          else if (fieldName.equals("uploadMemId")) {
                            List<Member> listMember = memberService.queryMemberAllOrSth(new Page(1, 30), new Member(((UserFile) list.get(i)).getUploadMemId(), -1));
                            tempJb.put("uploadMemId", listMember.size() > 0 ? listMember.get(0).getUserName() : "");
                         }else {
                            Object tempOb = this.getFieldValueByName(fieldName, list.get(i));
                                tempJb.put(fieldName, tempOb != null ? tempOb : "");
                         }
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
    public static String mkRecordName (String fileName, String type) {
        return fileName = fileName.concat("_").concat(new StringUtil().getCurrentTimeStr().replaceAll(" ", "_").replaceAll(":", "_")).concat(type);
    }
    public static boolean isOsWindows () {
        boolean flag = true;
        Properties properties = new Properties();
        String osName = properties.getProperty("os.name");
        flag = !StringUtil.isEmpty(osName) && osName.toLowerCase().indexOf("linux") > -1 ? false : true;
        return flag;
    }
    public static int formatStrFileTypeToInt (String type) {
        int result = 0;
        type = type.substring(type.indexOf(".") + 1);
        switch (type) {
            case "pdf" :
                result = 1;
                break;
            case "txt" :
                result = 1;
                break;
            default :
                String imgFilter = "jpeg|gif|jpg|png|bmp|pic|";
                String[] imgFilters = imgFilter.split("\\|");
                int count = 0;
                for (String temp : imgFilters) {
                    if (type.equalsIgnoreCase(temp)) {
                        result = 2;
                        break;
                    }
                    if (!type.equalsIgnoreCase(temp) && count == imgFilters.length - 1)
                        result = 0;
                    count ++;
                }
        }
        return result;
    }

    public JSONArray formatObToJson (ResultSet resultSet) {
        JSONArray array = new JSONArray();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                JSONObject temp = new JSONObject();
                for (int i = 1; i <= count; i ++) {
                    Object obj = resultSet.getObject(i);
                    if (obj instanceof Date)
                        this.formatTime((Date) obj, "yyyy-MM-dd HH:mm:ss");
                    temp.put(metaData.getColumnLabel(i), resultSet.getObject(i) == null ? "" : obj);
                }
                array.add(temp);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }
    public static void main(String[] args) {

        System.out.println(new StringUtil().getCurrentTimeStr());
    }
}
