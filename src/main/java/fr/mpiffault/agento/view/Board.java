package fr.mpiffault.agento.view;

import fr.mpiffault.agento.control.KeyboardHandler;
import fr.mpiffault.agento.control.MouseHandler;
import fr.mpiffault.agento.model.*;
import fr.mpiffault.agento.model.geometry.Position;
import fr.mpiffault.agento.model.geometry.Traceable;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;

import static fr.mpiffault.agento.model.Mode.RANDOM;

public class Board extends JPanel implements Runnable {

    private static final long serialVersionUID = -8954030686627371948L;
    public static final long TIME_RESOLUTION = 30l;
    @Getter
    private Environment environment;
    private final ArrayList<LinkedList<? extends Drawable>> layers;
    @Getter
    private Selectable selectedEntity;
    @Getter
    private Set<Integer> keysPressed;

    private Mode currentMode = RANDOM;

    public static final Color BLUE = new Color(137, 188, 197, 150);
    public static final Color PINK = new Color(230, 130, 139, 255);
    public static final Color RED = new Color(159, 64, 72, 255);
    public static final Color YELLOW = new Color(227, 171, 72, 255);

    Board(final Environment environment) {
        super();
        setBackground(Color.WHITE);

        this.environment = environment;
        this.keysPressed = new HashSet<>();
        this.environment.setKeysPressed(keysPressed);

        this.layers = new ArrayList<>();
        LinkedList<Drawable> firstLayer = new LinkedList <>();
        firstLayer.add(environment);
        this.layers.add(0, firstLayer);
        this.layers.add(1, environment.getAgentList());

        EventListener mouseHandler = new MouseHandler(this);
        this.addMouseListener((MouseListener) mouseHandler);
		this.addMouseMotionListener((MouseMotionListener) mouseHandler);

        this.setFocusable(true);
        this.addKeyListener(new KeyboardHandler(this));

    }

    public int getSizeX() {
        return this.environment.getSizeX();
    }

    public int getSizeY() {
        return this.environment.getSizeY();
    }

    public void selectEntity(Selectable selectable) {
        if (selectable != null) {
            selectable.select();
        }
        this.selectedEntity = selectable;
    }

    public boolean deselectEntity() {
        if (this.selectedEntity != null) {
            this.selectedEntity.deselect();
            this.selectedEntity = null;
            return true;
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Point mousePoint = getMousePosition();
        Position mousePosition;
        if (mousePoint != null) {
            mousePosition = new Position(mousePoint.getX(), mousePoint.getY());
        } else {
            mousePosition = new Position();
        }
        for (LinkedList<? extends Drawable> drawables : layers) {
            for (Drawable drawable : drawables) {
                drawable.draw(g2, mousePosition);
            }
        }
    }

    public void toggleFreeSelected() {
        if (selectedEntity instanceof Controllable) {
            ((Controllable) selectedEntity).setFree(!((Controllable) selectedEntity).isFree());
        }
    }

    public void toggleTraceAll() {
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

    public void toggleTraceSelected() {
        if (selectedEntity instanceof Traceable) {
            if (((Traceable) selectedEntity).isTrace()) {
                ((Traceable) selectedEntity).unTrace();
            } else {
                ((Traceable) selectedEntity).trace();
            }
        }
    }

    public void addControlKey(int keyCode) {
        keysPressed.add(keyCode);
    }

    public void removeControlKey(int keyCode) {
        keysPressed.remove(keyCode);
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

    public void toggleModeAll() {
        currentMode = Mode.nextMode(currentMode);
        environment.getAgentList().forEach(agent -> agent.setMode(currentMode));
    }

    public void toggleModeSelected() {
        if (selectedEntity instanceof Agent) {
            ((Agent)selectedEntity).toggleMode();
        }
    }
}
