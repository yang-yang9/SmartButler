package com.yangyang.bookkeeping.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yangyang.bookkeeping.adapter.AccountAdapter;
import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.bookkeeping.entity.AccountBean;
import com.yangyang.bookkeeping.weight.CalendarDialog;
import com.yangyang.smartbutler.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AccountHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private ImageView iv_calendar;
    private TextView tv_time;
    private ListView lv_history;

    private List<AccountBean> mDatas;
    private AccountAdapter adapter;

    private int year, month;
    private int dialogSelPos = -1;
    private int dialogSelMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_history);

        initView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);

        lv_history.setAdapter(adapter);

        initTime();
        tv_time.setText(year+"年"+month+"月");
        loadData(year, month);


        setLvClickListener();
        }

    private void setLvClickListener() {
        lv_history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccountBean bean = mDatas.get(position);
                int selId = bean.getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountHistoryActivity.this);
                builder.setTitle("提示信息")
                        .setMessage("确定删除这条记录吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBManager.deleteItemFromAccounttbById(selId);
                                mDatas.remove(bean);
                                adapter.notifyDataSetChanged();
                            }
                        });
                builder.create().show();
                return true;
            }
        });

    }

    private void loadData(int year, int month) {
        List<AccountBean> list = DBManager.getAccountOneMonthFromAccounttb(year, month);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_history_back);
        iv_calendar = findViewById(R.id.iv_history_calendar);
        tv_time = findViewById(R.id.tv_history_time);
        lv_history = findViewById(R.id.lv_history);

        iv_back.setOnClickListener(this);
        iv_calendar.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        //lv_history.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_history_back:
                finish();
                break;
            case R.id.iv_history_calendar:
                CalendarDialog dialog = new CalendarDialog(this, dialogSelPos, dialogSelMonth);
                dialog.show();
                dialog.setDialogSize();

                dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void onRefresh(int selPos, int year, int month) {
                        tv_time.setText(year+"年"+month+"月");
                        loadData(year,month);
                        dialogSelPos = selPos;
                        dialogSelMonth = month;
                    }
                });
                break;
            case R.id.tv_history_time:

                break;
            case R.id.lv_history:

                break;
        }
    }


}
