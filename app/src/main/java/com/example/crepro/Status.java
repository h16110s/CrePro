package com.example.crepro;

public class Status {
    int hp;
    int attack;
    int spAttack;
    int spDefence;
    int speed;

    public Status(int hp, int attack, int spAttack, int spDefence, int speed){
        this.hp = hp;
        this.attack = attack;
        this.spAttack = spAttack;
        this.spDefence = spDefence;
        this.speed = speed;
    }

    @Override
    public String toString(){
        String s;
        s = "   HP: " + this.hp + "\n";
        s += "  こうげき: " + this.attack + "\n";
        s += "  とくこう: " + this.spAttack + "\n";
        s += "  とくぼう: " + this.spDefence + "\n";
        s += "  すばやさ: " + this.speed + "\n";
        return s;
    }
}

