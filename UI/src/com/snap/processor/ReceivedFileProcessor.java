package com.snap.processor;

import static com.snap.common.util.SnapConstants.APP_DATA_LOCATION;
import static com.snap.common.util.SnapConstants.RECEIVED_SNAPSHOT;
import static com.snap.common.util.SnapConstants.SNAPSHOT_DETAIL_TXT;
import static com.snap.common.util.SnapConstants.SNAPSHOT_WINDOW_LIST;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.snap.common.util.FileDecompressor;
import com.snap.common.util.WriteFileUtility;
import com.snap.service.WorkStationTable;
import com.snap.ui.controller.WorkStationTableSingleton;

public class ReceivedFileProcessor {
	private static final Logger logger = Logger.getLogger(ReceivedFileProcessor.class.getName());

	public void listenForChanges(File file) throws IOException, InterruptedException {
		logger.debug("inside listenForChanges");
		WriteFileUtility.mkDir(file.getPath());
		Path path = file.toPath();
		if (file.isDirectory()) {
			WatchService ws = path.getFileSystem().newWatchService();
			path.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			WatchKey watch = null;
			while (true) {
				logger.debug("Watching directory: " + file.getPath());
				try {
					watch = ws.take();
				} catch (InterruptedException ex) {
					logger.error("Interrupted: "+ex);
				}
				List<WatchEvent<?>> events = watch.pollEvents();
				watch.reset();
				for (WatchEvent<?> event : events) {
					Kind<Path> kind = (Kind<Path>) event.kind();
					Path context = (Path) event.context();
					if (kind.equals(StandardWatchEventKinds.OVERFLOW)) {
						logger.debug("OVERFLOW");
					} else if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
						logger.debug("Created: " + context.getFileName());
						String inputZipFile = file.getPath() + "\\" + context.getFileName();
						String outputFolder = APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT + "\\" + context.getFileName();
						Thread.sleep(1000);
						Boolean result = FileDecompressor.unZipFile(inputZipFile, outputFolder);
						Thread.sleep(1000);
						if (result){
							deleteFile(new File(inputZipFile));
						}else{
							//file is not deleted...
						}
						write(outputFolder+"\\"+SNAPSHOT_DETAIL_TXT,APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT+"\\"+SNAPSHOT_WINDOW_LIST);
						updateWorkStationTable();
					} else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
						logger.debug("Deleted: " + context.getFileName());
					} else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
						logger.debug("Modified: " + context.getFileName());
					}
				}
			}
		} else {
			logger.error("Not a directory. Will exit.");
		}
	}

	private void deleteFile(File element) {
		logger.debug("Inside deleteFile");
		if (element.isDirectory()) {
			for (File sub : element.listFiles()) {
				deleteFile(sub);
			}
		}
		element.delete();
	}
	
	private void write(String inputFile, String outputFile) {
		try {
			File file = new File(inputFile);
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				List<String> csv_elements = new ArrayList<String>();
				while ((line = br.readLine()) != null) {
					csv_elements.add(line);
				}
				writeCSV(csv_elements,outputFile);
				br.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception: +e");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception: +e");
		}
		
	}
	
	private void updateWorkStationTable() throws IOException {
		logger.debug("inside updateWorkStationTable");
		WorkStationTableSingleton singleton= WorkStationTableSingleton.getInstance();
		
			List<WorkStationTable> insert = new ArrayList<WorkStationTable>();
			String splitBy = ",";
			File file = new File(APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT+"\\"+SNAPSHOT_WINDOW_LIST);
			if(file.exists()){
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] b = line.split(splitBy);
				insert.add(new WorkStationTable(b[0]));
			}
			br.close();
			singleton.setRecievedSnapShotTables(insert);
			}
		}
	
	private void writeCSV(List<String> elementName,String filePath) {
		logger.debug("inside writeCSV");
		File theFile = new File(filePath);
				try {
					FileWriter pw = new FileWriter(theFile);
					for (String name : elementName) {
						pw.append(name);
						pw.append("\n");
					}
					pw.flush();
					pw.close();
				} catch (IOException e) {
					logger.error("Exception"+ e);
				}
				
	}

	public static void main(String[] args) throws InterruptedException {

		File file = new File("C:\\Scripts\\Snap-Session\\received files");
		try {
			logger.debug("Listening on: " + file);
			ReceivedFileProcessor pathTest = new ReceivedFileProcessor();
			pathTest.listenForChanges(file);
		} catch (IOException ex) {
			logger.error("Exception: "+ex);
		}
	}
}