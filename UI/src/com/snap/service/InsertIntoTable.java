package com.snap.service;

import org.apache.log4j.Logger;

import javafx.beans.property.SimpleStringProperty;

public class InsertIntoTable {
	private static final Logger logger = Logger.getLogger(InsertIntoTable.class.getName());
	private SimpleStringProperty WorksatationName;
	private SimpleStringProperty WorksatationDetail;
	private SimpleStringProperty WorksatationDateTime;
	private SimpleStringProperty launchButton;
	private SimpleStringProperty actionButton;

	public InsertIntoTable(String Name, String Detail, String DateTime) {
		logger.debug("Inside InsertIntoTable()");
		this.WorksatationName = new SimpleStringProperty(Name);
		this.WorksatationDetail = new SimpleStringProperty(Detail);
		this.WorksatationDateTime = new SimpleStringProperty(DateTime);
		launchButton = new SimpleStringProperty(Name);
		actionButton= new SimpleStringProperty(Name);
		logger.debug("InsertIntoTable() End..");
	}

	public String getName() {
		return WorksatationName.get();
	}

	public void setName(String Name) {
		WorksatationName.set(Name);
	}

	public String getDetail() {
		return WorksatationDetail.get();
	}

	public void setDetail(String Detail) {
		WorksatationDetail.set(Detail);
	}

	public String getDateTime() {
		return WorksatationDateTime.get();
	}

	public void setDateTime(String DateTime) {
		WorksatationDateTime.set(DateTime);
	}

	public String getLaunchButton() {
		return launchButton.get();
	}

	public void setLaunchButton(SimpleStringProperty launchButton) {
		this.launchButton = launchButton;
	}
	
	public String getActionButton() {
		return actionButton.get();
	}

	public void setActionButton(SimpleStringProperty actionButton) {
		this.actionButton = actionButton;
	}

}
