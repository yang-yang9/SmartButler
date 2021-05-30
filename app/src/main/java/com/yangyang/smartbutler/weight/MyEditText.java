package com.yangyang.smartbutler.weight;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.yangyang.smartbutler.R;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.weight
 *   文件名：MyEditText
 *   创建者：YangYang
 *   描述：自定义EditText
 */


public class MyEditText extends AppCompatEditText {
        private Paint mPaint;
        private Paint wPaint;
        private int paperColor;
        public MyEditText(Context context) {
            super(context);
            init();
        }

        public MyEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }
        private void init(){
            Resources resources=getResources();
            paperColor=resources.getColor(R.color.colorPrimary);
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(paperColor);
            mPaint.setStyle(Paint.Style.FILL);
            wPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            wPaint.setColor(resources.getColor(R.color.color_white));
            wPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
            canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight()-2,wPaint);
            canvas.save();
            super.onDraw(canvas);
            canvas.restore();
        }

    }
