/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */

package layer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class Layer {

	protected String name = "Unnamed layer";
	protected boolean visible = true;
	protected Component parent;
	
	public abstract void draw(Graphics g, Area a);
	
	public abstract boolean handleClick(MouseEvent e, Area a);
	
	public void configure() {
		JOptionPane.showMessageDialog(parent, 
				"This layer does not have any configuration options.", 
				name, JOptionPane.INFORMATION_MESSAGE);
	}

	public final String getName() {
		return name;
	}
	
	public final String toString() {
		return (visible) ? name : name + " (hidden)";
	}

	public final boolean isVisible() {
		return visible;
	}
	
	final Component getParent() {
		return parent;
	}

	final void setName(String name) {
		if (name != null)
			this.name = name;
	}

	final void setVisible(boolean visible) {
		this.visible = visible;
	}

	final void setParent(Component parent) {
		this.parent = parent;
	}
	
}
