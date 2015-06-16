/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */

package layer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LayerDisplay extends JComponent implements ActionListener {

	String name;
	Layer[] layers;
	LayerCanvas canvas;
	LayerManager layerManager = null;
	
	JToolBar toolBar = new JToolBar();
	ButtonGroup cursorButtons = new ButtonGroup();
	
	JToggleButton select = new JToggleButton(Utilities.getImageIcon("images/select.gif"), true);
	JToggleButton zoom = new JToggleButton(Utilities.getImageIcon("images/zoom.gif"));
	JToggleButton center = new JToggleButton(Utilities.getImageIcon("images/center.gif"));
	
	JButton zoomToFit = new JButton(" Zoom to Fit ");
	JButton refresh = new JButton(" Refresh ");
	JButton manageLayers = new JButton(" Layers... ");
	JButton saveGIF = new JButton(" Save GIF... ");
	
	JLabel areaInfo = new JLabel();
	
	public LayerDisplay(String name, double width, double height, 
			Layer[] layers, boolean controls) {
		this(name, new Area(width, height), layers, controls);
	}
	
	public LayerDisplay(String name, Area area, Layer[] layers, boolean controls) {
		super();
		this.name = name;
		this.layers = layers;
		canvas = new LayerCanvas(area);
		
		for (int i = 0; i < layers.length; i++)
			layers[i].setParent(canvas);
		
		setLayout(new BorderLayout());
		setCursor(LayerCanvas.SELECT);
		
		JPanel component = new JPanel(new BorderLayout(), false);
		component.add(canvas, BorderLayout.CENTER);
		areaInfo.setText(name + ": " + area.getInfo());
		add(component, BorderLayout.CENTER);
			
		toolBar.add(select);
		select.setToolTipText("Select Tool");
		select.addActionListener(this);
		cursorButtons.add(select);

		toolBar.add(zoom);
		zoom.setToolTipText("Zoom Tool");
		zoom.addActionListener(this);
		cursorButtons.add(zoom);

		toolBar.add(center);
		center.setToolTipText("Center Tool");
		center.addActionListener(this);
		cursorButtons.add(center);
		
		toolBar.addSeparator();
		
		toolBar.add(zoomToFit);
		zoomToFit.setToolTipText("Resets the window size");
		zoomToFit.addActionListener(this);

		toolBar.add(refresh);
		refresh.setToolTipText("Redraws all visible layers");
		refresh.addActionListener(this);

		toolBar.add(manageLayers);
		manageLayers.setToolTipText("Manage the display's layers");
		manageLayers.addActionListener(this);

		toolBar.add(saveGIF);
		saveGIF.setToolTipText("Saves the current view to an image file");
		saveGIF.addActionListener(this);
		
		if (controls) {
			component.add(areaInfo, BorderLayout.SOUTH);
			add(toolBar, BorderLayout.NORTH);
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == select) {
			setCursor(LayerCanvas.SELECT);
		} else if (evt.getSource() == zoom) {
			setCursor(LayerCanvas.ZOOM);
		} else if (evt.getSource() == center) {
			setCursor(LayerCanvas.CENTER);
		} else if (evt.getSource() == zoomToFit) {
			canvas.area.reset();
			areaInfo.setText(name + ": " + canvas.area.getInfo());
			SwingUtilities.windowForComponent(this).pack();
			repaint();
		} else if (evt.getSource() == refresh) {
			canvas.repaint();
		} else if (evt.getSource() == manageLayers) {
			if (layerManager == null)
				layerManager = new LayerManager(this);
			layerManager.show();
		} else if (evt.getSource() == saveGIF) {
		    // Utilities.saveGIF(canvas);
		}
	}
	
	void setCursor(int cursor) {
		switch (cursor) {
		case LayerCanvas.SELECT:
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			break;
			
		case LayerCanvas.ZOOM:
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			break;
			
		case LayerCanvas.CENTER:
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			break;
		}
		canvas.cursor = cursor;
	}
	
	class LayerCanvas extends JComponent implements MouseListener, 
			MouseMotionListener {
	
		Area area;
		int cursor;
		static final int SELECT = 0;
		static final int ZOOM = 1;
		static final int CENTER = 2;
		
		boolean firstDraw;
		Point zoomStart, zoomLast;
			
		public LayerCanvas(Area area) {
			this.area = area;
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		
		public Dimension getPreferredSize() {
			return area.getPreferredSize(this);
		}
		
		public void paint(Graphics g) {
			Dimension currentSize = getSize();
			if (g == null || currentSize.height <= 0 || currentSize.width <= 0)
				return;
			
			// Clear image
			g.setColor(getBackground());
			g.fillRect(0, 0, currentSize.width, currentSize.height);
			
			// Draw border
			g.setColor(getBackground().darker());
			g.drawRect(0, 0, currentSize.width - 1, currentSize.height - 1);

			// Prepare area object
			area.setScreen(currentSize);
			areaInfo.setText(name + ": " + area.getInfo());

			// Draw layers
			for (int i = 0; i < layers.length; i++)
				if (layers[i].isVisible())
					layers[i].draw(g, area);
		}

		public void mouseClicked(MouseEvent e) {
			switch (cursor) {
			case SELECT:
				boolean handled = false;
				int i = layers.length;
				while (!handled && i > 0)
					if (layers[--i].isVisible())
						handled = layers[i].handleClick(e, area);
				break;
			
			case CENTER:
				area.center(e.getPoint());
				repaint();
				break;
			}
		}
				 
		public void mousePressed(MouseEvent e) {
			if ((cursor != ZOOM) || !SwingUtilities.isLeftMouseButton(e))
				return;
			
			zoomStart = e.getPoint();
			zoomLast = zoomStart;
			firstDraw = true;
		}
		
		public void mouseDragged(MouseEvent e) {
			if ((cursor != ZOOM) 
					|| (!SwingUtilities.isLeftMouseButton(e)) 
					|| (e.getPoint().equals(zoomLast)))
				return;
			
			Graphics g = getGraphics();
			g.setXORMode(Color.white);
			
			if (firstDraw) {
				firstDraw = false;
			} else {
				g.drawRect((zoomStart.x > zoomLast.x) ? zoomLast.x : zoomStart.x, 
						(zoomStart.y > zoomLast.y) ? zoomLast.y : zoomStart.y, 
						Math.abs(zoomStart.x - zoomLast.x), 
						Math.abs(zoomStart.y - zoomLast.y));
			}
			zoomLast = e.getPoint();
			g.drawRect((zoomStart.x > zoomLast.x) ? zoomLast.x : zoomStart.x, 
					(zoomStart.y > zoomLast.y) ? zoomLast.y : zoomStart.y, 
					Math.abs(zoomStart.x - zoomLast.x), 
					Math.abs(zoomStart.y - zoomLast.y));
		}
		
		public void mouseReleased(MouseEvent e) {
			if (cursor != ZOOM)
				return;
				
			boolean zoomIn = SwingUtilities.isLeftMouseButton(e);
			
			if (zoomIn) {
				Graphics g = getGraphics();
				g.setXORMode(Color.white);
				
				if (firstDraw) {
					firstDraw = false;
				} else {
					g.drawRect((zoomStart.x > zoomLast.x) ? zoomLast.x : zoomStart.x, 
							(zoomStart.y > zoomLast.y) ? zoomLast.y : zoomStart.y, 
							Math.abs(zoomStart.x - zoomLast.x), 
							Math.abs(zoomStart.y - zoomLast.y));
				}
				zoomLast = e.getPoint();
			}

			if ((Math.min(Math.abs(zoomStart.x - zoomLast.x), 
					Math.abs(zoomStart.y - zoomLast.y)) < 3)
					|| (!zoomIn))
				area.zoom(zoomStart, (zoomIn) ? 2.0 : 0.5);
			else
				area.zoom(zoomStart, zoomLast, zoomIn);
			repaint();
		}
		
		public void mouseEntered(MouseEvent e) {}
		
		public void mouseExited(MouseEvent e) {}
		
		public void mouseMoved(MouseEvent e) {}
	}
}
