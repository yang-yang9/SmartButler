package com.yangyang.smartbutler.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.adapter.ChatListAdapter;
import com.yangyang.smartbutler.entity.ChatListData;
import com.yangyang.smartbutler.utils.ShareUtils;
import com.yangyang.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.fragment
 *   文件名：ButlerFragment
 *   创建者：YangYang
 *   描述：管家服务Fragment
 */


public class ButlerFragment extends Fragment implements View.OnClickListener {
    private ListView lv_butler;
    //private Button btn_butler_left, btn_butler_right;

    private List<ChatListData> mList = new ArrayList<>();

    private String mfrom;
    private ChatListAdapter adapter;
    private ImageView iv_butler_speak;
    private EditText et_butler_text;
    private Button btn_butler_send;

    //TTS
    private SpeechSynthesizer mTts;

    public ButlerFragment(){

    }

    public static Fragment newInstance(String from){
        ButlerFragment butlerFragment = new ButlerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        butlerFragment.setArguments(bundle);

        return butlerFragment;
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
        View view = inflater.inflate(R.layout.fragment_butler, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);


        lv_butler = view.findViewById(R.id.lv_butler);
        iv_butler_speak = view.findViewById(R.id.iv_butler_speak);
        et_butler_text = view.findViewById(R.id.et_butler_text);
        btn_butler_send = view.findViewById(R.id.btn_butler_send);

        btn_butler_send.setOnClickListener(this);

        adapter = new ChatListAdapter(getActivity(), mList);
        lv_butler.setDividerHeight(0);
        lv_butler.setDivider(null);
        lv_butler.setAdapter(adapter);

        addLeftItem("主人，你好！");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_butler_send:
                String text = et_butler_text.getText().toString().trim();
                if (!TextUtils.isEmpty(text)){
                    if (text.length() > 30){
                        Toast.makeText(getActivity(), "字数已达上限", Toast.LENGTH_SHORT).show();
                    } else {
                        et_butler_text.setText("");
                        addRightItem(text);
                        String url = "https://api.jisuapi.com/iqa/query?appkey=" + StaticClass.QA_APP_KEY + "&question=" + text;

                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                parsingJson(t);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析返回的json数据
    private void parsingJson(String t) {
        try {
            JSONObject object = new JSONObject(t);
            JSONObject jsonResult = object.getJSONObject("result");

            String typeString = jsonResult.getString("type");
            if (typeString.equals("无回复")){
                addLeftItem("主人，很抱歉，管家不是特别懂");
            } else {
                String content = jsonResult.getString("content");
                addLeftItem(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text){
        boolean isSpeak = ShareUtils.getBoolean(getActivity(), "isSpeak", false);
        if (isSpeak){
            startSpeak(text);
        }


        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.TYPE_LEFT);
        data.setText(text);
        mList.add(data);
        adapter.notifyDataSetChanged();
        lv_butler.setSelection(lv_butler.getBottom());
    }

    //添加右边文本
    private void addRightItem(String text){
        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.TYPE_RIGHT);
        data.setText(text);
        mList.add(data);
        adapter.notifyDataSetChanged();
        lv_butler.setSelection(lv_butler.getBottom());
    }

    //开始播放
    private void startSpeak(String text){
        mTts.startSpeaking(text, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
}
