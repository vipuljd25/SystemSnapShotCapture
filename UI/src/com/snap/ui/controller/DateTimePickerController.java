package com.snap.ui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import com.snap.common.infra.SingletonAppData;
import com.snap.processor.LaunchScheduler;

public class DateTimePickerController implements Initializable {
	private static final Logger logger = Logger.getLogger(DateTimePickerController.class.getName());

	@FXML
	DatePicker dp = new DatePicker();

	@FXML
	ComboBox<String> withhours = new ComboBox<String>();

	@FXML
	ComboBox<String> withminutes = new ComboBox<String>();
	
	@FXML
	Button ok;

	ObservableList<String> minutes = FXCollections.observableArrayList( "00","01", "02", "03", "04", "05", "06", "07", "08", "09",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24","25", "26", "27", "28",
			"29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
			"47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59");
	ObservableList<String> hours = FXCollections.observableArrayList("00","01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
			"11", "12","13","14","15","16","17","18","19","20","21","22","23");

	public void ok_pressed() {
		logger.debug("ok of datePicker pressed");
		LocalDate date;
		date = dp.getValue();
	    if(null != date){
		
		String hour = withhours.getValue();
		String minute = withminutes.getValue();
		System.out.println(date);
		System.out.println(hour);
		System.out.println(minute);
		String dateTime=date+"::"+hour+":"+minute;
		if(null != SingletonAppData.getInstance().getScheduleSnap()){
		LaunchScheduler launchScheduler = new LaunchScheduler();
		try {
			launchScheduler.setUpJob(SingletonAppData.getInstance().getScheduleSnap(), dateTime);
			System.out.println(dateTime);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		}
		}
		Stage stage = (Stage) ok.getScene().getWindow();
		
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.debug("setting houres and minutes");
		withhours.setItems(hours);
		withminutes.setItems(minutes);
	}
}
