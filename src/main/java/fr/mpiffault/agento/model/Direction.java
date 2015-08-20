package fr.mpiffault.agento.model;

public class Direction {
    private double angle;
    private static double SUD = Math.PI / 2d;
    private static double EST = Math.PI;
    private static double OUEST = Math.PI * 2d;
    private static double NORD = SUD * 3d;

    public Direction(double angle) {
        this.angle = angle % (OUEST);
    }

    public Direction() {
        this(NORD);
    }

    public Vector getVector() {
        return new Vector(Math.cos(angle), Math.sin(angle));
    };

    /**
     * Ajoute l'angle dAngle en radian à cette Direction
     * @param dAngle
     */
    public void addAngle(double dAngle) {
        this.setAngle(angle + dAngle);
    }

    /**
     * Ajoute l'angle dAngle en degré à cette Direction
     * @param dAngle
     */
    public void addAngleDegre(double dAngle) {
        addAngle((dAngle * EST) / 180d);
    }

    public void setAngle (double angle) {
        this.angle = angle % OUEST;
    }
}
