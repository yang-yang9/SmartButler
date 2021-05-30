package com.yangyang.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.WeChatData;
import com.yangyang.smartbutler.utils.PicassoUtils;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.adapter
 *   文件名：WeChatAdapter
 *   创建者：YangYang
 *   描述：微信精选列表适配器
 */


public class WeChatAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<WeChatData> mList;
    private WeChatData data;

    private int width, height;
    private WindowManager wm;

    public WeChatAdapter(Context mContext, List<WeChatData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
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
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.wechat_item, null);
            viewHolder.tv_title = convertView.findViewById(R.id.tv_wechat_item_title);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_wechat_item_name);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_wechat_item_time);
            viewHolder.iv_image = convertView.findViewById(R.id.iv_wechat_item_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_name.setText(data.getWeixinname());
        viewHolder.tv_time.setText(data.getTime());
        PicassoUtils.loadImageViewSize(data.getImgUrl(), width/3, 200, viewHolder.iv_image);

        return convertView;
    }

    class ViewHolder{
        private TextView tv_title;
        private TextView tv_name;
        private TextView tv_time;
        private ImageView iv_image;
    }
}
