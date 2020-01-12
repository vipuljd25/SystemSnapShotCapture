package com.snap.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;

public class WriteFileUtility {

	private static final Logger logger = Logger.getLogger(WriteFileUtility.class.getName());

	public void writeFile(String filePath, Collection<String> list,boolean appendOption) {
		try {
		
			FileWriter fileWriter = new FileWriter(filePath,appendOption);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (String string : list) {
				if(string!=null){
				bufferedWriter.append(string.trim());
				bufferedWriter.newLine();
				}
			}
			bufferedWriter.close();
		} catch (IOException ex) {
			logger.error("Error writing to file '" + filePath + "'", ex);
		}
	}
	
	public static void mkDir(String filePath) {
		logger.debug("Inside mkDir()");
		File theDir = new File(filePath);

		if (!theDir.exists()) {
			logger.debug("creating directory: " + filePath);
			boolean result = false;

			try {
				theDir.mkdir();
				result = true;
			} catch (SecurityException e) {
				logger.error("Error Occured", e);
			}
			if (result) {
				logger.debug("DIR created");
			}
		}
	}

	public void writeFileIntoTxt(String filePath, String fileContain) {
		BufferedWriter bufferedWriter = null;
		try {

			FileWriter fileWriter = new FileWriter(filePath);
			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(fileContain);
			
			bufferedWriter.flush();
			bufferedWriter.close();

		} catch (IOException e) {
			logger.error("Error Occured", e);
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (IOException e) {
				logger.error("Error Occured", e);
			}
		}
	}
	
/*	public static void writeExistingFile(String file, List<String> list) throws IOException {
		BufferedWriter bufferedWriter =null;
		try {

			File inFile = new File(file);
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
			FileWriter fileWriter = new FileWriter(tempFile);
			 bufferedWriter = new BufferedWriter(fileWriter);
			for (String string : list) {
				if(string!=null){
				bufferedWriter.append(string.trim());
				bufferedWriter.newLine();
				}
			}
				
				bufferedWriter.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
			

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			bufferedWriter.close();

		}
	}*/

	public static void main(String args[]) throws IOException {
		// copyFile("C:\\Scripts\\Snap-Session\\IE_FOLDERS_PATHS.txt","C:\\Scripts\\Snap-Session\\new\\IE_FOLDERS_PATHS.txt");
	}
}