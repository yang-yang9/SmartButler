package com.yangyang.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.adapter.WeChatAdapter;
import com.yangyang.smartbutler.entity.WeChatData;
import com.yangyang.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.fragment
 *   文件名：WeChatArticleActivity
 *   创建者：YangYang
 *   描述：微信精选文章
 */
public class WeChatArticleActivity extends BaseActivity {
    private ListView mListView;
    private List<WeChatData> mList = new ArrayList<>();
    private int start = 0;
    private int num = 15;
    private List<String> mListTitle = new ArrayList<>();
    private List<String> mListUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_article);
        initView();
    }

    private void initView() {
        mListView = findViewById(R.id.lv_wechat_article);

        String url = " https://api.jisuapi.com/weixinarticle/get?channelid=1&start=" + start + "&num=" + num + "&appkey=" + StaticClass.QA_APP_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(WeChatArticleActivity.this, t, Toast.LENGTH_SHORT).show();
                //L.i(t);
                parsingJson(t);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WeChatArticleActivity.this, ArticleWebViewActivity.class);
                intent.putExtra("title", mListTitle.get(position));
                intent.putExtra("url", mListUrl.get(position));

                startActivity(intent);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int status = jsonObject.getInt("status");
            if (status == 0){
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                JSONArray jsonList = jsonResult.getJSONArray("list");
                for (int i = 0; i < jsonList.length(); i++) {
                    JSONObject json = (JSONObject) jsonList.get(i);
                    WeChatData data = new WeChatData();

                    String title = json.getString("title");
                    String url = json.getString("url");
                    data.setTitle(title);
                    data.setWeixinname(json.getString("weixinname"));
                    data.setTime(json.getString("time"));
                    data.setImgUrl(json.getString("pic"));
                    data.setNewsUrl(url);

                    mList.add(data);
                    mListTitle.add(title);
                    mListUrl.add(url);
                }

                WeChatAdapter adapter = new WeChatAdapter(this, mList);
                mListView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
