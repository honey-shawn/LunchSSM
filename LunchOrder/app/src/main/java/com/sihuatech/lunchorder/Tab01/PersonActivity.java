package com.sihuatech.lunchorder.Tab01;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sihuatech.lunchorder.R;
import com.sihuatech.lunchorder.util.MyHttpConnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.R.id.list;

public class PersonActivity extends Activity {
    private String url = "http://10.0.2.2:8080/LunchSSM/person/detail";
    private ListView personListView;
    ArrayList<HashMap<String, Object>> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab01_person);
        init();
        new InitListView().execute(url);
    }

    private void init() {
        personListView = (ListView) findViewById(R.id.PersonListView);
        mylist = new ArrayList<HashMap<String, Object>>();;
    }


    class InitListView extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //准备工作，显示加载进度条
        }

        @Override
        protected String doInBackground(String... params) {
            //后台线程
            MyHttpConnect myHttpConnect = new MyHttpConnect();
            return myHttpConnect.httpGetRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //请求数据完毕，更新UI
            try {
                JSONObject jsonObject = new JSONObject(result);
                Iterator<String> iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String keyId =  iterator.next();
                    System.out.println("============"+keyId);
                    JSONObject tmp = (JSONObject)(jsonObject.get(keyId));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("img", R.drawable.tab_find_frd_pressed);
                    map.put("name", tmp.get("name").toString());
                    map.put("enable", tmp.get("enable").toString());
                    mylist.add(map);
                }
                //生成适配器，数组===》ListItem
                SimpleAdapter mSchedule = new SimpleAdapter(PersonActivity.this, //没什么解释
                        mylist,//数据来源
                        R.layout.tab01_person_item,//ListItem的XML实现

                        //动态数组与ListItem对应的子项
                        new String[] {"img","name", "enable"},

                        //ListItem的XML文件里面的两个TextView ID
                        new int[] {R.id.img_person,R.id.personItemTitle,R.id.personItemText});
                //添加并且显示
                personListView.setAdapter(mSchedule);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
