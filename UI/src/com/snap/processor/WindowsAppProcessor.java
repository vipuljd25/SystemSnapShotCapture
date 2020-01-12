package com.snap.processor;

import static com.snap.common.util.SnapConstants.WINDOWS_APP_FILE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.snap.common.infra.SingletonAppData;
import com.snap.common.util.ReadFileUtility;
import com.snap.common.util.WriteFileUtility;

public class WindowsAppProcessor{
	private static final Logger logger = Logger.getLogger(WindowsAppProcessor.class.getName());
	
	List<String> list = new ArrayList<String>();
	CommandLineProcessor lineProcessor = new CommandLineProcessor();
	HashMap<String, String> map=null;
	ReadFileUtility readFileUtility = new ReadFileUtility();
	SingletonAppData singleton = SingletonAppData.getInstance();
	
	public void applicationProcessor(String snapPath, List<String> windowsAppNameList) {
		logger.debug("inside applicationProcessor");
		WriteFileUtility writeFile = new WriteFileUtility();
		writeFile.writeFile(snapPath + "\\" + WINDOWS_APP_FILE, windowsAppNameList,false);
	}

	public void applicationInvoke(String path) {
		logger.debug("inside applicationInvoke");
		
		map=(HashMap<String, String>) singleton.getMap();
		if(null==map)
		{
			logger.debug("Map Not NUll");
			lineProcessor.executeApplication();
		}
		map=(HashMap<String, String>) singleton.getMap();
		list = readFileUtility.readFile(path);
		for(String windowApp : list)
		{
			String appName=map.get(windowApp);
			if(null!=appName){
			lineProcessor.invkokeWindowsApp(appName);
			logger.debug(appName);
			}
		}
	}
	
	public void WindowApplicationInvoke(String appName) {
		logger.debug("inside WindowApplicationInvoke()");
		map=(HashMap<String, String>) singleton.getMap();
		if(null==map)
		{
			lineProcessor.executeApplication();
		}
		map=(HashMap<String, String>) singleton.getMap();
		if(null!=map.get(appName)){
			lineProcessor.invkokeWindowsApp(map.get(appName).toString());
			logger.debug(appName+" window app launched..");
		}
	}
}
