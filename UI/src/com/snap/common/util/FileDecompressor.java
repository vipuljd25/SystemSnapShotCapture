package com.snap.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

public class FileDecompressor {
	private static final Logger logger = Logger.getLogger(FileDecompressor.class.getName());
	List<String> fileList;
	private static final String INPUT_ZIP_FILE = "C:\\Scripts\\Snap-Session\\mozila.zip";
	private static final String OUTPUT_FOLDER = "C:\\Scripts\\Snap-Session\\outputzip";

	public static Boolean unZipFile(String inputZipFile, String outputFolder) {
		logger.debug("inside unZipFile");
		FileDecompressor unZip = new FileDecompressor();
		return unZip.unZipIt(inputZipFile, outputFolder);
	}

	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param output
	 *            zip file output folder
	 */
	public Boolean unZipIt(String zipFile, String outputFolder) {
		logger.debug("inside unZipIt");
		byte[] buffer = new byte[1024];
		ZipInputStream zis = null;
		FileOutputStream fos = null;

		try {
			// get the zip file content
			zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			if (ze != null) {
				// create output directory is not exists
				File folder = new File(outputFolder);
				if (!folder.exists()) {
					folder.mkdir();
				}
				while (ze != null) {

					String fileName = ze.getName();
					File newFile = new File(outputFolder + File.separator + fileName);

					System.out.println("file unzip : " + newFile.getAbsoluteFile());

					// create all non exists folders
					// else you will hit FileNotFoundException for compressed
					// folder
					new File(newFile.getParent()).mkdirs();

					fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
					ze = zis.getNextEntry();
				}
			} else {
				// file format error
				zis.closeEntry();
				zis.close();
				return false;
			}
			zis.closeEntry();
			zis.close();

			System.out.println("Done");
			return true;

		} catch (IOException ex) {
			logger.error("Exception occured..", ex);
			return false;
		}
	}

	public static void main(String[] args) {
		FileDecompressor unZip = new FileDecompressor();
		unZip.unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
	}

}
