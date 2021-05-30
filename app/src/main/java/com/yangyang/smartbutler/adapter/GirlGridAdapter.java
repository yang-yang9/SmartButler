package com.yangyang.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.GirlData;
import com.yangyang.smartbutler.utils.PicassoUtils;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.adapter
 *   文件名：GirlGridAdapter
 *   创建者：YangYang
 *   描述：美女社区GridView适配器
 */


public class GirlGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<GirlData> mList;
    private LayoutInflater inflater;
    private GirlData data;
    private WindowManager wm;
    //屏幕宽
    private int width;

    public GirlGridAdapter(Context mContext, List<GirlData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.girl_item,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_girl_community_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);
        //解析图片
        String url = data.getImgUrl();
        //L.i(url);
        PicassoUtils.loadImageViewSize(url ,width/2,500,viewHolder.imageView);
        //PicassoUtils.loadImageViewSize("https://ae01.alicdn.com/kf/U76a18e0d315e407a8daf3d91de033e31i.jpg" ,width/2,500,viewHolder.imageView);

        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
    }
}
