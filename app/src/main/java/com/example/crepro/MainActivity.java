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
    private static String jsonData = "";
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
        String s ="";
        try{
            Reader reader = new InputStreamReader(stream_in);
            byte[] buffer = new byte[stream_in.available()];
            while((stream_in.read(buffer)) != -1) {}
            s = new String(buffer);

            // 読み込んだ内容をJSONArrayにパース
            JSONArray jsonArray = new JSONArray(s);
            // パースした内容からListオブジェクトを作成
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject status = jsonObject.getJSONObject("stats");
                Log.d("NAME_OUT" ,jsonObject.getString("name") );
                Log.d("HP_OUT" ,status.getString("hp"));
            }
        } catch (IOException e){
            Log.e("IO", e.toString());
        } catch (JSONException e){
            Log.e("JSON", "json error");
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,foods);
        lv.setAdapter(arrayAdapter);

        try {
            stream_in.close();
        } catch (IOException e) {
            Log.e("IO","Close Error");
        }
    }


    public void sp(View v){
        tts.speak("こんにちは", TextToSpeech.QUEUE_FLUSH, null);

    }
}
