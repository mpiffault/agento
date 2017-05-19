package fr.mpiffault.agento.model;

import fr.mpiffault.agento.model.geometry.*;
import fr.mpiffault.agento.util.ColorUtils;
import fr.mpiffault.agento.view.Board;
import fr.mpiffault.agento.view.Drawable;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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
    private Mode mode;
    private Color color;

    public Agent(Environment environment) {
        this.environment = environment;
        this.selected = false;
        this.free = true;
        environment.addAgent(this);
        position = environment.getCenter();
        direction = new Direction();
        mode = Mode.RANDOM;
        color = ColorUtils.randomColor();
    }

    public Agent(Environment environment, Position position) {
        this(environment);
        this.position = position;
        color = ColorUtils.randomColor();
    }

    public String toString() {
        return "{" + position + "}";
    }

    public void setDirection(double angle) {
        this.direction = new Direction(angle);
    }

    private double torify(double coordinate, double maxCoordinate) {
        if (coordinate < 0) {
            coordinate += maxCoordinate;
        } else if (coordinate > maxCoordinate) {
            coordinate -= maxCoordinate;
        }
        return coordinate;
    }

    @Override
    public void move() {
        if (free) {
            switch (mode) {
                case RANDOM:
                    changeRandDir();
                    break;
                case FOLLOW_NEAREST:
                    followNearest();
                    break;
                default:
                    break;
            }
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

    private void followNearest() {
        Agent nearest = getNearest();
        direction.setAngle(Math.atan2(this.position.getX() - nearest.position.getX(), this.position.getY() - nearest.position.getY()) + Direction.SOUTH);
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

        if (trace) {
            g2.setColor(color);
            path.draw(g2, mousePosition);
        }
        if (selected) {
            drawSelected(g2);
        }
        if (this.position.isNear(mousePosition, 10d)) {
            Shape hoverShape = new Ellipse2D.Double(position.getX() - BODY_SIZE, position.getY() - BODY_SIZE, SELECT_SIZE, SELECT_SIZE);
            g2.setColor(Board.PINK);
            g2.draw(hoverShape);
        }
        g2.setColor(this.color);
        Shape body = new Ellipse2D.Double(position.getX() - (BODY_SIZE / 2d), position.getY() - (BODY_SIZE / 2d), BODY_SIZE, BODY_SIZE);
        g2.draw(body);
        g2.fill(body);
        g2.setColor(Board.RED);
        Line2D.Double line = getDirectionLine();
        g2.draw(line);

        g2.setColor(this.color);
        new Line(this.position, getNearest().position).draw(g2, mousePosition);
    }

    private Line2D.Double getDirectionLine() {
        return new Line2D.Double(position.getX(),
                position.getY(),
                position.getX() + (direction.getVector().getDx() * 5),
                position.getY() + (direction.getVector().getDy() * 5));
    }

    private void drawSelected(Graphics2D g2) {
        Shape selectShape = new Ellipse2D.Double(position.getX() - BODY_SIZE, position.getY() - BODY_SIZE, SELECT_SIZE, SELECT_SIZE);
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
            //double dist = a.position.squareDistanceTo(this.position);
            double dist = this.getToroidalDistanceTo(this);
            if (dist < minDistance) {
                minDistance = dist;
                nearest = a;
            }
        }
        return nearest;
    }

    /*
        +---------------------------------------+
        |(0,0)          |                       |(X,0)
        |      B        |                 B     |
        |     X-        |               -X      |
        |     |    1    |A(this)    3    |      |
        +---------------X-----------------------+
        |               |                       |
        |               |                       |
        |               |                       |
        |          2    |           4           |
        |     |B        |                |B     |
        |     X-        |               -X      |
        |               |                       |
        +---------------------------------------+
         (0,Y)                                   (X,Y)
     */
    private double getToroidalDistanceTo(Agent other) {
        double halfSizeX = environment.getSizeX() / 2;
        double halfSizeY = environment.getSizeY() / 2;
        double otherX = other.position.getX();
        double otherY = other.position.getY();
        double halfDiagonalLength = Math.sqrt((environment.getSizeX() * environment.getSizeX()) + (environment.getSizeY() * environment.getSizeY()));

        Position enclosingRectangleCenter;
        if (this.position.getX() > other.position.getX()) {
            if (this.position.getY() > this.position.getY()) {
                // first quarter
                enclosingRectangleCenter = new Position(otherX + halfSizeX, otherY + halfSizeY);
            } else {
                // second quarter
                enclosingRectangleCenter = new Position(otherX + halfSizeX, otherY - halfSizeY);
            }
        } else {
            if (this.position.getY() > this.position.getY()) {
                // third quarter
                enclosingRectangleCenter = new Position(otherX - halfSizeX, otherY + halfSizeY);
            } else {
                // fourth quarter
                enclosingRectangleCenter = new Position(otherX - halfSizeX, otherY - halfSizeY);
            }
        }

        return Math.abs(Math.sqrt(this.position.squareDistanceTo(enclosingRectangleCenter)) - halfDiagonalLength);
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

    public void toggleMode() {
        mode = Mode.nextMode(mode);
    }
}
