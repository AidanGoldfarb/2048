package com.aidangoldfarb.game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
/*
 * utility class that helps with centering text
 */
public class DrawUtils {

	private DrawUtils() {}
	/*
	 * gives information on pixel width in order to center 
	 */
	static int getMessageWidth(String message, Font font, Graphics2D g) {
		g.setFont(font);
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(message, g);
		return 
			(int)bounds.getWidth();
	}
	/*
	 * gives info on pixel height
	 */
	public static int getMessageHeight(String message, Font font, Graphics2D g) {
		g.setFont(font);
		if(message.length() == 0)
			return 0;
		TextLayout tl = new TextLayout(message, font, g.getFontRenderContext());
		return
			(int)tl.getBounds().getHeight();
	}
}
