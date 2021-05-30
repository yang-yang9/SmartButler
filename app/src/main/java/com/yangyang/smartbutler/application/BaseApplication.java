package com.yangyang.smartbutler.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;
import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.smartbutler.utils.StaticClass;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.application
 *   文件名：BaseApplication
 *   创建者：YangYang
 *   描述：Application
 */

public class BaseApplication extends Application {

    //创建
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //初始化Bmob
        //Bmob.initialize(this, StaticClass.BMOB_APP_ID);
        //设置BmobConfig
        BmobConfig config =new BmobConfig.Builder(this)
                .setApplicationId(StaticClass.BMOB_APP_ID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(500*1024)
                .build();
        //Bmob.getInstance().initConfig(config);
        Bmob.initialize(config);

        Bmob.resetDomain("http://yangsdk.waityousell.com/8/");
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=" + StaticClass.VOICE_KEY);

        //初始化百度地图API
        SDKInitializer.initialize(getApplicationContext());

        //初始化账本数据库
        DBManager.initDB(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
