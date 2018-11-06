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
    private static String pokeData;
    private final int requestCode = 1234;
    public  PokeDatum searchPokemon;

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
        try{
            Reader reader = new InputStreamReader(stream_in);
            byte[] buffer = new byte[stream_in.available()];
            while((stream_in.read(buffer)) != -1) {}
            pokeData = new String(buffer);
            stream_in.close();
        } catch (IOException e){
            Log.e("IO", e.toString());
        }
    }


    public void sp(View v){
        tts.speak("こんにちは", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void srbtn(View v){
        //インテント作成
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //表示させる文字列
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"音声を文字で出力します。");
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
            pokeSearch(speechToChar.get(0).replaceAll(" ",""));
//
//            //ダイアログ表示
            showDialog(this,"",searchPokemon.toString());

            super.onActivityResult(requestCode,resultCode,data);// お決まり
        }
    }


    public void pokeSearch(String name){
        Status statusTmp = null;
        // 読み込んだ内容をJSONArrayにパース
        JSONArray pokeArray = null;
        try {
            pokeArray = new JSONArray(pokeData);
            for(int i = 0; i < pokeArray.length(); i++) {
                JSONObject pokemon = (JSONObject) pokeArray.get(i);

                //ヒットした時にはポケモンの個体データを取得して返す
                if(pokemon.getString("name").equals(name)){
                    JSONObject stats = pokemon.getJSONObject("stats");
                    statusTmp = new Status(
                            stats.getInt("hp"),
                            stats.getInt("attack"),
                            stats.getInt("spAttack"),
                            stats.getInt("spDefence"),
                            stats.getInt("speed"));

                    JSONArray type = pokemon.getJSONArray("types");
                    JSONArray abilities = pokemon.getJSONArray("abilities");
                    JSONArray hiddenAbilities = pokemon.getJSONArray("hiddenAbilities");

                    searchPokemon = new PokeDatum(
                            pokemon.getInt("no"),
                            pokemon.getString("name"),
                            pokemon.getString("form"),
                            pokemon.getBoolean("isMegaEvolution"),
                            JsonArrayToList(type),
                            JsonArrayToList(abilities),
                            JsonArrayToList(hiddenAbilities),
                            statusTmp);
                    break;
                }

            }
        } catch (JSONException e) {
            Toast.makeText(this,"検索エラー", Toast.LENGTH_LONG).show();
            Log.e("JSON",e.toString());
        } catch (NullPointerException e){
            Toast.makeText(this,"読み込みエラー", Toast.LENGTH_LONG).show();
            Log.e("JSON-Read",e.toString());
        }
    }

    public ArrayList<String> JsonArrayToList(JSONArray input) throws JSONException {
        ArrayList<String> tmp = new ArrayList<>();
        for(int i = 0 ; i<input.length() ; i++){
            tmp.add(input.getString(i));
        }
        return tmp;
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
