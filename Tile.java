package com.aidangoldfarb.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
/*
 * class that sets up the tile object, each tile is represented by a 2droundedrectangle
 */
public class Tile {
	static final int WIDTH = 80;
	static final int HEIGHT = 80;
	static final int SLIDE_SPEED = 20;
	static final int ARC_WIDTH = 15;
	static final int ARC_HEIGHT = 15;
	
	private Point slideTo;
	private int value;
	private BufferedImage tileImage;
	private Color background;
	private Color text;
	private int x, y;
	private Font font; 
	private boolean beginningAnimation = true;
	private double scaleFirst = .1;
	private BufferedImage beginningImage;
	private boolean combinedAnimation = false;
	private double scaleCombine = 1.2;
	private BufferedImage combinedImage;
	private boolean canCombine = true;
	

	public Tile(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
		slideTo = new Point(x,y);
		tileImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		beginningImage = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_ARGB);
		combinedImage = new BufferedImage(WIDTH*2,HEIGHT*2, BufferedImage.TYPE_INT_ARGB);
		drawImage();
	}
	/*
	 * draws each tile based on its number
	 */
	private void drawImage() {
		Graphics2D g = (Graphics2D) tileImage.getGraphics();
		if(value == 2) {
			background = new Color(0xe9e9e9);
			text = new Color(0x000000);
		}
		else if(value == 4) {
			background = new Color(0xe6daab);
			text = new Color(0x000000);
		}
		else if(value == 8) {
			background = new Color(0xf79d3d);
			text = new Color(0xffffff);
		}
		else if(value == 16) {
			background = new Color(0xf28007);
			text = new Color(0xffffff);
		}
		else if(value == 32) {
			background = new Color(0xf55e3b);
			text = new Color(0xffffff);
		}
		else if(value == 64) {
			background = new Color(0xf00000);
			text = new Color(0xffffff);
		}
		else if(value == 128) {
			background = new Color(0xe9de84);
			text = new Color(0xffffff);
		}
		else if(value == 256) {
			background = new Color(0xf6e872);
			text = new Color(0xffffff);
		}
		else if(value == 512) {
			background = new Color(0xf5e455);
			text = new Color(0xffffff);
		}
		else if(value == 1024) {
			background = new Color(0xf7e12c);
			text = new Color(0xffffff);
		}
		else if(value == 2048) {
			background = new Color(0xffe400);
			text = new Color(0xffffff);
		}else {
		background = Color.BLACK;
		text = Color.WHITE;
		}
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(background);
		g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
		g.setColor(text);
		if(value <= 64) {
			font = Game.main.deriveFont(36f);
		}else
			font = Game.main;
		g.setFont(font);
		
		int drawX = WIDTH/2 - DrawUtils.getMessageWidth("" + value, font, g)/2; //gets center and moves to left half length
		int drawY = HEIGHT/2 + DrawUtils.getMessageHeight(""+value, font, g)/2; //java draws from bottom, so + not -
		g.drawString(""+value, drawX, drawY);
		g.dispose();
	}
	/*
	 * updates the tile every time the thread is updated(?)
	 * not really sure if the affinetansforms actually do anything 
	 */
	public void update() {
		if(beginningAnimation) { //starts as true;
			AffineTransform transform = new AffineTransform();
			transform.translate(WIDTH/2 - scaleFirst * WIDTH, HEIGHT - scaleFirst * HEIGHT/2);
			transform.scale(scaleFirst, scaleFirst);
			Graphics2D g2d = (Graphics2D)beginningImage.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setColor(new Color(0,0,0,0));
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			g2d.drawImage(tileImage, transform, null);
			scaleFirst += .1;
			g2d.dispose();
			if(scaleFirst >= 1)
				beginningAnimation = false;
		}
		else if(combinedAnimation) {
			AffineTransform transform = new AffineTransform();
			transform.translate(WIDTH/2 - scaleCombine * WIDTH, HEIGHT - scaleCombine * HEIGHT/2);
			transform.scale(scaleCombine, scaleCombine);
			Graphics2D g2d = (Graphics2D)combinedImage.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setColor(new Color(0,0,0,0));
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			g2d.drawImage(tileImage, transform, null);
			scaleCombine -= .05;
			g2d.dispose();
			if(scaleCombine <= 1)
				combinedAnimation = false;
		}
	}
	/*
	 * does the drawing 
	 */
	public void render(Graphics2D g) {
		if(beginningAnimation) {
			g.drawImage(beginningImage, x, y, null);
		}
		else if(combinedAnimation) {
			g.drawImage(combinedImage, (int)(x+WIDTH/2 - scaleCombine * WIDTH/2),
									   (int)(y+HEIGHT/2 - scaleCombine * HEIGHT/2), null);
		}
		else
			g.drawImage(tileImage, x, y, null);
		g.drawImage(tileImage, x, y, null);
		
	}
	/*
	 * getters and setters 
	 */
	public Point getSlideTo() {
		return slideTo;
	}

	public void setSlideTo(Point slideTo) {
		this.slideTo = slideTo;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int v) {
		value = v;
		drawImage();
	}
	public boolean canCombine() {
		return canCombine;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setCanCombine(boolean canCombine) {
		this.canCombine = canCombine;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public boolean isCombinedAnimation() {
		return combinedAnimation;
	}

	public void setCombinedAnimation(boolean combinedAnimation) {
		this.combinedAnimation = combinedAnimation;
		if(combinedAnimation)
			scaleCombine = 1.3; //makes sure same tile will reset to initial scale 
	}
}
