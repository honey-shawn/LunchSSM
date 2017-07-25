package com.sihuatech.lunchorder.Tab01;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sihuatech.lunchorder.R;
import com.sihuatech.lunchorder.util.MyHttpConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

public class RelationSelectActivity extends Activity {
    private String url = "http://10.0.2.2:8080/LunchSSM/menu/detail";
    private ListView menuListView;
    ArrayList<HashMap<String, Object>> mylist;
    JSONObject jsonObject;
    JSONArray jsonArray;//返回的json数组
    //TODO youwenti
    List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();//存储选中的数据
    String personId;//人名id
    String personName;//人名
    JSONArray bookedMenu;//当前已点的菜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab01_relation_select);
        init();
        new InitListView().execute(url);
    }

    private void init() {
        personId = getIntent().getStringExtra("id");
        personName = getIntent().getStringExtra("name");
        String menus = getIntent().getStringExtra("booked");
        if(menus!=null){
            Log.i("此人已点的菜单：",menus);
            try {
                bookedMenu = new JSONArray(menus);
                Log.i("array:",bookedMenu.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonObject = new JSONObject();
        jsonArray = new JSONArray();
        menuListView = (ListView) findViewById(R.id.SelectListView);
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
                    JSONObject tmp = (JSONObject)(jsonObject.get(keyId));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("name", tmp.get("name").toString());
                    map.put("id", tmp.get("id").toString());
                    map.put("price", tmp.get("price").toString());
                    mylist.add(map);
                }
                MyAdapter myAdapter = new MyAdapter(RelationSelectActivity.this,mylist);
                //添加并且显示
                menuListView.setAdapter(myAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public TextView name;
        public TextView price;
        public CheckBox checkBoxSelect;
    }

    class MyAdapter extends BaseAdapter{
        private LayoutInflater mInflater = null;
        private Context context;//用于接收传递过来的Context对象
        private ArrayList<HashMap<String, Object>> data;
        public MyAdapter(Context context,ArrayList<HashMap<String, Object>> data) {
            super();
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public int getCount() {
            // How many items are in the data set represented by this Adapter.(在此适配器中所代表的数据集中的条目数)
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.(获取数据集中与指定索引对应的数据项)
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // Get the row id associated with the specified position in the list.(取在列表中与指定索引对应的行id)
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //Get a View that displays the data at the specified position in the data set.
            //获取一个在数据集中指定索引的视图来显示数据
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.tab01_relation_select_item, null);
                holder.name = (TextView)convertView.findViewById(R.id.selectItemText);
                holder.price = (TextView)convertView.findViewById(R.id.selectItemPrice);

                holder.checkBoxSelect = (CheckBox) convertView.findViewById(R.id.checkBoxSelect);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.name.setText((String)data.get(position).get("name"));
            holder.price.setText((String)data.get(position).get("price"));
            //将已选的菜单复位
            if(bookedMenu!=null){
                String menuId = (String)data.get(position).get("id");
                String menuPrice = (String)data.get(position).get("price");
                String menuName = data.get(position).get("name").toString();
                for(int i=0;i<bookedMenu.length();i++){
                    try {
                        JSONArray tmp = new JSONArray(bookedMenu.get(i).toString());
//                        Log.i("tmp",tmp.toString());
                        if(tmp.get(0).equals(menuId)){
                            ArrayList<String> tmp_list = new ArrayList<String>();
                            holder.checkBoxSelect.setChecked(true);
                            tmp_list.add(menuId);
                            tmp_list.add(menuPrice);
                            tmp_list.add(menuName);
                            list.add(tmp_list);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            holder.checkBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("data", "发生变化" + isChecked);
                    JSONObject jsonObject;
                    int i= 0;
                    String menuId = data.get(position).get("id").toString();
                    String menuName = data.get(position).get("name").toString();
                    String menuPrice = data.get(position).get("price").toString();
                    ArrayList< String> tmp_list = new ArrayList< String>();
                    if(isChecked) {
                        tmp_list.add(menuId);
                        tmp_list.add(menuPrice);
                        tmp_list.add(menuName);
                        list.add(tmp_list);
                        Log.i("add list------",list.toString());
                    }else if(!isChecked){
                        Log.i("removing list------",list.toString());
                        for(ArrayList<String> tmp : list){
                            Log.i("remove:-------------",tmp.toString());
                            for(String str : tmp){
                                if(str.equals(menuId)){
                                    int index = list.indexOf(tmp);
                                    Log.i("index:",index+"");
                                    Log.i("tmp",tmp.toString());
                                    list.remove(index);
                                }
                            }
                        }
//                        list.remove(list.indexOf(menuId));
                        Log.i("removed list------",list.toString());
                    }

                }
            });
            return convertView;
        }
    }

    //监听返回键 不起作用，待测试
   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("relation", "first");
        setResult(RelationActivity.RESULT_OK, intent);
        RelationSelectActivity.this.finish();
    }*/

    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            try {
                if(list.size()==0){
                    Log.i("当前人名：",personName+"没有点餐！");
//                    intent.putExtra("relation", "");
                }else{
                    for(List<String> tmp_list : list){
//                    Log.i("tmp_list:",tmp_list.toString());
                        JSONArray tmp = new JSONArray();
                        for(String str : tmp_list){
//                        Log.i("str:",str);
                            tmp.put(str);
                        }
                        jsonArray.put(tmp);
                    }
                    jsonObject.put(personId,jsonArray);
                    Log.i("当前人名：",personName);
                    intent.putExtra("relation", jsonObject.toString());
                }
                setResult(RelationActivity.RESULT_OK_Select, intent);
                RelationSelectActivity.this.finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return true;
    }


}
