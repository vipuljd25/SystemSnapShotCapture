package com.snap.ui.controller;

import static com.snap.common.util.SnapConstants.ACTIVE_APP_LIST;
import static com.snap.common.util.SnapConstants.ADDED_WINDOW_NAME;
import static com.snap.common.util.SnapConstants.APP_DATA_LOCATION;
import static com.snap.common.util.SnapConstants.IE_URLS_FILE_PATH;
import static com.snap.common.util.SnapConstants.MOZILLA_URLS_PATH;
import static com.snap.common.util.SnapConstants.RECEIVED_SNAPSHOT;
import static com.snap.common.util.SnapConstants.WINDOW_NAME_EXE_PATH_LIST;
import static com.snap.common.util.SnapConstants.WINDOW_NAME_LIST;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.log4j.Logger;

import com.snap.common.infra.DeleteFromEditView;
import com.snap.common.infra.DeleteUsingIndex;
import com.snap.common.util.WriteFileUtility;
import com.snap.service.EditStationTable;

public class RecievedWorkStationEditController implements Initializable {
	private static final Logger logger = Logger.getLogger(RecievedWorkStationEditController.class.getName());
	@FXML
	private Button done;
	@FXML
	private TextField addressField;
	@FXML
	private Button addURL;
	@FXML
	private TableView<EditStationTable> table;
	@FXML
	private TableColumn<EditStationTable, String> name;
	@FXML
	private TableColumn<EditStationTable, String> action;
	@FXML
	private Button add;
	private List<File> files = new ArrayList<File>();
	private List<String> filesString = new ArrayList<String>();
	private WriteFileUtility writeFile = new WriteFileUtility();
	private List<String> addedPath = new ArrayList<String>();
	private DeleteFromEditView deletefromeditview = new DeleteFromEditView();
	private String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	private WorkStationTableSingleton singleton = WorkStationTableSingleton.getInstance();

	private final ObservableList<EditStationTable> list = FXCollections.observableArrayList();

