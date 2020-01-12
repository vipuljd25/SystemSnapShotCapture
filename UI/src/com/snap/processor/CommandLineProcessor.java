package com.snap.processor;

import static com.snap.common.util.SnapConstants.IE_URL_RECOVERY_JS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.snap.common.infra.SingletonAppData;
import com.snap.common.util.WriteFileUtility;
public class CommandLineProcessor {
	SingletonAppData s = SingletonAppData.getInstance();
	// static CommandLineProcessor a1 = new CommandLineProcessor();
	private static final Logger logger = Logger.getLogger(CommandLineProcessor.class.getName());
	final String[] commands = { "cmd", "/c", "echo %AppData%" };

	public void run(List<String> newList) {
		logger.debug("inside run()");
		if (newList != null) {
			for (String w : newList) {
				if(w.length()>2){
				String cmds[] = { "cmd", "/c", "start "+" \"\" "+ w };
				//String cmds[] = { "cmd", "/c", " "+ w };
				invokeCMD(cmds);
				}
			}
		} else {
			logger.error("list should not be null");
		}
		logger.debug("run() End.. ");
	}

	/*public void executeIEurls(List<String> newList) {
		logger.debug("inside executeIEurls() ");
		for (String w : newList) {
			String cmds[] = { "cmd", "/c", "start " + "iexplore " + w };
			invokeCMD(cmds);
			logger.info("IE URLS OR FOLDER" + w);
		}
		logger.debug("executeIEurls() End.. ");
	}*/

