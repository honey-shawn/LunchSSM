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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import static android.R.id.list;

public class RelationActivity extends Activity {
    public final static int RESULT_OK_Select = 200;//返回码：从选菜界面返回
    public final static int RESULT_OK_Detail = 201;//返回码:从订单详情页返回

    public final static int RESULT_REQUEST = 1;//请求码
    private String url = "http://10.0.2.2:8080/LunchSSM/person/detail";
    private ListView personListView;
    ArrayList<HashMap<String, Object>> mylist;
    JSONObject jsonObject;//最终返回的json订餐结果
    JSONObject personJson;//人json

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

    class MyItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> map = mylist.get(position);
            Object personId = map.get("id");
            Object name = map.get("name");
            Intent intent = new Intent(RelationActivity.this, RelationSelectActivity.class);
            intent.putExtra("id", personId.toString());
            intent.putExtra("name", name.toString());
            if(jsonObject!=null){
                if (!jsonObject.isNull(personId.toString())) {
                    Log.i("已存在的人员：", personId.toString());
                    try {
                        intent.putExtra("booked", jsonObject.getJSONArray(personId.toString()).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            startActivityForResult(intent, RESULT_REQUEST);
//            Log.i("id",personId.toString());
//            Log.i("name",name.toString());

        }
    }

    private void init() {
        personListView = (ListView) findViewById(R.id.RelationListView);
        mylist = new ArrayList<HashMap<String, Object>>();
        jsonObject = new JSONObject();

        et_freight = (EditText) findViewById(R.id.et_freight);
        et_free = (EditText) findViewById(R.id.et_free);
        et_gift = (EditText) findViewById(R.id.et_gift);
        book_submit = (ImageButton) findViewById(R.id.book_submit);
        book_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String free = et_free.getText().toString();
                String freight = et_freight.getText().toString();
                String gift = et_gift.getText().toString();

                if (free.equals("") || freight.equals("")) {
                    Toast.makeText(RelationActivity.this, "运费或满减未填写！", Toast.LENGTH_SHORT);
                    Log.i("提示：", "运费或满减未填写！");
                } else {

                    Intent intent = new Intent(RelationActivity.this, RelationDetailActivity.class);

                    intent.putExtra("free", free);//满减
                    intent.putExtra("freight", freight);//运费
                    Log.i("待处理的json：", jsonObject.toString());
                    if (!gift.equals("")) {
                        intent.putExtra("gift", gift);//红包

                    } else {
                        intent.putExtra("gift", 0);//红包
                        gift = "0";
                    }
                    double freight_num = Double.parseDouble(freight);//运费
                    double free_num = Double.parseDouble(free);//满减
                    double gift_num = Double.parseDouble(gift);//红包

                    JSONArray newDataArrray = new JSONArray();//新组建的json格式
                    JSONArray personIdArray = jsonObject.names();
                    try {
                        for (int i = 0; i < personIdArray.length(); i++) {

                            String personId = (String)personIdArray.get(i);
                            String personName = null;
                            Iterator<String> iterator = personJson.keys();
                            while (iterator.hasNext()) {
                                String keyId = iterator.next();
                                JSONObject tmp = (JSONObject) (personJson.get(keyId));
                                if(tmp.get("id").toString().equals(personId)){
                                    personName = tmp.get("name").toString();
                                    break;
                                }

                            }

                            JSONArray menuArray = (JSONArray) jsonObject.get(personId);
                            for(int j = 0;j<menuArray.length();j++){//遍历选得菜数组
                                JSONArray menuInfo = (JSONArray) menuArray.get(j);
                                JSONObject newItem = new JSONObject();//新json数组的元素
                                newItem.put("personId",personId);
                                newItem.put("menuId",menuInfo.get(0));
                                newItem.put("price",menuInfo.get(1));
                                newItem.put("menuName",menuInfo.get(2));
                                newItem.put("personName",personName);
                                newDataArrray.put(newItem);
                            }
                        }
                        Log.i("第一次构建的jsonArray：",newDataArrray.toString());
                        double cost = 0.0;//总价钱
                        for(int z = 0; z < newDataArrray.length();z++){
                            JSONObject tmp = (JSONObject) newDataArrray.get(z);
                            double price = Double.parseDouble(tmp.get("price").toString());
                            cost = cost + price;
                        }
                        Log.i("总价钱：",cost+"");
                        for(int h = 0; h < newDataArrray.length();h++){
                            JSONObject tmp = (JSONObject) newDataArrray.get(h);
                            double cur_price = Double.parseDouble(tmp.get("price").toString());
                            double end_cost = cost - free_num + freight_num - gift_num;//除去满减，红包，加上运费
                            Log.i("除去红包和满减加上运费之后的总价：",end_cost+"");
                            double price_end = (cur_price/cost)*end_cost;
                            Log.i("实付价格：",price_end+"");
                            DecimalFormat df = new DecimalFormat( "0.00");
                            tmp.put("priceEnd",df.format(price_end));
                            tmp.put("date",new Date().getTime());
                        }
                        Log.i("第二次构建的jsonArray：",newDataArrray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("data",newDataArrray.toString());
                    startActivityForResult(intent, RESULT_REQUEST);
                }
            }
        });
    }


    class InitListView extends AsyncTask<String, Void, String> {
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
                personJson = new JSONObject(result);
                Iterator<String> iterator = personJson.keys();
                while (iterator.hasNext()) {
                    String keyId = iterator.next();
//                    System.out.println("============"+keyId);
                    JSONObject tmp = (JSONObject) (personJson.get(keyId));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("img", R.drawable.tab_address_pressed);
                    map.put("name", tmp.get("name").toString());
//                    map.put("price", tmp.get("price").toString());
                    map.put("id", tmp.get("id").toString());
                    mylist.add(map);
                }
                //生成适配器，数组===》ListItem
                SimpleAdapter mSchedule = new SimpleAdapter(RelationActivity.this, //没什么解释
                        mylist,//数据来源
                        R.layout.tab01_person_item,//ListItem的XML实现

                        //动态数组与ListItem对应的子项
                        new String[]{"img", "name"},

                        //ListItem的XML文件里面的两个TextView ID
                        new int[]{R.id.img_person, R.id.personItemTitle/*,R.id.personItemText*/});
                //添加并且显示
                personListView.setAdapter(mSchedule);
                personListView.setOnItemClickListener(new MyItemClick());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //activity回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //resultCode就是在B页面中返回时传的parama，可以根据需求做相应的处理
        switch (requestCode) {
            case RESULT_REQUEST:
                if (resultCode == RESULT_OK_Select) {

                        String getReturnData = data.getStringExtra("relation");//获取返回结果
                        if(getReturnData != null){
                            try {
                                Log.i("返回订单：", getReturnData);
                                JSONObject tmp = new JSONObject(getReturnData);//封装成json
                                Iterator<String> iterator = tmp.keys();
                                while (iterator.hasNext()) {
                                    String keyId = iterator.next();
                                    JSONArray tmpName = (JSONArray) (tmp.get(keyId));
                                    jsonObject.put(keyId, tmpName);
                                }
                                Log.i("所有订餐:", jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                }else if (resultCode == RESULT_OK_Detail) {
                    String getReturnData = data.getStringExtra("detailReturn");//获取返回结果
                    if(getReturnData.equals("OK")){//数据提交成功，清除当前订餐json数据。
                        jsonObject = null;
                    }else if(getReturnData.equals("Failed")){//提交失败时，保持当前订餐信息；手动返回时，也保存；

                    }
                }
                break;
            default:
                break;
        }
    }

}
