package com.yangyang.bookkeeping.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.yangyang.bookkeeping.adapter.ChartViewPagerAdapter;
import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.bookkeeping.fragment.InComeChartFragment;
import com.yangyang.bookkeeping.fragment.OutComeChartFragment;
import com.yangyang.bookkeeping.weight.CalendarDialog;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MonthChartActivity extends AppCompatActivity {
    Button btn_in,btn_out;
    TextView tv_date,tv_in,tv_out;
    ViewPager vp_chart;
    int year;
    int month;
    int selectPos = -1,selectMonth =-1;
    List<Fragment> chartFragList;
    private InComeChartFragment incomeChartFragment;
    private OutComeChartFragment outcomeChartFragment;
    private ChartViewPagerAdapter chartVPAdapter;

    BmobUser user = BmobUser.getCurrentUser(User.class);
    String uId = user.getObjectId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year, month);

        initFrag();
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        vp_chart.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initView() {
        btn_in = findViewById(R.id.btn_chart_in);
        btn_out = findViewById(R.id.btn_chart_out);
        tv_date = findViewById(R.id.tv_chart_date);
        tv_in = findViewById(R.id.tv_chart_in);
        tv_out = findViewById(R.id.tv_chart_out);
        vp_chart = findViewById(R.id.chart_vp);
    }
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }
    private void initStatistics(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(uId, year, month, 1);  //收入总钱数
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(uId, year, month, 0); //支出总钱数
        int incountItemOneMonth = DBManager.getCountItemOneMonth(uId, year, month, 1);  //收入多少笔
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(uId, year, month, 0); //支出多少笔
        tv_date.setText(year+"年"+month+"月账单");
        tv_in.setText("共"+incountItemOneMonth+"笔收入, ￥ "+inMoneyOneMonth);
        tv_out.setText("共"+outcountItemOneMonth+"笔支出, ￥ "+outMoneyOneMonth);

    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        incomeChartFragment = new InComeChartFragment();
        outcomeChartFragment = new OutComeChartFragment();
        //添加数据到Fragment当中
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);

        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);

        chartVPAdapter = new ChartViewPagerAdapter(getSupportFragmentManager(), chartFragList);
        vp_chart.setAdapter(chartVPAdapter);
        //将Fragment加载到Acitivy当中
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_chart_back:
                finish();
                break;
            case R.id.iv_chart_rili:
                showCalendarDialog();
                break;
            case R.id.btn_chart_in:
                setButtonStyle(1);
                vp_chart.setCurrentItem(1);
                break;
            case R.id.btn_chart_out:
                setButtonStyle(0);
                vp_chart.setCurrentItem(0);
                break;
        }
    }

    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year,month);
                incomeChartFragment.setDate(year,month);
                outcomeChartFragment.setDate(year,month);
            }
        });
    }

    private void setButtonStyle(int kind){
        if (kind == 0) {
            btn_out.setBackgroundResource(R.drawable.button_enter_bg);
            btn_out.setTextColor(Color.WHITE);
            btn_in.setBackgroundResource(R.drawable.dialog_btn_frame);
            btn_in.setTextColor(Color.BLACK);
        }else if (kind == 1){
            btn_in.setBackgroundResource(R.drawable.button_enter_bg);
            btn_in.setTextColor(Color.WHITE);
            btn_out.setBackgroundResource(R.drawable.dialog_btn_frame);
            btn_out.setTextColor(Color.BLACK);
        }
    }
}
