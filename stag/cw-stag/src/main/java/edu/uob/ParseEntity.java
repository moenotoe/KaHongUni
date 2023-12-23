package edu.uob;
import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.ParserConstants;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Node;

import javax.swing.text.html.parser.Entity;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParseEntity extends java.lang.Object implements ParserConstants {
    private ArrayList<Location> map;
    private ArrayList<Players> players;
    boolean triggerDoor;
    boolean triggerBridge;


    public ParseEntity(String dotPath) throws FileNotFoundException, ParseException {
        this.map=new ArrayList<>();
        this.players= new ArrayList<>();
        triggerBridge=false;
        triggerDoor=false;
        ParseEntities(dotPath);

    }

    public void ParseEntities(String dotPath) throws FileNotFoundException, ParseException {
        FileReader fileRead = new FileReader(dotPath);
        Parser par = new Parser();
        boolean parsed = par.parse(fileRead);
        ArrayList<Graph> graphs=par.getGraphs();
        for (Graph locateAndPath : graphs) {
            for (Graph subgraph1 : locateAndPath.getSubgraphs()) {
                ArrayList<Graph> clusters = subgraph1.getSubgraphs();
                createEntity(clusters);
            }
        }


    }
    public void turnOnTriggerDoor(){
        this.triggerDoor=true;
    }
    public void turnOnTriggerBridge(){
        this.triggerBridge=true;
    }
    public void createEntity(ArrayList<Graph> clusters){
        for(Graph entity:clusters){
            Node nodeEntity=entity.getNodes(false).get(0);
            ArrayList<Graph> locateSubgrp=entity.getSubgraphs();
            Location cliEntity=new Location(nodeEntity.getId().getId(),nodeEntity.getAttribute("description"));
            cliEntity.setSubgraph(locateSubgrp);

            this.map.add(cliEntity);
        }
    }

    public ArrayList<Location> getMap(){
        return this.map;
    }

    //for get build in command
    public boolean isStuffInLocationAndCanGet(String currLocation, String targetStuff){
        //loop the location and find the stuff(Name and )
        for(Location loc:getMap()){
            if(loc.getName().equals(currLocation)){
                for (Map.Entry<String, GameEntity> entry:loc.getLocationEntity().entrySet()) {
                    String key=entry.getKey();
                    GameEntity value=entry.getValue();
                    if(key.equals(targetStuff)&&value.isCanGet()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    public void putIntoInvFromLocation(String targetStuff,Players players){
        for(Location loc:getMap()){
            if(loc.getName().equals(players.getCurrentLocation())){
                for (Map.Entry<String, GameEntity> entry:loc.getLocationEntity().entrySet()) {
                    String key=entry.getKey();
                    GameEntity value=entry.getValue();
                    if(key.equals(targetStuff)) {
                        players.addStuff(value);
                    }
                }
            }
        }
    }

    public boolean isTriggerDoor(){
        return this.triggerDoor;
    }

    public boolean isTriggerBridge(){
        return this.triggerBridge;
    }
    public Location getLocationByName(String name){
        Location tmp = null;

        for(int i=0;i<getMap().size();i++){
            if(getMap().get(i).getName().equals(name)){
                tmp=getMap().get(i);
            }
        }
        return tmp;
    }

    public ArrayList<String> getLocationList(){
        ArrayList<String> list=new ArrayList<>();
        for(int i=0;i<getMap().size();i++){
            list.add(getMap().get(i).getName());
        }
        return list;
    }

    public ArrayList<Players> getPlayers(){
        return this.players;
    }
    public Players getPlayerByName(String name){
        Players player=null;
        for(int i=0;i< getPlayers().size();i++){
            if(getPlayers().get(i).getName().equalsIgnoreCase(name)){
                player=getPlayers().get(i);
            }
        }
        return player;
    }

    public String initLocation(){
        String initLocation="";
        initLocation=getMap().get(0).getName();
        return initLocation;
    }

}