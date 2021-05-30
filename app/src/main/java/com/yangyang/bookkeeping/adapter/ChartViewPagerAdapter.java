package com.yangyang.bookkeeping.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.adapter
 *   文件名：ChartViewPagerAdapter
 *   创建者：YangYang
 *   描述：TODO
 */


public class ChartViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;

    public ChartViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
