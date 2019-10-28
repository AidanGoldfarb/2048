

import java.awt.event.KeyEvent;
/*
 * class used instead of implemented methods, now many keys can be pressed at once
 */
public class Keyboard {
	
	static boolean [] pressed = new boolean [256];
	static boolean [] prev = new boolean [256];
	
	
	private Keyboard() {};
	/*
	 * updates gameboard by logging keys pressed
	 */
	public static void update() {
		for(int i = 0; i<4; i++) {
			if(i == 0)
				prev[KeyEvent.VK_LEFT] = pressed[KeyEvent.VK_LEFT];
			if(i == 1)
				prev[KeyEvent.VK_RIGHT] = pressed[KeyEvent.VK_RIGHT];
			if(i == 2)
				prev[KeyEvent.VK_UP] = pressed[KeyEvent.VK_UP];
			if(i == 3)
				prev[KeyEvent.VK_DOWN] = pressed[KeyEvent.VK_DOWN];
		}
	}
	
	public static void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}
	
	public static void keyReleased(KeyEvent e) {
		pressed[e.getKeyCode()]= false;
	}
	
	public static boolean typed(int KeyEvent) {
		return
			!pressed[KeyEvent] && prev[KeyEvent]; //checks if pressed between updates
	}
	
}
