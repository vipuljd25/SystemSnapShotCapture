package com.snap.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MozillaJsonProcessor {
	private static final Logger logger = Logger.getLogger(MozillaJsonProcessor.class.getName());

	public List<String> getMozilaUrls(String path) {
		logger.debug("Inside getMozilaUrls()");
		FileReader reader = null;
		try {
			File theFile = new File(path + "\\sessionstore-backups\\recovery.js");

			// if the File does not exist, create it
			if (theFile.exists()) {

				reader = new FileReader(path + "\\sessionstore-backups\\recovery.js");

				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
				JSONArray windows = (JSONArray) jsonObject.get("windows");
				JSONObject innerObj = (JSONObject) windows.get(0);
				JSONArray tabs = (JSONArray) innerObj.get("tabs");

				List<String> urlList = new ArrayList<String>();
				Iterator i = tabs.iterator();

				while (i.hasNext()) {

					JSONObject innerObj1 = (JSONObject) i.next();
					JSONArray innerObj2 = (JSONArray) innerObj1.get("entries");
					JSONObject innerObj3 = (JSONObject) innerObj2.get(0);
					String url = (String) innerObj3.get("url");
					urlList.add(url);
					logger.debug("Mozila URLS " + url);
				}
				return urlList;
			} else {
				logger.error("recovery.js does not exists..");
				return null;
			}
		} catch (FileNotFoundException ex) {
			logger.error("Exception Occured", ex);
			return null;
		} catch (IOException ex) {
			logger.error("Exception Occured", ex);
			return null;
		} catch (ParseException ex) {
			logger.error("Exception Occured", ex);
			return null;
		} catch (NullPointerException ex) {
			logger.error("Exception Occured", ex);
			return null;
		}catch (Exception ex) {
			logger.error("Exception Occured", ex);
			return null; 
		}
		finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("Exception", e);
				}
		}
	}

	public static void main(String[] args) {
		CommandLineProcessor cmd = new CommandLineProcessor();
		// cmd.getMozilaFilePath();
		MozillaJsonProcessor jsonParse = new MozillaJsonProcessor();
		List<String> list = jsonParse.getMozilaUrls(cmd.getMozilaFilePath());
		for (String string : list) {
			logger.debug(string + "1");
		}
	}
}
