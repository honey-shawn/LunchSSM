package com.sihuatech.lunchorder.Tab01;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sihuatech.lunchorder.R;
import com.sihuatech.lunchorder.util.MyHttpConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.R.id.list;

public class RelationActivity extends Activity {
    public final static int RESULT_OK  = 200;//返回码
    public final static int RESULT_REQUEST  = 1;//请求码
    private String url = "http://10.0.2.2:8080/LunchSSM/menu/detail";
    private ListView menuListView;
    ArrayList<HashMap<String, Object>> mylist;
    //TODO 最终返回时，需计算最终价格一并返回
    JSONObject jsonObject;//最终返回的json订餐结果
    JSONObject menuJson;//菜单json

    private ImageButton book_submit;//跳转至订单详情页
    private EditText et_freight;//运费
    private EditText et_free;//满减
    private EditText et_gift;//红包



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab01_relation);
        init();
        new InitListView().execute(url);

    }

    class MyItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> map = mylist.get(position);
            Object menuId = map.get("id");
            Object name = map.get("name");
//            LauncherActivity.ListItem listitem = (LauncherActivity.ListItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(RelationActivity.this,RelationSelectActivity.class);
            intent.putExtra("id",menuId.toString());
            intent.putExtra("name",name.toString());
            if(!jsonObject.isNull(menuId.toString())){
                Log.i("已存在的菜单：",menuId.toString());
                try {
                    intent.putExtra("booked",jsonObject.getJSONArray(menuId.toString()).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            startActivityForResult(intent,RESULT_REQUEST);
            Log.i("id",menuId.toString());
            Log.i("name",name.toString());

        }
    }

    private void init() {
        menuListView = (ListView) findViewById(R.id.RelationListView);
        mylist = new ArrayList<HashMap<String, Object>>();
        jsonObject = new JSONObject();

        et_freight = (EditText)findViewById(R.id.et_freight);
        et_free = (EditText)findViewById(R.id.et_free);
        et_gift = (EditText)findViewById(R.id.et_gift);
        book_submit = (ImageButton)findViewById(R.id.book_submit);
        book_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String free = et_free.getText().toString();
                String freight = et_freight.getText().toString();
                String gift = et_gift.getText().toString();

                if(free.equals("")||freight.equals("")){
                    Toast.makeText(RelationActivity.this,"运费或满减未填写！",Toast.LENGTH_SHORT);
                }else{

                    Intent intent = new Intent(RelationActivity.this,RelationDetailActivity.class);
                    intent.putExtra("data",jsonObject.toString());
                    intent.putExtra("free",free);//满减
                    intent.putExtra("gift",gift);//红包
                    intent.putExtra("freight",freight);//运费
                    Log.i("待处理的json：",jsonObject.toString());

                    startActivity(intent);
                }


            }
        });
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
                 menuJson = new JSONObject(result);
                Iterator<String> iterator = menuJson.keys();
                while (iterator.hasNext()) {
                    String keyId =  iterator.next();
//                    System.out.println("============"+keyId);
                    JSONObject tmp = (JSONObject)(menuJson.get(keyId));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("img", R.drawable.tab_address_pressed);
                    map.put("name", tmp.get("name").toString());
                    map.put("price", tmp.get("price").toString());
                    map.put("id", tmp.get("id").toString());
                    mylist.add(map);
                }
                //生成适配器，数组===》ListItem
                SimpleAdapter mSchedule = new SimpleAdapter(RelationActivity.this, //没什么解释
                        mylist,//数据来源
                        R.layout.tab01_menu_item,//ListItem的XML实现

                        //动态数组与ListItem对应的子项
                        new String[] {"img","name", "price"},

                        //ListItem的XML文件里面的两个TextView ID
                        new int[] {R.id.img_menu,R.id.menuItemTitle,R.id.menuItemText});
                //添加并且显示
                menuListView.setAdapter(mSchedule);
                menuListView.setOnItemClickListener(new MyItemClick());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //activity回调函数
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode,  data);
        //resultCode就是在B页面中返回时传的parama，可以根据需求做相应的处理
        switch (requestCode){
            case RESULT_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        String getReturnData = data.getStringExtra("relation");//获取返回结果
                        Log.i("返回订单：",getReturnData);
                        JSONObject tmp = new JSONObject(getReturnData);//封装成json

                        Iterator<String> iterator = tmp.keys();
                        while (iterator.hasNext()) {
                            String keyId = iterator.next();
                            JSONArray tmpName = (JSONArray) (tmp.get(keyId));
                            jsonObject.put(keyId,tmpName);
                        }
                        Log.i("所有订餐:",jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
                    break;
        }
    }

}
