package com.snap.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMLateBindingObject;
import com.sun.jna.platform.win32.COM.IDispatch;

public class ExplorerPathProcessor {
	private static final Logger logger = Logger.getLogger(ExplorerPathProcessor.class.getName());

	public static List<String> populateExplorerPathList() {
		logger.debug("inside populateExplorerPathList()");
		try {
			//logger.debug("Inside populateExplorerPathList()");
			ShellApplication sa = new ShellApplication();

			//logger.debug(i);
			
			List<String> list = new ArrayList<String>();

			for (InternetExplorer ie : sa.Windows()) {
				if (ie != null && !"about:blank".equals(ie.getURL())) {
					
				//	logger.debug(ie.getURL());
					
					if(ie.getURL().contains("%20")){
					
						list.add(ie.getURL().replace("%20", " "));
					}
					else {
						list.add(ie.getURL());
					}
				}
			}
			 Ole32.INSTANCE.CoUninitialize();
			return list;
		} catch (COMException e) {
			logger.debug("Exception OCCurred"+ e);
			return null;
		}
	}

	private static class ShellApplication extends COMLateBindingObject {
		public ShellApplication() throws COMException {
			
			super("Shell.Application", false);
			//logger.debug("Inside ShellApplication()");
		}

		public ShellWindows Windows() {
			//logger.debug("Inside Windows()");
			return new ShellWindows((IDispatch) invoke("Windows").getValue());
		}

		public static class ShellWindows extends COMLateBindingObject implements
				Iterable<InternetExplorer> {
			private static class ShellWindowsIterator implements
					Iterator<InternetExplorer> {

				private ShellWindows source;

				private int count;

				private int max;

				public ShellWindowsIterator(ShellWindows collection) {
					//logger.debug("inside ShellWindowsIterator()");
					source = collection;
					max = source.Count();
				}

				public boolean hasNext() {
					
					return count < max;
				}

				public InternetExplorer next() {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}
					return source.Item(count++);
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}

			}

			public ShellWindows(IDispatch iDispatch) {
				super(iDispatch);
				//logger.debug("inside ShellWindows()");
			}

			public InternetExplorer Item(int idx) {
				//logger.debug("inside Item()");
				VARIANT arg = new VARIANT();
				arg.setValue(Variant.VT_I4, new LONG(idx));
				IDispatch result = (IDispatch) invoke("Item", arg).getValue();
				if (result == null) {
					return null;
				}
				return new InternetExplorer(result);
			}

			public int Count() {
				//logger.debug("inside Count()");
				return getIntProperty("Count");
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Iterator iterator() {
				//logger.debug("inside iterator()");
				return new ShellWindowsIterator(this);
			}
		}
	}

	private static class InternetExplorer extends COMLateBindingObject {
		
		
		public InternetExplorer(IDispatch iDispatch) {
			super(iDispatch);
			//logger.debug("inside InternetExplorer()");
		}

		public String getURL() {
			//logger.debug("inside getURL()");
			return getStringProperty("LocationURL");
		}
	}

	public static void main(String[] args) {
	List<String> list=	ExplorerPathProcessor.populateExplorerPathList();
	
	/*Cmd cmd = new Cmd();
	cmd.executeIEurls(list);*/
	for (String string : list) {
		logger.debug(string);
	}
	}
}
