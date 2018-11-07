package com.example.crepro;

import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class PokeSearcher {
    String pokeData;
    PokeDatum searchPokemon;

    public PokeDatum getPokemonInfo()throws NullPointerException{ return this.searchPokemon; }

    public PokeSearcher(InputStream inputData){
        try{
            Reader reader = new InputStreamReader(inputData);
            byte[] buffer = new byte[inputData.available()];
            while((inputData.read(buffer)) != -1) {}
            pokeData = new String(buffer);
            inputData.close();
        } catch (IOException e){
            Log.e("IO", e.toString());
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
                            stats.getInt("defence"),
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
            //いなかったとき
            this.pokeData = null;
        } catch (JSONException e) {
            Log.e("PS-JSON",e.toString());
        } catch (NullPointerException e){
            Log.e("PS-NULL",e.toString());
        }
    }

    public ArrayList<String> JsonArrayToList(JSONArray input) throws JSONException {
        ArrayList<String> tmp = new ArrayList<>();
        for(int i = 0 ; i<input.length() ; i++){
            tmp.add(input.getString(i));
        }
        return tmp;
    }
}
