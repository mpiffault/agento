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

    private static final double BODY_SIZE = 5d;
    private Environment environment;
    private Position position;
    private boolean selected;
    private Direction direction;

    private static final Random RAND = new Random();

    public Agent(Environment environment) {
        this.environment = environment;
        this.selected = false;
        environment.addAgent(this);
        position = environment.getCenter();
        direction = new Direction();
    }

    public Agent(Environment environment, Position position) {
        this(environment);
        this.position = position;
    }

    public String toString() {
        return "{" + position + "}";
    }

    public void setDirection(double angle) {
        this.direction = new Direction(angle);
    }

    public void move(double speed) {

        Vector d = direction.getVector();
        double newX = position.getX() + d.getDx() * speed;
        double newY = position.getY() + d.getDy() * speed;

        newX = torify(newX, environment.getSizeX());
        newY = torify(newY, environment.getSizeY());

        position.setX(newX);
        position.setY(newY);

    }

    private double torify(double coordinate, double maxCoordinate) {
        if (coordinate < 0) {
            coordinate  += maxCoordinate;
        } else if (coordinate  > maxCoordinate) {
            coordinate  -= maxCoordinate;
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
        if (selected) {
            Shape selectShape = new Ellipse2D.Double(this.getPosition().getX() - Agent.BODY_SIZE,this.getPosition().getY() - Agent.BODY_SIZE,  Agent.BODY_SIZE * 2d,  Agent.BODY_SIZE * 2d);
            g2.setColor(new Color(0, 255, 255, 100));
            g2.fill(selectShape);
        }
        g2.setColor(Color.ORANGE);
        Shape body = new Ellipse2D.Double(this.getPosition().getX() - (Agent.BODY_SIZE / 2d),this.getPosition().getY() - (Agent.BODY_SIZE / 2d),  Agent.BODY_SIZE,  Agent.BODY_SIZE);
        g2.draw(body);
        g2.fill(body);
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }
}
