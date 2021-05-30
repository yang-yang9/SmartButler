package com.yangyang.bookkeeping.entity;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.entity
 *   文件名：BarChartItemBean
 *   创建者：YangYang
 *   描述：用于描述绘制柱状图时，每一个柱子表示的对象实体类
 */

public class BarChartItemBean {
    int year;
    int month;
    int day;
    float summoney;

    public BarChartItemBean() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getSummoney() {
        return summoney;
    }

    public void setSummoney(float summoney) {
        this.summoney = summoney;
    }

    public BarChartItemBean(int year, int month, int day, float summoney) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.summoney = summoney;
    }
}
