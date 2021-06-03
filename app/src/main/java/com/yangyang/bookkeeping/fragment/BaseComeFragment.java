package com.yangyang.bookkeeping.fragment;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yangyang.bookkeeping.adapter.TypeBaseAdapter;
import com.yangyang.bookkeeping.entity.AccountBean;
import com.yangyang.bookkeeping.entity.TypeBean;
import com.yangyang.bookkeeping.utils.KeyBoardUtils;
import com.yangyang.bookkeeping.weight.PickerDialog;
import com.yangyang.bookkeeping.weight.RemarkDialog;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.fragment
 *   文件名：BaseComeFragment
 *   创建者：YangYang
 *   描述：收/支公共代码
 */


public abstract class BaseComeFragment extends Fragment implements View.OnClickListener {
    protected KeyboardView keyboardView;
    protected EditText et_money;
    protected ImageView iv_type;
    protected TextView tv_type,tv_beizhu,tv_time;
    protected GridView gv_type;
    protected List<TypeBean> typeList;
    protected TypeBaseAdapter typeBaseAdapter;
    protected AccountBean accountBean;
    //User user = BmobUser.getCurrentUser(User.class);
    BmobUser user = BmobUser.getCurrentUser(User.class);
    String uId = user.getObjectId();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();
        accountBean.setuId(uId);
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_out_come, container, false);
        initView(view);

        loadDataToGridView();
        setGridViewItemListener();
        setInitTime();
        return view;
    }

    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = format.format(date);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        tv_time.setText(time);

        accountBean.setTime(time);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);

    }

    private void setGridViewItemListener() {
        gv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeBaseAdapter.selectPosition = position;
                typeBaseAdapter.notifyDataSetInvalidated();
                TypeBean typeBean = typeList.get(position);
                String selectName = typeBean.getTypename();
                int sImageId = typeBean.getsImageId();

                tv_type.setText(selectName);
                iv_type.setImageResource(sImageId);

                accountBean.setTypename(selectName);
                accountBean.setsImageId(sImageId);
            }
        });
    }

    //从数据库拿到类型加载到gv
    protected void loadDataToGridView() {
        typeList = new ArrayList<>();
        typeBaseAdapter = new TypeBaseAdapter(getContext(), typeList);
        gv_type.setAdapter(typeBaseAdapter);


    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.keyboard_frag_record);
        et_money = view.findViewById(R.id.et_frag_record_money);
        iv_type = view.findViewById(R.id.iv_frag_record);
        tv_type = view.findViewById(R.id.tv_frag_record_type);
        tv_beizhu = view.findViewById(R.id.tv_frag_record_beizhu);
        tv_time = view.findViewById(R.id.tv_frag_record_time);
        gv_type = view.findViewById(R.id.gv_frag_record);

        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, et_money);
        boardUtils.showKeyboard();
        //设置接口，监听确定按钮按钮被点击了
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {

                String moneyStr = et_money.getText().toString();
                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);

                saveAccountToDB();
                // 点了确认返回上一级页面
                getActivity().finish();
            }
        });

        tv_beizhu.setOnClickListener(this);
        tv_time.setOnClickListener(this);
    }

    protected abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_frag_record_time:
                showTimeDialog();
                break;
            case R.id.tv_frag_record_beizhu:
                showRemarkDialog();
                break;
        }
    }

    private void showRemarkDialog() {
        RemarkDialog dialog = new RemarkDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setEnsureListener(new RemarkDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String remark = dialog.getEditText();
                if (!TextUtils.isEmpty(remark)) {
                    tv_beizhu.setText(remark);
                    accountBean.setBeizhu(remark);
                }
                dialog.cancel();
            }
        });
    }

    private void showTimeDialog() {
        PickerDialog dialog = new PickerDialog(getContext());
        dialog.show();
        dialog.setEnsureListener(new PickerDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                tv_time.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }
}
