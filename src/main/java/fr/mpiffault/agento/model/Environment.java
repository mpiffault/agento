package fr.mpiffault.agento.model;

import fr.mpiffault.agento.model.geometry.Position;
import fr.mpiffault.agento.util.DrawingUtils;
import fr.mpiffault.agento.view.Board;
import fr.mpiffault.agento.view.Drawable;
import lombok.Data;

import java.awt.*;
import java.util.LinkedList;

@Data
public class Environment implements Drawable{
	private int sizeX;
	private int sizeY;
	private final LinkedList<Agent> agentList;
	
	public Environment(int sizeX, int sizeY){
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.agentList = new LinkedList<>();
	}
	
	public Environment() {
		this(500, 350);
	}

    public String toString() {
        return "[sizeX: " + this.getSizeX() +
                " - sizeY: " + this.getSizeY() +
                "]";
    }

	public void tick() {
		for (Agent agent : this.agentList) {
            agent.changeRandDir();
            agent.move(0.1d * Board.TIME_RESOLUTION);
		}
	}
	
	public void addAgent(Agent agent) {
		agent.setEnvironment(this);
		this.agentList.add(agent);
	}

	public Position getCenter() {
		return new Position(this.getSizeX() / 2, this.getSizeY() / 2d);
	}

    public boolean isIncluded(Position position) {
        return position.getX() >= 0d
                && position.getX() <= sizeX
                && position.getY() >= 0d
                && position.getY() <= sizeY;
    }

    private double getContinuousProperty(int iX, int iY) {
        return (iX*iY)/900d;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawRect(0, 0, this.sizeX, this.sizeY);
    }


    private void drawGradient(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        for (int iX = 0 ; iX < this.sizeX ; iX++) {
            for (int iY = 0 ; iY < this.sizeY ; iY++) {
                double intensity = this.getContinuousProperty(iX, iY);
                g2.setColor(DrawingUtils.intensityToColor(intensity, 100));
                g2.drawLine(iX, iY, iX, iY);
            }
        }
    }

    public Agent getAgentAt(Position position) {
        for (Agent agent : this.agentList) {
            if (position.isNear(agent.getPosition(), 10)) {
                return agent;
            }
        }

        return null;
    }
}
