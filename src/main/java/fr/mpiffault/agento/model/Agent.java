package fr.mpiffault.agento.model;

import fr.mpiffault.agento.model.geometry.*;
import fr.mpiffault.agento.view.Board;
import fr.mpiffault.agento.view.Drawable;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Agent implements Drawable, Selectable, Traceable, Controllable {

    private static final double BODY_SIZE = 5d;
    private static final double SELECT_SIZE = 10d;
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

    private double torify(double coordinate, double maxCoordinate) {
        if (coordinate < 0) {
            coordinate  += maxCoordinate;
        } else if (coordinate  > maxCoordinate) {
            coordinate  -= maxCoordinate;
        }
        return coordinate;
    }

    @Override
    public void move() {
        if (free) {
            changeRandDir();
            step(3d);
        } else {
            moveAccordingToKeys(environment.getKeysPressed());
        }
    }

    public void step(double speed) {
        Vector d = direction.getVector();
        double newX = position.getX() + d.getDx() * speed;
        double newY = position.getY() + d.getDy() * speed;

        newX = torify(newX, environment.getSizeX());
        newY = torify(newY, environment.getSizeY());

        Position newPosition = new Position(newX, newY);
        if (trace) {
            if (path.getLastPosition() == null || !newPosition.isNear(path.getLastPosition(), 5d)) {
                path.add(newPosition);
            }
        }
        position = newPosition;
    }

    private void changeRandDir() {
        double dDir = (RAND.nextBoolean() ? 1d : -1d) * (0.33d * Board.TIME_RESOLUTION);
        direction.addAngleDegree(dDir);
    }

    private void moveAccordingToKeys(Set<Integer> keysPressed) {
        double angleToAdd = 0d;
        double speedToStep = 0d;
        if (keysPressed.contains(KeyEvent.VK_LEFT)) {
            angleToAdd -= 5d;
        }
        if (keysPressed.contains(KeyEvent.VK_UP)) {
            speedToStep += 3d;
        }
        if (keysPressed.contains(KeyEvent.VK_RIGHT)) {
            angleToAdd += 5d;
        }
        if (keysPressed.contains(KeyEvent.VK_DOWN)) {
            speedToStep -= 3d;
        }
        direction.addAngleDegree(angleToAdd);
        step(speedToStep);
    }

    @Override
    public void draw(Graphics2D g2, Position mousePosition) {
        g2.setColor(Board.RED);

        if(trace) {
            path.draw(g2, mousePosition);
        }
        if (selected) {
            drawSelected(g2);
        }
        if (this.position.isNear(mousePosition, 10d)) {
            Shape hoverShape = new Ellipse2D.Double(position.getX() - BODY_SIZE, position.getY() - BODY_SIZE,  SELECT_SIZE,  SELECT_SIZE);
            g2.setColor(Board.PINK);
            g2.draw(hoverShape);
        }
        g2.setColor(Board.YELLOW);
        Shape body = new Ellipse2D.Double(position.getX() - (BODY_SIZE / 2d), position.getY() - (BODY_SIZE / 2d),  BODY_SIZE,  BODY_SIZE);
        g2.draw(body);
        g2.fill(body);
        g2.setColor(Board.RED);
        Line2D.Double line = getDirectionLine();
        g2.draw(line);

        new Line(this.position, getNearest().position).draw(g2, mousePosition);
    }

    private Line2D.Double getDirectionLine() {
        return new Line2D.Double(position.getX(),
                    position.getY(),
                    position.getX() + (direction.getVector().getDx() * 5),
                    position.getY() + (direction.getVector().getDy() * 5));
    }

    private void drawSelected(Graphics2D g2) {
        Shape selectShape = new Ellipse2D.Double(position.getX() - BODY_SIZE, position.getY() - BODY_SIZE,  SELECT_SIZE,  SELECT_SIZE);
        g2.setColor(Board.BLUE);
        g2.fill(selectShape);
    }

    private Agent getNearest() {
        if (this.environment.getAgentList().size() <= 1) {
            return this;
        }
        List<Agent> filtered = this.environment.getAgentList().stream()
                .filter(a -> a != this)
                .collect(Collectors.toList());
        Agent nearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Agent a : filtered) {
            double dist = a.position.squareDistanceTo(this.position);
            if (dist < minDistance) {
                minDistance = dist;
                nearest = a;
            }
        }
        return nearest;
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
}
