package com.yangyang.flashlight.ui;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight.ui
 *   文件名：PoliceLight
 *   创建者：YangYang
 *   描述：警灯
 */


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.yangyang.flashlight.weight.HideTextView;
import com.yangyang.smartbutler.R;

public class PoliceLight extends Bulb{
    protected boolean mPoliceState;
    protected HideTextView mHideTextviewPoliceLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHideTextviewPoliceLight = (HideTextView) findViewById(R.id.tv_flashlight_hide_police_light);

    }

    public class PoliceThread extends Thread {
        public void run() {
            mPoliceState = true;
            while(mPoliceState)
            {

                mHandler.sendEmptyMessage(Color.BLUE);
                sleepExt(100);
                mHandler.sendEmptyMessage(Color.BLACK);
                sleepExt(100);
                mHandler.sendEmptyMessage(Color.RED);
                sleepExt(100);
                mHandler.sendEmptyMessage(Color.BLACK);
                sleepExt(100);

            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            int color = message.what;
            mUIPoliceLight.setBackgroundColor(color);
        }
    };
}
