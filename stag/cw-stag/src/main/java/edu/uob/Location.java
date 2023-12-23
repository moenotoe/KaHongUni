package edu.uob;

import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class Location extends GameEntity{

    private LinkedHashMap<String,GameEntity> locationEntity;
    private ArrayList<Graph> subgraph;
    public Location(String name, String description) {
        super(name, description);
        locationEntity=new LinkedHashMap<>();
    }

    public void setSubgraph(ArrayList<Graph> subgraph){
        this.subgraph=subgraph;
        for(Graph itemType:subgraph) {
            for(Node entity:itemType.getNodes(false)){
                String ArtefactName=itemType.getId().getId();
                if(ArtefactName.equals("artefacts")){
                    Artefacts artefacts=new Artefacts(entity.getId().getId(),entity.getAttribute("description"));
                    this.locationEntity.put(entity.getId().getId(),artefacts);
                } else if (ArtefactName.equals("furniture")) {
                    Furniture furniture=new Furniture(entity.getId().getId(),entity.getAttribute("description"));
                    this.locationEntity.put(entity.getId().getId(),furniture);
                } else if (ArtefactName.equals("characters")) {
                    Characters characters=new Characters(entity.getId().getId(),entity.getAttribute("description"));
                    this.locationEntity.put(entity.getId().getId(),characters);
                }
            }
        }
    }

    public  LinkedHashMap<String,GameEntity> getLocationEntity(){
        return this.locationEntity;
    }

    public void addLocationEntity(String stuffName, GameEntity entityName){
        this.locationEntity.put(stuffName,entityName);
    }

    public void removeLocationEntity(String stuffName){
        this.locationEntity.remove(stuffName);
    }


}
