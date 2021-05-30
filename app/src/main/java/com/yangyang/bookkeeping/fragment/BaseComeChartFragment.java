package com.yangyang.bookkeeping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.yangyang.bookkeeping.adapter.ChartItemAdapter;
import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.bookkeeping.entity.ChartItemBean;
import com.yangyang.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.fragment
 *   文件名：InComeChartFragment
 *   创建者：YangYang
 *   描述：TODO
 */


public abstract class BaseComeChartFragment extends Fragment {
    private ListView lv_chart;
    protected int year;
    protected int month;
    List<ChartItemBean> mDatas;   //数据源
    private ChartItemAdapter itemAdapter;
    protected BarChart barChart;
    TextView tv_chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        lv_chart = view.findViewById(R.id.lv_frag_chart);
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");

        mDatas = new ArrayList<>();
        itemAdapter = new ChartItemAdapter(getContext(), mDatas);

        lv_chart.setAdapter(itemAdapter);

        addHeaderViewToLV();
        return view;
    }

    private void addHeaderViewToLV() {
        View headerView = getLayoutInflater().inflate(R.layout.frag_chart_item_top,null);

        lv_chart.addHeaderView(headerView);

        barChart = headerView.findViewById(R.id.item_chartfrag_chart);
        tv_chart = headerView.findViewById(R.id.item_chartfrag_top_tv);

        barChart.getDescription().setEnabled(false);

        barChart.setExtraOffsets(20, 20, 20, 20);
        setAxis(year,month);

        setAxisData(year,month);
    }


    public void loadData(int year,int month,int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }

    public void setDate(int year,int month){
        this.year = year;
        this.month = month;
        // 清空柱状图当中的数据
        barChart.clear();
        barChart.invalidate();  //重新绘制柱状图
        setAxis(year,month);
        setAxisData(year,month);
    }

    protected abstract void setAxisData(int year, int month);

    /** 设置柱状图坐标轴的显示  方法必须重新*/
    protected  void setAxis(int year, final int month){
        //设置X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //设置x轴显示在下方
        xAxis.setDrawGridLines(true);  //设置绘制该轴的网格线
        //设置x轴标签的个数
        xAxis.setLabelCount(31);
        xAxis.setTextSize(12f);  //x轴标签的大小

        //设置X轴显示的值的格式
        xAxis.setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                int val = (int) value;

                if (val == 0) {
                    return month+"-1";
                }
                if (val==14) {
                    return month+"-15";
                }
                if (month==2) {
                    if (val == 27) {
                        return month+"-28";
                    }
                }else if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                    if (val == 30) {
                        return month+"-31";
                    }
                }else if(month==4||month==6||month==9||month==11){
                    if (val==29) {
                        return month+"-30";
                    }
                }
                return "";
            }
        });
        xAxis.setYOffset(10); // 设置标签对x轴的偏移量，垂直方向
        // y轴在子类的设置
        setYAxis(year,month);
    }

    protected abstract void setYAxis(int year,int month);
}
