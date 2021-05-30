package com.yangyang.flashlight.ui;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight.ui
 *   文件名：Bulb
 *   创建者：YangYang
 *   描述：灯泡
 */


import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;

import com.yangyang.flashlight.weight.HideTextView;
import com.yangyang.smartbutler.R;

public class Bulb extends Morse {
    protected boolean mBulbCrossFadeFlag;
    protected TransitionDrawable mDrawable;
    protected HideTextView mHideTextViewBulb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawable = (TransitionDrawable) iv_bulb.getDrawable();

        mHideTextViewBulb = findViewById(R.id.tv_flashlight_hide_bulb);

    }

    public void onClick_BulbCrossFade(View view){
        if (!mBulbCrossFadeFlag){
            mDrawable.startTransition(500);
            mBulbCrossFadeFlag = true;
            screenBrightness(1f);
        } else {
            mDrawable.reverseTransition(500);
            mBulbCrossFadeFlag = false;
            screenBrightness(mDefaultScreenBrightness);
        }
    }

}
