package com.snap.ui.controller;

import static com.snap.common.util.SnapConstants.APP_DATA_LOCATION;
import static com.snap.common.util.SnapConstants.SNAPSHOT_WINDOW_LIST;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.snap.common.util.WriteCSVFileUtility;
import com.snap.service.SnapShotService;
import com.snap.service.WorkStationTable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TakeScreenShotController {
	private static final Logger logger = Logger.getLogger(TakeScreenShotController.class.getName());
	@FXML
	TextField WorkStationName;
	/*@FXML
	TextArea WorkStationDetail;*/
	@FXML
	Button Save;
	@FXML
	Button cancel;

	public void savePressed() throws IOException {
		logger.debug("inside savePressed() ");
		
		WriteCSVFileUtility a = new WriteCSVFileUtility();
		if (!WorkStationName.getText().isEmpty()) {
		//	a.write(WorkStationName.getText().replaceAll(",", " "), WorkStationDetail.getText().replaceAll(",", " "));
			a.write(WorkStationName.getText().replaceAll(",", " ").trim(),null);
			SnapShotService.takeSnapShot(WorkStationName.getText().trim());
			Stage stage = (Stage) Save.getScene().getWindow();
			stage.close();
			UpdateInsertIntoTable();
		} else {
			logger.error("inside savePressed() error occured.WorkStationName must not be null..  ");
		}
	}
	public void cancelButtonPressed() {
		logger.debug("inside cancelButtonPressed() ");
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}
	
private void UpdateInsertIntoTable() throws IOException {
	WorkStationTableSingleton singleton= WorkStationTableSingleton.getInstance();

	logger.debug("inside MainController() ");
		List<WorkStationTable> insert = new ArrayList<WorkStationTable>();
		String splitBy = ",";
		File file = new File(APP_DATA_LOCATION + "\\" + SNAPSHOT_WINDOW_LIST);
		if(file.exists()){
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			String[] b = line.split(splitBy);
			insert.add(new WorkStationTable(b[0]));
		}
		br.close();
		singleton.setInsertIntoTables(insert);
		}else{
			logger.debug("at least one snapshot must be created first to view the table data");
		}
	}
}
