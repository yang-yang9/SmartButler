package com.yangyang.bookkeeping.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yangyang.bookkeeping.adapter.AccountAdapter;
import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.bookkeeping.entity.AccountBean;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private ImageView iv_search;
    private ListView lv_search;
    private EditText et_search;
    private TextView tv_empty;
    private List<AccountBean> mDatas;
    private AccountAdapter adapter;
    BmobUser user = BmobUser.getCurrentUser(User.class);
    String uId = user.getObjectId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        lv_search.setAdapter(adapter);
        lv_search.setEmptyView(tv_empty);
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_search_back);
        iv_search = findViewById(R.id.iv_search_sh);
        lv_search = findViewById(R.id.lv_search);
        et_search = findViewById(R.id.et_search);
        tv_empty = findViewById(R.id.tv_search_empty);


        iv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.iv_search_sh:
                String searchContent = et_search.getText().toString().trim();
                if (TextUtils.isEmpty(searchContent)) {
                    Toast.makeText(this,"输入内容不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<AccountBean> list = DBManager.getAccountListByRemarkFromAccounttb(uId, searchContent);
                mDatas.clear();
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