	public void donePressed() {
		Stage stage = (Stage) done.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	public RecievedWorkStationEditController() throws IOException {
		super();
		logger.debug("inside WorkStationDetailController() ");

		List<EditStationTable> insert = new ArrayList<EditStationTable>();
		String fileName = null;
		try {
			String snapShotName = singleton.getRecievedSnapName();
			System.out.println("inside try");
			FileReader fileReader = new FileReader(
					APP_DATA_LOCATION + "/" + RECEIVED_SNAPSHOT + "/" + snapShotName + "/" + ACTIVE_APP_LIST);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((fileName = bufferedReader.readLine()) != null) {
				File file = new File(APP_DATA_LOCATION + "/" + RECEIVED_SNAPSHOT + "/" + snapShotName + "/" + fileName);
				
				if(file.exists()){
				System.out.println("inside while");
				String fileElements = null;
				FileReader fileReader1 = new FileReader(file);
				BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
				if (IE_URLS_FILE_PATH.equals(fileName) || MOZILLA_URLS_PATH.equals(fileName)) {

					while ((fileElements = bufferedReader1.readLine()) != null) {

						EditStationTable stationTable = new EditStationTable(fileElements, fileName, fileElements);
						insert.add(stationTable);

					}
					bufferedReader1.close();

				} else {
					while ((fileElements = bufferedReader1.readLine()) != null) {
						if (fileElements.matches(regex)) {
							EditStationTable stationTable = new EditStationTable(fileElements, fileName, fileElements);
							insert.add(stationTable);
						} else {
							String onlyName = new File(fileElements).getName().toString();
							EditStationTable stationTable = new EditStationTable(onlyName, fileName, fileElements);
							insert.add(stationTable);
						}

					}
				}
					bufferedReader1.close();
				}
			}
			bufferedReader.close();
			list.addAll(insert);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void initialize(URL location, ResourceBundle resources) {

		final DeleteUsingIndex deleteusingindex = new DeleteUsingIndex();
		final String snapShotName = singleton.getRecievedSnapName();
		System.out.println("inside initialize");
		logger.debug("inside initialize() ");
		name.setCellValueFactory(new PropertyValueFactory<EditStationTable, String>("Name"));
		System.out.println(snapShotName);

		action.setCellValueFactory(new PropertyValueFactory<EditStationTable, String>("actionButton"));
		Callback<TableColumn<EditStationTable, String>, TableCell<EditStationTable, String>> actionCellFactory = //
				new Callback<TableColumn<EditStationTable, String>, TableCell<EditStationTable, String>>() {
					public TableCell call(final TableColumn<EditStationTable, String> param) {
						final TableCell<EditStationTable, String> cell = new TableCell<EditStationTable, String>() {

							final Button btn = new Button("");
							Image image = new Image("/img/Recycle_Bin_Full.png", 25, 25, false, false);

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setMinSize(50, 50);
									btn.setMaxSize(50, 50);
									btn.setGraphic(new ImageView(image));
									btn.setOnAction(new EventHandler<ActionEvent>() {
										public void handle(ActionEvent actionEvent) {
											int indextobedeleted = 0;
											EditStationTable intoTable = getTableView().getItems().get(getIndex());

											System.out.println(intoTable.getName());
											System.out.println(intoTable.getApplicationType());
											try {

												indextobedeleted = deletefromeditview.removeLineFromFile(
														APP_DATA_LOCATION + "/" + RECEIVED_SNAPSHOT + "/" + snapShotName
																+ "/" + intoTable.getApplicationType(),
														intoTable.getFileElement());

												if (intoTable.getApplicationType().trim()
														.equals(WINDOW_NAME_LIST.trim())) {
													System.out.println("inside if");
													deleteusingindex.removeLineFromFile(
															APP_DATA_LOCATION + "/" + RECEIVED_SNAPSHOT + "/"
																	+ snapShotName + "/" + WINDOW_NAME_EXE_PATH_LIST,
															indextobedeleted);
												}
												System.out.println(indextobedeleted);

											} catch (IOException e) {
												// TODO Auto-generated catch
												// block77.
												e.printStackTrace();
											}
											list.remove(getIndex());
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

		add.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				Stage primaryStage = new Stage();
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All types", " *.*");
				fileChooser.getExtensionFilters().add(extFilter);
				files = fileChooser.showOpenMultipleDialog(primaryStage);
				if (files != null && files.size() > 0) {
					for (File f : files) {
						filesString.add("\"" + f.toString() + "\"");
						EditStationTable stationTable = new EditStationTable(f.getName(), ADDED_WINDOW_NAME,
								f.toString());// adding only Name of the file
						list.add(stationTable);
					}
				}
				writeFile.writeFile(
						APP_DATA_LOCATION + "/" + RECEIVED_SNAPSHOT + "/" + snapShotName + "/" + ADDED_WINDOW_NAME,
						filesString,true);

			}
		});
		addURL.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				

				String path = addressField.getText();
				addressField.clear();
				addedPath.add("\""+path+"\"");
				if(!path.isEmpty()){
					if(path.matches(regex))
					{
						EditStationTable stationTable = new EditStationTable(path, ADDED_WINDOW_NAME,
								path);// adding only Name of the file
						list.add(stationTable);
					}
					else
					{
						String onlyName = new File(path).getName().toString();
						EditStationTable stationTable = new EditStationTable(onlyName, ADDED_WINDOW_NAME,
								path);// adding only Name of the file
						list.add(stationTable);
					}
					writeFile.writeFile(
							APP_DATA_LOCATION + "/" + RECEIVED_SNAPSHOT + "/" + snapShotName + "/" + ADDED_WINDOW_NAME,
							addedPath,true);
				}
			}
		});
		action.setCellFactory(actionCellFactory);
		table.setItems(list);
	}
}
