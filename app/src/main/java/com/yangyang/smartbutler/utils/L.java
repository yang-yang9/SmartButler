package com.yangyang.smartbutler.utils;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：L
 *   创建者：YangYang
 *   描述：Log封装类
 */


import android.util.Log;

public class L {

    //开关
    public static final boolean SWITCH = true;
    public static final String TAG = "SmartButler";

    public static void d(String text){
        if (SWITCH){
            Log.d(TAG, text);
        }
    }

    public static void i(String text){
        if (SWITCH){
            Log.i(TAG, text);
        }
    }

    public static void w(String text){
        if (SWITCH){
            Log.w(TAG, text);
        }
    }

    public static void e(String text){
        if (SWITCH){
            Log.e(TAG, text);
        }
    }

    public static void f(String text){
        if (SWITCH){
            Log.wtf(TAG, text);
        }
    }
}
