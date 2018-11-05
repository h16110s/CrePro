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
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts ;
    private static String pokeData;

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

    public void pokeSearch(View v){
        String test = "アーボ";
        PokeDatum temp = null;
        Status statusTmp = null;
        // 読み込んだ内容をJSONArrayにパース
        JSONArray pokeArray = null;
        try {
            pokeArray = new JSONArray(pokeData);
            for(int i = 0; i < pokeArray.length(); i++) {
                JSONObject pokemon = (JSONObject) pokeArray.get(i);

                //ヒットした時にはポケモンの個体データを取得して返す
                if(pokemon.getString("name").equals(test)){

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

                    temp = new PokeDatum(
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
            Log.e("JSON",e.toString());
        } catch (NullPointerException e){
            Log.e("JSON-Read",e.toString());
        }
    }

    public List<String> JsonArrayToList(JSONArray input) throws JSONException {
        List<String> tmp = new LinkedList<>();
        for(int i = 0 ; i<input.length() ; i++){
            tmp.add(input.getString(i));
        }
        return tmp;
    }
}
