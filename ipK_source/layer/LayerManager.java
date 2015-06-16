/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */

package layer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class LayerManager extends JDialog implements ActionListener, MouseListener {
	
	LayerDisplay layerDisplay;
	boolean locationNotSet = true;
	int index;
	
	JPanel buttonPanel = new JPanel();
	JButton showHideButton = new JButton("Show / Hide");
	JButton configButton = new JButton("Configure...");
	
	JList layerList;
	
	JPopupMenu popup = new JPopupMenu();
	JMenuItem showHideItem = new JMenuItem("Show / Hide");
	JMenuItem configItem = new JMenuItem("Configure...");
	
	LayerManager(LayerDisplay layerDisplay) {
		super((JFrame) SwingUtilities.windowForComponent(layerDisplay), 
				layerDisplay.name + ": Layers...", false);
		this.layerDisplay = layerDisplay;
		
		showHideItem.addActionListener(this);
		popup.add(showHideItem);
		popup.addSeparator();
		
		configItem.addActionListener(this);
		popup.add(configItem);
		
		showHideButton.addActionListener(this);
		showHideButton.setToolTipText("Show or hide the layer");
		getRootPane().setDefaultButton(showHideButton);
		buttonPanel.add(showHideButton);

		configButton.addActionListener(this);
		configButton.setToolTipText("Configure the layer");
		buttonPanel.add(configButton);
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		layerList = new JList(layerDisplay.layers);
		layerList.setPrototypeCellValue(
				Utilities.getLongestString(layerDisplay.layers) + " (hidden)");
		layerList.addMouseListener(this);
		
		JScrollPane scrollPane = new JScrollPane(layerList);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == showHideItem)
			showHideLayer(index);
		else if (e.getSource() == showHideButton)
			showHideLayer(layerList.getSelectedIndex());
		else if (e.getSource() == configItem)
			configLayer(index);
		else if (e.getSource() == configButton)
			configLayer(layerList.getSelectedIndex());
	}
	
	public void mouseClicked(MouseEvent e) {
		index = layerList.locationToIndex(e.getPoint());
		if (index < 0)
			return;
		
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (e.getClickCount() == 2)
				showHideLayer(index);
		} else
			popup.show(layerList, e.getX(), e.getY());
	}
	
	void showHideLayer(int index) {
		if ((index >= 0) && (index < layerDisplay.layers.length)) {	
			layerDisplay.layers[index].setVisible(
					!layerDisplay.layers[index].isVisible());
			layerList.repaint();
			layerDisplay.repaint();
		}
	}
	
	void configLayer(int index) {
		if ((index >= 0) && (index < layerDisplay.layers.length))
			layerDisplay.layers[index].configure();
	}
	
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
	
}
