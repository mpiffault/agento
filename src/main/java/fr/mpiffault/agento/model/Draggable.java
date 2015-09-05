package fr.mpiffault.agento.model;

import fr.mpiffault.agento.model.geometry.Position;

public interface Draggable extends Selectable {
    void setPosition(Position position);
}
