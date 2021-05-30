package com.yangyang.bookkeeping.entity;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.entity
 *   文件名：ChartItemBean
 *   创建者：YangYang
 *   描述：账单详情页listview适配器数据对象
 */


public class ChartItemBean {
    private int sImageId;
    private String type;
    private float ratio;   //所占比例
    private float totalMoney;  //此项的总钱数

    public ChartItemBean() {
    }

    public ChartItemBean(int sImageId, String type, float ratio, float totalMoney) {
        this.sImageId = sImageId;
        this.type = type;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }
}
