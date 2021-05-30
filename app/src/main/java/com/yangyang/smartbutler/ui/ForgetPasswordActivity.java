package com.yangyang.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：ForgetPasswordActivity
 *   创建者：YangYang
 *   描述：忘记/重置密码页
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_old_password;
    private EditText et_new_password;
    private EditText et_ack_password;
    private Button btn_change_password;
    private EditText et_email;
    private Button btn_forget_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
    }

    private void initView() {
        et_old_password = findViewById(R.id.et_forget_old_password);
        et_new_password = findViewById(R.id.et_forget_new_password);
        et_ack_password = findViewById(R.id.et_forget_ack_password);
        btn_change_password = findViewById(R.id.btn_forget_change_password);
        et_email = findViewById(R.id.et_forget_email);
        btn_forget_password = findViewById(R.id.btn_forget_password);

        btn_change_password.setOnClickListener(this);
        btn_forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forget_change_password:
                String old_password = et_old_password.getText().toString().trim();
                String new_password = et_new_password.getText().toString().trim();
                String ack_password = et_ack_password.getText().toString().trim();
                if (TextUtils.isEmpty(old_password)){
                    Snackbar.make(et_old_password, "请输入旧密码", Snackbar.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(new_password)){
                    Snackbar.make(et_new_password, "请输入新密码", Snackbar.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(ack_password)){
                    Snackbar.make(et_ack_password, "请确认密码", Snackbar.LENGTH_LONG).show();
                } else if (!TextUtils.isEmpty(old_password) &&
                        !TextUtils.isEmpty(new_password) &&
                        !TextUtils.isEmpty(ack_password)){
                    if (new_password.equals(ack_password)){
                        User.updateCurrentUserPassword(old_password, ack_password, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码失败: " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_forget_password:
                String email = et_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)){
                    //TODO 判断是否是邮箱格式
                    User.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Snackbar.make(et_email, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", Snackbar.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "邮箱发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
