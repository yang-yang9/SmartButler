package com.yangyang.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.utils.L;
import com.yangyang.smartbutler.utils.StaticClass;
import com.yangyang.smartbutler.view.DispatchLinearlayout;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.fragment
 *   文件名：SmsService
 *   创建者：YangYang
 *   描述：短信提醒服务
 */

public class SmsService extends Service implements View.OnClickListener {
    private SmsReceiver smsReceiver;
    private String smsPhone;
    private String smsContent;

    //窗口管理器
    private WindowManager wm;
    //布局参数
    private WindowManager.LayoutParams layoutParams;
    //窗口布局
    private DispatchLinearlayout mView;

    private TextView tv_phone;
    private TextView tv_content;
    private Button btn_reply;

    private HomeWitchReceiver mHomeWitchReceiver;
    public static final String SYSTEM_DIALOGS_RESON_KEY = "reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY = "homekey";

    public SmsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        L.i("init service");

        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StaticClass.SMS_ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);

        registerReceiver(smsReceiver, intentFilter);

        mHomeWitchReceiver = new HomeWitchReceiver();
        IntentFilter intentFilter1 = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeWitchReceiver, intentFilter1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");

        unregisterReceiver(smsReceiver);
        unregisterReceiver(mHomeWitchReceiver);
    }



    public class SmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StaticClass.SMS_ACTION.equals(action)){
                
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                for (Object obj : objs) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    smsPhone = sms.getOriginatingAddress();
                    smsContent = sms.getMessageBody();
                    L.i("短信内容：" + smsPhone + ":" + smsContent);
                    showWindow();
                }
            }

        }
    }

    //收到短信时弹窗提示
    private void showWindow() {
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        //标记
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //格式：透明
        layoutParams.format = PixelFormat.TRANSPARENT;
        // 设置窗体显示类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type =WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mView = (DispatchLinearlayout) View.inflate(getApplicationContext(), R.layout.sms_window_item, null);

        tv_phone = mView.findViewById(R.id.tv_sms_window_phone);
        tv_content = mView.findViewById(R.id.tv_sms_window_content);
        btn_reply = mView.findViewById(R.id.btn_sms_window_reply);

        tv_phone.setText("发件人：" + smsPhone);
        tv_content.setText(smsContent);
        btn_reply.setOnClickListener(this);

        wm.addView(mView, layoutParams);

        mView.setDispatchKeyEventListener(dispatchKeyEventListener);

    }

    private DispatchLinearlayout.DispatchKeyEventListener dispatchKeyEventListener = new DispatchLinearlayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否是按了返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                L.i("点击了BACK键");
                if (mView.getParent() != null){
                    wm.removeView(mView);
                }
                return true;
            }

            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sms_window_reply:
                sendSMS();

                //跳转到发短信页面，窗口消失
                if (mView.getParent() != null){
                    wm.removeView(mView);
                }
                break;
        }
    }
    //回复短信
    private void sendSMS() {
        Uri uri = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    //监听Home键的广播，用来关闭短信提示窗口
    class HomeWitchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_RESON_KEY);
                if (SYSTEM_DIALOGS_HOME_KEY.equals(reason)){
                    L.i("点击了HOME键");
                    if (mView.getParent() != null){
                        wm.removeView(mView);
                    }
                }

            }
        }
    }
}
