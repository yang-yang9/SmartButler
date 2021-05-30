package com.yangyang.bookkeeping.weight;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.yangyang.smartbutler.R;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.bookkeeping.weight
 *   文件名：RemarkDialog
 *   创建者：YangYang
 *   描述：添加备注弹窗
 */


public class RemarkDialog extends Dialog implements View.OnClickListener {
    private EditText et_remark;
    private Button btn_cancel, btn_ensure;
    private OnEnsureListener listener;

    public void setEnsureListener(OnEnsureListener listener) {
        this.listener = listener;
    }

    public RemarkDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_remark);

        initView();
        //setDialogSize();
    }

    public void setDialogSize() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Display d = getWindow().getWindowManager().getDefaultDisplay();
        params.width = d.getWidth();
        params.gravity = Gravity.BOTTOM;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setAttributes(params);

        handler.sendEmptyMessageDelayed(1, 200);
    }

    private void initView() {
        et_remark = findViewById(R.id.et_dialog_remark);
        btn_ensure = findViewById(R.id.btn_dialog_remark_ensure);
        btn_cancel = findViewById(R.id.btn_dialog_remark_cancel);

        et_remark.setOnClickListener(this);
        btn_ensure.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    /**接口作用：在调用接口的地方写好要做的事，设置进来，在点击确认可以调用
     * */
    public interface OnEnsureListener {
        public void onEnsure();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_dialog_remark:

                break;
            case R.id.btn_dialog_remark_ensure:
                if (listener != null){
                    listener.onEnsure();
                }
                break;
            case R.id.btn_dialog_remark_cancel:
                cancel();
                break;
        }
    }


    public String getEditText() {
        return et_remark.getText().toString().trim();
    }

    //自动弹出软键盘延迟
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
