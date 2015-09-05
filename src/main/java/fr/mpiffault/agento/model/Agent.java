package fr.mpiffault.agento.model;

import fr.mpiffault.agento.model.geometry.*;
import fr.mpiffault.agento.view.Board;
import fr.mpiffault.agento.view.Drawable;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Random;
import java.util.Set;

@Data
public class Agent implements Drawable, Selectable, Traceable, Controllable {

    private static final double BODY_SIZE = 5d;
    private Environment environment;
    private Position position;
    private boolean selected;
    private boolean trace;
    private Direction direction;
    private Path path;

    private static final Random RAND = new Random();
    private boolean free;

    public Agent(Environment environment) {
        this.environment = environment;
        this.selected = false;
        this.free = true;
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

        Position newPosition = new Position(newX, newY);
        if (trace) {
            if (path.getLastPosition() == null || !newPosition.isNear(path.getLastPosition(), 5.0)) {
                path.add(newPosition);
            }
        }
        position = newPosition;
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
        Line2D.Double line = new Line2D.Double(this.getPosition().getX(),
                this.getPosition().getY(),
                this.getPosition().getX() + (this.getDirection().getVector().getDx() * 5),
                this.getPosition().getY() + (this.getDirection().getVector().getDy() * 5));
        g2.draw(line);

        if(trace) {
            path.draw(g2);
        }
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

    @Override
    public void select() {
        this.selected = true;
        this.free = false;
    }

    @Override
    public void deselect() {
        this.selected = false;
        this.free = true;
    }

    @Override
    public void unTrace() {
        this.trace = false;
        this.path = null;
    }

    @Override
    public void trace() {
        this.trace = true;
        this.path = new Path();
    }

    @Override
    public void moveAccordingToKeys(Set<Integer> keysPressed) {
        if (keysPressed.contains(KeyEvent.VK_LEFT)) {
            direction.addAngleDegree(-5);
        }
        if (keysPressed.contains(KeyEvent.VK_UP)) {
            move(0.1d * Board.TIME_RESOLUTION);
        }
        if (keysPressed.contains(KeyEvent.VK_RIGHT)) {
            direction.addAngleDegree(5);
        }
        if (keysPressed.contains(KeyEvent.VK_DOWN)) {
            move(-0.1d * Board.TIME_RESOLUTION);
        }
    }
}
