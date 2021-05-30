package com.yangyang.smartbutler.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yangyang.smartbutler.MainActivity;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;
import com.yangyang.smartbutler.utils.L;
import com.yangyang.smartbutler.utils.ShareUtils;
import com.yangyang.smartbutler.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：LoginActivity
 *   创建者：YangYang
 *   描述：登录页
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private Button btn_register;
    private EditText et_login_username;
    private EditText et_login_password;
    private CheckBox cb_login_remember_pass;
    private TextView tv_login_forget_pass;

    private CustomDialog dialog;
    private AlertDialog mDialog;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;//权限请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestPermission();
        checkPermission1();

        //initView();
    }

    private void checkPermission1() {
        //收到短信时弹出窗口权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(getApplicationContext())) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,100);
            }
            if (!Settings.System.canWrite(this)) {
                //ToastUtil.longTips("请在该设置页面勾选，才可以使用路况提醒功能");
                Uri selfPackageUri = Uri.parse("package:"
                        + this.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        selfPackageUri);
                startActivity(intent);
            }
        }

    }

    public void requestPermission() {
        //权限检查
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            initView();
        }
    }



    private void initView() {
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        cb_login_remember_pass = findViewById(R.id.cb_login_remember_pass);
        tv_login_forget_pass = findViewById(R.id.tv_login_forget_pass);

        dialog = new CustomDialog(this, 500, 500, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //dialog显示时屏幕点击无效
        dialog.setCancelable(false);


        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_login_forget_pass.setOnClickListener(this);

        //根据sharepreference判断是否应该显示密码
        boolean isChecked = ShareUtils.getBoolean(this, "rememberPass", false);
        String remember_pass = ShareUtils.getString(this, "username", "");
        if (!remember_pass.equals("")){
            et_login_username.setText(remember_pass);
        }
        if (isChecked){
            et_login_password.setText(ShareUtils.getString(this, "password", ""));
            cb_login_remember_pass.setChecked(isChecked);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String username = et_login_username.getText().toString().trim();
                String password = et_login_password.getText().toString().trim();
                
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                    dialog.show();

                    try {
                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        user.login(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                dialog.dismiss();
                                if (e == null){
                                    if (user.getEmailVerified()){
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "邮箱尚未验证！", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //TODO 不同情况弹出不同的Toast
                                    if (e.getErrorCode() == 101) {
                                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "连接超时，请检查网络状况", Toast.LENGTH_SHORT).show();
                                    }

                                    //Toast.makeText(LoginActivity.this, "登陆失败： " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e){
                        L.e("有错误:loginactivity");
                        e.printStackTrace();
                    }

                }
                
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_login_forget_pass:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            } else {
                initView();

            }

        }
    }

    AlertDialog mPermissionDialog;

    String mPackName = "com.huawei.liwenzhi.weixinasr";

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();

                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        //当登录页面销毁时保存用户名和密码的状态
        //记住密码单选框的状态
        ShareUtils.putBoolean(this, "rememberPass", cb_login_remember_pass.isChecked());
        //是否记住密码
        if (cb_login_remember_pass.isChecked()){
            //被勾选：保存密码
            ShareUtils.putString(this, "username", et_login_username.getText().toString().trim());
            ShareUtils.putString(this, "password", et_login_password.getText().toString().trim());

        } else {
            //不勾选：不保存且清除已有密码
            //ShareUtils.deleteShare(this, "password");
            ShareUtils.deleteShare(this, "password");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
