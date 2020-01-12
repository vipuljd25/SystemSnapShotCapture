package com.snap.service;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import static com.snap.common.util.SnapConstants.*;
import com.snap.common.util.ReadFileUtility;
import com.snap.common.util.ScreenSaver;
import com.snap.common.util.MinimizeAll;
import com.snap.common.util.WriteFileUtility;
import com.snap.processor.ApplicationProcessor;
import com.snap.processor.CommandLineProcessor;
import com.snap.processor.IE_FolderPathProcessor;
import com.snap.processor.MSDOCApplicationProcessorImpl;
import com.snap.processor.MSExcelApplicationProcessorImpl;
import com.snap.processor.MSPowerPointApplicationProcessorImpl;
import com.snap.processor.MSProjectApplicationProcessorImpl;
import com.snap.processor.MSPublisherApplicationProcessorImpl;
import com.snap.processor.MSVisioApplicationProcessorImpl;
import com.snap.processor.WindowsAppProcessor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.snap.ui.controller.*;
public class SnapLaunchService {
	private static final Logger logger = Logger.getLogger(SnapLaunchService.class.getName());
	private final String folderExe = "C:\\Windows\\Explorer.EXE";
	final List<String> pathList = new ArrayList<String>();
	ReadFileUtility readFile = new ReadFileUtility();
	CommandLineProcessor cmd = new CommandLineProcessor();
	private static int finalTotal = 0;
	private List<String> exePathList = new ArrayList<String>();
	private List<String> folderAndFilePathList = new ArrayList<String>();
	private String windowPattern = null;
	private WriteFileUtility fileUtility= new WriteFileUtility();
	
	public class Finder extends SimpleFileVisitor<Path> {
		private final Logger logger = Logger.getLogger(Finder.class.getName());

		private int numMatches = 0;
		private final PathMatcher matcher;

		Finder(String pattern) {
			logger.debug("inside Finder Constructor");
			String windowPattern = pattern;
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		}

		// Compares the glob pattern against
		// the file or directory name.
		void find(Path file) {
			Path name = file.getFileName();
			if (name != null && matcher.matches(name)) {
				numMatches++;

				String filepath = file.toString();

				int index = folderAndFilePathList.indexOf(windowPattern);
				String exePath = exePathList.get(index);

				int index1 = filepath.lastIndexOf('\\');
				String string = filepath.substring(index1 + 1, filepath.length());

				if (exePath.equalsIgnoreCase(folderExe)) {
					pathList.add(filepath);
				}
				else if (string.contains(".")) {
					pathList.add(filepath);
				} else {
					pathList.add(exePath);
				}
			}
		}

		void done(String windowPattern) {
			logger.debug("Inside done()");
			logger.debug("Matched: " + numMatches);
			finalTotal = finalTotal + numMatches;

			if (finalTotal == 0) {
				int index = folderAndFilePathList.indexOf(windowPattern);

				String exePath = exePathList.get(index);
				logger.debug(windowPattern + "--index:" + index + "--Path:" + exePath);
				pathList.add(exePath);
			}
		}

