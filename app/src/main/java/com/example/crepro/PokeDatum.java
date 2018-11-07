package com.example.crepro;

import java.util.ArrayList;
import java.util.List;

public class PokeDatum {
    int no;
    String name;
    String form;
    boolean isMegaEvolution;
    ArrayList<String> types;
    ArrayList<String> abilities;
    ArrayList<String> hiddenAbilities;
    Status status;

    public  PokeDatum (){
        this.name = null;
    }
    public PokeDatum(int no, String name,String form ,boolean isMega, ArrayList<String> types, ArrayList<String> abi, ArrayList<String> hidden, Status status){
        this.no = no;
        this.name = name;
        this.form = form;
        this.isMegaEvolution = isMega;
        this.types = types;
        this.abilities = abi;
        this.hiddenAbilities = hidden;
        this.status = status;
    }
    @Override
    public String toString(){
        String s;
        s = "No: " + this.no + "\n";
        s += "Name: " + this.name + "\n";
        if(this.form != ""){
            s += this.form + "\n";
        }
        s += "メガ進化: " + this.isMegaEvolution + "\n";
        s += "タイプ: " +types.toString() + "\n";
        s += "特性: " + abilities.toString() + "\n";
        s += "夢特性: " + hiddenAbilities.toString() + "\n";
        s += status.toString();
        return s;
    }

    public String makeReadData(){
        String s = "";
        s += this.name + "。";
        for(int i = 0; i < types.size(); i++){
            s += this.types.get(i) + "、";
        }
        s += "タイプ。";
        s += "とくせいは";
        for(int i = 0 ; i < abilities.size() ; i++){
            s += this.abilities.get(i);
        }
        return s;
    }
}

