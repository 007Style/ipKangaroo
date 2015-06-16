/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */

package layer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TextLayer extends Layer implements ActionListener {

	static final int INDENT = 16; // pixels
	static final int DELAY = 100; // miliseconds
	static final int INIT_SPEED = 2; // pixels
	static final Font FONT = new Font("Sans-Serif", Font.BOLD, 13);
	static final Color COLOR = Color.black;
	
	int y, lineHeight, position = -1000;
	int previousSpeed, speed = INIT_SPEED;
	Timer timer;
	String[] text;
	
	public TextLayer(String name, String[] text) {
		setName(name);
		this.text = text;
	}
	
	public void draw(Graphics g, Area a) {
		if (timer == null) {
			lineHeight = g.getFontMetrics().getHeight();
			position = -lineHeight;

			timer = new Timer(DELAY, this);
			timer.setInitialDelay(DELAY);
			timer.start();
		}
		
		Dimension d = a.getScreen();
		if (position <= -lineHeight * 2)
			position = text.length * lineHeight + d.height;
		y = d.height - position;
		
		g.setFont(FONT);
		g.setColor(COLOR);
		for (int i = 0; i < text.length; i++) {
			if (y < d.height + lineHeight)
				g.drawString(text[i], INDENT, y);
			y += lineHeight;
		}
	}
	
	public boolean handleClick(MouseEvent e, Area a) {
		if (e.getClickCount() > 1) {
			if (speed == 0)
				speed = -previousSpeed;
			else
				speed = -speed;
		} else {
			if (speed != 0) {
				previousSpeed = speed;
				speed = 0;
			} else
				speed = previousSpeed;
		}
		if (speed == 0)
			timer.stop();
		else if (!timer.isRunning())
			timer.start();
		return true;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (getParent().isShowing() && isVisible()) {
			getParent().repaint();
			position = (y < 0) ? -lineHeight : position + speed;
		} else if (!getParent().isShowing()) {
			timer.stop();
			timer = null;
		}
	}
	
}
