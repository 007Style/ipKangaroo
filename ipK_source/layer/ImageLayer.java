/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */

package layer;

import java.awt.*;
import java.awt.event.*;

public class ImageLayer extends Layer {

	Image image, resizedImage;
	Dimension size = new Dimension();
	
	public ImageLayer(String name, Image image) {
		setName(name);
		this.image = image;
	}
	
	public void draw(Graphics g, Area a) {
		if (!size.equals(a.getScreen())) {
			size = a.getScreen();
			resizedImage = image.getScaledInstance(size.width, size.height, 
					Image.SCALE_FAST);
		}
		
		g.drawString("Loading " + name + " image...", TextLayer.INDENT, TextLayer.INDENT);
		g.drawImage(resizedImage, 0, 0, getParent());
	}
	
	public boolean handleClick(MouseEvent e, Area a) {
		return false;
	}
	
}
