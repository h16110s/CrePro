package com.example.crepro;

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

import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts ;
    ListView lv;
    private static String[] name;

    private static String[] foods = {
        "Apple", "Banana","Hello World"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, null);
        lv = (ListView) findViewById(R.id.listView);
        try{
            FileInputStream fileInputStream = new FileInputStream( getFilesDir()+"pokemon_data.json");
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            fileInputStream.close();

            // 読み込んだ内容をJSONArrayにパース
            String json = new String(buffer);
            JSONArray jsonArray = new JSONArray(json);

            // パースした内容からListオブジェクトを作成
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                name[i] = jsonObject.get("name").toString();
//                Item item = new Item();
//                item.setId((int) jsonObject.get("id"));
//                item.setName((String) jsonObject.get("name"));
//                items.add(item);
            }
        }catch (IOException e){
            Log.e("IO", e.toString());
        }catch (JSONException e){
            Log.e("JSON", "json error");
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,foods);
        lv.setAdapter(arrayAdapter);
    }


    public void sp(View v){
        tts.speak("こんにちは", TextToSpeech.QUEUE_FLUSH, null);

    }
}
