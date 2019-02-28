package com.aidangoldfarb.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
/*
 * class that handles game running features, such as threads and key inputs
 */
public class Game extends JPanel implements KeyListener, Runnable{

	private static final long serialVersionUID = 1L;
	JFrame frame = new JFrame("2048");
	static final int WIDTH = 400;
	static final int HEIGHT = 630;
	static final Font main = new Font("Bebas Neue Regular", Font.PLAIN, 28);
	private Thread game;
	private boolean running;
	private BufferedImage Image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private GameBoard board;
	
	public Game() {
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		board = new GameBoard(WIDTH/2 - GameBoard.boardWidth/2, HEIGHT - GameBoard.boardHeight - 10);
		
		
	}
	
	private void update() {
		board.update();
		Keyboard.update();
	}
	
	private void render() {
		Graphics2D g = (Graphics2D)Image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		board.render(g);
		g.dispose(); //renders board and disposes of unneeded graphics 
		
		Graphics2D g2d = (Graphics2D)getGraphics(); 
		g2d.drawImage(Image,0,0,null);
		g2d.dispose();
	}
	/*
	 * fps and related vars are unused but kept in for later implementation 
	 */
	@Override
	public void run() { 	
		int fps = 0, updates = 0;
		long fpsTimer = System.currentTimeMillis();
		double nsPerUpdate = 1000000000.0 /60; //nanosecs per update
		
		double t = System.nanoTime(); //update time
		double unproccessed = 0;
		
		while(running) { //when running
			boolean shouldRender = false;
			double now = System.nanoTime();
			unproccessed += (now-t) / nsPerUpdate;
			t = now;
			
		//update queue
		while(unproccessed>0) {
			updates++;
			update();
			unproccessed--;
			shouldRender = true;
		}
		
		//rendering
		if(shouldRender) {
			fps++;
			try {
			render();
			}catch(Exception e) {}		
			shouldRender = false;
		}
		else
			try {
				Thread.sleep(1);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//fps
		if(System.currentTimeMillis() - fpsTimer > 1000) {
			System.out.printf("%d fps %d updates", fps, updates); //prints fps formated
			System.out.println();
			fps = 0;
			updates = 0;
			fpsTimer += 1000;
		}
		
	}
	/*
	 * start game
	 */
	public synchronized void start() {
		if(running)
			return;
		running = true;
		game = new Thread(this, "game");
		game.start();
	}
	/*
	 * end game
	 */
	public synchronized void stop() {
		if(running) 
			return;
		running = false;
		System.exit(0);
	}
	
	public void confirmQuit() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Do you want to quit? yes/no ");
		String str = sc.next();
		if(str.equalsIgnoreCase("yes"))
			System.exit(0);
		else
			sc.close();
	}
	
	public void restart() {
		Game game1 = new Game();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game1);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game1.start();
	}
	
	public void confirmRestart() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Do you want to restart? yes/no ");
		String str = sc.next();
		if(str.equalsIgnoreCase("yes")) {
			running = false;
			Start.frame.dispose();
			frame.dispose();
			frame.setVisible(false);
			restart();
		}
		else
			sc.close();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) { 
			System.out.print("UP_ARROWKEY - ");
			System.out.println("valid");
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) { 
			System.out.print("DOWN_ARROWKEY - ");
			System.out.println("valid");
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) { 
			System.out.print("RIGHT_ARROWKEY - ");
			System.out.println("valid");
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) { 
			System.out.print("LEFT_ARROWKEY - ");
			System.out.println("valid");
		}
		
		else if (e.getKeyChar() == 'q' ||
				 e.getKeyChar() == 'r') {
			System.out.print(e.getKeyChar() + " pressed - ");
			System.out.println("valid");
		}
		else { 
			System.out.print(e.getKeyChar() + " pressed - ");
			System.out.println("invalid");
		}
		if(e.getKeyChar() == 'q') {
			confirmQuit();
		}
		if(e.getKeyChar() == 'r') {
			confirmRestart();
		}
		Keyboard.keyPressed(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Keyboard.keyReleased(e);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
