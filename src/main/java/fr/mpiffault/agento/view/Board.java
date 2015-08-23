package fr.mpiffault.agento.view;

import fr.mpiffault.agento.model.Agent;
import fr.mpiffault.agento.model.Environment;
import fr.mpiffault.agento.model.geometry.Position;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

public class Board extends JPanel implements Runnable {

    private static final long serialVersionUID = -8954030686627371948L;
    public static long TIME_RESOLUTION = 30l;
    @Getter
    private final Environment environment;
    private ArrayList<LinkedList<? extends Drawable>> layers;

    Board(final Environment environment) {
        super();
        this.layers = new ArrayList<>();
        setBackground(Color.WHITE);
        this.environment = environment;
        LinkedList<Drawable> firstLayer = new LinkedList <>();
        firstLayer.add(environment);
        this.layers.add(0, firstLayer);
        this.layers.add(1, environment.getAgentList());

        Mouse s = new Mouse();
        this.addMouseListener(s);
		this.addMouseMotionListener(s);

    }

    private class Mouse extends MouseAdapter {
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
    public int getSizeX() {
        return this.environment.getSizeX();
    }

    public int getSizeY() {
        return this.environment.getSizeY();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (LinkedList<? extends Drawable> drawables : layers) {
            for (Drawable drawable : drawables) {
                drawable.draw(g2);
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while(true) {
            try {
                Thread.sleep(TIME_RESOLUTION);
                this.environment.tick();
                this.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
