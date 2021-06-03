package com.yangyang.bookkeeping.fragment;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.fragment
 *   文件名：InComeChartFragment
 *   创建者：YangYang
 *   描述：TODO
 */


import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.bookkeeping.entity.BarChartItemBean;

import java.util.ArrayList;
import java.util.List;

public class OutComeChartFragment extends BaseComeChartFragment {

    @Override
    public void onResume() {
        super.onResume();

        loadData(year, month, 0);
    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year, month, 0);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        //获取这个月每天的支出总金额，
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(uId, year, month, 0);
        if (list.size() == 0) {
            barChart.setVisibility(View.GONE);
            tv_chart.setVisibility(View.VISIBLE);
        }else{
            barChart.setVisibility(View.VISIBLE);
            tv_chart.setVisibility(View.GONE);
            //设置有多少根柱子
            List<BarEntry> barEntries1 = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                //初始化每一根柱子，添加到柱状图当中
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries1.add(entry);
            }
            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay();   //获取日期
                // 根据天数，获取x轴的位置
                int xIndex = day-1;
                BarEntry barEntry = barEntries1.get(xIndex);
                barEntry.setY(itemBean.getSummoney());
            }
            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.BLACK); // 值的颜色
            barDataSet1.setValueTextSize(8f); // 值的大小
            barDataSet1.setColor(Color.RED); // 柱子的颜色

            // 设置柱子上数据显示的格式
            barDataSet1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    if (value==0) {
                        return "";
                    }
                    return value + "";
                }
            });

            /*barDataSet1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // 此处的value默认保存一位小数
                    if (value==0) {
                        return "";
                    }
                    return value + "";
                }
            });*/
            sets.add(barDataSet1);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f); // 设置柱子的宽度
            barChart.setData(barData);
        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        //获取本月收入最高的一天为多少，将他设定为y轴的最大值
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(uId, year, month, 0);
        float max = (float) Math.ceil(maxMoney);   // 将最大金额向上取整
        //设置y轴
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);

        //设置不显示图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }
}
