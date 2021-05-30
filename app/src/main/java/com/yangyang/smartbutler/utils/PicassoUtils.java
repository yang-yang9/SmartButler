package com.yangyang.smartbutler.utils;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：PicassoUtils
 *   创建者：YangYang
 *   描述：封装picasso图片库
 */


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoUtils {

    //默认形式加载
    public static void loadImageView(String url, ImageView imageView){
        Picasso.get().load(url).into(imageView);
    }

    //指定大小形式加载
    public static void loadImageViewSize(String url, int width, int height, ImageView imageView){
        L.i("开始加载");
        Picasso.get().load(url).resize(width, height).centerCrop().into(imageView);
    }

    //加载有默认图片且加载错误有错误图片
    public static void loadImageViewHolder(String url, int loadImg, int errorImg, ImageView imageView){
        Picasso.get().load(url).placeholder(loadImg).error(errorImg).into(imageView);
    }

    //裁剪
    public static void loadImageViewCrop(String url, ImageView imageView){
        Picasso.get().load(url).transform(new CropSquareTransformation()).into(imageView);
    }

    //按比例裁剪 矩形
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() { return "square()"; }
    }
}
