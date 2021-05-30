package com.yangyang.flashlight.ui;
/*
 *   项目名：SmartButler
 *   包名：com.yangyang.flashlight.ui
 *   文件名：Morse
 *   创建者：YangYang
 *   描述：摩尔斯电码
 */


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.yangyang.smartbutler.R;

import java.util.HashMap;
import java.util.Map;

public class Morse extends WarningLight {
    private final int DOT_TIME = 200;
    private final int LINE_TIME = DOT_TIME * 3;
    private final int DOT_LINE_TIME = DOT_TIME;
    private final int CHAR_CHAR_TIME = DOT_TIME * 3;
    private final int WORD_WORD_TIME = DOT_TIME * 7;

    private String mMorseCode;
    private Map<Character, String> mMorseCodeMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

    }

    @Override
    protected void initView() {
        super.initView();
        btn_send_morse = findViewById(R.id.btn_flashlight_send_morse);
        btn_send_morse.setOnClickListener(this);
    }

    private void initData() {
        mMorseCodeMap.put('a', ".-");
        mMorseCodeMap.put('b', "-...");
        mMorseCodeMap.put('c', "-.-.");
        mMorseCodeMap.put('d', "-..");
        mMorseCodeMap.put('e', ".");
        mMorseCodeMap.put('f', "..-.");
        mMorseCodeMap.put('g', "--.");
        mMorseCodeMap.put('h', "....");
        mMorseCodeMap.put('i', "..");
        mMorseCodeMap.put('j', ".---");
        mMorseCodeMap.put('k', "-.-");
        mMorseCodeMap.put('l', ".-..");
        mMorseCodeMap.put('m', "--");
        mMorseCodeMap.put('n', "-.");
        mMorseCodeMap.put('o', "---");
        mMorseCodeMap.put('p', ".--.");
        mMorseCodeMap.put('q', "--.-");
        mMorseCodeMap.put('r', ".-.");
        mMorseCodeMap.put('s', "...");
        mMorseCodeMap.put('t', "-");
        mMorseCodeMap.put('u', "..-");
        mMorseCodeMap.put('v', "...-");
        mMorseCodeMap.put('w', ".--");
        mMorseCodeMap.put('x', "-..-");
        mMorseCodeMap.put('y', "-.--");
        mMorseCodeMap.put('z', "--..");

        mMorseCodeMap.put('0', "-----");
        mMorseCodeMap.put('1', ".----");
        mMorseCodeMap.put('2', "..---");
        mMorseCodeMap.put('3', "...--");
        mMorseCodeMap.put('4', "....-");
        mMorseCodeMap.put('5', ".....");
        mMorseCodeMap.put('6', "-....");
        mMorseCodeMap.put('7', "--...");
        mMorseCodeMap.put('8', "---..");
        mMorseCodeMap.put('9', "----.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_flashlight_send_morse:
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                    Toast.makeText(this, "当前设备没有闪光灯", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verifyMorseCode()){
                    sendSentense(mMorseCode);
                }
                break;
        }
    }

    protected void sleepExt(long t){
        try {
            Thread.sleep(t);
        } catch (Exception e){

        }
    }

    //发送点
    private void sendDot(){
        openFlashlight();
        sleepExt(DOT_TIME);
        closeFlashlight();
    }

    //发送线
    private void sendLine(){
        openFlashlight();
        sleepExt(LINE_TIME);
        closeFlashlight();
    }

    //发送字符
    private void sendChar(char c){
        String morseCode = mMorseCodeMap.get(c);
        if (morseCode != null){
            char lastChar = ' ';
            for (int i=0; i<morseCode.length(); i++){
                char dotLine = morseCode.charAt(i);
                if (dotLine == '.'){
                    sendDot();
                } else if (dotLine == '-'){
                    sendLine();
                }
                if (i > 0 && i < morseCode.length() - 1){
                    if (lastChar == '.' && dotLine == '-'){
                        sleepExt(DOT_LINE_TIME);
                    }
                }
                lastChar = dotLine;
            }
        }
    }

    //发送单词
    private void sendWord(String s){
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sendChar(c);
            if (i < s.length() - 1){
                sleepExt(CHAR_CHAR_TIME);
            }
        }
    }

    private void sendSentense(String s){
        String[] words = s.split(" +");
        for (int i = 0; i < words.length; i++) {
            sendWord(words[i]);
            if (i < words.length - 1){
                sleepExt(WORD_WORD_TIME);
            }
        }
        Toast.makeText(this, "摩尔斯电码已经发送完成！", Toast.LENGTH_SHORT).show();
    }

    private boolean verifyMorseCode(){
        mMorseCode = et_morse_code.getText().toString().trim().toLowerCase();
        if (TextUtils.isEmpty(mMorseCode)){
            Toast.makeText(this, "请输入摩尔斯电码", Toast.LENGTH_LONG).show();
            return false;
        }

        for (int i = 0; i < mMorseCode.length(); i++) {
            char c = mMorseCode.charAt(i);
            if (!(c >= 'a' && c <= 'z') && !(c >= '0' && c <= '9') && c != ' '){
                Toast.makeText(this, "摩尔斯电码只能是字母、数字和空格", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}
