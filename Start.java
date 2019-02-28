package com.aidangoldfarb.game;

import javax.swing.JFrame;
/*
 * driver method that sets up frame and runs game object
 */
public class Start {
	static JFrame frame = new JFrame("2048");
	public static void main(String [] args) {
		Game game = new Game();
		frame = new JFrame("2048");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.start();
	}

}
