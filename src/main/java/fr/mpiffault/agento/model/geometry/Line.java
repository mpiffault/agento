package fr.mpiffault.agento.model.geometry;

import fr.mpiffault.agento.view.Drawable;

import java.awt.*;
import java.awt.geom.Line2D;

public class Line implements Drawable{

    private Line2D line;

    public Line(Position p1, Position p2) {
        line = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    @Override
    public void draw(Graphics2D g2, Position mousePosition) {
        g2.setColor(Color.RED);
        g2.draw(line);
    }
}
