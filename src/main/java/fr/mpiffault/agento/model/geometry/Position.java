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

    public Position() {
        this.x = 0d;
        this.y = 0d;
    }

    public String toString() {
        return "(x:" + x +
                "/y:" + y +
                ")";
    }

    public double squareDistanceTo(Position otherPosition) {
        double dX = Math.abs(this.x - otherPosition.x);
        double dY = Math.abs(this.y - otherPosition.y);
        return (dX * dX) + (dY * dY);
    }

    public boolean isNear(Position otherPosition, double maxValue) {
        return this.squareDistanceTo(otherPosition) < maxValue * maxValue;
    }

    public void addX(double dx) {
        this.x += dx;
    }

    public void addY(double dy) {
        this.y += dy;
    }
}
