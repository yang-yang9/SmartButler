package com.yangyang.flashlight.ui;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight.ui
 *   文件名：ColorLight
 *   创建者：YangYang
 *   描述：彩色灯
 */


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.yangyang.flashlight.weight.ColorPickerDialog;
import com.yangyang.flashlight.weight.HideTextView;
import com.yangyang.smartbutler.R;

public class ColorLight extends PoliceLight implements ColorPickerDialog.OnColorChangedListener {
    protected int mCurrentColorLight = Color.BLUE;
    protected HideTextView mHideTextVewColorLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHideTextVewColorLight = findViewById(R.id.tv_flashlight_hide_color);
        checkPermission();
    }

    public void onClick_ShowColorPicker(View view){
        new ColorPickerDialog(this, this, Color.BLUE).show();
    }

    @Override
    public void colorChanged(int color) {
        mUIColorLight.setBackgroundColor(color);
        mCurrentColorLight = color;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(ColorLight.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Toast.makeText(ColorLight.this, "not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
