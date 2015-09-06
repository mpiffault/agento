package fr.mpiffault.agento.model.geometry;

import fr.mpiffault.agento.view.Drawable;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Drawable path with synchronized actions.
 */
public class Path implements Drawable{
    private final List<Line2D> path;
    @Getter
    private Position lastPosition;

    public Path() {
        this.path = new LinkedList<>();
        this.lastPosition = null;
    }

    @Override
    public void draw(Graphics2D g2, Position mousePosition) {
        synchronized (path) {
            for (Line2D line : path) {
                g2.draw(line);
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("path[");
        synchronized (path){
            for (Line2D line : path) {
                builder.append(line);
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public void add(Position position) {

            if (lastPosition != null && position.squareDistanceTo(this.lastPosition) < 40d) {
                Line2D newLine = new Line2D.Double(position.getX(), position.getY(), this.lastPosition.getX(), this.lastPosition.getY());
                synchronized (path) {
                    path.add(newLine);
                }
                this.lastPosition = position;
            } else {
                this.lastPosition = position;
            }
    }
}
