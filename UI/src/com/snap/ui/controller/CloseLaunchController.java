package com.snap.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.snap.processor.ExplorerPathProcessor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CloseLaunchController implements Initializable {
	private static final Logger logger = Logger.getLogger(CloseLaunchController.class.getName());

	@FXML
	Button ok;
	@FXML
	Label lbl;
	
	public void okPressed()
	{
		logger.debug("Inside okPressed()");
		Stage stage = (Stage) ok.getScene().getWindow();
		stage.close();
	}

	
	public void setName(String name)
	{
		
		
		logger.debug("set Label on frame");
		lbl.setText(name);
	}
  
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
