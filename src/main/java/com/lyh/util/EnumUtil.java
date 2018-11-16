package com.lyh.util;

import com.alibaba.fastjson.JSONArray;

public class EnumUtil {
    public static String formatIntSexToStr (int sex) {
        String str = "";
        switch (sex) {
            case 0 : str = "男";
                break;
            case 1 : str = "女";
                break;
        }
        return str;
    }
    public static String formatIntTypeToStr (int type) {
        String str = "";
        switch (type) {
            case 0 : str = "歌曲";
                break;
            case 1 : str = "文章";
                break;
        }
        return str;
    }
    public static String formatIntStateToStr (int state) {
        String str = "";
        switch (state) {
            case 0 : str = "未播";
                break;
            case 1 : str = "已播";
                break;
            case 2 : str = "被搁置";
                break;
        }
        return str;
    }
    public static String formatIntRoleToStr (int roleId) {
        String str = "";
        switch (roleId) {
            case 0 : str = "用户";
                break;
            case 1 : str = "主播";
                break;
            case 2 : str = "管理员";
                break;
        }
        return str;
    }

}
