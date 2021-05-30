package com.yangyang.flashlight;

import android.graphics.Color;
import android.view.View;

import com.yangyang.flashlight.ui.ColorLight;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight
 *   文件名：FlashlightActivity
 *   创建者：YangYang
 *   描述：手电筒主界面
 */
public class FlashlightActivity extends ColorLight {


    public void onClick_Controller(View view){
        hideAllUI();
        if (mCurrentUIType != UIType.UI_TYPE_MAIN){
            mUIMain.setVisibility(View.VISIBLE);
            mCurrentUIType = UIType.UI_TYPE_MAIN;
            mWarningLightFlicker = false;
            screenBrightness(mDefaultScreenBrightness / 255f);
            if (mBulbCrossFadeFlag){
                mDrawable.reverseTransition(0);
            }
            mBulbCrossFadeFlag = false;
            mPoliceState = false;
        } else {
            switch (mLastUIType){
                case UI_TYPE_FLASHLIGHT:
                    mUIFlashlight.setVisibility(View.VISIBLE);
                    mCurrentUIType = UIType.UI_TYPE_FLASHLIGHT;
                    break;
                case UI_TYPE_WARNINGLIGHT:
                    mUIWarningLight.setVisibility(View.VISIBLE);
                    mCurrentUIType = UIType.UI_TYPE_WARNINGLIGHT;
                    screenBrightness(1f);
                    new WarningLightThread().start();
                    break;
                case UI_TYPE_MORSE:
                    mUIMorse.setVisibility(View.VISIBLE);
                    mCurrentUIType = UIType.UI_TYPE_MORSE;
                    break;
                case UI_TYPE_BULB:
                    mUIBulb.setVisibility(View.VISIBLE);
                    mCurrentUIType = UIType.UI_TYPE_BULB;
                    break;
                case UI_TYPE_POLICE:
                    mUIPoliceLight.setVisibility(View.VISIBLE);
                    mCurrentUIType = UIType.UI_TYPE_POLICE;
                    new PoliceThread().start();
                    break;
                default:

                    break;
            }
        }
    }

    public void onClick_ToFlashlight(View view){
        hideAllUI();
        mUIFlashlight.setVisibility(View.VISIBLE);
        mLastUIType = UIType.UI_TYPE_FLASHLIGHT;
        mCurrentUIType = UIType.UI_TYPE_FLASHLIGHT;
    }

    public void onClick_ToWarningLight(View view){
        hideAllUI();
        mUIWarningLight.setVisibility(View.VISIBLE);
        mLastUIType = UIType.UI_TYPE_WARNINGLIGHT;
        mCurrentUIType = mLastUIType;
        screenBrightness(1f);
        new WarningLightThread().start();
    }

    public void onClick_ToMorse(View view){
        hideAllUI();
        mUIMorse.setVisibility(View.VISIBLE);
        mCurrentUIType = UIType.UI_TYPE_MORSE;
        mCurrentUIType = mLastUIType;
    }
    public void onClick_ToBulb(View view){
        hideAllUI();
        mUIBulb.setVisibility(View.VISIBLE);
        mHideTextViewBulb.hide();
        mHideTextViewBulb.setTextColor(Color.BLACK);

        mCurrentUIType = UIType.UI_TYPE_BULB;
        mCurrentUIType = mLastUIType;
    }

    public void onClick_ToColor(View view){
        hideAllUI();
        mUIColorLight.setVisibility(View.VISIBLE);
        screenBrightness(1f);
        mCurrentUIType = UIType.UI_TYPE_COLOR;
        mCurrentUIType = mLastUIType;

        mHideTextVewColorLight.setTextColor(Color.rgb(255-Color.red(mCurrentColorLight), 255-Color.green(mCurrentColorLight), 255-Color.blue(mCurrentColorLight)));

    }

    public void onClick_ToPoliceLight(View view){
        hideAllUI();
        mUIPoliceLight.setVisibility(View.VISIBLE);
        screenBrightness(1f);
        mCurrentUIType = UIType.UI_TYPE_POLICE;
        mCurrentUIType = mLastUIType;
        mHideTextviewPoliceLight.hide();
        new PoliceThread().start();
    }
}
