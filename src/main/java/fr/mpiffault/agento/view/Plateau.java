package fr.mpiffault.agento.view;

import fr.mpiffault.agento.model.Agent;
import fr.mpiffault.agento.model.Environment;
import fr.mpiffault.agento.model.Position;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class Plateau extends JPanel implements Runnable {

    private static final long serialVersionUID = -8954030686627371948L;
    public static long RESOLUTION = 30l;
    private final Environment environment;
    @Getter
    private int sizeX;
    @Getter
    private int sizeY;

    Plateau(final Environment environment) {
        super();
        setBackground(Color.WHITE);
        this.environment = environment;
        this.sizeX = (int) this.environment.getSizeX();
        this.sizeY = (int) this.environment.getSizeY();

        Souris s = new Souris();
        this.addMouseListener(s);
		this.addMouseMotionListener(s);

    }

    private class Souris extends MouseAdapter {
        Agent c;

        @Override
        public void mousePressed(MouseEvent e) {
            c = new Agent(environment, new Position(e.getX(), e.getY()));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (c != null) {
                c.setPosition(new Position(e.getX(), e.getY()));
            }
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
		// drawGradient(g2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawAgents(g2);
        g2.setColor(Color.RED);
        g2.drawRect(0, 0, this.sizeX, this.sizeY);
    }

    public void drawAgents(Graphics2D g2) {
        for (Agent agent : environment.getListeAgents()) {
            drawAgent(agent, g2);
        }
    }

    private void drawAgent(Agent agent, Graphics2D g2) {
        g2.setColor(Color.RED);
        Line2D.Double line = new Line2D.Double(agent.getPosition().getX() % sizeX,
                agent.getPosition().getY() % sizeY,
                agent.getPosition().getX() % sizeX + (agent.getDirection().getVector().getDx() * 5),
                agent.getPosition().getY() % sizeY + (agent.getDirection().getVector().getDy() * 5));
        g2.draw(line);
    }

    public void drawGradient(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        for (int iX = 0 ; iX < sizeX ; iX++) {
            for (int iY = 0 ; iY < sizeY ; iY++) {
                double intensite = environment.getGravityIntensityAt(iX, iY);
                g2.setColor(intensiteToColor(intensite, 100));
                g2.drawLine(iX, iY, iX, iY);
            }
        }
    }

    /**
     * Retourne une couleur selon une intensité comprise entre 0(NOIR) et 1785(BLANC)
     * @param intensiteAsDouble
     * @param maxValue
     * @return
     */
    public Color intensiteToColor(double intensiteAsDouble, double maxValue) {
        intensiteAsDouble = (intensiteAsDouble / maxValue) * 1785;
        int intensite = (int) intensiteAsDouble;
        if (intensite >= 1785) return Color.WHITE;
        if (intensite <= 0) return Color.BLACK;
        if (intensite < 255)
            return new Color(0,0,intensite);
        else if (intensite < 510)
            return new Color(0, intensite%255, 255);
        else if (intensite < 765)
            return new Color(0, 255, 255 - intensite%255);
        else if (intensite < 1020)
            return new Color(intensite%255, 255, 0);
        else if (intensite < 1275)
            return new Color(255, 255 - intensite%255, 0);
        else if (intensite < 1530)
            return new Color(255, 0, intensite%255);
        else if (intensite < 1785)
            return new Color(255, intensite%255, 255);
        return Color.WHITE;

    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(RESOLUTION);
                environment.tick();
                this.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
