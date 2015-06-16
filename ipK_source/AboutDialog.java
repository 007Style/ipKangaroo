/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */
 
/*
This file displays the scrolling 'About' dialogue box in ipKtalk
*/

import layer.*;
import java.awt.*;
import javax.swing.*;

public class AboutDialog extends JDialog {
    
    String[] text = {"ipKangaroo", 
                     "Rutgers University", 
                     "",
                     "ipKtalk version 5.2.2",
                     "", "ipKangaroo java client  Developed by:", 
                     "    Daneyand Singley", "    Vivek Bedi", 
                     "", "",
                        "ipkangaroo.no-ip.net  Developed by:",
                        "    Even Lerer",
                        "    Mary Alexander",
                        "    Chi-Wei Yung",
                        "    Michael Weakland",
                        "", "",
                        "A special thanks to: Roland Wunderlich,",
                        "for development support.",
                        "","",
                        "A special thanks to: Roderick Rivera,",
                        "for development support.",
                        "","",
                        "Java Scroll capabilities Copyright(c) 1998",
                        "by Roland Wunderlich, All rights reserved.",
                        "","",
                        "Audio assistance provided by: Sun Microsystems",
                        "(c) 1999 All Rights Reserved",
                        "","",
                        "ipKangaroo capabilities Copyright(c) 2000-2001",
                        "by ipKangaroo.eyep.net staff.",
                        "email: admin@ipKangaroo.no-ip.net",
                        "All rights reserved."};
    
    AboutDialog(Frame owner) {
        super(owner, "About ipKangaroo", true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Layer[] layers = new Layer[2];
        layers[0] = new ImageLayer("About background", 
                                   Utilities.getImage("ipkbk.jpg"));
        layers[1] = new TextLayer("About text", text);
        
        getContentPane().add(new LayerDisplay("About datum.viewer", 
                                              640.0, 490.0, layers, false));

        pack();
        setLocationRelativeTo(owner);
        show();
    }
    
}
