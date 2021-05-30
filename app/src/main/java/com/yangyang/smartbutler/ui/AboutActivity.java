package com.yangyang.smartbutler.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.ui
 *   文件名：AboutActivity
 *   创建者：YangYang
 *   描述：设置
 */
public class AboutActivity extends BaseActivity {
    private ListView mListView;
    private List<String> mListName = new ArrayList<>();
    private List<String> mListContent = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //去掉actionbar阴影
        getSupportActionBar().setElevation(0);
        initView();

    }

    private void initView() {
        mListView = findViewById(R.id.lv_about_soft);

        mListName.add("应用名：               " + getString(R.string.app_name));
        mListName.add("版本号：               " + UtilTools.getVersion(this));
        mListName.add("感谢：               526宿舍全体" );
        /*mListContent.add(getString(R.string.app_name));
        mListContent.add(UtilTools.getVersion(this));
        mListContent.add("526宿舍全体");*/

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListName);
        //mListView.setAdapter(new MyAdapter());
        mListView.setAdapter(mAdapter);
    }


    private class MyAdapter extends BaseAdapter{
        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return mListName.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(AboutActivity.this).inflate(R.layout.about_software_item, parent, false);
            TextView tv_name = convertView.findViewById(R.id.tv_about_item_name);
            TextView tv_content = convertView.findViewById(R.id.tv_about_item_content);
            tv_name.setText(mListName.get(position));
            tv_content.setText(mListContent.get(position));
            return convertView;
        }
    }
}
