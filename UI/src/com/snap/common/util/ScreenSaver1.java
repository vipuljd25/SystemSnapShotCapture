package com.snap.common.util;

import java.awt.Color;
import java.awt.GraphicsEnvironment;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class ScreenSaver1 {
    private static final JFrame frame = new JFrame();

   /* public static void startScreenSaver() throws Exception {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         
         frame.add(new JLabel(new ImageIcon("img/Add.png")));
         frame.setBackground(Color.BLUE);
         frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         frame.setResizable(false);
         frame.setUndecorated(true);
         frame.validate();
         GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
         frame.setVisible(true);
    }

    public static void stopScreenSaver() {
         frame.setVisible(false);
    }*/
    public static void main(String[] args) throws Exception
    {
    	//startScreenSaver();
    }
}

