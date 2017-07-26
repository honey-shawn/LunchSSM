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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

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

    /**
     * post请求，返回json数据
     * @param url
     * @param content
     * @return StringJson
     */
    public static String postGetJson(String url, String content) {
        try {
            URL mUrl = new URL(url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            //设置链接超时时间
            mHttpURLConnection.setConnectTimeout(15000);
            //设置读取超时时间
            mHttpURLConnection.setReadTimeout(15000);
            //设置请求参数
            mHttpURLConnection.setRequestMethod("POST");
            //添加Header
//            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            //接收输入流
            mHttpURLConnection.setDoInput(true);
            //传递参数时需要开启
            mHttpURLConnection.setDoOutput(true);
            //Post方式不能缓存,需手动设置为false
            mHttpURLConnection.setUseCaches(false);

            mHttpURLConnection.connect();

            DataOutputStream dos = new DataOutputStream(mHttpURLConnection.getOutputStream());

            String postContent = content;

            dos.write(postContent.getBytes());
            dos.flush();
            // 执行完dos.close()后，POST请求结束
            dos.close();

            // 获取代码返回值
            int respondCode = mHttpURLConnection.getResponseCode();
            Log.i("respondCode","respondCode="+respondCode );
            // 获取返回内容类型
            String type = mHttpURLConnection.getContentType();
            Log.i("type", "type="+type);
            // 获取返回内容的字符编码
            String encoding = mHttpURLConnection.getContentEncoding();
            Log.i("encoding", "encoding="+encoding);
            // 获取返回内容长度，单位字节
            int length = mHttpURLConnection.getContentLength();
            Log.i("length", "length=" + length);
//            // 获取头信息的Key
//            String key = mHttpURLConnection.getHeaderField(idx);
//            Log.d("key", "key="+key);
            // 获取完整的头信息Map
            Map<String, List<String>> map = mHttpURLConnection.getHeaderFields();
            if (respondCode == 200) {
                // 获取响应的输入流对象
                InputStream is = mHttpURLConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                String msg = new String(message.toByteArray());
                Log.d("Common", msg);
                return msg;
            }
            return "finish";
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * post请求，返回状态码
     * @param url
     * @param params
     * @return 状态码
     */
    public static int httpPostRequest(String url,String params){
        String json;
        // 构造请求的json串
        int statusCode = 0;//返回码
        try {
            StringEntity entity = new StringEntity(params, "utf-8");
//            HttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
//            HttpConnectionParams.setSoTimeout(httpParams, 10000);
            HttpClient client = new DefaultHttpClient();//httpParams
            HttpPost post =new HttpPost(url);
            System.out.println("post.setEntity");
            post.setEntity(entity);
            System.out.println("client.execute(post)");
            HttpResponse response = client.execute(post);
            System.out.println("response:");
            System.out.println(response);
            statusCode = response.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return statusCode;
        }
    }
}
