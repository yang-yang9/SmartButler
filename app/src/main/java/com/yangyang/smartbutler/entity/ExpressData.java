package com.yangyang.smartbutler.entity;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.entity
 *   文件名：ExpressData
 *   创建者：YangYang
 *   描述：快递查询实体
 */


public class ExpressData {
    private String time;
    private String status;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ExpressData{" +
                "time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
