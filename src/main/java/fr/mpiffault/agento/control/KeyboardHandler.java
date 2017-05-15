package fr.mpiffault.agento.control;

import fr.mpiffault.agento.view.Board;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class KeyboardHandler extends KeyAdapter {

    private static List<Integer> controlKeys = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
    private Board board;

    public KeyboardHandler(Board board) {
        this.board = board;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        board.addControlKey(e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F:
                this.board.toggleFreeSelected();
                break;
            case KeyEvent.VK_T:
                if (e.isShiftDown()) {
                    this.board.toggleTraceAll();
                } else {
                    this.board.toggleTraceSelected();
                }
                break;
            case KeyEvent.VK_M:
                if (e.isShiftDown()) {
                    this.board.toggleModeAll();
                } else {
                    this.board.toggleModeSelected();
                }
                break;
            default:
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        board.removeControlKey(e.getKeyCode());
    }
}
