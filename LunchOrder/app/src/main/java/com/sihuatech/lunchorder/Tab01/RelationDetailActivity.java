package com.sihuatech.lunchorder.Tab01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sihuatech.lunchorder.R;
import com.sihuatech.lunchorder.util.Contants;
import com.sihuatech.lunchorder.util.MyHttpConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RelationDetailActivity extends Activity {
    private ImageButton send_OK;
    private String url = Contants.base_url + "/relation/add";
    String params;//http传递的实体内容
    private ListView relationDetailListView;
    ArrayList<HashMap<String, String>> mylist;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            if(val.equals("200")){
                Toast.makeText(RelationDetailActivity.this,"提交成功！",Toast.LENGTH_SHORT);
                Intent intent = new Intent();
                intent.putExtra("detailReturn", "OK");
                setResult(RelationActivity.RESULT_OK_Detail, intent);
                RelationDetailActivity.this.finish();
            }else{
                Toast.makeText(RelationDetailActivity.this,"提交失败，注意检查网络！",Toast.LENGTH_LONG);
            }
            // UI界面的更新等相关操作
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            int status = MyHttpConnect.httpPostRequest(url,params);
            // 在这里进行 http request.网络请求相关操作
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", status+"");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab01_relation_detail);
        init();
    }

    private void init() {
        mylist = new ArrayList<HashMap<String,String>>();
        params = getIntent().getStringExtra("data");
        Log.i("params:",params);

        /*JSONObject jsonObjectTmp = new JSONObject();
        try {

            JSONArray jsonArrayTmp = new JSONArray(params);
            jsonObjectTmp.put("data",jsonArrayTmp);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        send_OK = (ImageButton)findViewById(R.id.send_OK);
        send_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(networkTask).start();
            }
        });
        //初始化控件
        relationDetailListView = (ListView) findViewById(R.id.relationDetailListView);
        //初始化list
        try {

            JSONArray jsonArray = new JSONArray(params);
            for(int i=0;i<jsonArray.length();i++){
                HashMap<String,String> map = new HashMap<String,String>();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                map.put("personName",jsonObject.get("personName").toString());
                map.put("menuName",jsonObject.get("menuName").toString());
                map.put("price",jsonObject.get("price").toString());
                map.put("priceEnd",jsonObject.get("priceEnd").toString());
                mylist.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //初始化适配器
        SimpleAdapter mSchedule = new SimpleAdapter(RelationDetailActivity.this, //没什么解释
                mylist,//数据来源
                R.layout.tab01_relation_detail_item,//ListItem的XML实现

                //动态数组与ListItem对应的子项
                new String[] {"personName","menuName", "price","priceEnd"},

                //ListItem的XML文件里面的两个TextView ID
                new int[] {R.id.detailName,R.id.detailMenu,R.id.detailPrice,R.id.detailPriceEnd});
        //添加并且显示
        relationDetailListView.setAdapter(mSchedule);
    }

    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("detailReturn", "Failed");
            setResult(RelationActivity.RESULT_OK_Detail, intent);
            RelationDetailActivity.this.finish();
        }
        return true;
    }

}
