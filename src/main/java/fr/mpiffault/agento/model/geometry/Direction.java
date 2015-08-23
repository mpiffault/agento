package fr.mpiffault.agento.model.geometry;

public class Direction {
    private double angle;
    public static double SOUTH = Math.PI / 2d;
    public static double EAST = Math.PI;
    public static double WEST = Math.PI * 2d;
    public static double NORTH = Math.PI * (3d / 2d);

    public Direction(double angle) {
        this.angle = angle % (WEST);
    }

    public Direction() {
        this(NORTH);
    }

    public Vector getVector() {
        double dx = veryLittleToZero(Math.cos(angle));
        double dy = veryLittleToZero(Math.sin(angle));

        return new Vector(dx, dy);
    }

    /*
     Attempt to fix the results of Math.cos()/sin()
     which should be 0 but are not
     */
    private double veryLittleToZero(double d) {
        return Math.abs(d) < 1.0E-10 ? 0 : d;
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
