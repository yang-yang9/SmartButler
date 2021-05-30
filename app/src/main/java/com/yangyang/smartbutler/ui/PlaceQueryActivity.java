package com.yangyang.smartbutler.ui;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.utils.L;
import com.yangyang.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.fragment
 *   文件名：PlaceQueryActivity
 *   创建者：YangYang
 *   描述：归属地查询
 */

public class PlaceQueryActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_number;
    private ImageView iv_company;
    private TextView tv_result;
    private Button btn_one, btn_two, btn_three, btn_del,
            btn_four, btn_five, btn_six, btn_zero,
            btn_seven, btn_eight, btn_nine, btn_query;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_query);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.activity_place_query);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);*/

        initView();

    }

    private void initView() {
        et_number = findViewById(R.id.et_place_query_number);
        et_number.setInputType(InputType.TYPE_NULL);

        iv_company = findViewById(R.id.iv_place_query_company);
        tv_result = findViewById(R.id.tv_place_query_result);
        btn_one = findViewById(R.id.btn_one);
        btn_two = findViewById(R.id.btn_two);
        btn_three = findViewById(R.id.btn_three);
        btn_del = findViewById(R.id.btn_delete);
        btn_four = findViewById(R.id.btn_four);
        btn_five = findViewById(R.id.btn_five);
        btn_six = findViewById(R.id.btn_six);
        btn_zero = findViewById(R.id.btn_zero);
        btn_seven = findViewById(R.id.btn_seven);
        btn_eight = findViewById(R.id.btn_eight);
        btn_nine = findViewById(R.id.btn_nine);
        btn_query = findViewById(R.id.btn_query);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_zero.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
        btn_query.setOnClickListener(this);

        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_number.setText("");
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        String str = et_number.getText().toString().trim();
        switch (v.getId()){
            case R.id.btn_one:
            case R.id.btn_two:
            case R.id.btn_three:
            case R.id.btn_four:
            case R.id.btn_five:
            case R.id.btn_six:
            case R.id.btn_seven:
            case R.id.btn_eight:
            case R.id.btn_nine:
            case R.id.btn_zero:
                if (flag){
                    flag = false;
                    str = "";
                    et_number.setText("");
                }
                et_number.setText(str + ((Button)v).getText());
                et_number.setSelection(str.length() + 1);
                break;
            case R.id.btn_delete:
                if (!TextUtils.isEmpty(str) && str.length() > 0){
                    et_number.setText(str.substring(0, str.length() - 1));
                    et_number.setSelection(str.length() - 1);
                }
                break;
            case R.id.btn_query:
                if (!TextUtils.isEmpty(str)){
                    getPhone(str);
                }
                break;
        }
    }

    private void getPhone(String str) {
        String url = "http://apis.juhe.cn/mobile/get?phone=" + str + "&key=" + StaticClass.PLACE_QUERY_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i(t);
                parsingJson(t);
            }
        });
    }


    /*
    * {
"resultcode":"200",
"reason":"Return Successd!",
"result":{
    "province":"浙江",
    "city":"杭州",
    "areacode":"0571",
    "zip":"310000",
    "company":"中国移动",
    "card":""
}
}
    * */
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultcode = jsonObject.getInt("resultcode");
            if (resultcode == 200){
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                String province = jsonResult.getString("province");
                String city = jsonResult.getString("city");
                String areacode = jsonResult.getString("areacode");
                String zip = jsonResult.getString("zip");
                String company = jsonResult.getString("company");

                tv_result.setText("归属地:" + province + city + "\n"
                        + "区号:" + areacode + "\n"
                        + "邮编:" + zip + "\n"
                        + "运营商:" + company + "\n");
                /*tv_result.append("归属地:" + province + city + "\n");
                tv_result.append("区号:" + areacode + "\n");
                tv_result.append("邮编:" + zip + "\n");
                tv_result.append("运营商:" + company + "\n");*/

                switch (company){
                    case "移动":
                        iv_company.setBackgroundResource(R.drawable.china_mobile);

                        break;
                    case "联通":
                        iv_company.setBackgroundResource(R.drawable.china_unicom);

                        break;
                    case "电信":
                        iv_company.setBackgroundResource(R.drawable.china_telecom);

                        break;
                }
                flag = true;
            } else if (resultcode == 203){
                int error_code = jsonObject.getInt("error_code");
                if (error_code == 201101){
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                } else if (error_code == 201102){
                    Toast.makeText(this, "错误的手机号码", Toast.LENGTH_SHORT).show();
                } else if (error_code == 201103){
                    Toast.makeText(this, "查询无结果", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
