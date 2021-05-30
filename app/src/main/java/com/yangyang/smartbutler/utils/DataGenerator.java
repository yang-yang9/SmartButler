package com.yangyang.smartbutler.utils;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.fragment
 *   文件名：DataGenerator
 *   创建者：YangYang
 *   描述：工具类DataGenerator
 */


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.yangyang.smartbutler.MainActivity;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.fragment.ButlerFragment;
import com.yangyang.smartbutler.fragment.FunctionFragment;
import com.yangyang.smartbutler.fragment.PersonalFragment;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    public static final int []mTabRes = new int[]{R.drawable.butler_default,R.drawable.function_default,R.drawable.personal_default};
    public static final int []mTabResPressed = new int[]{R.drawable.butler_selected,R.drawable.function_selected,R.drawable.personal_selected};
    public static final String []mTabTitle = new String[]{"管家","功能","我的"};


    public static List<Fragment> getFragments(String from){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ButlerFragment.newInstance(from));
        fragments.add(FunctionFragment.newInstance(from));
        fragments.add(PersonalFragment.newInstance(from));
        return fragments;
    }


    //获取Tab内容
    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        UtilTools.setFontSuDaHei(context, tabText);

        return view;
    }

}
