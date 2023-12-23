package edu.uob;

public class Characters extends GameEntity{
    private boolean canGet;
    public Characters(String name, String description) {
        super(name, description);
        this.canGet=false;
    }
}
