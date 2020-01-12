package com.snap.common.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ReadFileUtility {
	private static final Logger logger = Logger.getLogger(ReadFileUtility.class.getName());

	public List<String> readFile(String filePath) {
		logger.debug("inside readFile(");
		String line = null;

		try {
			FileReader fileReader = new FileReader(filePath);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<String> list = new ArrayList<String>();

			while ((line = bufferedReader.readLine()) != null) {
				
				list.add(line);
			}
			  for (String string : list){
				  logger.debug(filePath +":: "+string);
				  
			  }
			
			bufferedReader.close();
			return list;
		}
		catch (FileNotFoundException ex) {
			logger.error("Unable to open file '" + filePath + "'"+ ex);
			return null;
		} catch (IOException ex) {
			logger.error("Error reading file '" + filePath + "'"+ ex);
			return null;
		}
	}
}
