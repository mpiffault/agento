package fr.mpiffault.agento.model.geometry;

import lombok.Data;

@Data
public class Vector {
    private final double dx;
    private final double dy;

    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public String toString() {
        return "=>(dx:" + dx +
                "/dy:" + dy +
                ")";
    }
}
