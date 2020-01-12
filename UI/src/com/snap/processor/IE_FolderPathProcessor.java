package com.snap.processor;

import static com.snap.common.util.SnapConstants.FOLDER_URLS_FILE_PATH;
import static com.snap.common.util.SnapConstants.FOLDER_URL_RECOVERY_VBS;
import static com.snap.common.util.SnapConstants.IE_URLS_FILE_PATH;
import static com.snap.common.util.SnapConstants.IE_URL_RECOVERY_VBS;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.snap.common.util.ReadFileUtility;
import com.snap.common.util.WriteFileUtility;

public class IE_FolderPathProcessor {
	private static final Logger logger = Logger.getLogger(IE_FolderPathProcessor.class.getName());

	public void ieUrlInvoke(String path) {
		logger.debug("inside ieUrlInvoke()");
		File theDir = new File(path);
		if (theDir.exists()) {
			ReadFileUtility fileUtility = new ReadFileUtility();
			List<String> list = fileUtility.readFile(path);
			if (list != null && list.size() > 0) {
				CommandLineProcessor cmd = new CommandLineProcessor();
				cmd.executeIEurls(list, path);
			} else {
				// no ie urls to be executed...
			}
		} else {
			// file does not exists..
		}
	}

	public void folderUrlInvoke(String path) {
		logger.debug("inside folderUrlInvoke()");
		File theDir = new File(path);
		if (theDir.exists()) {
			List<String> list = readFilePaths(path);
			if (list != null && list.size() > 0) {
				CommandLineProcessor lineProcessor = new CommandLineProcessor();
				lineProcessor.executeFolderurls(list);
			} else {
				// no folder urls to be executed...
			}
		} else {
			// file does not exists..
		}
	}

	public void ieUrlProcessor(String path) {
		logger.debug("inside ieUrlProcessor()");
		WriteFileUtility fileUtility = new WriteFileUtility();
		String folderurlsFilePath = path + "\\" + IE_URLS_FILE_PATH;
		String ieTabAddressVBS = "Dim objShell\r\nDim objShellWindows\r\nDim objFSO\r\nDim objItem\r\nDim objFile\r\nDim objFileName\r\nDim LocationURL\r\nDim objExportFile\r\nDim currentDirectory\r\n\r\nSet objShell = CreateObject(\"Shell.Application\")\r\n\r\nSet objShellWindows = objShell.Windows()\r\nSet objFSO = CreateObject(\"Scripting.FileSystemObject\")\r\nConst ForWriting = 2\r\n\r\ncurrentDirectory = Left(WScript.ScriptFullName,(Len(WScript.ScriptFullName))-(len(WScript.ScriptName)))\r\n\r\nIf objShellWindows.Count = 0  Then\r\n\t'WScript.Echo \"Can not find any opend tabs in Internet Explorer.\"\r\nElse\r\n\tSet objExportFile = objFSO.OpenTextFile("
				+ "\"" + folderurlsFilePath + "\""
				+ ", ForWriting, True, OpenAsUnicode)\r\n\t\r\n\tFor Each objItem In objShellWindows\r\n\t\tFileFullName = objItem.FullName\r\n\t\tobjFile = objFSO.GetFile(objItem.FullName)\r\n\t\tobjFileName = objFSO.GetFileName(objFile)\r\n\t\t\r\n\t\tIf LCase(objFileName) = \"iexplore.exe\" Then\r\n\t\t\tLocationURL = objItem.LocationURL\r\n\t\t\tobjExportFile.WriteLine LocationURL \t\r\n\t\t\tEnd If\r\n\tNext\r\nEnd If";
		fileUtility.writeFileIntoTxt(path + "\\" + IE_URL_RECOVERY_VBS, ieTabAddressVBS);
		final String[] commands = { "cmd", "/c", path + "\\" + IE_URL_RECOVERY_VBS };
		CommandLineProcessor lineProcessor = new CommandLineProcessor();
		lineProcessor.invokeCMD(commands);
	}

	public void folderUrlProcessor(String path) {
		logger.debug("inside folderUrlProcessor()");
		WriteFileUtility fileUtility = new WriteFileUtility();
		String folderurlsFilePath = path + "\\" + FOLDER_URLS_FILE_PATH;
		String folderAddressVBS = "Dim objShell\r\nDim objShellWindows\r\nDim objFSO\r\nDim objItem\r\nDim objFile\r\nDim objFileName\r\nDim LocationURL\r\nDim objExportFileURL\r\nDim currentDirectory\r\n\r\n\r\nSet objShell = CreateObject(\"Shell.Application\")\r\n\r\nSet objShellWindows = objShell.Windows()\r\n\r\nSet objFSO = CreateObject(\"Scripting.FileSystemObject\")\r\nConst ForWriting = 2\r\n\r\ncurrentDirectory = Left(WScript.ScriptFullName,(Len(WScript.ScriptFullName))-(len(WScript.ScriptName)))\r\n\r\nIf objShellWindows.Count = 0  Then\r\n\t'WScript.Echo \"Can not find any opend tabs in Internet Explorer.\"\r\nElse\r\n\r\n\tSet objExportFileURL = objFSO.OpenTextFile("
				+ "\"" + folderurlsFilePath + "\""
				+ ", ForWriting, True, OpenAsUnicode)\r\n\t\r\n\tFor Each objItem In objShellWindows\r\n\t\tFileFullName = objItem.FullName\r\n\t\tobjFile = objFSO.GetFile(objItem.FullName)\r\n\t\tobjFileName = objFSO.GetFileName(objFile)\r\n\t\t\r\n\t\tIf LCase(objFileName) = \"explorer.exe\" Then\r\n\t\t\tLocationURL = objItem.LocationURL\r\n\t\t\tobjExportFileURL.WriteLine LocationURL \r\n\t\tEnd If\r\n\tNext\r\nEnd If";

		fileUtility.writeFileIntoTxt(path + "\\" + FOLDER_URL_RECOVERY_VBS, folderAddressVBS);
		final String[] commands = { "cmd", "/c", path + "\\" + FOLDER_URL_RECOVERY_VBS };
		CommandLineProcessor lineProcessor = new CommandLineProcessor();
		lineProcessor.invokeCMD(commands);
	}

	public static void main(String[] args) {
		IE_FolderPathProcessor folderPathProcessor = new IE_FolderPathProcessor();
		 folderPathProcessor.folderUrlInvoke("C:\\Scripts 1\\test1\\FOLDER_URLS_FILE.txt");
		// folderPathProcessor.ieUrlInvoke("C:\\Scripts 1\\test1\\IE_URLS_FILE.txt");
	//	folderPathProcessor.folderUrlProcessor("C:\\Scripts 1\\test1");
	//	folderPathProcessor.ieUrlProcessor("C:\\Scripts 1\\test1");
	}

	private List<String> readFilePaths(String path) {
		String line = null;
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<String> list = new ArrayList<String>();
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("%20")) {
					list.add(line.replace("%20", " "));
				} else {
					list.add(line);
				}
			}
			bufferedReader.close();
			return list;
		} catch (FileNotFoundException ex) {
			// logger.error("Unable to open file '" + filePath + "'"+ ex);
			return null;
		} catch (IOException ex) {
			// logger.error("Error reading file '" + filePath + "'"+ ex);
			return null;
		}
	}
}