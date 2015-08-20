package fr.mpiffault.agento.model;

import lombok.Data;

@Data
public class Vector {
    private double dx;
    private double dy;

    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
