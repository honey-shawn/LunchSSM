package com.sihuatech.util;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class Tools {
	 //获取请求体中的字符串(POST)
    public static String getBodyData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
        }
        return data.toString();
    }
}
