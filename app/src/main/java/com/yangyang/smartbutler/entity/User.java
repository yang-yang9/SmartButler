package com.yangyang.smartbutler.entity;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.entity
 *   文件名：User
 *   创建者：YangYang
 *   描述：用户属性
 */


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    /*BmobUser继承BmobObject，有默认属性：username、password、email*/

    private int age;
    private boolean sex;
    private String describe;

    private BmobFile photo;

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
