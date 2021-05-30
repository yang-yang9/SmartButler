package com.yangyang.smartbutler.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.service.SmsService;
import com.yangyang.smartbutler.utils.L;
import com.yangyang.smartbutler.utils.ShareUtils;
import com.yangyang.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.ui
 *   文件名：SettingActivity
 *   创建者：YangYang
 *   描述：设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private Switch sw_speak;
    private Switch sw_SMS;

    private LinearLayout ll_update;
    private TextView tv_version;
    private LinearLayout ll_about;

    private String versionName;
    private int versionCode;

    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        sw_speak = findViewById(R.id.sw_setting_speak);
        sw_speak.setOnClickListener(this);

        sw_SMS = findViewById(R.id.sw_setting_SMS_reminder);
        sw_SMS.setOnClickListener(this);

        boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);

        boolean isSMS = ShareUtils.getBoolean(this, "isSMS", false);
        sw_SMS.setChecked(isSMS);

        ll_update = findViewById(R.id.ll_setting_update);
        ll_update.setOnClickListener(this);


        tv_version = findViewById(R.id.tv_setting_version);

        try {
            getVersionNameCode();
            tv_version.setText("检测版本：" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            tv_version.setText("检测版本");
        }

        ll_about = findViewById(R.id.ll_setting_about);
        ll_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sw_setting_speak:
                //切换相反
                sw_speak.setSelected(!sw_speak.isSelected());
                //保存状态
                ShareUtils.putBoolean(this, "isSpeak", sw_speak.isChecked());
                break;
            case R.id.sw_setting_SMS_reminder:
                sw_SMS.setSelected(!sw_SMS.isSelected());
                ShareUtils.putBoolean(this, "isSMS", sw_SMS.isChecked());
                if (sw_SMS.isChecked()){
                    L.i("开启");
                    Toast.makeText(this, "开启", Toast.LENGTH_SHORT).show();
                    startService(new Intent(this, SmsService.class));
                } else {
                    L.i("关闭");
                    Toast.makeText(this, "关闭", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(this, SmsService.class));
                }
                break;
            case R.id.ll_setting_update:
                //Toast.makeText(this, "检测更新", Toast.LENGTH_SHORT).show();

                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i("json:" + t);

                        parsingJson(t);
                    }

                    @Override
                    public void onFailure(VolleyError error) {
                        L.i("失败：" + error.toString());
                    }
                });
                break;
            case R.id.ll_setting_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            if (code > versionCode){
                showUpdateDialog(jsonObject.getString("content"));
            } else {
                Toast.makeText(this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //弹出升级对话框
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle("新版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SettingActivity.this, UpdateActivity.class);
                        intent.putExtra("url", url);

                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //默认执行dismiss
            }
        }).show();
    }

    //获取版本号
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        //0代表查询本地
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }
}
