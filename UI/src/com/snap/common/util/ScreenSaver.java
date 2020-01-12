package com.snap.common.util;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class ScreenSaver extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel contentPane;
    JLabel imageLabel = new JLabel();
    private static final Logger logger = Logger.getLogger(ScreenSaver.class.getName());
    public ScreenSaver() {
        try {
        	logger.debug("Inside main frame constructor Loading GIF While PRocessing");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            getContentPane().setBackground(Color.BLACK);
            setResizable(false);
            setUndecorated(true);
            setAlwaysOnTop( true );
            setOpacity((float) 0.8);
            validate();
            contentPane = (JPanel) getContentPane();
            contentPane.setLayout(new BorderLayout());
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            // add the image label
            ImageIcon ii = new ImageIcon(this.getClass().getResource(
                    "/img/process.gif"));
            imageLabel.setIcon(ii);
            contentPane.add(imageLabel, java.awt.BorderLayout.CENTER);
            //contentPane.add(BorderLayout.EAST,imageLabel);
            // show it
            //this.setLocationRelativeTo();
            this.setVisible(true);
            
        } catch (Exception exception){
            logger.error(exception);
        }
    }
    public void mainFrameStop()
    {
    	logger.debug("Inside MainFrameStop to Stop Screen Saver");
    	this.setVisible(false);
    }
    public static void main(String[] args) {
        new ScreenSaver();
    }

}