package com.yangyang.smartbutler.ui;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：ShareUtils
 *   创建者：YangYang
 *   描述：闪屏页
 *          1.延时2s
 *          2.判断程序是否是第一次运行
 *          3.自定义字体
 *          4.Activity全屏
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.utils.ShareUtils;
import com.yangyang.smartbutler.utils.StaticClass;
import com.yangyang.smartbutler.utils.UtilTools;

public class SplashActivity extends AppCompatActivity {

    private TextView tv_splash;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case StaticClass.DELAYED_SPLASH:
                    if (isFirst()){
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();

                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    private void initView() {

        handler.sendEmptyMessageDelayed(StaticClass.DELAYED_SPLASH, 2000);
        tv_splash = findViewById(R.id.tv_splash);

        //设置闪屏页字体

        UtilTools.setFontBaiZhou(SplashActivity.this, tv_splash);
    }


    //判断程序是否首次运行
    private boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this, StaticClass.IS_FIRST, true);
        if (isFirst){
            ShareUtils.putBoolean(this, StaticClass.IS_FIRST, false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //禁止返回键
        //super.onBackPressed();
    }
}
