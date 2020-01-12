package com.snap.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.snap.processor.CommandLineProcessor;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;


	public class test {
		static int  pid1;
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
					// logger.debug("Inside callback()");
					if (User32.instance.IsWindowVisible(hWnd)) {
						String path = getModuleFilename(hWnd);
					//	System.out.println(path);
                        // System.out.println("handle ID: "+hWnd.toNative());
						byte[] buffer = new byte[1024];
						User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
						String title = Native.toString(buffer);
						if(!title.equals("") && !title.equalsIgnoreCase("Program Manager") && !title.equalsIgnoreCase("Start") &&  !title.contains("?")){
							titleList.add(title);
					//System.out.println("title: "+title+"Handle: "+hWnd);
							System.out.println("Handle: "+hWnd+"path: "+path);
					//User32.instance.CloseWindow(hWnd);
					//User32.instance.DestroyWindowA(hWnd);
					User32.instance.SetForegroundWindow(hWnd);
				//	byte[] buffer1 = new byte[1024];
					//Kernel32.INSTANCE.GetFinalPathNameByHandleA(hWnd,buffer1,buffer1.length,Kernel32.GW_HWNDNEXT);
					//HANDLE h=Kernel32.INSTANCE.CreateToolhelp32Snapshot(0x00000008,pid1);
				//	System.out.println("Handle: "+h+"path: "+path);
					//Win32Process process = new Win32Process();
					//process.terminate(h);
					
					//String filepath = Native.toString(buffer1);
					//System.out.println("filePath: "+filepath );
					/*try {
						Win32Process process = new Win32Process();
						process.terminate(hWnd);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
				
						}
					//	System.out.println("title"+title);
					//	User32.instance.PostMessageA(hWnd,WinUser.WM_CLOSE,null,null);
					}
					return true;
				}
			}, 0);
			
			return titleList;
		}

		private static interface WndEnumProc extends StdCallLibrary.StdCallCallback {
			boolean callback(HWND hWnd, int lParam);
		}

		public static interface User32 extends StdCallLibrary {
			final User32 instance = (User32) Native.loadLibrary("user32", User32.class);

			boolean EnumWindows(WndEnumProc wndenumproc, int lParam);

			boolean IsWindowVisible(HWND hWnd);

			void GetWindowTextA(HWND hWnd, byte[] buffer, int buflen);

			int GetTopWindow(int hWnd);

			int GetWindow(int hWnd, int flag);

			final int GW_HWNDNEXT = 2;

			int GetWindowThreadProcessId(HWND hwnd, IntByReference pid);
			
			 HWND FindWindowA(String className, String windowName);
			 
			 boolean CloseWindow(HWND hwnd);
			 
			 HWND GetWindow(HWND h,DWORD d);
			 
			 void PostMessageA(HWND h,int i,WPARAM w,LPARAM l);
			 
			 HWND SetFocus(HWND h);
			 
			 boolean SetForegroundWindow(HWND h);
		}
		
		
		public interface Kernel32 extends StdCallLibrary {
			Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

			public Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);
			
			boolean  CloseHandle(HWND h);
			
			boolean  TerminateProcess(HWND h,int i);
			
			final int GW_HWNDNEXT = 4;
			 void GetFinalPathNameByHandleA(HANDLE hWnd, byte[] buffer, int buflen,int  dwFlags);
			   
			   HANDLE  CreateToolhelp32Snapshot(int flag, int pid);
			   
			  
				
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
			int pidId = pid.getValue();
			pid1=pidId;
			int result = psapi.INSTANCE.GetModuleFileNameExA(process, zero, exePathname, 512);
			String text = Native.toString(exePathname).substring(0, result);
			String info = text;
			return info+pidId;
		}
		
		/*public static List<String> compareList(List<String> bList,List<String> aList) {
			
			List<String> list= new ArrayList<String>();
			for (String title : aList) {
				if(!bList.contains(title))
					list.add(title);
			}
			return	checkWindowVisibility(list);
		}
		private static List<String> checkWindowVisibility(List<String> nameList){
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
		}
*/
		public static void main(String[] args) throws IOException {
			
	/*		HWND h=User32.instance.FindWindowA(null,"Start");
			if (User32.instance.IsWindowVisible(h)) {

				byte[] buffer = new byte[1024];
				User32.instance.GetWindowTextA(h, buffer, buffer.length);
				String title = Native.toString(buffer);
			System.out.println(title);
			}*/
		//	System.out.println(getWindowTitle());
			getWindowTitle();
			}
		}
