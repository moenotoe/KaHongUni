package edu.uob;

public class Artefacts extends GameEntity{
    private boolean canGet;
    public Artefacts(String name, String description) {
        super(name, description);
        this.canGet=true;
    }

    public String getName()
    {
        return super.getName();
    }

    public String getDescription()
    {
        return super.getDescription();
    }

    public boolean isCanGet() {
        return this.canGet;
    }
}
