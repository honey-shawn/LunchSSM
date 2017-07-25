package com.sihuatech.lunchorder.util;

/**
 * Created by 10252 on 2017/7/25.
 */

public class Tools {
    public static String changeToWeek(int value){
        String result = null;
        switch (value){
            case 1:
                result = "周一";
                break;
            case 2:
                result = "周二";
                break;
            case 3:
                result = "周三";
                break;
            case 4:
                result = "周四";
                break;
            case 5:
                result = "周五";
                break;
            case 6:
                result = "周六";
                break;
            case 7:
                result = "周日";
                break;
        }
        return result;
    }
}
