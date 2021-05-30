package com.yangyang.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.ChatListData;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.adapter
 *   文件名：ChatListAdapter
 *   创建者：YangYang
 *   描述：对话列表适配器
 */


public class ChatListAdapter extends BaseAdapter {
    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;


    private Context mContext;
    private LayoutInflater inflater;
    private ChatListData chatListData;
    private List<ChatListData> listDatas;

    public ChatListAdapter(Context mContext, List<ChatListData> listDatas) {
        this.mContext = mContext;
        this.listDatas = listDatas;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLeftText viewHolderLeftText = null;
        ViewHolderRightText viewHolderRightText = null;
        int type = getItemViewType(position);
        if(convertView == null){
            switch (type){
                case TYPE_LEFT:
                    viewHolderLeftText = new ViewHolderLeftText();
                    convertView = inflater.inflate(R.layout.chat_list_left_item, null);
                    viewHolderLeftText.tv_left_text = convertView.findViewById(R.id.tv_chat_left_text);
                    convertView.setTag(viewHolderLeftText);
                    break;
                case TYPE_RIGHT:
                    viewHolderRightText = new ViewHolderRightText();
                    convertView = inflater.inflate(R.layout.chat_list_right_item, null);
                    viewHolderRightText.tv_right_text = convertView.findViewById(R.id.tv_chat_right_text);
                    convertView.setTag(viewHolderRightText);
                    break;
            }
        } else {
            switch (type){
                case TYPE_LEFT:
                    viewHolderLeftText = (ViewHolderLeftText) convertView.getTag();
                    break;
                case TYPE_RIGHT:
                    viewHolderRightText = (ViewHolderRightText) convertView.getTag();
                    break;
            }
        }

        ChatListData data = listDatas.get(position);
        switch (type){
            case TYPE_LEFT:
                viewHolderLeftText.tv_left_text.setText(data.getText());
                break;
            case TYPE_RIGHT:
                viewHolderRightText.tv_right_text.setText(data.getText());
                break;
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        ChatListData data = listDatas.get(position);
        int type = data.getType();
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    //左文本
    class ViewHolderLeftText{
        private TextView tv_left_text;
    }

    //右文本
    class ViewHolderRightText{
        private TextView tv_right_text;
    }
}
