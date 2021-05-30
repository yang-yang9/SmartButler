package com.yangyang.smartbutler.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.ui
 *   文件名：BaseActivity
 *   创建者：YangYang
 *   描述：Activity 基类
 */


/*
* 主要做的事情：
* ·统一的属性;
* ·统一的接口;
* ·统一的函数;
* */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //响应返回键点击事件

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
