package com.yangyang.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.utils
 *   文件名：ShareUtils
 *   创建者：YangYang
 *   描述：引导页
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager guideViewPager;

    private List<View> mList = new ArrayList<>();
    private View view1, view2, view3;

    private LinearLayout ll_guide;
    private ImageView point1, point2,point3;
    private ImageView iv_back;

    private TextView tv_one, tv_two, tv_three;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();


    }

    private void initView() {
        guideViewPager = findViewById(R.id.viewpager_guide);

        view1 = View.inflate(this, R.layout.pager_item_one, null);
        view2 = View.inflate(this, R.layout.pager_item_two, null);
        view3 = View.inflate(this, R.layout.pager_item_three, null);

        //给pager3的按钮设置点击事件
        view3.findViewById(R.id.btn_start).setOnClickListener(this);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        guideViewPager.setAdapter(new GuideAdapter());

        //引导页小圆点
        point1 = findViewById(R.id.point_one);
        point2 = findViewById(R.id.point_two);
        point3 = findViewById(R.id.point_three);
        setPointImg(true, false, false);

        ll_guide = findViewById(R.id.ll_guide);

        //引导页跳过
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        guideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        ll_guide.setVisibility(View.VISIBLE);
                        setPointImg(true, false, false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        ll_guide.setVisibility(View.VISIBLE);
                        setPointImg(false, true, false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        ll_guide.setVisibility(View.INVISIBLE);
                        setPointImg(false, false, true);
                        iv_back.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_one = view1.findViewById(R.id.tv_guide_one);
        tv_two = view2.findViewById(R.id.tv_guide_two);
        tv_three = view3.findViewById(R.id.tv_guide_three);

        UtilTools.setFontShouJi(GuideActivity.this, tv_one);
        UtilTools.setFontShouJi(GuideActivity.this, tv_two);
        UtilTools.setFontShouJi(GuideActivity.this, tv_three);
    }




    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewPager)container).addView(mList.get(position));

            return mList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager)container).removeView(mList.get(position));
            //super.destroyItem(container, position, object);
        }
    }

    private void setPointImg(boolean isChecked1, boolean isChecked2, boolean isChecked3) {
        if (isChecked1){
            point1.setImageResource(R.drawable.point_on);
        } else {
            point1.setImageResource(R.drawable.point_off);
        }

        if (isChecked2){
            point2.setImageResource(R.drawable.point_on);
        } else {
            point2.setImageResource(R.drawable.point_off);
        }

        if (isChecked3){
            point3.setImageResource(R.drawable.point_on);
        } else {
            point3.setImageResource(R.drawable.point_off);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
            case R.id.iv_back:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
