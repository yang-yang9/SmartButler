package com.yangyang.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.utils.L;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.fragment
 *   文件名：ArticleWebViewActivity
 *   创建者：YangYang
 *   描述：新闻详情页
 */

public class ArticleWebViewActivity extends BaseActivity {
    private ProgressBar mProgressBar;
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_web_view);

        initView();
    }

    private void initView() {
        mProgressBar = findViewById(R.id.pb_article_web_view);
        mWebView = findViewById(R.id.wv_article_web_view);

        //将带过来的数据设置进去
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");

        L.i(url);
        getSupportActionBar().setTitle(title);

        //支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        //设置接口回调
        mWebView.setWebChromeClient(new WebViewClient());

        //加载网页
        mWebView.loadUrl(url);

        //本地显示，不跳转到浏览器
        mWebView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public class WebViewClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100){
                mProgressBar.setVisibility(View.GONE);
            }

            super.onProgressChanged(view, newProgress);
        }
    }
}
