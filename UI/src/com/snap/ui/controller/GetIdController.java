package com.snap.ui.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import com.snap.common.infra.SingletonAppData;
import com.snap.common.util.FileCompressor;
import com.snap.common.util.JavaMail;

public class GetIdController  {
	private static final Logger logger = Logger.getLogger(GetIdController.class.getName());
	@FXML
	TextField textfieldlabel;
	@FXML
	TextField Email;
	@FXML
	Button Done;
	@FXML
	Button addAtachment;
	@FXML
	TextArea AttachmentArea;

	List<String> files = new ArrayList<String>();
	//private WorkStationTableSingleton singleton = WorkStationTableSingleton.getInstance();

	public void buttonPressed() throws Exception {
		logger.debug(SingletonAppData.getInstance().getFolderPath().toString());
		mailFile(SingletonAppData.getInstance().getFolderPath().toString(), SingletonAppData.getInstance().getFolderName().toString(),
				Email.getText());
	}

	public void addAttachmentPressed() {
		logger.debug("Inside addAttachmentPressed");
		Stage primaryStage = new Stage();
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All types", " *.*");
		fileChooser.getExtensionFilters().add(extFilter);
		List<File> file = fileChooser.showOpenMultipleDialog(primaryStage);
		if ( file != null) {
			for (File f : file) {
				files.add(f.toString());
			}
			String text = "";
			for (int i = 0; i < files.size(); i++) {

				text = text + "   Attachment " + "[" + (i+1) + "]  " + files.get(i) + "\n";
			}
			AttachmentArea.setText(text);
		}

	}

	private void mailFile(String path, String folderName, String mail) throws Exception {

		logger.debug("Inside mailFile Path:"+path);
		String zipPath = path + "zip";
		FileCompressor.makeZipFile(path, zipPath);
		for (String filename : files) {
			logger.debug("Name OF File"+filename);
		}
		JavaMail.sendMail(files, folderName, mail, zipPath);
		files.clear();
		Stage stage = (Stage) Done.getScene().getWindow();
		stage.close();
	}

}
