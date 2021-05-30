package com.yangyang.smartbutler.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.yangyang.smartbutler.R;
import com.yzq.zxinglibrary.encode.CodeCreator;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：QrCodeActivity
 *   创建者：YangYang
 *   描述：二维码页面
 */
public class QrCodeActivity extends BaseActivity {
    private ImageView iv_qr_code;
    private Bitmap logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        Intent intent = new Intent();
        logo = (Bitmap) intent.getParcelableExtra("key");
        initView();
    }

    private void initView() {
        iv_qr_code = findViewById(R.id.iv_qr_code);
        int width = getResources().getDisplayMetrics().widthPixels;

        //Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap qrCodebitmap = CodeCreator.createQRCode("我是智能管家", width*3/4, width*3/4, logo);
        iv_qr_code.setImageBitmap(qrCodebitmap);
    }
}
