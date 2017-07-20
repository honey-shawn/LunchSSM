package com.sihuatech.lunchorder.Tab01;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sihuatech.lunchorder.MainActivity;
import com.sihuatech.lunchorder.R;

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
    public TabOne(Context context){
        this.context = context;
    }
    public void initView(View view, Class<?> cls0,Class<?> cls1,Class<?> cls2){
        tv_menu = (TextView) view.findViewById(R.id.tv_menu);
        tv_person = (TextView) view.findViewById(R.id.tv_person);
        tv_relation = (TextView) view.findViewById(R.id.tv_relation);
        setListener(tv_menu,cls0);
        setListener(tv_person,cls1);
        setListener(tv_relation,cls2);
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
}
