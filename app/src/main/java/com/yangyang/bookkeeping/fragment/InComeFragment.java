package com.yangyang.bookkeeping.fragment;


import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.bookkeeping.entity.TypeBean;
import com.yangyang.smartbutler.R;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.fragment
 *   文件名：InComeFragment
 *   创建者：YangYang
 *   描述：记录收入页面
 */
public class InComeFragment extends BaseComeFragment {


    @Override
    protected void loadDataToGridView() {
        super.loadDataToGridView();
        List<TypeBean> inList = DBManager.getTypeList(1);
        typeList.addAll(inList);
        typeBaseAdapter.notifyDataSetChanged();

        tv_type.setText("其他");
        iv_type.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    protected void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertToAccounttb(accountBean);
    }
}
