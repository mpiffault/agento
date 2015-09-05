package fr.mpiffault.agento.view;

import fr.mpiffault.agento.model.*;
import fr.mpiffault.agento.model.geometry.Position;
import fr.mpiffault.agento.model.geometry.Traceable;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Board extends JPanel implements Runnable {

    private static final long serialVersionUID = -8954030686627371948L;
    public static final long TIME_RESOLUTION = 30l;
    @Getter
    private Environment environment;
    private final ArrayList<LinkedList<? extends Drawable>> layers;
    private Selectable selectedEntity;
    private Set<Integer> keysPressed;

    Board(final Environment environment) {
        super();
        this.layers = new ArrayList<>();
        this.keysPressed = new HashSet<>();
        setBackground(Color.WHITE);
        this.environment = environment;
        environment.setKeysPressed(keysPressed);
        LinkedList<Drawable> firstLayer = new LinkedList <>();
        firstLayer.add(environment);
        this.layers.add(0, firstLayer);
        this.layers.add(1, environment.getAgentList());

        EventListener mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                boolean entityDeselected = deselectEntity();
                Selectable nearest = environment.getAgentAt(new Position(e.getX(), e.getY()));
                if (nearest != null) {
                    selectEntity(nearest);
                } else if(!entityDeselected) {
                    Agent a = new Agent(environment, new Position(e.getX(), e.getY()));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedEntity != null && selectedEntity instanceof Draggable) {
                    ((Draggable)selectedEntity).setPosition(new Position(e.getX(), e.getY()));
                }
            }
        };

        this.addMouseListener((MouseListener) mouseHandler);
		this.addMouseMotionListener((MouseMotionListener) mouseHandler);

        KeyListener keyHandler = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keysPressed.add(e.getKeyCode());
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'f' && selectedEntity instanceof Controllable) {
                    ((Controllable)selectedEntity).setFree(!((Controllable)selectedEntity).isFree());
                } else if (e.getKeyChar() == 't' && selectedEntity instanceof Traceable) {
                    if (((Traceable)selectedEntity).isTrace()) {
                        ((Traceable)selectedEntity).unTrace();
                    } else {
                        ((Traceable)selectedEntity).trace();
                    }
                } else if (e.getKeyChar() == 'T') {
                    if (environment.isFullTrace()) {
                        environment.setFullTrace(false);
                        for (Traceable t : environment.getAgentList()) {
                            t.unTrace();
                        }
                    } else {
                        environment.setFullTrace(true);
                        for (Traceable t : environment.getAgentList()) {
                            t.trace();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keysPressed.remove(e.getKeyCode());
            }
        };
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

    }

    public int getSizeX() {
        return this.environment.getSizeX();
    }

    public int getSizeY() {
        return this.environment.getSizeY();
    }

    private void selectEntity(Selectable selectable) {
        if (selectable != null) {
            selectable.select();
        }
        this.selectedEntity = selectable;
    }

    private boolean deselectEntity() {
        if (this.selectedEntity != null) {
            this.selectedEntity.deselect();
            this.selectedEntity = null;
            return true;
        }
        return false;
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

    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);
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
