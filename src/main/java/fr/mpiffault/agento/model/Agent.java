package fr.mpiffault.agento.model;

import fr.mpiffault.agento.view.Plateau;
import lombok.Data;

import java.util.Random;

@Data
public class Agent {

    private Environment environement;
    private Position position;
    private Direction direction;

    private static Random RAND = new Random();

    public Agent(Environment environement) {
        this.environement = environement;
        environement.addAgent(this);
        position = environement.getCenter();
        direction = new Direction();
    }

    public Agent(Environment environement, Position position) {
        this(environement);
        this.position = position;
    }

    public void setDirection(double angle) {
        this.direction = new Direction(angle);
    }

    public void move(double vitesse) {
        Vector d = direction.getVector();
        double newX = position.getX() + d.getDx() * vitesse;
        double newY = position.getY() + d.getDy() * vitesse;

        newX = torify(newX, environement.getSizeX());
        newY = torify(newY, environement.getSizeY());

        position.setX(newX);
        position.setY(newY);

    }

    private double torify(double coordonee, double maxCoordonee) {
        if (coordonee < 0) {
            coordonee  += maxCoordonee;
        } else if (coordonee  > maxCoordonee) {
            coordonee  -= maxCoordonee;
        }
        return coordonee;
    }

    public void changeRandDir() {
        double dDir = (RAND.nextBoolean() ? 1d : -1d) * (0.33d * Plateau.RESOLUTION);
        direction.addAngleDegre(dDir);
    }
}
