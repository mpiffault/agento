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
        if (controlKeys.contains(e.getKeyCode())) {
            board.addControlKey(e.getKeyCode());
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_F:
                    this.board.toggleFreeSelected();
                    break;
                case KeyEvent.VK_T:
                    if (e.isShiftDown()) {
                        this.board.toggleFullTrace();
                    } else {
                        this.board.toggleSelectedTrace();
                    }
                    break;
                default:
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        board.removeControlKey(e.getKeyCode());
    }
}