	public void executeMozilaurls(List<String> newList) {
		logger.debug("inside executeMozilaurls() ");
		String cmds1[] = { "cmd", "/c", "start " + "firefox "};
		invokeCMD(cmds1);
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			logger.error("Exception occured..", e);
		}
		for (String w : newList) {
			String cmds[] = { "cmd", "/c", "start " + "firefox " + w };
			invokeCMD(cmds);
			logger.info("MOZILA URLS" + w);
		}
		logger.debug("executeMozilaurls() End.. ");
	}

	public void showHiddenExtention() {
		final String regEdit = "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Advanced /v HideFileExt /t REG_DWORD /d 0 /f";
		String cmds[] = { "cmd", "/c", regEdit };
		invokeCMD(cmds);
		logger.debug("Show extention enabled...");
	}

	public void invokeCMD(String cmds[]) {
		try {
			Runtime.getRuntime().exec(cmds);
			logger.debug("invoked cmd ");
				Thread.sleep(1000);
		} catch (Exception e1) {
			logger.error("Exception occured..", e1);
		}
	}

	public List<String> invokeCMDToReadCommandLine(String cmds[]) {
		logger.debug("inside invokeCMDToReadCommandLine() ");
		List<String> list = null;
		BufferedReader reader = null;
		try {
			list = new ArrayList<String>();
			Process p = Runtime.getRuntime().exec(cmds);
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
				logger.debug(line);
			}
			return list;
		} catch (Exception e1) {
			logger.error("Exception occured..", e1);
			return null;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				logger.error("Exception occured..", e);
			}
			logger.debug("END invokeCMDToReadCommandLine() ");
		}
	}

	public String getMozilaFilePath() {
		logger.debug("inside getMozilaFilePath() ");
		String filePath = null;
		CommandLineProcessor cmd = new CommandLineProcessor();
		List<String> list = cmd.invokeCMDToReadCommandLine(commands);
		String path = null;
		if (list.size() > 0) {
			path = list.get(0);
		}
		String profile_path = path + "\\Mozilla\\Firefox\\Profiles";

		String[] dirCmd = { "cmd", "/c", "dir " + profile_path };
		List<String> dirList = cmd.invokeCMDToReadCommandLine(dirCmd);
		if (dirList.size() > 0) {
			for (String string : dirList) {
				if (string.contains("default")) {

					int index = string.lastIndexOf(" ");

					filePath = profile_path + "\\" + string.substring(index + 1, string.length());
				}
			}
			logger.debug("Recover.jsMozila File Path  " + filePath);
		}
		return filePath;
	}

	public String getNotePadurl(int pid) {
		logger.debug("inside getNotePadurl() ");
		// final String[] wmicCommand = { "cmd", "/c", "wmic process where
		// "+"ProcessID='"+pid+"' get CommandLine /FORMAT:LIST"};
		final String[] wmicCommand = { "cmd", "/c", "wmic process where " + "ProcessID='" + pid + "' get CommandLine" };
		List<String> list = invokeCMDToReadCommandLine(wmicCommand);
		String path = null;
		if (list.size() > 0) {
			path = list.get(2);
		}
		return path;
	}

	public void executeNotePadurls(List<String> newList) {
		logger.debug("inside executeNotePadurls() ");
		for (String w : newList) {
			if(w.length()>35){
			String cmds[] = { "cmd", "/c",  w };
			invokeCMD(cmds);
			}
		}
		logger.debug("executeNotePadurls() End.. ");
	}
	
	public void executeApplication(List<String> newList) {
		logger.debug("inside executeApplication() ");
		for (String w : newList) {
			if(w.length()>0){
			String cmds[] = { "cmd", "/c ",  w };
			invokeCMD(cmds);
			}
		}
		logger.debug("executeApplication() End.. ");
	}
	
	public void executeFolderurls(List<String> newList) {
		logger.debug("inside executeFolderurls() ");
		for (String w : newList) {
			String cmds[] = { "cmd", "/c", "start explorer.exe " + w };
			invokeCMD(cmds);
		}
		logger.debug("executeFolderUrls() End.. ");
	}

	public void executeIEurls(List<String> urlList, String filePath) {
		logger.debug("Inside executeIEurls().. ");
		
		int index1 = filePath.lastIndexOf('\\');
		String fpath = filePath.substring(0, index1 ); 
		String s = null;
		String js =null;
		String urls =null;
		
		for (String url : urlList) {
		s = s +"\""+ url +"\",";
		 urls = s.substring(4, s.length() - 1);
		}
		 js = "var navOpenInNewWindow = 0x1;\r\nvar navOpenInNewTab = 0x800;\r\nvar navOpenInBackgroundTab = 0x1000;\r\nvar intLoop = 0;\r\nvar intArrUBound = 0;\r\nvar navFlags = "
					+ "navOpenInBackgroundTab;\r\nvar objIE;\r\n\r\n\r\n\r\n\r\nvar url = " + "[" + urls
					+ "];\r\n\r\n\r\n   intArrUBound = url.length;"
					+ "\r\n   \r\n    objIE = new ActiveXObject(\"InternetExplorer.Application\");\r\n    \r\n\r\n   \r\n\r\nfor (var i in url) {\r\n  \r\nobjIE.Navigate2(url[i], navFlags);\r\n}\r\n\r\n  "
					+ "  objIE.Visible = true;\r\n    objIE = null;";
		 
		 WriteFileUtility fileUtility= new WriteFileUtility();
		 fileUtility.writeFileIntoTxt(fpath+"\\"+IE_URL_RECOVERY_JS, js);
		
		final String[] commands = { "cmd", "/c", fpath+"\\"+IE_URL_RECOVERY_JS };
		invokeCMD(commands);
		logger.debug("executeIEurls() End.. ");
	}
	
	public void executeWindowsApplication(List<String> newList) {
		logger.debug("inside executeApplication() ");
		for (String w : newList) {
			if(w.length()>0){
			String cmds[] = { "cmd", "/c",  " "+"C:\\Scripts\\search.vbs "+w };
			invokeCMD(cmds);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error("Exception occured..", e);
			}
			}
		}
		logger.debug("executeApplication() End.. ");
	}

	public void invkokeWindowsApp(String toBeLaunched) {
		logger.debug("inside run()");
		String cmds[] = { "cmd", "/c","start "+ "Shell:AppsFolder\\" + toBeLaunched };
		// String cmds[] = { "cmd", "/c", " "+ w };
		invokeCMD(cmds);
		logger.debug("invkokeWindowsApp() End.. ");
	}

	public void executeApplication() {
		logger.debug("inside executeApplication()");
		List<String> list = new ArrayList<String>();
		String cmds[] = { "cmd.exe", "/c",
				"" + "\" mode 300 && powershell -Command \"Get-StartApps | Format-Table -AutoSize\"" };
		list = invokeCMDToReadCommandLine(cmds);
		HashMap<String,String> map= new HashMap<String,String>();
		for (String line : list) {
			if (!line.isEmpty()) {
				String[] words = new String[2];
				words = line.split("   ", 2);
				map.put(words[0].trim(), words[1].trim());
				// System.out.println(words[0]+""+words[1]);
			}
		}
		s.setMap(map);
	}

	public static void main(String[] args) throws InterruptedException {

		CommandLineProcessor cmd = new CommandLineProcessor();

		// List<String> newList = new ArrayList<String>();
		// newList.add("www.google.com");
		// newList.add("wmic process get ProcessID,Caption,Commandline");
		// String[] commands = { "cmd", "/c", "echo %AppData%" };
		// cmd.executeIEurls(newList);
		// cmd.invokeCMD(commands);
		// LogsPrototype.CMD_CLASS.debug(cmd.getMozilaFilePath());
		// read();
		//cmd.run(null);
		List<String> newList = new ArrayList<String>();
		 newList.add("\""+"Maps"+"\"");
		 newList.add("\""+"Store"+"\"");
		// newList.add("\""+"C:\\Windows\\system32\\NOTEPAD.EXE "+"\"");
		cmd.executeWindowsApplication(newList);
	}
}
