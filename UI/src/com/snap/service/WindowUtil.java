package com.snap.service;
import static com.snap.common.util.SnapConstants.WINDOWSAPPS_EXE;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.snap.processor.CommandLineProcessor;
import com.snap.processor.WindowsAppProcessor;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

	public class WindowUtil {
		private static final Logger logger = Logger.getLogger(SnapShotService.class.getName());
		
		public static List<String> getWindowTitle() throws IOException {
			logger.debug("inside getWindowTitle()");
			final CommandLineProcessor cmd = new CommandLineProcessor();
			cmd.showHiddenExtention();
			final List<String> titleList = new ArrayList<String>();

			int top = User32.instance.GetTopWindow(0);
			while (top != 0) {
				top = User32.instance.GetWindow(top, User32.GW_HWNDNEXT);
			}
			User32.instance.EnumWindows(new WndEnumProc() {

				public boolean callback(HWND hWnd, int lParam) {
					
					if (User32.instance.IsWindowVisible(hWnd)) {

					
						String title = getWindowTitleName(hWnd);
						if(!title.equals("") && !title.equalsIgnoreCase("Program Manager") && !title.equalsIgnoreCase("Start") &&  !title.contains("?")){
							titleList.add(title);
					System.out.println("title"+title);
						}
					
					}
					return true;
				}
			}, 0);
			
			return titleList;
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
			
			 boolean CloseWindow(HWND hwnd);

			int GetWindowThreadProcessId(HWND hwnd, IntByReference pid);
			
			 HWND FindWindowA(String className, String windowName);
			 
			 void PostMessageA(HWND h,int i,WPARAM w,LPARAM l);
			 
			 Boolean SetForegroundWindow(HWND h);
		}
		
		private interface psapi extends StdCallLibrary {
			psapi INSTANCE = (psapi) Native.loadLibrary("psapi", psapi.class);
			int GetModuleFileNameExA(Pointer process, Pointer hModule, byte[] lpString, int nMaxCount);
		};
		
		private interface Kernel32 extends StdCallLibrary {
			Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

			public Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);

			public int GetTickCount();
		};

		private static String getModuleFileExePath(HWND hwnd) {
			byte[] exePathname = new byte[512];
			Pointer zero = new Pointer(0);
			IntByReference pid = new IntByReference();
			User32.instance.GetWindowThreadProcessId(hwnd, pid);
			Pointer process = Kernel32.INSTANCE.OpenProcess(1040, false, pid.getValue());
			int result = psapi.INSTANCE.GetModuleFileNameExA(process, zero, exePathname, 512);
			String text = Native.toString(exePathname).substring(0, result);
			String path = text;
			return path;
		}
		
		private static String getWindowTitleName(HWND hWnd){
			byte[] buffer = new byte[1024];
			User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
			String title = Native.toString(buffer);
			return title;
		}
		
		
		public static List<String> compareList(List<String> bList,List<String> aList) {
			
			List<String> list= new ArrayList<String>();
			for (String title : aList) {
				if(!bList.contains(title))
					list.add(title);
			}
		//	return	checkWindowVisibility(list);
			return list;
		}
		/*private static List<String> checkWindowVisibility(List<String> nameList){
			List<String> list = new ArrayList<String>();
			
			for (String titleName : nameList) {
				HWND h=User32.instance.FindWindowA(null,titleName);
				if (User32.instance.IsWindowVisible(h)) {

					byte[] buffer = new byte[1024];
					User32.instance.GetWindowTextA(h, buffer, buffer.length);
					String title = Native.toString(buffer);
					
					if(!title.equals("") && !title.contains("?"))
						list.add(title);
				}	
			}
			return list;
		}*/
		
		public static void minimizeAll(){

			logger.debug("inside minimizeAll()");
			final CommandLineProcessor cmd = new CommandLineProcessor();
			cmd.showHiddenExtention();
		

			int top = User32.instance.GetTopWindow(0);
			while (top != 0) {
				top = User32.instance.GetWindow(top, User32.GW_HWNDNEXT);
			}
			User32.instance.EnumWindows(new WndEnumProc() {

				public boolean callback(HWND hWnd, int lParam) {
				
					if (User32.instance.IsWindowVisible(hWnd)) {
						User32.instance.CloseWindow(hWnd);
					}
					return true;
				}
			}, 0);
			
		}
		
		public static void closeApplication(List<String> list){
			logger.debug("inside closeApplication()");
			if(null != list && list.size()>0)
			{
				for (String titleName : list) {
					HWND hWnd=User32.instance.FindWindowA(null,titleName);
					if(null!=hWnd){
					if (User32.instance.IsWindowVisible(hWnd)) {
						String exePath=getModuleFileExePath(hWnd);
						String title=getWindowTitleName(hWnd);
						exePath = exePath.toLowerCase();
					/*	if(exePath.contains(WINDOWSAPPS_EXE)){
							
						WindowsAppProcessor appProcessor= new WindowsAppProcessor();
						appProcessor.WindowApplicationInvoke(title);
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						}*/
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						User32.instance.PostMessageA(hWnd,WinUser.WM_CLOSE,null,null);
						logger.info("Handle : "+titleName+" has been closed....");
					}
					}else {
						logger.info("Handle not found for title: "+titleName);
					}
					
				}
			}
		}
		public static void main(String[] args) throws IOException {
	
			System.out.println(getWindowTitle());
			//minimizeAll();
			}
		}
