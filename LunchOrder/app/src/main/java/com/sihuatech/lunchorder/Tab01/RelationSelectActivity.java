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

import static android.R.id.list;

public class RelationSelectActivity extends Activity {
    private String url = "http://10.0.2.2:8080/LunchSSM/person/detail";
    private ListView personListView;
    ArrayList<HashMap<String, Object>> mylist;
    JSONObject jsonObject;
    JSONArray jsonArray;//返回的json数组
    List<String> list = new ArrayList<String>();//存储选中的数据
    String menuId;//菜名id
    String menuName;//菜名
    JSONArray bookedPerson;//已点当前菜的人员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab01_relation_select);
        init();
        new InitListView().execute(url);
    }

    private void init() {
        menuId = getIntent().getStringExtra("id");
        menuName = getIntent().getStringExtra("name");
        String persons = getIntent().getStringExtra("booked");
        if(persons!=null){
            Log.i("点此餐的人员：",persons);
            try {
                bookedPerson = new JSONArray(persons);
                Log.i("array:",bookedPerson.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonObject = new JSONObject();
        jsonArray = new JSONArray();
        personListView = (ListView) findViewById(R.id.SelectListView);
        mylist = new ArrayList<HashMap<String, Object>>();;
    }

    /*class MyItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> map = mylist.get(position);
            Object name = map.get("name");
            Object personId = map.get("id");
            Log.i("name",name.toString());
            

        }
    }*/

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
                    mylist.add(map);
                }
                //生成适配器，数组===》ListItem
                /*SimpleAdapter mSchedule = new SimpleAdapter(RelationSelectActivity.this, //没什么解释
                        mylist,//数据来源
                        R.layout.tab01_relation_select_item,//ListItem的XML实现

                        //动态数组与ListItem对应的子项
                        new String[] {"name"},

                        //ListItem的XML文件里面的两个TextView ID
                        new int[] {R.id.selectItemText});*/
                MyAdapter myAdapter = new MyAdapter(RelationSelectActivity.this,mylist);
                //添加并且显示
                personListView.setAdapter(myAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public TextView name;
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
                holder.checkBoxSelect = (CheckBox) convertView.findViewById(R.id.checkBoxSelect);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.name.setText((String)data.get(position).get("name"));
            //将已选的人员复位
            if(bookedPerson!=null){
                String personId = (String)data.get(position).get("id");
                for(int i=0;i<bookedPerson.length();i++){
                    try {
                        if(bookedPerson.get(i).equals(personId)){
                            holder.checkBoxSelect.setChecked(true);
                            list.add(personId);
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
                    String personId = data.get(position).get("id").toString();
                    String personName = data.get(position).get("name").toString();
                    if(isChecked) {
                        list.add(personId);
                        Log.i("id:-------------",personId);
                        Log.i("add:-------------",personName);
                        Log.i("list------",list.toString());
                    }else if(!isChecked){
                        Log.i("remove:-------------",personName);
                        list.remove(list.indexOf(personId));
                        Log.i("list------",list.toString());
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
        //TODO
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
                for(String str : list){
                    jsonArray.put(str);
                }
                jsonObject.put(menuId,jsonArray);
                Log.i("当前菜名：",menuName);
                intent.putExtra("relation", jsonObject.toString());
                setResult(RelationActivity.RESULT_OK, intent);
                RelationSelectActivity.this.finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return true;
    }


}
