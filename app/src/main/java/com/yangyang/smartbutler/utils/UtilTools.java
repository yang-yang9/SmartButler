package com.yangyang.smartbutler.utils;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：UtilTools
 *   创建者：YangYang
 *   描述：工具类
 *          setFont:设置字体
 *
 */


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangyang.smartbutler.entity.User;

import java.io.ByteArrayOutputStream;

import cn.bmob.v3.BmobUser;

public class UtilTools {

    public static void setFontBaiZhou(Context mContext, TextView textView){
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/白舟忍者.otf");
        textView.setTypeface(typeface);
    }

    public static void setFontSuDaHei(Context mContext, TextView textView){
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/苏打黑.ttf");
        textView.setTypeface(typeface);
    }

    public static void setFontSongTi(Context mContext, TextView textView){
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/方正书宋简体.ttf");
        textView.setTypeface(typeface);
    }

    public static void setFontShouJi(Context mContext, TextView textView){
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/默陌老屋手迹.ttf");
        textView.setTypeface(typeface);
    }


    //将图片存到share preference
    public static void putImageToShare(Context mContext, ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

        byte[] bytes = outputStream.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));

        ShareUtils.putString(mContext, "image_title", imgString);
    }


    //将云数据库里的图片设置给imageview
    public static void getImageToView(Context mContext, ImageView imageView){
        User user = BmobUser.getCurrentUser(User.class);
    }

    //获取版本号
    public static String getVersion(Context mContext){
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "未知";
        }
    }
}
