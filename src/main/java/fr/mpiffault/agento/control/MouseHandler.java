package fr.mpiffault.agento.control;

import fr.mpiffault.agento.model.Agent;
import fr.mpiffault.agento.model.Draggable;
import fr.mpiffault.agento.model.Selectable;
import fr.mpiffault.agento.model.geometry.Position;
import fr.mpiffault.agento.view.Board;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter{
    private Board board;

    public MouseHandler(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        boolean entityDeselected = board.deselectEntity();
        Selectable nearest = board.getEnvironment().getAgentAt(new Position(e.getX(), e.getY()));
        if (nearest != null) {
            board.selectEntity(nearest);
        } else if(!entityDeselected) {
            Agent a = new Agent(board.getEnvironment(), new Position(e.getX(), e.getY()));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.getSelectedEntity() != null && board.getSelectedEntity() instanceof Draggable) {
            ((Draggable)board.getSelectedEntity()).setPosition(new Position(e.getX(), e.getY()));
        }
    }
}
