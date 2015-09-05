package fr.mpiffault.agento.model.geometry;

public class Direction {
    private double angle;
    public static final double SOUTH = Math.PI / 2d;
    public static final double EAST = Math.PI;
    public static final double WEST = Math.PI * 2d;
    public static final double NORTH = Math.PI * (3d / 2d);

    public Direction(double angle) {
        this.angle = angle % (WEST);
    }

    public Direction() {
        this(NORTH);
    }

    public Vector getVector() {
        if (this.angle == SOUTH) {
            return new Vector(0d, 1d);
        }
        if (this.angle == EAST) {
            return new Vector(-1d, 0d);
        }
        if (this.angle == WEST) {
            return new Vector(1d, 0d);
        }
        if (this.angle == NORTH) {
            return new Vector(0d, -1d);
        }
        return new Vector(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Adds angle dAngle in radians to this Direction
     * @param dAngle angle to add in radian
     */
    public void addAngle(double dAngle) {
        this.setAngle(angle + dAngle);
    }

    /**
     * Adds angle dAngle in degrees to this Direction
     * @param dAngle angle to add in degrees
     */
    public void addAngleDegree(double dAngle) {
        addAngle(Math.toRadians(dAngle));
    }

    public void setAngle (double angle) {
        this.angle = angle % WEST;
    }
}
