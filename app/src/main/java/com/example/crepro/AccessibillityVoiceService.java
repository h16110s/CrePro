package com.example.crepro;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class AccessibillityVoiceService extends AccessibilityService {
    Handler _handler = new Handler();
    int WAIT_TIME = 1000;
    public TextToSpeech tts ;
    @Override
    public  void onCreate(){
        super.onCreate();
        tts = new TextToSpeech(this,null);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo accessibilityNodeInfo = event.getSource();
        if (null == accessibilityNodeInfo)
            return;

        String className = accessibilityNodeInfo.getClassName().toString(); //!< 音声認識の判断クラス
        final CharSequence text = accessibilityNodeInfo.getText();  //!< 音声認識に登録されたテキスト
        if(-1 == className.indexOf("com.google.android.apps.gsa.searchplate")
                || null == text)
            return;
        _handler.removeCallbacksAndMessages(null);
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                execVoiceCommand(text.toString());
            }
        }, WAIT_TIME);
    }

    @Override
    public void onInterrupt() {
    }
    public void execVoiceCommand(String command){
        Log.d("voice command", command);
        Toast.makeText(getApplicationContext(), command, Toast.LENGTH_SHORT).show();
        tts.speak(command, TextToSpeech.QUEUE_FLUSH,null);
        // write your command
    }
}