		// Invoke the pattern matching
		// method on each file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			find(file);
			return CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
			find(dir);
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			return CONTINUE;
		}
	}

	private void run(List<String> windowNameList, List<String> windowExePathList, String snapShotName) throws IOException {
		for (String windowName : windowNameList) {
			int index = 0;
			index = windowNameList.indexOf(windowName);
			if (windowName.contains(".")) {
				folderAndFilePathList.add(windowName);
				exePathList.add(windowExePathList.get(index));
			} else {
				pathList.add(windowExePathList.get(index));
			}
		}

		File[] paths;
		paths = File.listRoots();

		/* file and exe. search.... */
		for (String list : folderAndFilePathList) {
			this.windowPattern = list;
			Finder finder = null;

			if (list.contains("\\") || list.contains("/") || list.contains(":") || list.contains("*")
					|| list.contains("?") || list.contains("\"") || list.contains("<") || list.contains(">")
					|| list.contains("|")) {
				String blankString = "";
				finder = new Finder(blankString);
				finder.done(list);
			} else {
				finder = new Finder(list);
				for (File path : paths) {
					String str = path.toString();
					String slash = "\\";

					String s = new StringBuilder(str).append(slash).toString();

					Path startingDir = Paths.get(s);
					Files.walkFileTree(startingDir, finder);
				}
				finder.done(list);
			}
		}
		invokeCMD(snapShotName);
	}

	private void invokeCMD(String snapShotName) {
		List<String> allPathList = new ArrayList<String>();
		// pathList.addAll(exePathList);
		// invokeMozila(pathList);
		for (String m : pathList) {
			int index1 = m.lastIndexOf('\\');
			String string = m.substring(index1 + 1, m.length());
			if (string.contains("firefox.exe")) {
				logger.debug("firefox.exe excluded from exe list..");
			} else {
				// String o = "\"\"" + " " + "\"" + m + "\"";
				String o = "\"" + m + "\"";
				allPathList.add(o);
			}
		}
		if(allPathList.size()>0)
		fileUtility.writeFile(snapShotName + "\\"  + TO_BE_CLOSED_LIST, allPathList,false);
		
		cmd.run(allPathList);

	}

	/*private void invokeIEAndFolderurls(String path) {
		File theDir = new File(path);
		if (theDir.exists()) {
			ReadFileUtility readFile = new ReadFileUtility();
			List<String> filePathList = readFile.readFile(path);
			// CommandLineProcessor cmd = new CommandLineProcessor();
			// cmd.executeIEurls(filePathList,path);

		} else {
			logger.debug("no invokeIEAndFolderurls files..");
		}
	}*/

	private void invokeNotePadrurls(String path) {
		logger.debug("inside invokeNotePadrurls()");
		File theDir = new File(path);

		// if the directory does not exist, create it
		if (theDir.exists()) {
			ReadFileUtility readFile = new ReadFileUtility();
			List<String> filePathList = readFile.readFile(path);
			CommandLineProcessor cmd = new CommandLineProcessor();
			cmd.executeNotePadurls(filePathList);
		} else {
			logger.debug("no notepad files..");
		}
	}

	private void invokeWindowsApp(String path) {
		logger.debug("inside invokeWindowsApp()");
		File theDir = new File(path);
		if (theDir.exists()) {
			WindowsAppProcessor appProcessor = new WindowsAppProcessor();
			appProcessor.applicationInvoke(path);
		} else {
			logger.debug("no WindowsApp found..");
		}
	}

	private void invokeMozila(String path) {

		logger.debug("inside invokeMozila()");
		File theDir = new File(path);

		// if the directory does not exist, create it
		if (theDir.exists()) {
			ReadFileUtility readFile = new ReadFileUtility();
			List<String> filePathList = readFile.readFile(path);
			CommandLineProcessor cmd = new CommandLineProcessor();
			cmd.executeMozilaurls(filePathList);
			/* } */
		} else {
			logger.debug("no mozila files..");
		}
	}

	public void snapLaunch(String snapShotName) throws IOException, AWTException {
		// String snapPath = APP_DATA_LOCATION + "\\" + snapShotName;
		//String snapPath = snapShotName;
		//snapPath = snapShotName;
		//ScreenSaver screenSaver = new ScreenSaver();
		List<String> blist= WindowUtil.getWindowTitle();
		List<String> filePathList = readFile.readFile(snapShotName + "\\" + WINDOW_NAME_LIST);
		List<String> exePathList = readFile.readFile(snapShotName + "\\" + WINDOW_NAME_EXE_PATH_LIST);

		logger.debug("Launch STARTED........");
		SnapLaunchService find = new SnapLaunchService();
		find.run(filePathList, exePathList,snapShotName);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		invokeWindowsApp(snapShotName + "\\" + WINDOWS_APP_FILE);
		invokeMozila(snapShotName + "\\" + MOZILLA_URLS_PATH);
		IE_FolderPathProcessor ieUrlInvoke = new IE_FolderPathProcessor();
		ieUrlInvoke.ieUrlInvoke(snapShotName + "\\" + IE_URLS_FILE_PATH);
		ieUrlInvoke.folderUrlInvoke(snapShotName + "\\" + FOLDER_URLS_FILE_PATH);

		invokeNotePadrurls(snapShotName + "\\" + NOTEPAD_PATH_LIST);

		invokeMSApplication(snapShotName);
	
		invokeAddedApplication(snapShotName);
	
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<String> alist= WindowUtil.getWindowTitle();
		
		if(!snapShotName.contains("SystemCrash"))
		fileUtility.writeFile(snapShotName + "\\"  + FILES_TO_BE_CLOSED_LIST, WindowUtil.compareList(blist,alist),false);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//WindowUtil.minimizeAll();
		MinimizeAll.minimize();
		//screenSaver.mainFrameStop();
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/com/snap/ui/fxml/CloseLaunch.fxml").openStream());
			CloseLaunchController closeLaunch = (CloseLaunchController)loader.getController();
			closeLaunch.setName(LAUNCH_DONE);
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("/com/snap/ui/controller/CloseLanchPopup.css").toExternalForm());
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.getIcons().add(new Image("app.png"));
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.setScene(scene);
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();
			 try {
		            Thread.sleep(SLEEP_TIME_FOR_POPUP);
		         } catch (Exception e) {
		            logger.error("Exception"+e);
		         }
			 primaryStage.close();
		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
	}
	
	private void invokeAddedApplication(String snapPath){
		File docDir = new File(snapPath + "\\" + ADDED_WINDOW_NAME);
		if (docDir.exists()) {
			List<String> filePathList = readFile.readFile(snapPath + "\\" + ADDED_WINDOW_NAME);
			cmd.run(filePathList);
		}
	}

	private void invokeMSApplication(String snapPath) {
		logger.debug("inside invokeMSApplication()");
		ApplicationProcessor processor;

		File docDir = new File(snapPath + "\\" + MSDOC_URLS_FILE_PATH);
		if (docDir.exists()) {
			processor = new MSDOCApplicationProcessorImpl();
			if (processor instanceof MSDOCApplicationProcessorImpl)
				processor.applicationInvoke(snapPath + "\\" + MSDOC_URLS_FILE_PATH);
		}

		File excelDir = new File(snapPath + "\\" + MSEXCEL_URLS_FILE_PATH);
		if (excelDir.exists()) {
			processor = new MSExcelApplicationProcessorImpl();
			if (processor instanceof MSExcelApplicationProcessorImpl)
				processor.applicationInvoke(snapPath + "\\" + MSEXCEL_URLS_FILE_PATH);
		}

		File pptDir = new File(snapPath + "\\" + MSPPT_URLS_FILE_PATH);
		if (pptDir.exists()) {
			processor = new MSPowerPointApplicationProcessorImpl();
			if (processor instanceof MSPowerPointApplicationProcessorImpl)
				processor.applicationInvoke(snapPath + "\\" + MSPPT_URLS_FILE_PATH);
		}

		File projectDir = new File(snapPath + "\\" + MSPROJECT_URLS_FILE_PATH);
		if (projectDir.exists()) {
			processor = new MSProjectApplicationProcessorImpl();
			if (processor instanceof MSProjectApplicationProcessorImpl)
				processor.applicationInvoke(snapPath + "\\" + MSPROJECT_URLS_FILE_PATH);
		}

		File publishDir = new File(snapPath + "\\" + MSPUBLISHER_URLS_FILE_PATH);
		if (publishDir.exists()) {
			processor = new MSPublisherApplicationProcessorImpl();
			if (processor instanceof MSPublisherApplicationProcessorImpl)
				processor.applicationInvoke(snapPath + "\\" + MSPUBLISHER_URLS_FILE_PATH);
		}

		File visiohDir = new File(snapPath + "\\" + MSVISIO_URLS_FILE_PATH);
		if (visiohDir.exists()) {
			processor = new MSVisioApplicationProcessorImpl();
			if (processor instanceof MSVisioApplicationProcessorImpl)
				processor.applicationInvoke(snapPath + "\\" + MSVISIO_URLS_FILE_PATH);
		}
	}

	public static void main(String[] args) throws IOException {
		/*
		 * ReadFileUtility readFile = new ReadFileUtility(); List<String>
		 * filePathList = readFile.readFile(WINDOW_NAME_LIST_LOCATION);
		 * List<String> exePathList = readFile.readFile(EXE_PATH_LIST_LOCATION);
		 * 
		 * LogsPrototype.CMD_CLASS.debug("STARTED........"); SnapLaunchService
		 * find = new SnapLaunchService(); find.run(filePathList, exePathList);
		 * find.invokeIEAndFolderurls();
		 */

		SnapLaunchService find = new SnapLaunchService();
		//find.snapLaunch("C:\\Scripts\\Snap-Session\\webapp4");
	}
}
