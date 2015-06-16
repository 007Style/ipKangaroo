/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */
 
 /*
 This class displays the 'About' dialogue for ipKchat.
 */

import java.lang.*;
import java.lang.String;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;


public class About extends Frame implements ActionListener
{
  Panel paneltxtfld = new Panel(new GridBagLayout());
  public About()
  {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    
    Panel panelbut = new Panel();

    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    
    add(panelbut, gbc);
    
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1.0;
    
    add(paneltxtfld, gbc);

    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;

    TextArea ta1;
   
    ta1 = new TextArea("ipKchat    v5.2.2\n\nDeveloped by:\nDaneyand Singley, Vivek Bedi\n\n(c) 2001 ipKangaroo, All Rights Reserverd.", 7, 40,1);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    paneltxtfld.add(ta1, gbc);
    ta1.setEditable(false);

    Button close = new Button(" - Close - ");

    Font    font = new Font ("Times", Font.BOLD, 14);
    Color   numberColor = new Color (50, 200, 255);
    Color   equalsColor = new Color (120, 160, 170);
    setFont (font);
    
    panelbut.add(close);
    close.setBackground(numberColor);
    close.addActionListener(this);
    
    WindowListener2 lWindow = new WindowListener2();
    addWindowListener(lWindow);
    
    this.setLocation(150,150);
    setTitle("ipKchat About");
    setBackground(Color.white);
    setSize (400, 200);
    pack();
    setResizable(false);
    show ();
  }
  
  public void actionPerformed(ActionEvent event)
  { 
    if(event.getActionCommand() == " - Close - ")
      {
        this.dispose();
      }
  }
  
  class WindowListener2 extends WindowAdapter
  {
    public void windowClosing(WindowEvent event)
    {
      Window win = event.getWindow();
      win.setVisible(false);
      win.dispose();
      //System.exit(0);
    }
  }
  
  public static void main (String args[])
  {  
    About as = new About();
  }
}









