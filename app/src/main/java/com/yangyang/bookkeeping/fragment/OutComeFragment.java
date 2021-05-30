package com.yangyang.bookkeeping.fragment;


import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.bookkeeping.entity.TypeBean;
import com.yangyang.smartbutler.R;

import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.fragment
 *   文件名：OutComeFragment
 *   创建者：YangYang
 *   描述：记录支出页面
 */
public class OutComeFragment extends BaseComeFragment {

    @Override
    protected void loadDataToGridView() {
        super.loadDataToGridView();
        List<TypeBean> outList = DBManager.getTypeList(0);
        typeList.addAll(outList);
        typeBaseAdapter.notifyDataSetChanged();

        tv_type.setText("其他");
        iv_type.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    protected void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertToAccounttb(accountBean);
    }
}
