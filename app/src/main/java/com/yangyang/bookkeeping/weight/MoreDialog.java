package com.yangyang.bookkeeping.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.yangyang.bookkeeping.ui.AccountHistoryActivity;
import com.yangyang.bookkeeping.ui.BookkeepingSettingActivity;
import com.yangyang.bookkeeping.ui.MonthChartActivity;
import com.yangyang.smartbutler.R;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.weight
 *   文件名：MoreDialog
 *   创建者：YangYang
 *   描述：更多弹窗
 */


public class MoreDialog extends Dialog implements View.OnClickListener {
    Button btn_setting,btn_history,btn_info;
    ImageView iv_error;

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);

        btn_history = findViewById(R.id.btn_dialog_more_record);
        btn_info = findViewById(R.id.btn_dialog_more_info);
        btn_setting = findViewById(R.id.btn_dialog_more_setting);
        iv_error = findViewById(R.id.iv_dialog_more);

        btn_history.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_info.setOnClickListener(this);
        iv_error.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_dialog_more_record:
                intent.setClass(getContext(), AccountHistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.btn_dialog_more_info:
                intent.setClass(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.btn_dialog_more_setting:
                intent.setClass(getContext(), BookkeepingSettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.iv_dialog_more:

                break;
        }
        cancel();
    }

    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());  //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
