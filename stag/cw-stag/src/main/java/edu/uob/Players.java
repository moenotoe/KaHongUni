package edu.uob;

import java.util.ArrayList;
import java.util.Map;

public class Players{

    private String name;
    private int health;
    private ArrayList<GameEntity> inventory;
    private String currentLocation;
    private ParseEntity entity;
    public Players(String name,ParseEntity entity) {
        this.entity=entity;
        this.name=name;
        this.currentLocation=this.entity.getMap().get(0).getName();
        this.inventory=new ArrayList<>();
        this.health=3;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String location){
        this.currentLocation=location;
    }

    public void addStuff(GameEntity stuff){
        this.inventory.add(stuff);
    }
    public ArrayList<GameEntity> getStuff(){
        return this.inventory;
    }

    public String getAllStuffDes(){
        StringBuilder description=new StringBuilder();
        for(int i=0;i<inventory.size();i++){
            description.append(inventory.get(i).getDescription()+"\n");
        }
        return description.toString();
    }

    public ArrayList<String> getAllStuffName(){
        ArrayList<String> name=new ArrayList<>();
        for(int i=0;i<inventory.size();i++){
            name.add(inventory.get(i).getName());
        }
        return name;
    }



    public void removeStuff(String stuff){
        for(int i=0;i<inventory.size();i++){
            if(inventory.get(i).getName().equals(stuff)){
                inventory.remove(inventory.get(i));
            }
        }
    }

    public GameEntity getStuffObj(String stuffName){
        GameEntity tmp = null;
        for(int i=0;i<inventory.size();i++){
            if(inventory.get(i).getName().equals(stuffName)){
                tmp=inventory.get(i);
            }
        }
        return tmp;
    }

    public void lostHealth(){
        this.health--;
    }
    public void increaseHealth(){
        this.health++;
    }
    public int getHealth(){
        return this.health;
    }
    public String getName(){
        return this.name;
    }

    public void resetPlayer(){
        inventory.clear();
        this.health=3;
    }




}
