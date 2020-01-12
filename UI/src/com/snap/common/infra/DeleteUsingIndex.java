package com.snap.common.infra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class DeleteUsingIndex {
	private static final Logger logger = Logger.getLogger(DeleteUsingIndex.class.getName());
	public void removeLineFromFile(String file, int indextobedeleted) throws IOException {
		logger.debug("Inside Index to Be Deleted");
		int count = 0; 
		BufferedReader br = null;
		PrintWriter pw = null;
		try {

			File inFile = new File(file);

			if (!inFile.isFile()) {
				logger.debug("Parameter is not an existing file");
				return ;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			br = new BufferedReader(new FileReader(file));
			pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {
				
				
				if (count==indextobedeleted) {
					logger.debug("inside if for continue");
					indextobedeleted = 999999;
					continue;
				}
				count ++;
				pw.println(line);
				pw.flush();
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				logger.debug("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				logger.debug("Could not rename file");

		} catch (FileNotFoundException ex) {
			logger.error("Exception occured"+ex);
		} catch (IOException ex) {
			logger.error("Exception occured"+ex);
		} finally {
			pw.close();
			br.close();

		}
	}
}
