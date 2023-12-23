package edu.uob;

public abstract class GameEntity
{
    private String name;
    private String description;

    private boolean canGet;
    public GameEntity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isCanGet(){
        return this.canGet;
    }
}
