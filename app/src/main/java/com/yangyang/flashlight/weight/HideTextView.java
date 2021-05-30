package com.yangyang.flashlight.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight.weight
 *   文件名：HideTextView
 *   创建者：YangYang
 *   描述：3秒之后自动隐藏
 */

@SuppressLint("AppCompatCustomView")
public class HideTextView extends TextView {

    public HideTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0){
                setVisibility(View.GONE);
            } else if (msg.what == 1){
                setVisibility(View.VISIBLE);
            }
        }
    };

    class TextViewThread extends Thread {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);

            try {
                sleep(3000);
                mHandler.sendEmptyMessage(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void hide(){
        new TextViewThread().start();
    }
}
