package fr.mpiffault.agento.model;

import fr.mpiffault.agento.model.geometry.Direction;
import fr.mpiffault.agento.model.geometry.Position;
import fr.mpiffault.agento.model.geometry.Vector;
import fr.mpiffault.agento.view.Board;
import fr.mpiffault.agento.view.Drawable;
import lombok.Data;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Random;

@Data
public class Agent implements Drawable {

    public static final double BODY_SIZE = 5d;
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

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        Line2D.Double line = new Line2D.Double(this.getPosition().getX() % environment.getSizeX(),
                this.getPosition().getY() % environment.getSizeY(),
                this.getPosition().getX() % environment.getSizeX() + (this.getDirection().getVector().getDx() * 5),
                this.getPosition().getY() % environment.getSizeY() + (this.getDirection().getVector().getDy() * 5));
        g2.draw(line);
        g2.setColor(Color.ORANGE);
        Shape body = new Ellipse2D.Double(this.getPosition().getX() - (Agent.BODY_SIZE / 2d),this.getPosition().getY() - (Agent.BODY_SIZE / 2d),  Agent.BODY_SIZE,  Agent.BODY_SIZE);
        g2.draw(body);
        g2.fill(body);
    }

}
