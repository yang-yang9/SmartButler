package com.yangyang.flashlight.ui;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight.ui
 *   文件名：Flashlight
 *   创建者：YangYang
 *   描述：控制手电筒热点类（点击热点区域控制手电筒开关）
 */


import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.utils.L;

public class Flashlight extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageViewFlashlight = findViewById(R.id.iv_flashlight_img);
        mImageViewFlashlight.setTag(false);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        ViewGroup.LayoutParams layoutParams = mImageViewFlashlightController.getLayoutParams();
        layoutParams.height = point.y * 3 / 4;
        layoutParams.width = point.x / 3;

        mImageViewFlashlightController.setLayoutParams(layoutParams);



    }


    public void onClick_Flashlight(View view) {
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "当前设备没有闪光灯", Toast.LENGTH_LONG).show();
            return;
        }
        if (((Boolean) mImageViewFlashlight.getTag()) == false) {
            openFlashlight();

        } else {
            closeFlashlight();
        }
    }


    protected void openFlashlight(){
        L.i("手电筒开启");
        Toast.makeText(this, "手电筒开启", Toast.LENGTH_SHORT).show();
        TransitionDrawable drawable = (TransitionDrawable) mImageViewFlashlight.getDrawable();
        drawable.startTransition(200);

        //设置手电筒是否点亮
        mImageViewFlashlight.setTag(true);

        try {
            //手电筒和相机必须同时打开，所以将相机的图像变为纹理
            mCamera = Camera.open();
            int textureId = 0;
            mCamera.setPreviewTexture(new SurfaceTexture(textureId));
            mCamera.startPreview();

            mParameters = mCamera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParameters);

        } catch (Exception e){

        }
    }

    protected void closeFlashlight(){
        L.i("手电筒关闭");
        TransitionDrawable drawable = (TransitionDrawable) mImageViewFlashlight.getDrawable();
        if (((Boolean)mImageViewFlashlight.getTag())){
            drawable.reverseTransition(200);
            mImageViewFlashlight.setTag(false);

            if (mCamera != null){
                mParameters = mCamera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(mParameters);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeFlashlight();
    }
}
