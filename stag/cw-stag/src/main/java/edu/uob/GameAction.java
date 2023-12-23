package edu.uob;

import java.util.HashSet;

public class GameAction
{
    HashSet<String> trigger;
    HashSet<String> subjects;
    HashSet<String> consumed;
    HashSet<String> products;

    String narr;
    public GameAction(){
        this.trigger=new HashSet<>();  //the elements must key phrase
        this.subjects=new HashSet<>();
        this.consumed=new HashSet<>();
        this.products=new HashSet<>();
        this.narr="";
    }

    public HashSet<String> getSubjects(){
        return this.subjects;
    }
    public HashSet<String> getConsumed(){
        return this.consumed;
    }
    public HashSet<String> getProducts(){
        return this.products;
    }





}
