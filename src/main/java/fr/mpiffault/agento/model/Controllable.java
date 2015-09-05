package fr.mpiffault.agento.model;

import java.util.Set;

public interface Controllable extends Selectable{
    void moveAccordingToKeys(Set<Integer> keysPressed);
    void setFree(boolean free);
    boolean isFree();
}
