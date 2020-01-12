package com.snap.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class FileCompressor {
	private static final Logger logger = Logger.getLogger(FileCompressor.class.getName());
	List<String> fileList;
	// private static String
	// OUTPUT_ZIP_FILE="C:\\Scripts\\Snap-Session\\test5\\test5.zip";
	// private static String SOURCE_FOLDER ="C:\\Scripts\\Snap-Session\\test5";
	private static String OUTPUT_ZIP_FILE = null;
	private static String SOURCE_FOLDER = null;

	FileCompressor() {
		logger.debug("inside Constructor[FileCompressor]");
		fileList = new ArrayList<String>();
	}

	public static void makeZipFile(String sourceDir, String outputZipFile) {
		logger.debug("inside makeZipFile");
		OUTPUT_ZIP_FILE = outputZipFile;
		SOURCE_FOLDER = sourceDir;

		FileCompressor appZip = new FileCompressor();
		appZip.generateFileList(new File(SOURCE_FOLDER));
		appZip.zipIt(OUTPUT_ZIP_FILE);
	}

	/**
	 * Zip it
	 * 
	 * @param zipFile
	 *            output ZIP file location
	 */
	public void zipIt(String zipFile) {
		logger.debug("inside zipIt");
		byte[] buffer = new byte[1024];
		FileOutputStream fos=null;
		ZipOutputStream zos=null;
		FileInputStream in=null;
		try {
			 fos = new FileOutputStream(zipFile);
			 zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);

			for (String file : this.fileList) {

				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);

				 in = new FileInputStream(SOURCE_FOLDER + File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
			}
			zos.closeEntry();
			zos.close();
			logger.debug("Done Zipping");
		} catch (IOException ex) {
			logger.error("Exception occured..", ex);
		}
	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	public void generateFileList(File node) {
		logger.debug("inside generateFileList");
		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}

	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file
	 *            file path
	 * @return Formatted file path
	 */
	private String generateZipEntry(String file) {
		logger.debug("inside generateZipEntry");
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}

	public static void main(String[] args) {
		FileCompressor appZip = new FileCompressor();
		appZip.generateFileList(new File(SOURCE_FOLDER));
		appZip.zipIt(OUTPUT_ZIP_FILE);
	}
}
