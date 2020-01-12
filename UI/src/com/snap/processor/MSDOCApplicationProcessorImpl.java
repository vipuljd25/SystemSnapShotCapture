package com.snap.processor;

import static com.snap.common.util.SnapConstants.MSDOC_URLS_FILE_PATH;
import static com.snap.common.util.SnapConstants.MSDOC_URL_RECOVERY_VBS;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.snap.common.util.ReadFileUtility;
import com.snap.common.util.WriteFileUtility;

public class MSDOCApplicationProcessorImpl implements ApplicationProcessor{
	private static final Logger logger = Logger.getLogger(MSDOCApplicationProcessorImpl.class.getName());

	public void applicationProcessor(String path) {
		logger.debug(" Inside applicationProcessor Executing VB");
		WriteFileUtility fileUtility = new WriteFileUtility();
		String folderurlsFilePath = path + "\\" + MSDOC_URLS_FILE_PATH;
		String msdocAddressVBS="\r\nDim objFSO\r\nDim objExportFile\r\nDim objItem\r\nDim quote\r\nDim path\r\nDim name\r\n\r\nSet objFSO = CreateObject(\"Scripting.FileSystemObject\")\r\nConst ForWriting = 2\r\n\r\nOn Error Resume Next\r\n\r\nSet objWord = GetObject( , \"Word.Application\")\r\n\r\nOn Error GoTo 0\r\nIf Not TypeName(objWord) = \"Empty\" Then\r\n\r\nSet objExportFile = objFSO.OpenTextFile("+ "\"" + folderurlsFilePath + "\""+ ", ForWriting, True, OpenAsUnicode)\r\n\r\nFor Each objWorkbook In objWord.Documents\r\n\r\nquote = \"\"\"\"\r\npath = quote & objWorkbook.path & quote \r\nname = quote & objWorkbook.name & quote\r\n\r\nobjExportFile.WriteLine path  & \"\\\" & name\r\n\r\nNext\r\nEnd If";
		fileUtility.writeFileIntoTxt(path + "\\" + MSDOC_URL_RECOVERY_VBS, msdocAddressVBS);
		final String[] commands = { "cmd", "/c", path + "\\" + MSDOC_URL_RECOVERY_VBS };
		CommandLineProcessor lineProcessor = new CommandLineProcessor();
		lineProcessor.invokeCMD(commands);
		
	}

	public void applicationInvoke(String path) {
		logger.debug(" Inside applicationInvoke Executing VB");

		File theDir = new File(path);
		if (theDir.exists()) {
			ReadFileUtility fileUtility = new ReadFileUtility();
			List<String> list = fileUtility.readFile(path);
			if (list != null && list.size() > 0) {
				CommandLineProcessor cmd = new CommandLineProcessor();
				cmd.executeApplication(list);
			} else {
				logger.debug("no ie urls to be executed...");
			}
		} else {
			logger.debug("File Does Not Exists");
		}
	}
	
	public static void main(String[] args) {
		MSDOCApplicationProcessorImpl applicationProcessorImpl= new MSDOCApplicationProcessorImpl();
		applicationProcessorImpl.applicationInvoke("C:\\Scripts\\Snap-Session\\sample214\\MSDOC_URLS_FILE.txt");
	}

}
