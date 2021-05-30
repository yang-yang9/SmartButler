package com.yangyang.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yangyang.bookkeeping.BookKeepingActivity;
import com.yangyang.flashlight.FlashlightActivity;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.ui.ExpressActivity;
import com.yangyang.smartbutler.ui.GirlCommunityActivity;
import com.yangyang.smartbutler.ui.PlaceQueryActivity;
import com.yangyang.smartbutler.ui.WeChatArticleActivity;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.fragment
 *   文件名：ButlerFragment
 *   创建者：YangYang
 *   描述：功能合集Fragment
 */


public class FunctionFragment extends Fragment implements View.OnClickListener {
    private String mfrom;

    private LinearLayout ll_express_inquiry;
    private LinearLayout ll_place_inquiry;
    private LinearLayout ll_weixin_article;
    private LinearLayout ll_girl_community;
    private LinearLayout ll_flashlight;
    private LinearLayout ll_bookkeeping;

    public FunctionFragment(){

    }

    public static Fragment newInstance(String from){
        FunctionFragment functionFragment = new FunctionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        functionFragment.setArguments(bundle);

        return functionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mfrom = (String) getArguments().get("from");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_express_inquiry = view.findViewById(R.id.ll_function_express_inquiry);
        ll_place_inquiry = view.findViewById(R.id.ll_function_place_inquiry);
        ll_weixin_article = view.findViewById(R.id.ll_function_weixin_article);
        ll_girl_community = view.findViewById(R.id.ll_function_girl_image);
        ll_flashlight = view.findViewById(R.id.ll_function_flashlight);
        ll_bookkeeping = view.findViewById(R.id.ll_function_bookkeeping);

        ll_express_inquiry.setOnClickListener(this);
        ll_place_inquiry.setOnClickListener(this);
        ll_weixin_article.setOnClickListener(this);
        ll_girl_community.setOnClickListener(this);
        ll_flashlight.setOnClickListener(this);
        ll_bookkeeping.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_function_express_inquiry:
                startActivity(new Intent(getActivity(), ExpressActivity.class));
                break;
            case R.id.ll_function_place_inquiry:
                startActivity(new Intent(getActivity(), PlaceQueryActivity.class));
                break;
            case R.id.ll_function_weixin_article:
                startActivity(new Intent(getActivity(), WeChatArticleActivity.class));
                break;
            case R.id.ll_function_girl_image:
                startActivity(new Intent(getActivity(), GirlCommunityActivity.class));
                break;
            case R.id.ll_function_flashlight:
                startActivity(new Intent(getActivity(), FlashlightActivity.class));
                break;
            case R.id.ll_function_bookkeeping:
                startActivity(new Intent(getActivity(), BookKeepingActivity.class));
                break;
        }
    }
}
