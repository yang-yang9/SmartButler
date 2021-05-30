package com.yangyang.bookkeeping.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.yangyang.smartbutler.R;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.weight
 *   文件名：PickerDialog
 *   创建者：YangYang
 *   描述：选择时间Dialog
 */


public class PickerDialog extends Dialog implements View.OnClickListener {
    private EditText et_hour, et_minute;
    private Button btn_ensure, btn_cancel;
    private DatePicker datePicker;

    public PickerDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnEnsureListener{
        public void onEnsure(String time, int year, int month, int day);
    }

    OnEnsureListener listener;

    public void setEnsureListener(OnEnsureListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picker_time);
        initView();
        hideDatePickerHead();
    }

    private void initView() {
        et_hour = findViewById(R.id.et_dialog_time_hour);
        et_minute = findViewById(R.id.et_dialog_time_minute);
        btn_ensure = findViewById(R.id.btn_dialog_time_ensure);
        btn_cancel = findViewById(R.id.btn_dialog_time_cancel);
        datePicker = findViewById(R.id.dp_dialog_time);

        btn_ensure.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dialog_time_ensure:
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int dayOfMonth = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);
                if (month<10){
                    monthStr = "0"+month;
                }
                String dayStr = String.valueOf(dayOfMonth);
                if (dayOfMonth<10){
                    dayStr="0"+dayOfMonth;
                }

                String hourStr = et_hour.getText().toString();
                String minuteStr = et_minute.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour=hour%24;
                }
                int minute = 0;
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr);
                    minute=minute%60;
                }

                hourStr=String.valueOf(hour);
                minuteStr=String.valueOf(minute);
                if (hour<10){
                    hourStr="0"+hour;
                }
                if (minute<10){
                    minuteStr="0"+minute;
                }
                String timeFormat = year+"年"+monthStr+"月"+dayStr+"日 "+hourStr+":"+minuteStr;
                if (listener != null) {
                    listener.onEnsure(timeFormat, year, month, dayOfMonth);
                }
                cancel();
                break;
            case R.id.btn_dialog_time_cancel:
                cancel();
                break;
        }
    }


    //隐藏DatePicker头布局
    private void hideDatePickerHead() {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }
        //5.0+
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);
            return;
        }
        // 6.0+
        headerId = getContext().getResources().getIdentifier("date_picker_header","id","android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }


}
