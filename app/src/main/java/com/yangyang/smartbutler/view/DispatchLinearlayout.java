package com.yangyang.smartbutler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.view
 *   文件名：DispatchLinearlayout
 *   创建者：YangYang
 *   描述：TODO
 */


public class DispatchLinearlayout extends LinearLayout {
    private DispatchKeyEventListener dispatchKeyEventListener;


    public DispatchLinearlayout(Context context) {
        super(context);
    }

    public DispatchLinearlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLinearlayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return dispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }

    public static interface DispatchKeyEventListener {
        boolean dispatchKeyEvent(KeyEvent event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果不为空说明调用了，去获取事件
        if (dispatchKeyEventListener != null){
            return dispatchKeyEventListener.dispatchKeyEvent(event);
        }

        return super.dispatchKeyEvent(event);
    }
}
