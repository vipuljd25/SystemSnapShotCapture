package com.snap.service;

import org.apache.log4j.Logger;

import javafx.beans.property.SimpleStringProperty;

public class EditStationTable {
	private static final Logger logger = Logger.getLogger(EditStationTable.class.getName());
	private SimpleStringProperty name;
	private String applicationType;
	private SimpleStringProperty actionButton;
	private String fileElement;

	public String getFileElement() {
		return fileElement;
	}

	public void setFileElement(String fileElement) {
		this.fileElement = fileElement;
	}

	public EditStationTable(String name, String applicationType,String fileElement) {
		logger.debug("Inside InsertIntoTable()");
		this.fileElement = fileElement;
		this.name = new SimpleStringProperty(name);
		this.applicationType = applicationType;
		actionButton= new SimpleStringProperty(name);
		logger.debug("InsertIntoTable() End..");
	}

	public String getName() {
		return name.get();
	}

	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}


	public String getActionButton() {
		return actionButton.get();
	}

	public void setActionButton(SimpleStringProperty actionButton) {
		this.actionButton = actionButton;
	}

}
