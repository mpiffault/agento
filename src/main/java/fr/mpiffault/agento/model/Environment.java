package fr.mpiffault.agento.model;

import fr.mpiffault.agento.view.Plateau;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Environment {
	private double sizeX;
	private double sizeY;
	private ArrayList<Agent> listeAgents;
	
	public Environment(double sizeX, double sizeY){	
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.listeAgents = new ArrayList<>();
	}
	
	public Environment() {
		this(500, 350);
	}
	
	public void tick() {
		for (Agent agent : this.listeAgents) {
            agent.changeRandDir();
            agent.move(0.1d * Plateau.RESOLUTION);
		}
	}
	
	public void addAgent(Agent agent) {
		agent.setEnvironement(this);
		this.listeAgents.add(agent);
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
