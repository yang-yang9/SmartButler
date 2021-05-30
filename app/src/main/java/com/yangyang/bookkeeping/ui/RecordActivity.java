package com.yangyang.bookkeeping.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yangyang.bookkeeping.adapter.RecordPagerAdapter;
import com.yangyang.bookkeeping.fragment.InComeFragment;
import com.yangyang.bookkeeping.fragment.OutComeFragment;
import com.yangyang.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.ui
 *   文件名：RecordActivity
 *   创建者：YangYang
 *   描述：记账页面
 */
public class RecordActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initView();
        initViewPager();
    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        OutComeFragment outFrag = new OutComeFragment(); //支出
        InComeFragment inFrag = new InComeFragment(); //收入
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        RecordPagerAdapter adapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);


        //将tablayout与viewpager关联
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabs_record);
        viewPager = findViewById(R.id.vp_record);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_record_back:
                finish();
                break;
        }
    }
}
