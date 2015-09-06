package fr.mpiffault.agento.model;

public interface Controllable extends Selectable{
    void move();
    void setFree(boolean free);
    boolean isFree();
}
