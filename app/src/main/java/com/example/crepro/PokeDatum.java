package com.example.crepro;

public class PokeDatum {
    int no;
    String name;
    boolean isMegaEvolution;
    String[] types;
    String[] avilities;
    String[] hiddenAbilities;
    Status status;

    public  PokeDatum (){
        this.name = null;
    }
    public PokeDatum(int no, String name, boolean isMega, String[] types, String[] avi, String[] hidden, Status status){
        this.no = no;
        this.name = name;
        this.isMegaEvolution = isMega;
        this.types = types;
        this.avilities = avi;
        this.hiddenAbilities = hidden;
        this.status = status;
    }
}

