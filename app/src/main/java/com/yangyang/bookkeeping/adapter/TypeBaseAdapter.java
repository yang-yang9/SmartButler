package com.yangyang.bookkeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangyang.bookkeeping.entity.TypeBean;
import com.yangyang.smartbutler.R;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.adapter
 *   文件名：TypeBaseAdapter
 *   创建者：YangYang
 *   描述：收入支出页面，GridView适配器
 */


public class TypeBaseAdapter extends BaseAdapter {
    Context mContext;
    List<TypeBean> mDatas;
    public int selectPosition = 0;

    public TypeBaseAdapter(Context mContext, List<TypeBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.frag_item_record_gv, parent, false);
        ImageView img = convertView.findViewById(R.id.iv_item_record_frag);
        TextView text = convertView.findViewById(R.id.tv_item_record_frag);

        TypeBean typeBean = mDatas.get(position);
        text.setText(typeBean.getTypename());
        if (selectPosition == position){
            img.setImageResource(typeBean.getsImageId());
        } else {
            img.setImageResource(typeBean.getImageId());
        }
        return convertView;
    }
}
