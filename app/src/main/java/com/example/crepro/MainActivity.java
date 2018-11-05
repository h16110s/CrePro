package com.example.crepro;

import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts ;
    ListView lv;
    private static String pokeData;
    private  String[] name;
    private static String[] foods = {
        "Apple", "Banana","Hello World"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, null);
        lv = (ListView) findViewById(R.id.listView);
        Resources res = this.getResources();
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,foods);
        lv.setAdapter(arrayAdapter);
    }


    public void sp(View v){
        tts.speak("こんにちは", TextToSpeech.QUEUE_FLUSH, null);
    }
    public void pokeSearch(View v){
        String test = "ポッチャマ";
        // 読み込んだ内容をJSONArrayにパース
        JSONArray pokeArray = null;
        try {
            pokeArray = new JSONArray(pokeData);
            for(int i = 0; i < pokeArray.length(); i++) {
                JSONObject pokemon = (JSONObject) pokeArray.get(i);
                //ヒットした時にはポケモンの個体データを取得して返す
                if(pokemon.getString("name").equals(test)){
                    JSONObject stats = pokemon.getJSONObject("stats");
                    JSONArray type = pokemon.getJSONArray("types");
                    JSONArray abilities = pokemon.getJSONArray("abilities");
                    JSONArray hiddenAbilities = pokemon.getJSONArray("hiddenAbilities");
                }
            }
        } catch (JSONException e) {
            Log.e("JSON",e.toString());
        } catch (NullPointerException e){
            Log.e("JSON-Read",e.toString());
        }
        // パースした内容からListオブジェクトを作成
    }
}
