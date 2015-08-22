package fr.mpiffault.agento.model;

import fr.mpiffault.agento.view.Board;
import lombok.Data;

import java.util.Random;

@Data
public class Agent {

    private Environment environment;
    private Position position;
    private Direction direction;

    private static Random RAND = new Random();

    public Agent(Environment environment) {
        this.environment = environment;
        environment.addAgent(this);
        position = environment.getCenter();
        direction = new Direction();
    }

    public Agent(Environment environment, Position position) {
        this(environment);
        this.position = position;
    }

    public String toString() {
        return "{posX: " + this.position.getX() +
                " - posY: " + this.position.getY() +
                "}";
    }

    public void setDirection(double angle) {
        this.direction = new Direction(angle);
    }

    public void move(double vitesse) {
        Vector d = direction.getVector();
        double newX = position.getX() + d.getDx() * vitesse;
        double newY = position.getY() + d.getDy() * vitesse;

        newX = torify(newX, environment.getSizeX());
        newY = torify(newY, environment.getSizeY());

        position.setX(newX);
        position.setY(newY);

    }

    private double torify(double coordinate, double maxCoordonee) {
        if (coordinate < 0) {
            coordinate  += maxCoordonee;
        } else if (coordinate  > maxCoordonee) {
            coordinate  -= maxCoordonee;
        }
        return coordinate;
    }

    public void changeRandDir() {
        double dDir = (RAND.nextBoolean() ? 1d : -1d) * (0.33d * Board.TIME_RESOLUTION);
        direction.addAngleDegree(dDir);
    }
}
