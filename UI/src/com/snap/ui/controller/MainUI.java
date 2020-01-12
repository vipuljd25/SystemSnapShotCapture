package com.snap.ui.controller;

import static com.snap.common.util.SnapConstants.APP_DATA_LOCATION;
import static com.snap.common.util.SnapConstants.RECEIVED_FILES;
import static com.snap.common.util.SnapConstants.WINDOW_TITLE;
import static com.snap.common.util.SnapConstants.CLOSE_EXE_FILE_VBS;
import static com.snap.common.util.SnapConstants.APP_DATA_VBS;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.snap.common.util.WriteFileUtility;
import com.snap.processor.ReceivedFileProcessor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainUI extends Application {
	private static final Logger logger = Logger.getLogger(MainUI.class.getName());

	@Override
	public void start(Stage primaryStage) {
		logger.debug("inside start() ");
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/snap/ui/fxml/Main.fxml"));
			primaryStage.getIcons().add(new Image("app.png"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			logger.error("ERROR", e);
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.exit(0);
	}

	public static void main(String[] args) throws Exception {
		logger.debug("inside main() ");
		MainUI mainUI = new MainUI();
		mainUI.writeCloseExeVBS();
		mainUI.init();
		
		Executors.newCachedThreadPool().execute(new Runnable() {
		    public void run() {
		    	
		    	ReceivedFileProcessor receivedFileProcessor = new ReceivedFileProcessor();
			try {
				receivedFileProcessor.listenForChanges(new File(APP_DATA_LOCATION+"\\"+RECEIVED_FILES));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    }
		});
		logger.debug("ExplorerPathWritingScheduler started.. ");
		launch(args);
	}
	
	private  void writeCloseExeVBS(){
		WriteFileUtility fileUtility = new WriteFileUtility();
		File file= new File(APP_DATA_VBS+ "\\" +CLOSE_EXE_FILE_VBS);
		if(!file.exists()){
		String vbContent="Option Explicit\r\nDim objFSO, strTextFile, strData, strLine, arrLines\r\nDim ret\r\nDim qry\r\nDim wmi\r\nDim wshShell\r\nDim p\r\nCONST ForReading = 1\r\nstrTextFile = WScript.Arguments.Item(0)\r\nSet objFSO = CreateObject(\"Scripting.FileSystemObject\")\r\nstrData = objFSO.OpenTextFile(strTextFile,ForReading).ReadAll\r\narrLines = Split(strData,vbCrLf)\r\nFor Each strLine in arrLines\r\nSet wshShell = CreateObject(\"WScript.Shell\")\r\nstrLine= Trim(strLine)\r\nIf Len(strLine) <=0  Then\r\nElse\r\nSet wmi = GetObject(\"winmgmts://./root/cimv2\")\r\nqry = \"SELECT * FROM Win32_Process WHERE  ExecutablePath = \" & strLine\r\nFor Each p In wmi.ExecQuery(qry)\r\np.Terminate()\r\nNext\r\nEnd If\r\nNext\r\nSet objFSO = Nothing";
		fileUtility.writeFileIntoTxt(file.getPath(),vbContent);
		}
	}
}
