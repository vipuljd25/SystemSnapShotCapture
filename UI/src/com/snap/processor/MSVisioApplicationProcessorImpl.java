package com.snap.processor;

import static com.snap.common.util.SnapConstants.MSVISIO_URLS_FILE_PATH;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.snap.common.util.ReadFileUtility;

import static com.snap.common.util.SnapConstants.MSVISIO_RECOVERY_VBS;

import com.snap.common.util.WriteFileUtility;

public class MSVisioApplicationProcessorImpl implements ApplicationProcessor{
	private static final Logger logger = Logger.getLogger(MSVisioApplicationProcessorImpl.class.getName());

	public void applicationProcessor(String path) {
		logger.debug("inside applicationProcessor Executing vbs");
		WriteFileUtility fileUtility = new WriteFileUtility();
		String folderurlsFilePath = path + "\\" + MSVISIO_URLS_FILE_PATH;
		String visioAddressVBS= "\r\nDim objFSO\r\nDim objExportFile\r\nDim objItem\r\nDim name\r\nDim path\r\nDim concat\r\nDim quote\r\n\r\nSet objFSO = CreateObject(\"Scripting.FileSystemObject\")\r\nConst ForWriting = 2\r\n\r\nOn Error Resume Next\r\n\r\nSet objWord = GetObject( , \"Visio.Application\")\r\n\r\nOn Error GoTo 0\r\nIf Not TypeName(objword) = \"Empty\" Then\r\n\r\nSet objExportFile = objFSO.OpenTextFile("+ "\"" + folderurlsFilePath + "\""+ ", ForWriting, True, OpenAsUnicode)\r\nFor Each objWorkbook In objWord.Documents\r\n\r\nquote = \"\"\"\"\r\npath = quote & objWorkbook.path & quote \r\nname = quote & objWorkbook.name & quote\r\n\r\nobjExportFile.WriteLine path  & \"\\\" & name\r\n\r\n\r\nNext\r\n\r\nEnd If";
		fileUtility.writeFileIntoTxt(path + "\\" + MSVISIO_RECOVERY_VBS, visioAddressVBS);
		final String[] commands = { "cmd", "/c", path + "\\" + MSVISIO_RECOVERY_VBS };
		CommandLineProcessor lineProcessor = new CommandLineProcessor();
		lineProcessor.invokeCMD(commands);
	}

	public void applicationInvoke(String path) {
		logger.debug("Inside applicationInvoke ");
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

}
