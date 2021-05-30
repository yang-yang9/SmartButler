package com.yangyang.bookkeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangyang.bookkeeping.entity.AccountBean;
import com.yangyang.smartbutler.R;

import java.util.Calendar;
import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.adapter
 *   文件名：AccountAdapter
 *   创建者：YangYang
 *   描述：账本条目适配器
 */


public class AccountAdapter extends BaseAdapter {
    private Context mContext;
    private List<AccountBean> mDatas;
    private LayoutInflater inflater;
    int year, month, day;

    public AccountAdapter(Context mContext, List<AccountBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(mContext);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.bookkeeping_item_main_lv, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AccountBean bean = mDatas.get(position);
        viewHolder.iv_type.setImageResource(bean.getsImageId());
        viewHolder.tv_type.setText(bean.getTypename());
        viewHolder.tv_remark.setText(bean.getBeizhu());
        viewHolder.tv_money.setText("￥ "+bean.getMoney());
        if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day) {
            String time = bean.getTime().split(" ")[1];
            viewHolder.tv_time.setText("今天 "+time);
        }else {
            viewHolder.tv_time.setText(bean.getTime());
        }
        return convertView;

    }


    class ViewHolder {
        ImageView iv_type;
        TextView tv_type, tv_remark, tv_time, tv_money;

        public ViewHolder(View view) {
            iv_type = view.findViewById(R.id.iv_item_main_lv);
            tv_type = view.findViewById(R.id.tv_item_main_lv_title);
            tv_remark = view.findViewById(R.id.tv_item_main_lv_beizhu);
            tv_time = view.findViewById(R.id.tv_item_main_lv_time);
            tv_money = view.findViewById(R.id.tv_item_main_lv_money);
        }
    }
}
