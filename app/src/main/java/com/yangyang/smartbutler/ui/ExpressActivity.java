package com.yangyang.smartbutler.ui;



import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.adapter.ExpressAdapter;
import com.yangyang.smartbutler.entity.ExpressData;
import com.yangyang.smartbutler.utils.L;
import com.yangyang.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.ui
 *   文件名：ExpressActivity
 *   创建者：YangYang
 *   描述：快递查询
 */

public class ExpressActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_express_name;
    private EditText et_express_number;
    private EditText et_express_phne_number;
    private Button btn_express_query;
    private ListView lv_express_result;
    private List<ExpressData> mList = new ArrayList<>();
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        initView();

    }

    private void initView() {
        et_express_name = findViewById(R.id.et_express_name);
        et_express_number = findViewById(R.id.et_express_number);
        et_express_phne_number = findViewById(R.id.et_express_phone_number);
        btn_express_query = findViewById(R.id.btn_express_query);
        lv_express_result = findViewById(R.id.lv_express_query_result);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //inputMethodManager.hideSoftInputFromWindow(et_express_name.getWindowToken(), 0);
        btn_express_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_express_query:
                String name = et_express_name.getText().toString().trim();
                String number = et_express_number.getText().toString().trim();
                String phoneNumber = et_express_phne_number.getText().toString().trim();

                String url  = "";
                if (TextUtils.isEmpty(phoneNumber)){
                    url = "https://api.jisuapi.com/express/query?appkey=" + StaticClass.QA_APP_KEY + "&type=" + name + "&number=" + number;
                } else {
                    url = "https://api.jisuapi.com/express/query?appkey=" + StaticClass.QA_APP_KEY + "&type=" + name + "&number=" + number + "&mobile=" + phoneNumber;
                }

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(this, "请输入快递公司名字", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(number)){
                    Toast.makeText(this, "单号不能为空", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)){
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t);
                            L.i("json:" + t);
                            parsingJson(t);
                        }
                    });
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                }
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int status = jsonObject.getInt("status");
            if (status == 0){
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                JSONArray jsonArray = jsonResult.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    ExpressData data = new ExpressData();
                    data.setStatus(json.getString("status"));
                    data.setTime(json.getString("time"));
                    mList.add(data);
                }

                ExpressAdapter adapter = new ExpressAdapter(this, mList);
                lv_express_result.setAdapter(adapter);
            } else if (status == 201){
                Toast.makeText(this, "快递单号为空", Toast.LENGTH_SHORT).show();
            } else if (status == 202){
                Toast.makeText(this, "快递公司为空", Toast.LENGTH_SHORT).show();
            } else if (status == 203){
                Toast.makeText(this, "快递公司不存在", Toast.LENGTH_SHORT).show();
            } else if (status == 204){
                Toast.makeText(this, "快递公司识别失败", Toast.LENGTH_SHORT).show();
            } else if (status == 205){
                Toast.makeText(this, "没有信息", Toast.LENGTH_SHORT).show();
            } else if (status == 206){
                Toast.makeText(this, "快递单号错误", Toast.LENGTH_SHORT).show();
            } else if (status == 207){
                Toast.makeText(this, "快递单号错误次数过多", Toast.LENGTH_SHORT).show();
            } else if (status == 208){
                Toast.makeText(this, "单号暂时没有信息", Toast.LENGTH_SHORT).show();
            } else if (status == 209){
                Toast.makeText(this, "单号没有信息", Toast.LENGTH_SHORT).show();
            } else if (status == 220){
                Toast.makeText(this, "需要收件人/寄件人手机号", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
