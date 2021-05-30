package com.yangyang.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.ExpressData;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.adapter
 *   文件名：ExpressAdapter
 *   创建者：YangYang
 *   描述：快递查询适配器
 */


public class ExpressAdapter extends BaseAdapter {
    private Context mContext;
    private List<ExpressData> mList;
    private LayoutInflater inflater;
    private ExpressData data;

    public ExpressAdapter(Context mContext, List<ExpressData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (viewHolder == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_express_item, null);
            viewHolder.datatime = convertView.findViewById(R.id.tv_express_item_time);
            viewHolder.status = convertView.findViewById(R.id.tv_express_item_status);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);
        viewHolder.status.setText(data.getStatus());
        viewHolder.datatime.setText(data.getTime());

        return convertView;
    }

    //listview优化
    class ViewHolder{
        private TextView datatime;
        private TextView status;
    }
}
