package com.snap.ui.controller;

import static com.snap.common.util.SnapConstants.APP_DATA_LOCATION;
import static com.snap.common.util.SnapConstants.SNAPSHOT_WINDOW_LIST;
import static com.snap.common.util.SnapConstants.WINDOW_TITLE;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.snap.common.infra.SingletonAppData;
import com.snap.service.SnapLaunchService;
import com.snap.service.WorkStationTable;

public class WorkStationDetailController implements Initializable {
	private static final Logger logger = Logger.getLogger(WorkStationDetailController.class.getName());
	@FXML
	private TableView<WorkStationTable> table;
	@FXML
	private TableColumn<WorkStationTable, String> launch;
	@FXML
	private TableColumn<WorkStationTable, String> name;
	@FXML
	private TableColumn<WorkStationTable, String> detail;
	@FXML
	private TableColumn<WorkStationTable, String> dateTime;
	@FXML
	private TableColumn<WorkStationTable, String> action;
	@FXML
	private TableColumn<WorkStationTable, String> share;

	private final ObservableList<WorkStationTable> list = FXCollections.observableArrayList();
	// static String mailid1;

	public WorkStationDetailController() throws IOException {
		super();
		logger.debug("inside WorkStationDetailController() ");
		List<WorkStationTable> insert = new ArrayList<WorkStationTable>();
		String splitBy = ",";
		File file = new File(APP_DATA_LOCATION + "\\" + SNAPSHOT_WINDOW_LIST);
		if (file.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] b = line.split(splitBy);
				System.out.println(b[0]);
				/*
				 * System.out.println(b[1]); System.out.println(b[2]);
				 */
				insert.add(new WorkStationTable(b[0], null, null));
			}
			br.close();
			for (WorkStationTable insertIntoTable : insert) {
				list.add(insertIntoTable);
			}
		} else {
			logger.debug("at least one snapshot must be created first to view the table data");
		}
	}

	public void initialize(URL location, ResourceBundle resources) {
		logger.debug("inside initialize() ");
		name.setCellValueFactory(new PropertyValueFactory<WorkStationTable, String>("Name"));
		detail.setCellValueFactory(new PropertyValueFactory<WorkStationTable, String>("Detail"));
		dateTime.setCellValueFactory(new PropertyValueFactory<WorkStationTable, String>("DateTime"));
		launch.setCellValueFactory(new PropertyValueFactory<WorkStationTable, String>("Launch"));
		Callback<TableColumn<WorkStationTable, String>, TableCell<WorkStationTable, String>> cellFactory = //
				new Callback<TableColumn<WorkStationTable, String>, TableCell<WorkStationTable, String>>() {
					public TableCell call(final TableColumn<WorkStationTable, String> param) {
						final TableCell<WorkStationTable, String> cell = new TableCell<WorkStationTable, String>() {

							final Button btn = new Button("Launch");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(new EventHandler<ActionEvent>() {
										public void handle(ActionEvent actionEvent) {
											WorkStationTable intoTable = getTableView().getItems().get(getIndex());
											System.out.println(intoTable.getLaunchButton() + "launched..");
											SnapLaunchService find = new SnapLaunchService();
											try {
												find.snapLaunch(APP_DATA_LOCATION + "\\" + intoTable.getLaunchButton());
											} catch (IOException | AWTException e) {
												
												logger.error("Exception"+e);
											}
										}
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		launch.setCellFactory(cellFactory);

		action.setCellValueFactory(new PropertyValueFactory<WorkStationTable, String>("actionButton"));
		Callback<TableColumn<WorkStationTable, String>, TableCell<WorkStationTable, String>> actionCellFactory = //
				new Callback<TableColumn<WorkStationTable, String>, TableCell<WorkStationTable, String>>() {
					public TableCell call(final TableColumn<WorkStationTable, String> param) {
						final TableCell<WorkStationTable, String> cell = new TableCell<WorkStationTable, String>() {

							final Button btn = new Button("Delete");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(new EventHandler<ActionEvent>() {
										public void handle(ActionEvent actionEvent) {
											WorkStationTable intoTable = getTableView().getItems().get(getIndex());
											System.out.println(intoTable.getLaunchButton() + "launched..");

											try {
												CSVReader reader2 = new CSVReader(new FileReader(
														APP_DATA_LOCATION + "\\" + SNAPSHOT_WINDOW_LIST));
												List<String[]> allElements = reader2.readAll();
												allElements.remove(getIndex());
												FileWriter sw = new FileWriter(
														APP_DATA_LOCATION + "\\" + SNAPSHOT_WINDOW_LIST);
												CSVWriter writer = new CSVWriter(sw);
												writer.writeAll(allElements);
												writer.close();
												reader2.close();
												list.remove(getIndex());

												deleteFile(new File(APP_DATA_LOCATION + "\\" + intoTable.getName()));

												/*
												 * Path dirPath =
												 * Paths.get(APP_DATA_LOCATION +
												 * "\\" + intoTable.getName());
												 * Files.deleteIfExists(dirPath)
												 * ;
												 */

											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		action.setCellFactory(actionCellFactory);

		// Share Button
		share.setCellValueFactory(new PropertyValueFactory<WorkStationTable, String>("sharebutton"));
		Callback<TableColumn<WorkStationTable, String>, TableCell<WorkStationTable, String>> shareCellFactory = //
				new Callback<TableColumn<WorkStationTable, String>, TableCell<WorkStationTable, String>>() {
					public TableCell call(final TableColumn<WorkStationTable, String> param) {
						final TableCell<WorkStationTable, String> cell = new TableCell<WorkStationTable, String>() {

							final Button btn = new Button("Share");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(new EventHandler<ActionEvent>() {
										public void handle(ActionEvent actionEvent) {
											WorkStationTable intoTable = getTableView().getItems().get(getIndex());
											try {
												Stage primaryStage = new Stage();
												Parent root = FXMLLoader
														.load(getClass().getResource("/com/snap/ui/fxml/GetId.fxml"));
												Scene scene = new Scene(root);
												scene.getStylesheets().add(
														getClass().getResource("application.css").toExternalForm());
												// primaryStage.getIcons().add(new
												// Image("/path/to/stackoverflow.jpg"));
												primaryStage.setTitle(WINDOW_TITLE);
												primaryStage.setScene(scene);
												primaryStage.show();
												SingletonAppData.getInstance().setFolderPath(
														new File(APP_DATA_LOCATION + "\\" + intoTable.getName())
																.toString());
												SingletonAppData.getInstance().setFolderName(intoTable.getName().toString());
											} catch (Exception e) {
												logger.error("Exception occured", e);
											}
										}
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		share.setCellFactory(shareCellFactory);
		table.setItems(list);
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
}
