package com.yangyang.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：ShareUtils
 *   创建者：YangYang
 *   描述：注册页
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_age;
    private EditText et_describe;
    private RadioGroup mRadioGroup;
    private EditText et_pass;
    private EditText et_password;
    private EditText et_email;
    private Button btn_register;

    private boolean isBoy = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_age = findViewById(R.id.et_age);
        et_describe = findViewById(R.id.et_describe);
        mRadioGroup = findViewById(R.id.mRadioGroup);
        et_pass = findViewById(R.id.et_pass);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        btn_register = findViewById(R.id.btnRegistered);

        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistered:
                //拿到输入框的值
                String username = et_username.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String describe = et_describe.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(age) &&
                        !TextUtils.isEmpty(pass) &&
                        !TextUtils.isEmpty(password) &&
                        !TextUtils.isEmpty(email)){

                    //两次输入的密码相同然后进行注册
                    if(pass.equals(password)){
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.rb_boy){
                                    isBoy = true;
                                } else {
                                    isBoy = false;
                                }
                            }
                        });

                        if (TextUtils.isEmpty(describe)){
                            describe = "这个人很懒，什么都没有";
                        }

                        //注册
                        User user = new User();
                        user.setUsername(username);
                        user.setAge(Integer.parseInt(age));
                        user.setDescribe(describe);
                        user.setSex(isBoy);
                        user.setPassword(pass);
                        user.setEmail(email);
                        
                        user.signUp(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                if (e == null){
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    if (e.getErrorCode() == 202) {
                                        Toast.makeText(RegisterActivity.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
                                    } else if (e.getErrorCode() == 203) {
                                        Toast.makeText(RegisterActivity.this, "邮箱已经被注册", Toast.LENGTH_SHORT).show();
                                    } else if (e.getErrorCode() == 203) {
                                        Toast.makeText(RegisterActivity.this, "邮箱已经存在", Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(RegisterActivity.this, "注册失败: " + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    } else {
                        Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    }

                } else if (TextUtils.isEmpty(username)){
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(age)){
                    Toast.makeText(this, "年龄不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)){
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
                }  else if (TextUtils.isEmpty(email)){
                    Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
