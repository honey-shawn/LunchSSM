package com.sihuatech.lunchorder.Tab01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.sihuatech.lunchorder.R;
import com.sihuatech.lunchorder.util.MyHttpConnect;

public class RelationDetailActivity extends Activity {
    private ImageButton send_OK;
    private String url;
    String params;//http传递的实体内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab01_relation_detail);
        init();
    }

    private void init() {
        params = getIntent().getStringExtra("data");
        send_OK = (ImageButton)findViewById(R.id.send_OK);
        send_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 实体内容需要处理
                MyHttpConnect.httpPostRequest(url,params);
            }
        });
    }

}
