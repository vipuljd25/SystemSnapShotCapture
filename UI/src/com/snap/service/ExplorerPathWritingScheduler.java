package com.snap.service;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import static com.snap.common.util.SnapConstants.*;

import com.snap.common.util.WriteFileUtility;
import com.snap.processor.CommandLineProcessor;
import com.snap.processor.ExplorerPathProcessor;

import java.util.Date;


// Create a class extends with TimerTask
public class ExplorerPathWritingScheduler extends TimerTask {
	private static final Logger logger = Logger.getLogger(ExplorerPathWritingScheduler.class.getName());
	Date now; // to display current time

	// Add your task here
	
	public void run() {
		
		now = new Date(); // initialize date
	//	System.out.println("Time is :" + now); // Display current time
			ExplorerPathProcessor explorerPath= new ExplorerPathProcessor();
			WriteFileUtility writeFile = new WriteFileUtility();
			//writeFile.writeFile(APP_DATA_LOCATION + "\\" +IE_URLS_FILE_PATH,explorerPath.populateExplorerPathList());
			//logger.debug("run() End...");
	}
	
	
}
