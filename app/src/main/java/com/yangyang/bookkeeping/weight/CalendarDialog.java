package com.yangyang.bookkeeping.weight;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yangyang.bookkeeping.adapter.CalendarAdapter;
import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.weight
 *   文件名：CalendarDialog
 *   创建者：YangYang
 *   描述：历史账单页面日期选择
 */


public class CalendarDialog extends Dialog implements View.OnClickListener {
    ImageView iv_close;
    GridView gv_calendar;
    LinearLayout ll_calendar_top;

    List<TextView> yearViewList;
    List<Integer> yearList;

    int selectPos = -1;
    private CalendarAdapter adapter;
    int selectMonth = -1;
    BmobUser user = BmobUser.getCurrentUser(User.class);
    String uId = user.getObjectId();

    public interface OnRefreshListener{
        public void onRefresh(int selPos,int year,int month);
    }
    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CalendarDialog(@NonNull Context context, int selectPos, int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);

        initView();
        addViewToLayout();
        initGridView();

        setGvItemClickListener();
    }

    private void setGvItemClickListener() {
        gv_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selPos = position;
                adapter.notifyDataSetInvalidated();

                int month = position + 1;
                int year = adapter.year;
                onRefreshListener.onRefresh(selectPos,year,month);
                cancel();
            }
        });
    }

    private void initGridView() {
        int selYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selYear);

        if (selectMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selPos = month;
        } else {
            adapter.selPos = selectMonth - 1;
        }
        gv_calendar.setAdapter(adapter);
    }

    private void addViewToLayout() {
        yearViewList = new ArrayList<>();
        yearList = DBManager.getYearListFromAccounttb(uId);

        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }
        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.gv_item_cal_dialog_top, null);
            ll_calendar_top.addView(view);   //将view添加到布局当中
            TextView hsvTv = view.findViewById(R.id.tv_item_dialog_cal_hsv);
            hsvTv.setText(year+"");
            yearViewList.add(hsvTv);
        }

        if (selectPos == -1) {
            selectPos = yearViewList.size() - 1;
        }

        changeBtnBg(selectPos);

        setHSVItemClickListener();
    }

    private void setHSVItemClickListener() {
        for (int i = 0; i < yearViewList.size(); i++) {
            TextView view = yearViewList.get(i);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeBtnBg(pos);
                    selectPos = pos;
                    // 获取被选中的年份，然后下面的GridView显示数据源会发生变化
                    int year = yearList.get(selectPos);
                    adapter.setYear(year);
                }
            });
        }
    }

    private void changeBtnBg(int selectPos) {
        for (int i = 0; i < yearViewList.size(); i++) {
            TextView tv = yearViewList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_frame);
            tv.setTextColor(Color.BLACK);
        }

        TextView selView = yearViewList.get(selectPos);
        selView.setBackgroundResource(R.drawable.button_enter_bg);
        selView.setTextColor(Color.WHITE);
    }

    private void initView() {
        iv_close = findViewById(R.id.iv_dialog_calendar_close);
        gv_calendar = findViewById(R.id.gv_dialog_calendar);
        ll_calendar_top = findViewById(R.id.ll_dialog_calendar);

        iv_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_dialog_calendar_close:
                cancel();
                break;

        }
    }

    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }

}
