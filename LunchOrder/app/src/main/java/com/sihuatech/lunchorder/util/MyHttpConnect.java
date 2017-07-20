package com.sihuatech.lunchorder.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by 10252 on 2017/7/13.
 */

public class MyHttpConnect {

    public static String httpGetRequest(String url){
        String json;

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);

        HttpClient myHttpClient=new DefaultHttpClient(httpParams);
        HttpGet get =new HttpGet(url);

        try {
            HttpResponse response = myHttpClient.execute(get);
            //int code = response.getStatusLine().getStatusCode();

            HttpEntity mHttpEntity =response.getEntity();
            json= EntityUtils.toString(mHttpEntity);
            Log.i("Http",json);

        }  catch (ClientProtocolException e) {
            return "ClientProtocolException";
        } catch (IOException e) {
            return "IOException";
        }
        return json;
    }

    public static int httpPostRequest(String url,String params){
        String json;
        // 构造请求的json串
        JSONObject para = new JSONObject();
        int statusCode = 0;//返回码
        try {
            para.put("data", params);
            StringEntity entity = new StringEntity(para.toString(), "utf-8");
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
            HttpConnectionParams.setSoTimeout(httpParams, 10000);
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post =new HttpPost(url);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            response.getStatusLine().getStatusCode();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return statusCode;
        }
    }
}
