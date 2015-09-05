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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Data
public class Agent implements Drawable {

    public static final int LEFT = 37;
    public static final int UP = 38;
    public static final int RIGHT = 39;
    public static final int DOWN = 40;

    private static final double BODY_SIZE = 5d;
    private Environment environment;
    private Position position;
    private boolean selected;
    private boolean trace;
    private Direction direction;
    private List<Position> path;

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

        if (trace) {
            Position nouvellePosition = new Position(newX, newY);
            if (path != null) {
                if (!path.isEmpty()) {
                    if (!nouvellePosition.isNear(path.get(path.size()-1), 5.0)) {
                        path.add(nouvellePosition);
                    }
                } else {
                    path.add(nouvellePosition);
                }
            }
        }
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
        Line2D.Double line = new Line2D.Double(this.getPosition().getX(),
                this.getPosition().getY(),
                this.getPosition().getX() + (this.getDirection().getVector().getDx() * 5),
                this.getPosition().getY() + (this.getDirection().getVector().getDy() * 5));
        g2.draw(line);
        if(trace) {
            if (path != null && !path.isEmpty()) {
                Position p1 = path.get(0);
                for (Position p2 : path) {
                    if (p1.squareDistanceTo(p2) < 40d) {
                        Line2D.Double linePath = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                        g2.draw(linePath);
                    }
                    p1 = p2;
                }
            }
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

    public void select() {
        this.selected = true;
        this.free = false;
    }

    public void deselect() {
        this.selected = false;
        this.free = true;
    }

    public void unTrace() {
        this.trace = false;
        if (path != null) {
            this.path.clear();
        }
        this.path = null;
    }

    public void trace() {
        this.trace = true;
        this.path = new ArrayList<>();
    }

    public void moveAccordingToKeys(Set<Integer> keysPressed) {
        if (keysPressed.contains(LEFT)) {
            direction.addAngleDegree(-5);
        }
        if (keysPressed.contains(UP)) {
            move(0.1d * Board.TIME_RESOLUTION);
        }
        if (keysPressed.contains(RIGHT)) {
            direction.addAngleDegree(5);
        }
        if (keysPressed.contains(DOWN)) {
            move(-0.1d * Board.TIME_RESOLUTION);
        }
    }
}
