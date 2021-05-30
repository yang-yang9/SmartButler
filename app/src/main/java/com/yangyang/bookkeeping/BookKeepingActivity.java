package com.yangyang.bookkeeping;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yangyang.bookkeeping.adapter.AccountAdapter;
import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.bookkeeping.entity.AccountBean;
import com.yangyang.bookkeeping.ui.MonthChartActivity;
import com.yangyang.bookkeeping.ui.RecordActivity;
import com.yangyang.bookkeeping.ui.SearchActivity;
import com.yangyang.bookkeeping.weight.BudgetDialog;
import com.yangyang.bookkeeping.weight.MoreDialog;
import com.yangyang.smartbutler.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping
 *   文件名：BookKeepingActivity
 *   创建者：YangYang
 *   描述：账本
 */

public class BookKeepingActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_search;
    private ImageButton ib_more;
    private Button btn_add;
    private ImageView iv_back;
    private ListView lv_today;
    private List<AccountBean> mDatas;
    private AccountAdapter adapter;
    private int year, month, day;

    private View headView;
    private TextView tv_top_out,tv_top_in,tv_top_budget, tv_top_con;
    private ImageView iv_top_show;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_keeping);

        initTime();
        initView();
        addListViewHeader();

        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        lv_today.setAdapter(adapter);
    }

    private void addListViewHeader() {
        headView = getLayoutInflater().inflate(R.layout.bookkeeping_item_main_top, null);
        lv_today.addHeaderView(headView);

        tv_top_out = headView.findViewById(R.id.tv_item_main_lv_top_out);
        tv_top_in = headView.findViewById(R.id.tv_item_main_lv_top_in);
        tv_top_budget = headView.findViewById(R.id.tv_item_main_lv_top_budget);
        tv_top_con = headView.findViewById(R.id.tv_item_main_lv_top_day);
        iv_top_show = headView.findViewById(R.id.iv_item_main_lv_top_hide);

        tv_top_budget.setOnClickListener(this);
        headView.setOnClickListener(this);
        iv_top_show.setOnClickListener(this);

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {
        iv_search = findViewById(R.id.iv_bookkeeping_main_search);
        ib_more = findViewById(R.id.btn_bookkeeping_main_more);
        btn_add = findViewById(R.id.btn_bookkeeping_main_edit);
        iv_back = findViewById(R.id.iv_bookkeeping_main_back);
        lv_today = findViewById(R.id.lv_bookkeeping_main);

        iv_search.setOnClickListener(this);
        ib_more.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        lv_today.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                }
                int pos = position - 1;
                AccountBean bean = mDatas.get(pos);
                int beanId = bean.getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(BookKeepingActivity.this);
                builder.setTitle("提示信息")
                        .setMessage("确定删除这条记录吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBManager.deleteItemFromAccounttbById(beanId);
                                mDatas.remove(bean);
                                adapter.notifyDataSetChanged();
                                setMainTopShow();
                            }
                        }).create().show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_bookkeeping_main_search:
                Intent SearchIntent = new Intent(this, SearchActivity.class);
                startActivity(SearchIntent);
                break;
            case R.id.btn_bookkeeping_main_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();

                break;
            case R.id.btn_bookkeeping_main_edit:
                Intent intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_bookkeeping_main_back:
                finish();
                break;
            case R.id.tv_item_main_lv_top_budget:
                showBudgetDialog();
                break;
            case R.id.iv_item_main_lv_top_hide:
                toggleShowOrHide();
                break;
        }
        if (v == headView) {
            Intent intent = new Intent(this, MonthChartActivity.class);

            startActivity(intent);
        }
    }

    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();

        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("budget", money);
                editor.commit();

                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float surplus = money - outcomeOneMonth;

                tv_top_budget.setText("￥" + surplus);
            }
        });
    }

    //明文true或密文
    private boolean isShow = true;
    private void toggleShowOrHide() {
        if (isShow) {
            PasswordTransformationMethod method = PasswordTransformationMethod.getInstance();
            tv_top_in.setTransformationMethod(method);
            tv_top_out.setTransformationMethod(method);
            tv_top_budget.setTransformationMethod(method);

            iv_top_show.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        } else {
            HideReturnsTransformationMethod method = HideReturnsTransformationMethod.getInstance();
            tv_top_in.setTransformationMethod(method);
            tv_top_out.setTransformationMethod(method);
            tv_top_budget.setTransformationMethod(method);

            iv_top_show.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setMainTopShow();
    }

    private void setMainTopShow() {
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥"+outcomeOneDay+"  收入 ￥"+incomeOneDay;
        tv_top_con.setText(infoOneDay);

        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        tv_top_in.setText("￥"+incomeOneMonth);
        tv_top_out.setText("￥"+outcomeOneMonth);

        //设置显示运算剩余
        float bmoney = preferences.getFloat("budget", 0);//预算
        if (bmoney == 0) {
            tv_top_budget.setText("￥ 0");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            tv_top_budget.setText("￥"+syMoney);
        }
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
