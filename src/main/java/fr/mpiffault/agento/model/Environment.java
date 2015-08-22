package fr.mpiffault.agento.model;

import fr.mpiffault.agento.view.Board;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Environment {
	private double sizeX;
	private double sizeY;
	private ArrayList<Agent> agentList;
	
	public Environment(double sizeX, double sizeY){	
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.agentList = new ArrayList<>();
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

    public double getGravityIntensityAt(int iX, int iY) {
        return (iX*iY)/900d;
    }
}
