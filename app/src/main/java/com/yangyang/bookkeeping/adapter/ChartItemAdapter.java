package com.yangyang.bookkeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangyang.bookkeeping.entity.ChartItemBean;
import com.yangyang.bookkeeping.utils.FloatUtils;
import com.yangyang.smartbutler.R;

import java.util.List;

/*
* 账单详情页面，listview的适配器
* */
public class ChartItemAdapter extends BaseAdapter {
    Context context;
    List<ChartItemBean> mDatas;
    LayoutInflater inflater;
    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_chart_item_lv,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        ChartItemBean bean = mDatas.get(position);
        holder.iv.setImageResource(bean.getsImageId());
        holder.tv_type.setText(bean.getType());
        float ratio = bean.getRatio();
        String pert = FloatUtils.ratioToPercent(ratio);
        holder.tv_ratio.setText(pert);

        holder.tv_total.setText("￥ "+bean.getTotalMoney());
        return convertView;
    }

    class ViewHolder{
        TextView tv_type, tv_ratio, tv_total;
        ImageView iv;
        public ViewHolder(View view){
            tv_type = view.findViewById(R.id.tv_item_chart_frag_type);
            tv_ratio = view.findViewById(R.id.tv_item_chart_frag_pert);
            tv_total = view.findViewById(R.id.tv_item_chart_frag_sum);
            iv = view.findViewById(R.id.iv_item_chart_frag);
        }
    }
}
