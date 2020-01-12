package com.snap.service;

import static com.snap.common.util.SnapConstants.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
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
import com.snap.processor.MozillaJsonProcessor;
import com.snap.processor.WindowsAppProcessor;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public class SnapShotService {
	private static final String NOTEPAD_EXE = "notepad.exe";
	private static final String IEXPLORE_EXE = "iexplore.exe";
	private static final String EXPLORER_EXE = "explorer.exe";
	private static final String MS_DOC_EXE = "winword.exe";
	private static final String MS_PPT_EXE = "powerpnt.exe";
	private static final String MS_EXCEL_EXE = "excel.exe";
	private static final String MS_PRRJECT_EXE = "winproj.exe";
	private static final String MS_PUBLISHER_EXE = "mspub.exe";
	private static final String MS_VISIO_EXE = "visio.exe";
	private static final String FIREFOX_EXE = "firefox.exe";
	private static final String CHROME_EXE = "chrome.exe";
	private static final Logger logger = Logger.getLogger(SnapShotService.class.getName());
	private static int pidId = 0;
	private static boolean ieFlag = false;
	private static boolean folderFlag = false;
	private static boolean msDocFlag = false;
	private static boolean msExcelFlag = false;
	private static boolean msVisioFlag = false;
	private static boolean msProjectFlag = false;
	private static boolean msPublisherFlag = false;
	private static boolean msPowerPointFlag = false;
	private static boolean windowsAppFlag = false;
	private static String WINDOWSAPPS_FOLDER_PATH = "c:\\program files\\windowsapps";
	private static String WINDOWSAPPS_EXE = "applicationframehost.exe";
	
	
	public static void takeSnapShot(String folderName) throws IOException {
		final List<String> filenameList = new ArrayList<String>();
		logger.debug("inside takeSnapShot()");
		final IE_FolderPathProcessor ie_FolderPathProcessor = new IE_FolderPathProcessor();
		final CommandLineProcessor cmd = new CommandLineProcessor();
		cmd.showHiddenExtention();
		final List<String> windowNameList = new ArrayList<String>();
		final List<String> windowExePathList = new ArrayList<String>();
		final List<Integer> order = new ArrayList<Integer>();
		final List<String> notePadPaths = new ArrayList<String>();
		final List<String> windowsAppNameList = new ArrayList<String>();

		int top = User32.instance.GetTopWindow(0);
		while (top != 0) {
			order.add(top);
			top = User32.instance.GetWindow(top, User32.GW_HWNDNEXT);
		}
		User32.instance.EnumWindows(new WndEnumProc() {

			public boolean callback(HWND hWnd, int lParam) {
				// logger.debug("Inside callback()");
				if (User32.instance.IsWindowVisible(hWnd)) {
					String path = getModuleFilename(hWnd);

					byte[] buffer = new byte[1024];
					User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
					String title = Native.toString(buffer);
					String local_path = path.toLowerCase();

					if( null != title && null != local_path && title.length()>0 && local_path.length()>0){
					if (!title.equals("") && !local_path.contains(EXPLORER_EXE) && !local_path.contains(IEXPLORE_EXE)
							&& !local_path.contains(NOTEPAD_EXE) && !local_path.contains(MS_DOC_EXE)
							&& !local_path.contains(MS_PPT_EXE) && !local_path.contains(MS_EXCEL_EXE)
							&& !local_path.contains(MS_PRRJECT_EXE) && !local_path.contains(MS_PUBLISHER_EXE)
							&& !local_path.contains(MS_VISIO_EXE) && !local_path.contains(FIREFOX_EXE)
							&& !local_path.contains(WINDOWSAPPS_FOLDER_PATH) && !local_path.contains(WINDOWSAPPS_EXE)
							&& !local_path.contains(CHROME_EXE) && !local_path.contains("snap.exe") && !local_path.contains("bin\\javaw.exe") ){
						if (title.contains(".")) {
							String[] k = title.split("\\.(?=[^\\.]+$)");
							String[] l = k[1].split(" ");
							windowNameList.add(k[0] + "." + l[0]);
							windowExePathList.add(path);
						} else {
							windowNameList.add(title);
							windowExePathList.add(path);
						}
						
						// inflList.add(new WindowInfo(hWnd, title, path));
					} else if (local_path.contains(NOTEPAD_EXE)) {
						String notePadUrl = cmd.getNotePadurl(pidId);
						notePadPaths.add(notePadUrl);
					} else if (!ieFlag && local_path.contains(IEXPLORE_EXE)) {
						ieFlag = true;
					} else if (!folderFlag && local_path.contains(EXPLORER_EXE)) {
						folderFlag = true;
					} else if (!msDocFlag && local_path.contains(MS_DOC_EXE)) {
						msDocFlag = true;
					} else if (!msPowerPointFlag && local_path.contains(MS_PPT_EXE)) {
						msPowerPointFlag = true;
					} else if (!msExcelFlag && local_path.contains(MS_EXCEL_EXE)) {
						msExcelFlag = true;
					} else if (!msProjectFlag && local_path.contains(MS_PRRJECT_EXE)) {
						msProjectFlag = true;
					} else if (!msPublisherFlag && local_path.contains(MS_PUBLISHER_EXE)) {
						msPublisherFlag = true;
					} else if (!msVisioFlag && local_path.contains(MS_VISIO_EXE)) {
						msVisioFlag = true;
					}
					else if (local_path.contains(WINDOWSAPPS_EXE)) {
						windowsAppFlag=true;
						//windowsAppNameList.add("\""+title+"\"");
						windowsAppNameList.add(title);
					}
					}
				}
				return true;
			}
		}, 0);

		WriteFileUtility writeFile = new WriteFileUtility();
		ApplicationProcessor processor;
		String snapPath = null;
		if (folderName != null) {
			snapPath = polpulateSnapLocation(folderName);
		} else {
			// folder name must be declared..
			logger.error(" folder name must be declared..");
		}

		if(windowNameList.size()>0 || windowExePathList.size()>0 ){
		writeFile.writeFile(snapPath + "\\" + WINDOW_NAME_LIST, windowNameList,false);
		writeFile.writeFile(snapPath + "\\" + WINDOW_NAME_EXE_PATH_LIST, windowExePathList,false);
		filenameList.add(WINDOW_NAME_LIST);
		}
		if (ieFlag) {
			ie_FolderPathProcessor.ieUrlProcessor(snapPath);
			filenameList.add(IE_URLS_FILE_PATH);
			logger.debug("ie urls found...");
		}
		if (folderFlag) {
			ie_FolderPathProcessor.folderUrlProcessor(snapPath);
			filenameList.add(FOLDER_URLS_FILE_PATH);
			logger.debug("folder urls found...");
		}
		if (msDocFlag) {
			processor = new MSDOCApplicationProcessorImpl();
			processor.applicationProcessor(snapPath);
			filenameList.add(MSDOC_URLS_FILE_PATH);
			logger.debug("MSDOC urls found...");
		}
		if (msPowerPointFlag) {
			processor = new MSPowerPointApplicationProcessorImpl();
			processor.applicationProcessor(snapPath);
			filenameList.add(MSPPT_URLS_FILE_PATH);
			logger.debug("MSPPT urls found...");
		}
		if (msExcelFlag) {
			processor = new MSExcelApplicationProcessorImpl();
			processor.applicationProcessor(snapPath);
			filenameList.add(MSEXCEL_URLS_FILE_PATH);
			logger.debug("MSEXCEL urls found...");
		}
		if (msProjectFlag) {
			processor = new MSProjectApplicationProcessorImpl();
			processor.applicationProcessor(snapPath);
			filenameList.add(MSPROJECT_URLS_FILE_PATH);
			logger.debug("MSPROJECT urls found...");
		}
		if (msPublisherFlag) {
			processor = new MSPublisherApplicationProcessorImpl();
			processor.applicationProcessor(snapPath);
			filenameList.add(MSPUBLISHER_URLS_FILE_PATH);
			logger.debug("MSPUBLISHER urls found...");
		}
		if (msVisioFlag) {
			processor = new MSVisioApplicationProcessorImpl();
			processor.applicationProcessor(snapPath);
			filenameList.add(MSVISIO_URLS_FILE_PATH);
			logger.debug("MSVISIO urls found...");
		}
		if (windowsAppFlag) {
			WindowsAppProcessor appProcessor = new WindowsAppProcessor();
			appProcessor.applicationProcessor(snapPath,windowsAppNameList);
			filenameList.add(WINDOWS_APP_FILE);
			logger.debug("windowsApps found...");
		}

		MozillaJsonProcessor jsonParse = new MozillaJsonProcessor();
		List<String> mozilaUrlsList = jsonParse.getMozilaUrls(cmd.getMozilaFilePath());
		if (mozilaUrlsList != null && mozilaUrlsList.size() > 0){
			writeFile.writeFile(snapPath + "\\" + MOZILLA_URLS_PATH, mozilaUrlsList,false);
			filenameList.add(MOZILLA_URLS_PATH);	
		}
		if (notePadPaths.size() > 0) {
			writeFile.writeFile(snapPath + "\\" + NOTEPAD_PATH_LIST, notePadPaths,false);
			filenameList.add(NOTEPAD_PATH_LIST);
		}
		filenameList.add(ADDED_WINDOW_NAME);
		
		File file = new File(snapPath+"/"+ADDED_WINDOW_NAME);

	      if (file.createNewFile()){
	        System.out.println("File is created!");
	      }else{
	        System.out.println("File already exists.");
	      }
		
		writeFile.writeFile(snapPath + "\\" + ACTIVE_APP_LIST, filenameList,false);
		
	}

	private static interface WndEnumProc extends StdCallLibrary.StdCallCallback {
		boolean callback(HWND hWnd, int lParam);
	}

	private static interface User32 extends StdCallLibrary {
		final User32 instance = (User32) Native.loadLibrary("user32", User32.class);

		boolean EnumWindows(WndEnumProc wndenumproc, int lParam);

		boolean IsWindowVisible(HWND hWnd);

		void GetWindowTextA(HWND hWnd, byte[] buffer, int buflen);

		int GetTopWindow(int hWnd);

		int GetWindow(int hWnd, int flag);

		final int GW_HWNDNEXT = 2;

		int GetWindowThreadProcessId(HWND hwnd, IntByReference pid);
	}

	private interface Kernel32 extends StdCallLibrary {
		Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

		public Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);

		public int GetTickCount();
	};

	private interface psapi extends StdCallLibrary {
		psapi INSTANCE = (psapi) Native.loadLibrary("psapi", psapi.class);
		int GetModuleFileNameExA(Pointer process, Pointer hModule, byte[] lpString, int nMaxCount);
	};

	private static String getModuleFilename(HWND hwnd) {
		// logger.debug("inside getModuleFilename()");
		byte[] exePathname = new byte[512];
		Pointer zero = new Pointer(0);
		IntByReference pid = new IntByReference();
		User32.instance.GetWindowThreadProcessId(hwnd, pid);
		Pointer process = Kernel32.INSTANCE.OpenProcess(1040, false, pid.getValue());
		pidId = pid.getValue();
		int result = psapi.INSTANCE.GetModuleFileNameExA(process, zero, exePathname, 512);
		String text = Native.toString(exePathname).substring(0, result);
		String info = text;
		return info;
	}

	private static String polpulateSnapLocation(String folderName) {
		logger.debug("inside polpulateSnapLocation()");
		String path = APP_DATA_LOCATION + "\\" + folderName;
		WriteFileUtility.mkDir(path);
		return path;
	}
	public static void main(String[] args) throws IOException {
		takeSnapShot("windowsapp6");
	}
}
