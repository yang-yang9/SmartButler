package com.yangyang.smartbutler.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.adapter
 *   文件名：MyViewPagerAdapter
 *   创建者：YangYang
 *   描述：ViewPager适配器
 */


public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        fragment = fragments.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id",""+position);
        if (fragment != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
