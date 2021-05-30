package com.yangyang.bookkeeping.entity;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.db
 *   文件名：TypeBean
 *   创建者：YangYang
 *   描述：表示收入或支出具体类型的类
 */

public class TypeBean {
    int id;
    String typename;
    int imageId;
    int sImageId;
    int kind;

    public TypeBean() {
    }

    public TypeBean(int id, String typename, int imageId, int sImageId, int kind) {
        this.id = id;
        this.typename = typename;
        this.imageId = imageId;
        this.sImageId = sImageId;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
