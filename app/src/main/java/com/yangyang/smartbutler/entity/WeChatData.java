package com.yangyang.smartbutler.entity;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.entity
 *   文件名：WeChatData
 *   创建者：YangYang
 *   描述：微信精选实体类
 */


public class WeChatData {
    private String title;
    private String time;
    private String weixinname;
    private String imgUrl;
    private String newsUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeixinname() {
        return weixinname;
    }

    public void setWeixinname(String weixinname) {
        this.weixinname = weixinname;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
