package com.example.crepro;

import java.util.List;

public class PokeDatum {
    int no;
    String name;
    String form;
    boolean isMegaEvolution;
    List<String> types;
    List<String> abilities;
    List<String> hiddenAbilities;
    Status status;

    public  PokeDatum (){
        this.name = null;
    }
    public PokeDatum(int no, String name,String form ,boolean isMega, List<String> types, List<String> abi, List<String> hidden, Status status){
        this.no = no;
        this.name = name;
        this.form = form;
        this.isMegaEvolution = isMega;
        this.types = types;
        this.abilities = abi;
        this.hiddenAbilities = hidden;
        this.status = status;
    }
}

