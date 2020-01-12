package com.snap.common.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MinimizeAll {

	public static void main(String[] args) throws IOException,AWTException {
		
		minimize();
	}
	public static void minimize() throws IOException, AWTException
	{
		Robot r = new Robot();
		r.setAutoDelay(250);
		r.keyPress(KeyEvent.VK_WINDOWS);
		r.keyPress(KeyEvent.VK_D);
		r.keyRelease(KeyEvent.VK_D);
		r.keyRelease(KeyEvent.VK_WINDOWS);
	}

}
