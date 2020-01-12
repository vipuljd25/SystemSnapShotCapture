package com.snap.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.snap.common.util.SnapConstants.*;
import org.apache.log4j.Logger;

public class WriteCSVFileUtility {
	private static final Logger logger = Logger.getLogger(WriteCSVFileUtility.class.getName());

	public void write(String name, String detail) {

		logger.debug("Inside write()");
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		File theFile = new File(APP_DATA_LOCATION + "\\" + SNAPSHOT_WINDOW_LIST);

		try {
			// if the File does not exist, create it
			if (!theFile.exists()) {
				logger.debug("New File Created");

				// FileWriter pw = new
				// FileWriter(APP_DATA_LOCATION+"\\"+SNAPSHOT_WINDOW_LIST);
				FileWriter pw = new FileWriter(theFile);

				pw.append(name);
				/*pw.append(",");
				pw.append(detail);
				pw.append(",");
				pw.append(df.format(dateobj));*/
				pw.append("\n");
				pw.flush();
				pw.close();

				logger.debug("Writen Succesful");
				logger.info(
						"SnapShot Detail:  Name:" + name + ",Detail:" + detail + ",CreatedDate:" + df.format(dateobj));
				logger.debug("Writen Succesful");

			} else {
				logger.debug("file Already Exists");

				// FileWriter pw = new
				// FileWriter(APP_DATA_LOCATION+"\\"+SNAPSHOT_WINDOW_LIST,true);//True
				// For Appending Data To The File
				FileWriter pw = new FileWriter(theFile, true);// True For
																// Appending
																// Data To The
																// File

				pw.append(name);
				/*pw.append(",");
				pw.append(detail);
				pw.append(",");
				pw.append(df.format(dateobj));*/
				pw.append("\n");
				pw.flush();
				pw.close();

				logger.info(
						"SnapShot Detail:  Name:" + name + ",Detail:" + detail + ",CreatedDate:" + df.format(dateobj));
				logger.debug("Writen Succesful");
			}
		} catch (IOException e) {
			logger.error("Error occured.. " + e);
		}

		writeFileIntoWindowListTxt(name, detail);
		logger.debug(" write() End..");
	}

	private void writeFileIntoWindowListTxt(String name, String detail) {
		logger.debug("Inside writeFileIntoWindowListTxt()");
		String path = APP_DATA_LOCATION + "\\" + name;
		WriteFileUtility.mkDir(path);
		File file = new File(APP_DATA_LOCATION + "\\" + name + "\\" + SNAPSHOT_DETAIL_TXT);
		try {
			FileWriter fw = new FileWriter(file);
			fw.append(name);
			/*fw.append(",");
			fw.append(detail);
			fw.append(",");
			fw.append(df.format(dateobj));*/
			fw.append("\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			logger.error("Error occured.. " + e);
		}
		logger.debug("writeFileIntoWindowListTxt() Done..");
	}
}
