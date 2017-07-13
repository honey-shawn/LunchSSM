package com.sihuatech.sihuatechorder;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {
    private TextView tab_img01;
    private TextView tab_img02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        // 模块1
        tab_img01 = (TextView) findViewById(R.id.id_tab_01);
        tab_img01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        Tab1Activity.class);
                startActivity(intent);
            }
        });

        // 模块2
        tab_img02 = (TextView) findViewById(R.id.id_tab_02);
        tab_img02.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        Tab2Activity.class);
                startActivity(intent);
            }
        });

        // 模块3
        TextView tab_img03 = (TextView) findViewById(R.id.id_tab_03);
        tab_img03.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        Tab3Activity.class);
                startActivity(intent);
            }
        });
    }
}
