package fr.mpiffault.agento.view;

import fr.mpiffault.agento.model.geometry.Position;

import java.awt.*;

public interface Drawable {
    void draw(Graphics2D g2, Position mousePosition);
}
