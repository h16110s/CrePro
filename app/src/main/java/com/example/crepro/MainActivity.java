package com.example.crepro;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts ;
    private final int requestCode = 1234;
    PokeSearcher pokeSearcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //音声合成インスタンス
        tts = new TextToSpeech(this, null);

        //データを取得するためのリソースファイルのインスタンス
        Resources res = this.getResources();

        //データを取得
        InputStream stream_in = res.openRawResource(R.raw.pokemon_data);
        pokeSearcher = new PokeSearcher(stream_in);
    }

    public void sp(View v){
        tts.speak("こんにちは", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void srbtn(View v){
        //インテント作成
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //表示させる文字列
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"ポケモンのデータを音声で検索します。");
        //アクティビティ開始
        startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onActivityResult(int rCode, int resultCode, Intent data){
        //自分が投げたインテントであれば応答する
        if(rCode == requestCode && resultCode == RESULT_OK){
            //すべての結果を配列に受け取る
            ArrayList<String> speechToChar = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //ここでは、認識結果が複数あった場合に結合している。
            String spokenString = "";
            for(int i = 0; i < speechToChar.size() ; i++){
                spokenString += speechToChar.get(i) + "\n";
            }
            //トーストで表示
            Toast.makeText(this,spokenString, Toast.LENGTH_LONG).show();

            //検索 とりあえず1番目のもの
            pokeSearcher.pokeSearch(speechToChar.get(0).replaceAll(" ",""));

//            //ダイアログ表示
            showDialog(this,"",pokeSearcher.getPokemonInfo().toString());

            super.onActivityResult(requestCode,resultCode,data);// お決まり
        }
    }

    private void showDialog(final Activity activity, String title, String text){
        AlertDialog.Builder ad = new AlertDialog.Builder(activity);
        ad.setTitle(title);
        ad.setMessage(text);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.setResult(Activity.RESULT_OK);
            }
        });
        ad.create();
        ad.show();
    }
}
