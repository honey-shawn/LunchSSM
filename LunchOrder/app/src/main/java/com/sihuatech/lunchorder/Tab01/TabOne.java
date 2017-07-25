package com.sihuatech.lunchorder.Tab01;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sihuatech.lunchorder.MainActivity;
import com.sihuatech.lunchorder.R;
import com.sihuatech.lunchorder.util.MyHttpConnect;
import com.sihuatech.lunchorder.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.start;

/**
 * Created by 10252 on 2017/7/13.
 * tab01中的控件初始化和事件绑定
 */

public class TabOne {
    private Context context;
    private TextView tv_menu;
    private TextView tv_person;
    private TextView tv_relation;
    private TextView tv_nullNotification;//listvie为空提醒

    private ListView relationDailyNoListView;
    private ListView relationWeeklyNoListView;
    ArrayList<HashMap<String, String>> dailylist;
    ArrayList<HashMap<String, String>> weeklist;

    String dailyUrl = "http://10.0.2.2:8080/LunchSSM/relation/detail";//TODO
    String weekUrl = "http://10.0.2.2:8080/LunchSSM/relation/detail";//TODO

    public TabOne(Context context){
        this.context = context;
    }

    public void initView(View view, Class<?> cls0,Class<?> cls1,Class<?> cls2){
        tv_menu = (TextView) view.findViewById(R.id.tv_menu);
        tv_person = (TextView) view.findViewById(R.id.tv_person);
        tv_relation = (TextView) view.findViewById(R.id.tv_relation);
        tv_nullNotification = (TextView)view.findViewById(R.id.tv_nullNotification);

        relationDailyNoListView = (ListView) view.findViewById(R.id.relationDailyNoListView);
        relationWeeklyNoListView = (ListView)view.findViewById(R.id.relationWeeklyNoListView);
        setListener(tv_menu,cls0);
        setListener(tv_person,cls1);
        setListener(tv_relation,cls2);
        new DailyInitListView().execute(dailyUrl);
        new WeekInitListView().execute(weekUrl);
    }
    public void setListener(View view, final Class<?> cls){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context,cls);
                context.startActivity(intent);
            }
        });
    }
    //Item点击事件监听
    class MyItemClick implements AdapterView.OnItemClickListener {
        ArrayList<HashMap<String, String>> mylist;
        public MyItemClick(ArrayList<HashMap<String, String>> mylist){
            mylist = this.mylist;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> map = mylist.get(position);
//            String relationId = map.get("id");
            //TODO 对话框提示删除
            Log.i("点击了。。。：",map.get("menuName"));
            /*Intent intent = new Intent(context, RelationSelectActivity.class);
            startActivityForResult(intent, RESULT_REQUEST);*/
        }
    }

    //加载数据
    class DailyInitListView extends AsyncTask<String,Void,String> {
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
            dailylist = initList(result);
            //初始化适配器
            MyAdapter myAdapter = new MyAdapter(context,dailylist);
            //添加并且显示
            relationDailyNoListView.setAdapter(myAdapter);
            relationDailyNoListView.setOnItemClickListener(new MyItemClick(dailylist));
        }
    }

    //加载数据
    class WeekInitListView extends AsyncTask<String,Void,String> {
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
            Log.i("re0...................:",result);
            if(result.equals("{}")){
                tv_nullNotification.setVisibility(View.VISIBLE);
                relationWeeklyNoListView.setVisibility(View.GONE);
            }
            //请求数据完毕，更新UI
            weeklist = initList(result);
            //初始化适配器
            MyAdapter myAdapter = new MyAdapter(context,weeklist);
            //添加并且显示
            relationWeeklyNoListView.setAdapter(myAdapter);
            relationWeeklyNoListView.setOnItemClickListener(new MyItemClick(weeklist));
        }
    }

    //初始化数据
    public ArrayList<HashMap<String, String>> initList(String result){
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String keyId =  iterator.next();
                JSONObject tmp = (JSONObject)(jsonObject.get(keyId));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("personName", tmp.get("personName").toString());
                map.put("id", tmp.get("id").toString());
                map.put("menuName", tmp.get("menuName").toString());
                map.put("priceEnd", tmp.get("priceEnd").toString());
                //TODO 时间需要处理,改成周几
                Long time = Long.parseLong(tmp.get("time").toString());
                Date date = new Date(time);
                String week_cn = Tools.changeToWeek(date.getDay());
                Log.i("周几：",week_cn);

                map.put("time", week_cn);//
                list.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return list;
        }
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public TextView personName;
        public TextView time;
        public TextView menuName;
        public TextView priceEnd;
    }

    class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        private Context context;//用于接收传递过来的Context对象
        private ArrayList<HashMap<String, String>> data;
        public MyAdapter(Context context,ArrayList<HashMap<String, String>> data) {
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
                convertView = mInflater.inflate(R.layout.tab01_relation_detail_item, null);
                holder.personName = (TextView)convertView.findViewById(R.id.detailName);
                holder.menuName = (TextView)convertView.findViewById(R.id.detailMenu);
                holder.time = (TextView)convertView.findViewById(R.id.detailPrice);
                holder.priceEnd = (TextView)convertView.findViewById(R.id.detailPriceEnd);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.personName.setText((String)data.get(position).get("personName"));
            holder.menuName.setText((String)data.get(position).get("menuName"));
            holder.time.setText((String)data.get(position).get("time"));
            holder.priceEnd.setText((String)data.get(position).get("priceEnd"));
            return convertView;
        }
    }
}
