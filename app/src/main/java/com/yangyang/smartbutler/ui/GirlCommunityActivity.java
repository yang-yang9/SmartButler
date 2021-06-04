package com.yangyang.smartbutler.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.adapter.GirlGridAdapter;
import com.yangyang.smartbutler.entity.GirlData;
import com.yangyang.smartbutler.utils.L;
import com.yangyang.smartbutler.utils.PicassoUtils;
import com.yangyang.smartbutler.utils.StaticClass;
import com.yangyang.smartbutler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.ui
 *   文件名：GirlCommunityActivity
 *   创建者：YangYang
 *   描述：美女社区
 */

public class GirlCommunityActivity extends BaseActivity {
    private LinearLayout llProgress;
    private GridView mGridView;

    private List<GirlData> mList = new ArrayList<>();
    //适配器
    private GirlGridAdapter mAdapter;
    //提示框
    private CustomDialog dialog;
    //预览图片
    private ImageView iv_img;
    private ImageView iv_close;
    //图片地址的数据
    private List<String> mListUrl = new ArrayList<>();
    //PhotoView
    private PhotoViewAttacher mAttacher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl_community);

        //initView();
    }

    private void initView() {
        llProgress = findViewById(R.id.ll_progress);
        mGridView = findViewById(R.id.gv_girl_community);

        //初始化提示框
        dialog = new CustomDialog(this, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, R.layout.dialog_girl,
                R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        iv_img = (ImageView) dialog.findViewById(R.id.iv_img);
        iv_close = dialog.findViewById(R.id.iv_close);

        /*String welfare = null;
        try {
            //Gank升級 需要转码
            welfare = URLEncoder.encode(getString(R.string.text_welfare), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        //解析
        int startId = getRandom(1, 10) * 10;
        String url = StaticClass.GIRL_URL + "&start=" + startId + "&count=30";
        //Toast.makeText(this, startId+"数量", Toast.LENGTH_SHORT).show();
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("Girl Json:" + t);
                parsingJson(t);
                llProgress.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);
            }
        });

        //监听点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                PicassoUtils.loadImageView(mListUrl.get(position), iv_img);
                //缩放
                mAttacher = new PhotoViewAttacher(iv_img);
                //刷新
                mAttacher.update();
                dialog.show();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String url = json.getString("url");

                mListUrl.add(url);

                GirlData data = new GirlData();
                data.setImgUrl(url);
                mList.add(data);
            }
            mAdapter = new GirlGridAdapter(this, mList);
            //设置适配器
            mGridView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取重定向地址
     *
     * @param path
     * @return
     * @throws Exception
     */
    private String getRedirectUrl(String path) {
        String url = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(5000);
            url = conn.getHeaderField("Location");
            L.i("Location:" + url);
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }


    public int getRandom(int start, int end){
        return (int)(Math.random() * (end-start+1) + start);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}
