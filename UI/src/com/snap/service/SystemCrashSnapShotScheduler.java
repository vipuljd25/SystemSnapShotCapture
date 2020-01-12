package com.snap.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.TimerTask;

import static com.snap.common.util.SnapConstants.APP_DATA_LOCATION;
import static com.snap.common.util.SnapConstants.SYSTEM_CRASH;
import static com.snap.common.util.SnapConstants.SYSTEM_CRASH_TMP;
import org.apache.log4j.Logger;



public class SystemCrashSnapShotScheduler extends TimerTask {
	private static final Logger logger = Logger.getLogger(SystemCrashSnapShotScheduler.class.getName());

	Date now;

	// Add your task here
	public void run() {

		logger.debug("Inside Run");
		now = new Date(); // initialize date
		logger.debug("Time is :" + now); // Display current time
		try {
			synchronized (this) {
				File file = new File(APP_DATA_LOCATION + "\\" + SYSTEM_CRASH_TMP);
				deleteFile(file);
				SnapShotService.takeSnapShot(SYSTEM_CRASH_TMP);
				
				File dest = new File(APP_DATA_LOCATION + "\\" + SYSTEM_CRASH);
				deleteFile(dest);
				dest.mkdirs(); // make sure that the dest directory exists
				Path destPath = dest.toPath();
				for (File sourceFile : file.listFiles()) {
				    Path sourcePath = sourceFile.toPath();
				    Files.copy(sourcePath, destPath.resolve(sourcePath.getFileName()));
				}
			}

		}/* catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */catch (IOException e) {
			logger.error("ERROR: " + e);
		}
	}

	private void deleteFile(File element) {
		logger.debug("Deleting SystemCrash");
		if (element.isDirectory()) {
			for (File sub : element.listFiles()) {
				deleteFile(sub);
			}
		}
		element.delete();
	}

}
