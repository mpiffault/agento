package fr.mpiffault.agento.view;

import fr.mpiffault.agento.model.Agent;
import fr.mpiffault.agento.model.Environment;

import javax.swing.*;

public class App {
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "True");
		
		JFrame frame = new JFrame("AgentO");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Environment e = new Environment();
		Agent a = new Agent(e);
		
		
		Board board = new Board(e);
		
		Thread t = new Thread(board);
		
		frame.add(board);
		frame.setSize(board.getSizeX() + 1, board.getSizeY() + 38);
		frame.setVisible(true);
		
		t.start();
	}
}
