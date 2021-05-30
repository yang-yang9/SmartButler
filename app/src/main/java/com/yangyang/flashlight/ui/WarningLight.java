package com.yangyang.flashlight.ui;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight.ui
 *   文件名：WarningLight
 *   创建者：YangYang
 *   描述：警报灯
 */


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.yangyang.smartbutler.R;

public class WarningLight extends Flashlight{
    protected boolean mWarningLightFlicker; //true:闪烁
    protected boolean mWarningLightState; //标记亮的灯的位置(上或下)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWarningLightFlicker = true;
    }


    //用多线程控制黄灯的上下位置
    public class WarningLightThread extends Thread{
        @Override
        public void run() {
            super.run();
            mWarningLightFlicker = true;
            while (mWarningLightFlicker){
                try {
                    Thread.sleep(300);
                    mWarningHandler.sendEmptyMessage(0);
                } catch (Exception e){

                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mWarningHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mWarningLightState){
                iv_warning_on.setImageResource(R.drawable.warning_light_on);
                iv_warning_off.setImageResource(R.drawable.warning_light_off);
                mWarningLightState = false;
            } else {
                iv_warning_on.setImageResource(R.drawable.warning_light_off);
                iv_warning_off.setImageResource(R.drawable.warning_light_on);
                mWarningLightState = true;
            }
        }
    };
}
