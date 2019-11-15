package com.example.crepro;

import android.accessibilityservice.AccessibilityService;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.InputStream;

public class AccessibillityVoiceService extends AccessibilityService {
    Handler _handler = new Handler();
    int WAIT_TIME = 1000;
    public TextToSpeech tts ;
    public PokeSearcher pokeSearcher;
    @Override
    public  void onCreate(){
        super.onCreate();
        tts = new TextToSpeech(this,null);

        //データを取得するためのリソースファイルのインスタンス
        Resources res = this.getResources();

        InputStream stream_in = res.openRawResource(R.raw.pokemon_data);
        pokeSearcher = new PokeSearcher(stream_in);
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
        Toast.makeText(getApplicationContext(),command, Toast.LENGTH_SHORT).show();
        pokeSearcher.pokeSearch(command.replaceAll(" ",""));
        Toast.makeText(getApplicationContext(),pokeSearcher.getPokemonInfo().toString(), Toast.LENGTH_LONG).show();
//        showDialog(this, "",pokeSearcher.getPokemonInfo().toString());
//        tts.speak(command, TextToSpeech.QUEUE_FLUSH,null);
        // write your command
    }

    private void showDialog(final AccessibillityVoiceService activity, String title, String text){
        AlertDialog.Builder ad = new AlertDialog.Builder(activity);
        ad.setTitle(title);
        ad.setMessage(text);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                activity.setResult(Activity.RESULT_OK);
            }
        });
        ad.create();
        ad.show();
    }
}