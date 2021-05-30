package com.yangyang.smartbutler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.yangyang.smartbutler.adapter.MyViewPagerAdapter;
import com.yangyang.smartbutler.fragment.ButlerFragment;
import com.yangyang.smartbutler.fragment.FunctionFragment;
import com.yangyang.smartbutler.fragment.PersonalFragment;
import com.yangyang.smartbutler.ui.SettingActivity;
import com.yangyang.smartbutler.utils.DataGenerator;
import com.yangyang.smartbutler.utils.L;
import com.yangyang.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    //private List<String> mTitle;
    private List<Fragment> mFragments;

    private MyViewPagerAdapter adapter;

    private FloatingActionButton fab_setting;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().setElevation(0);

            //去掉actionbar
            Objects.requireNonNull(getSupportActionBar()).hide();

            initView();
        } catch (Exception e){
            L.e("有错误:mainactivity");
            e.printStackTrace();
        }


        //Bugly测试
        //CrashReport.testJavaCrash();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mTabLayout = findViewById(R.id.mTableLayout);
        mViewPager = findViewById(R.id.mViewPager);
        fab_setting = findViewById(R.id.fab_setting);

        fab_setting.setOnClickListener(this);

        mFragments = new ArrayList<>();
        mFragments.add(new ButlerFragment());
        mFragments.add(new FunctionFragment());
        mFragments.add(new PersonalFragment());

        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), mFragments);
        //设置预加载
        mViewPager.setOffscreenPageLimit(mFragments.size());

        mViewPager.setAdapter(adapter);


        //设置初始ViewPager页面
        mViewPager.setCurrentItem(1, false);

        for (int i = 0; i < 3; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }
        chooseFirst();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), true);
                recoverItem();
                View view =tab.getCustomView();
                ImageView imageView = view.findViewById(R.id.tab_content_image);
                TextView textView = view.findViewById(R.id.tab_content_text);
                imageView.setImageDrawable(getResources().getDrawable(DataGenerator.mTabResPressed[tab.getPosition()]));
                textView.setTextColor(getResources().getColor(R.color.colorAccent));


                UtilTools.setFontSuDaHei(MainActivity.this, textView);

                /*管家页面隐藏悬浮按钮
                * */
                if (tab.getPosition() == 0){
                    fab_setting.setVisibility(View.GONE);
                } else {
                    fab_setting.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }




    //初始化
    private void chooseFirst () {

        //指定初始的Tab
        TabLayout.Tab tabAt = mTabLayout.getTabAt(1);
        tabAt.select();
        View view = tabAt.getCustomView();
        ImageView imageView = view.findViewById(R.id.tab_content_image);
        TextView textView = view.findViewById(R.id.tab_content_text);
        imageView.setImageDrawable(getResources().getDrawable(DataGenerator.mTabResPressed[1]));
        textView.setTextColor(getResources().getColor(R.color.colorAccent));

        UtilTools.setFontSuDaHei(MainActivity.this, textView);

    }

    private void recoverItem() {
        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tabAt = mTabLayout.getTabAt(i);
            View view = tabAt.getCustomView();
            ImageView imageView = view.findViewById(R.id.tab_content_image);
            TextView textView = view.findViewById(R.id.tab_content_text);
            imageView.setImageDrawable(getResources().getDrawable(DataGenerator.mTabRes[i]));
            textView.setTextColor(getResources().getColor(R.color.colorText));

            UtilTools.setFontSuDaHei(MainActivity.this, textView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}