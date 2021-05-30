package com.yangyang.flashlight.ui;

import android.hardware.Camera;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yangyang.smartbutler.R;


/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight.ui
 *   文件名：BaseActivity
 *   创建者：YangYang
 *   描述：手电筒基类
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    public void onClick(View v) {

    }

    protected enum UIType{
        UI_TYPE_MAIN, UI_TYPE_FLASHLIGHT, UI_TYPE_WARNINGLIGHT,
        UI_TYPE_MORSE, UI_TYPE_BULB, UI_TYPE_COLOR, UI_TYPE_POLICE,
        UI_TYPE_SETTINGS
    }

    protected ImageView mImageViewFlashlight;
    protected ImageView mImageViewFlashlightController;
    protected Camera mCamera;
    protected android.hardware.Camera.Parameters mParameters;
    protected ImageView mImageViewSetting;

    //UI界面
    protected FrameLayout mUIFlashlight;
    protected LinearLayout mUIMain;
    protected LinearLayout mUIWarningLight;
    protected LinearLayout mUIMorse;
    protected FrameLayout mUIBulb;
    protected FrameLayout mUIColorLight;
    protected FrameLayout mUIPoliceLight;

    protected UIType mCurrentUIType = UIType.UI_TYPE_FLASHLIGHT;
    protected UIType mLastUIType = UIType.UI_TYPE_FLASHLIGHT;

    //warning light
    protected ImageView iv_warning_on;
    protected ImageView iv_warning_off;

    //屏幕默认亮度
    protected int mDefaultScreenBrightness;

    protected EditText et_morse_code;
    protected Button btn_send_morse;

    protected ImageView iv_bulb;

    protected int mFinishCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);

        initView();
        mDefaultScreenBrightness = getScreenBrightness();
    }

    protected void initView() {
        mImageViewFlashlight = findViewById(R.id.iv_flashlight_img);
        mImageViewFlashlightController = findViewById(R.id.iv_flashlight_controller);

        mUIFlashlight = findViewById(R.id.fl_flashlight);
        mUIMain = findViewById(R.id.ll_flashlight_main);
        mUIWarningLight = findViewById(R.id.ll_flashlight_warning_light);
        mUIMorse = findViewById(R.id.ll_flashlight_morse);
        mUIBulb = findViewById(R.id.fl_flashlight_bulb);
        mUIColorLight = findViewById(R.id.fl_flashlight_color);
        mUIPoliceLight = findViewById(R.id.fl_flashlight_police_light);

        iv_warning_on = findViewById(R.id.iv_warning_light_on);
        iv_warning_off = findViewById(R.id.iv_warning_light_off);

        et_morse_code = findViewById(R.id.et_flashlight_morse_code);
        btn_send_morse = findViewById(R.id.btn_flashlight_send_morse);

        iv_bulb = findViewById(R.id.iv_flashlight_bulb);
    }


    protected void hideAllUI(){
        mUIFlashlight.setVisibility(View.GONE);
        mUIMain.setVisibility(View.GONE);
        mUIWarningLight.setVisibility(View.GONE);
        mUIMorse.setVisibility(View.GONE);
        mUIBulb.setVisibility(View.GONE);
        mUIColorLight.setVisibility(View.GONE);
        mUIPoliceLight.setVisibility(View.GONE);
    }

    //value:屏幕亮度---1最亮，0最暗
    protected void screenBrightness(float value){
        try {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.screenBrightness = value;
            getWindow().setAttributes(params);
        } catch (Exception e){

        }
    }

    protected int getScreenBrightness(){
        int value = 0;
        try {
            value = android.provider.Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e){

        }
        return value;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mFinishCount = 0;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        mFinishCount++;
        if (mFinishCount == 1){
            Toast.makeText(this, "再按一次退出手电筒", Toast.LENGTH_LONG).show();
        } else if (mFinishCount == 2){
            super.finish();
        }
    }
}
