package com.loveplusplus.update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/6/8.
 */

public class Activity_update extends Activity {

    private TextView affirmTv;
    private TextView cancelTv;
    private TextView contentTv;
    private String apkUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.acitivty_update);
//
//        initView();
//        initData();

    }

    private void initView() {
//        //立即更新
//        affirmTv = (TextView) findViewById(R.id.affirmTv);
//        //稍后
//        cancelTv = (TextView) findViewById(R.id.cancelTv);
//        //内容
//        contentTv = (TextView) findViewById(R.id.contentTv);
//        //版本号
//
//        affirmTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!apkUrl.equals("")) {
//                    goToDownload(Activity_update.this, apkUrl);
//                    finish();
//                }
//            }
//        });
//
//        cancelTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    private void initData() {
//
//        Intent intent = getIntent();
//        String content = intent.getStringExtra("content");
//        apkUrl = intent.getStringExtra("apkUrl");
//        affirmTv.setText("立即升级");
//        cancelTv.setText("稍后再说");
//        contentTv.setText(Html.fromHtml(content));
    }


}
