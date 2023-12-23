package edu.uob;

public class Furniture extends GameEntity{
    private boolean canGet;
    public Furniture(String name, String description) {
        super(name, description);
        this.canGet=false;
    }


}
