package fr.mpiffault.agento.model.geometry;

import lombok.Data;

@Data
public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addX(double dx) {
        this.x += dx;
    }

    public void addY(double dy) {
        this.y += dy;
    }
}
