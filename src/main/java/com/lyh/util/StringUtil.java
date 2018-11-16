package com.lyh.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyh.entity.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StringUtil {

    public static boolean isEmpty(String str){
        if(str==null||"".equals(str))return true;
        else return false;
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
                            tempJb.put("stationState", EnumUtil.formatIntSexToStr(((Station) list.get(i)).getStationState()));
                        else if (fieldName.equals("lastTimeBroadcast"))
                            tempJb.put("lastTimeBroadcast", this.formatTime(((Station) list.get(i)).getLastTimeBroadcast(), "yyyy-MM-dd HH:mm:ss"));
                        else if (fieldName.equals("uploadDate"))
                            tempJb.put("uploadDate", this.formatTime(((UserFile) list.get(i)).getUploadDate(), "yyyy-MM-dd HH:mm:ss"));
                        else if (fieldName.equals("roleId") && (list.get(i).getClass() == Member.class))
                            tempJb.put("roleId", EnumUtil.formatIntRoleToStr(((Member) list.get(i)).getRoleId()));
                            else
                                tempJb.put(fieldName, this.getFieldValueByName(fieldName, list.get(i)));
                }
                array.add(tempJb);
            }
        //}

        return array;
    }

    public String formatTime (Date date, String formatStr) {
        String str = "";
        try {
            SimpleDateFormat spdf = new SimpleDateFormat(formatStr);
            str = spdf.format(date);
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
}
