package com.snap.ui.controller;
import static com.snap.common.util.SnapConstants.APP_DATA_LOCATION;
import static com.snap.common.util.SnapConstants.EDIT_WINDOW_TITLE;
import static com.snap.common.util.SnapConstants.FILES_TO_BE_CLOSED_LIST;
import static com.snap.common.util.SnapConstants.RECEIVED_SNAPSHOT;
import static com.snap.common.util.SnapConstants.SCHEDULE_WINDOW_TITLE;
import static com.snap.common.util.SnapConstants.SHARE_WINDOW_TITLE;
import static com.snap.common.util.SnapConstants.SNAPSHOT_WINDOW_LIST;
import static com.snap.common.util.SnapConstants.SYSTEM_CRASH;
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
import java.util.Timer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;
import com.snap.common.infra.SingletonAppData;
import com.snap.common.util.WriteCSVFileUtility;
import com.snap.service.CloseApplicationService;
import com.snap.service.SnapLaunchService;
import com.snap.service.SystemCrashSnapShotScheduler;
import com.snap.service.WorkStationTable;

public class MainController implements Initializable {
	
	@FXML
	Button TakeSnapShot;
	@FXML
	Button Deafault_WorkStation;
	@FXML
	Button WorkStation;
	@FXML
	Button Enabled;
	@FXML
	Button Disabled;
	@FXML
	ComboBox<String> comboBox;
	@FXML
	MenuButton menuButton;
	@FXML
	MenuButton recievedMenuButton;

	Timer time = null;
	private static final Logger logger = Logger.getLogger(MainController.class.getName());
	private WorkStationTableSingleton singleton = WorkStationTableSingleton.getInstance();
	

	public void createWorkStationPressed() {
		logger.debug("inside createWorkStationPressed() ");
		try {
			Stage primaryStage = new Stage();
			primaryStage.getIcons().add(new Image("app.png"));
			Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
	}

	public void TakeSnapShotPressed() {
		logger.debug("inside TakeSnapShotPressed() ");
		try {
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/TakeScreenShot.fxml"));
			Scene scene = new Scene(root);
			primaryStage.getIcons().add(new Image("app.png"));
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
	}

	public void launchCrashSnapShot() {
		try {
			SnapLaunchService find = new SnapLaunchService();
			find.snapLaunch(APP_DATA_LOCATION + "\\" + SYSTEM_CRASH);
		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
	}

	public void myOfficeWorkStationPressed() {
		logger.debug("inside myOfficeWorkStationPressed() ");
		try {
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/WorkStationDetail.fxml"));
			Scene scene = new Scene(root);
			primaryStage.getIcons().add(new Image("app.png"));
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
	}

	public void receivedSnapShotPressed() {
		logger.debug("inside receivedSnapShotPressed() ");
		try {
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/ReceivedWorkStationDetail.fxml"));
			Scene scene = new Scene(root);
			primaryStage.getIcons().add(new Image("app.png"));
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
	}

	public void disablePressed() {
		logger.debug("inside disablePressed() ");
		time.cancel();
	}

	private void enabledPressed() throws IOException {
		logger.debug("inside enabledPressed() ");
		try {
			// Timer time = new Timer(); // Instantiate Timer Object
			SystemCrashSnapShotScheduler st = new SystemCrashSnapShotScheduler(); // Instantiate
			time = new Timer();
			time.schedule(st, 0, 10000);
			logger.debug("SystemCrashSnapShotScheduler started... ");
		} catch (Exception e) {
			logger.error("Execption Occured  ", e);
		}
	}
	
	private void editWorkStationPressed() {
		logger.debug("inside editWorkStationPressed() ");
		try {
			Stage primaryStage = new Stage();
			primaryStage.getIcons().add(new Image("app.png"));
			Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/WorkStationEdit.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.setTitle(EDIT_WINDOW_TITLE + singleton.getSnapName());
			primaryStage.setScene(scene);
			primaryStage.showAndWait();

		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
	}
	
	private void editRecievedWorkStationPressed() {
		logger.debug("inside editWorkStationPressed() ");
		try {
			Stage primaryStage = new Stage();
			primaryStage.getIcons().add(new Image("app.png"));
			Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/RecievedWorkStationEdit.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.setScene(scene);
			primaryStage.showAndWait();

		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
	}

	public void launch() {
	}

	public void MouseEntred() {
		logger.debug("Entered...");
	}

	public void MouseExited() {
		logger.debug("Exited...");
	}

	public void setData() throws IOException {
		menuButton.getItems().clear();
		WriteCSVFileUtility a = new WriteCSVFileUtility();
		final List<Menu> menus = new ArrayList<Menu>();
		Menu menu;
		List<WorkStationTable> list = singleton.getInsertIntoTables();
		//System.out.println(+"dxfcgvhbj");
		/*if(list==null)
		{
			a.write(SYSTEM_CRASH.replaceAll(",", " "),null);
			setSnapShotElements();
			list = singleton.getInsertIntoTables();
		}*/
		if(list!=null){
		for (int j = 0; j < list.size(); j++) {
			final int i = j;
			list.get(j);
			final MenuItem launch = new MenuItem("Launch");
			final MenuItem share = new MenuItem("Share");
			final MenuItem delete = new MenuItem("Delete");
			final MenuItem edit = new MenuItem("Edit");
			final MenuItem close = new MenuItem("Close");
			final MenuItem schedule = new MenuItem("ScheduleLaunch");
			menu = new Menu(list.get(j).getName());
			/*if(j==0){
				menu.getItems().addAll(launch);
			}else{
				menu.getItems().addAll(launch, share, delete,edit,close,schedule);
			}*/
			menu.getItems().addAll(launch, share, delete,edit,close,schedule);
			
			menus.add(menu);
			launch.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					logger.debug("Menu" + launch.getParentMenu().getText() + "Menu item" + launch.getText());
					SnapLaunchService find = new SnapLaunchService();
					try {
						/*if(launch.getParentMenu().getText().contains("SystemCrash")){
							synchronized (SystemCrashSnapShotScheduler.class) {
								menuButton.hide();
								find.snapLaunch(APP_DATA_LOCATION + "\\" + launch.getParentMenu().getText());
							}
						}else{
							menuButton.hide();
							find.snapLaunch(APP_DATA_LOCATION + "\\" + launch.getParentMenu().getText());
						}*/
						menuButton.hide();
						find.snapLaunch(APP_DATA_LOCATION + "\\" + launch.getParentMenu().getText());
						logger.debug(launch.getParentMenu().getText() + "  lanched....");
					} catch (IOException | AWTException e) {
						
						logger.error("EXCEPTION"+e);
					}
				}
			});

			delete.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					try {
						
						File file = new File(APP_DATA_LOCATION + "\\" + SNAPSHOT_WINDOW_LIST);
						if (file.exists()) {
							BufferedReader br = new BufferedReader(new FileReader(file));
							String line;
							List<String> csv_elements = new ArrayList<String>();
							while ((line = br.readLine()) != null) {
								csv_elements.add(line);
							}
							csv_elements.remove(i);
							writeCSV(csv_elements,APP_DATA_LOCATION + "\\" + SNAPSHOT_WINDOW_LIST);
							br.close();
						}
						
						deleteFile(new File(APP_DATA_LOCATION + "\\" + delete.getParentMenu().getText()));
						setSnapShotElements();

					} catch (FileNotFoundException e) {
						logger.error("Exception"+e);
					} catch (IOException e) {
						logger.error("Exception"+e);
					}
				}
			});
			share.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					System.out.println("Menu" + share.getParentMenu().getText() + "Menu item" + share.getText());

					try {
						singleton.setSnapName(edit.getParentMenu().getText().toString());
						Stage primaryStage = new Stage();
						Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/GetId.fxml"));
						Scene scene = new Scene(root);
						primaryStage.getIcons().add(new Image("app.png"));
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setTitle(SHARE_WINDOW_TITLE + singleton.getSnapName());
						primaryStage.setScene(scene);
						primaryStage.show();
						SingletonAppData.getInstance().setFolderPath(
								new File(APP_DATA_LOCATION + "\\" + share.getParentMenu().getText()).toString());
						SingletonAppData.getInstance().setFolderName(share.getParentMenu().getText().toString());
					} catch (Exception e) {
						logger.error("Exception occured", e);
					}
				}
			});
			edit.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					singleton.setSnapName(edit.getParentMenu().getText().toString());
					//System.out.println(edit.getParentMenu().getText().toString());
					editWorkStationPressed();
				}
			});
			close.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent args0){
					String snapName =close.getParentMenu().getText().toString();
					//File CloseApplicationList = new File(APP_DATA_LOCATION + "\\" + snapName+"\\"+TO_BE_CLOSED_LIST);
					File CloseFileList = new File(APP_DATA_LOCATION + "\\" + snapName+"\\"+FILES_TO_BE_CLOSED_LIST);
					
				//	if(CloseApplicationList.exists() || CloseFileList.exists()){
					menuButton.hide();
					if(CloseFileList.exists()){
					singleton.setSnapToBeClosed(snapName);
					CloseApplicationService service=	new CloseApplicationService(); 
					service.close(APP_DATA_LOCATION + "\\" + snapName);
					}
				}
			});
			
			schedule.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent args0){
					
					SingletonAppData.getInstance().setScheduleSnap(APP_DATA_LOCATION + "\\" + schedule.getParentMenu().getText());
					if(null != SingletonAppData.getInstance().getScheduleSnap()){
					try {
						Stage primaryStage = new Stage();
						Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/DateTimePicker.fxml"));
						Scene scene = new Scene(root);
						primaryStage.resizableProperty().setValue(Boolean.FALSE);
						primaryStage.getIcons().add(new Image("app.png"));
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setTitle(SCHEDULE_WINDOW_TITLE + schedule.getParentMenu().getText());
						primaryStage.setScene(scene);
						primaryStage.show();
						
					} catch (IOException e) {
						logger.error("Exception"+e);
					}
				}}
			});
		}
		}
		menuButton.getItems().addAll(menus);
	}

	public void setRecievedSnapData() throws IOException{
		recievedMenuButton.getItems().clear();
		
		final List<Menu> menus = new ArrayList<Menu>();
		Menu menu;
		List<WorkStationTable> list = singleton.getRecievedSnapShotTables();
		if(list!=null){
		for (int j = 0; j < list.size(); j++) {
			final int i = j;
			list.get(j);
			final MenuItem launch = new MenuItem("Launch");
			final MenuItem share = new MenuItem("Share");
			final MenuItem delete = new MenuItem("Delete");
			final MenuItem edit = new MenuItem("Edit");
			final MenuItem close = new MenuItem("Close");
			final MenuItem schedule = new MenuItem("ScheduleLaunch");
			menu = new Menu(list.get(j).getName());
			menu.getItems().addAll(launch, share, delete,edit,close,schedule);
			menus.add(menu);
			launch.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					System.out.println("Menu" + launch.getParentMenu().getText() + "Menu item" + launch.getText());
					SnapLaunchService find = new SnapLaunchService();
					try {
						logger.debug(launch.getParentMenu().getText() + "  lanched....");
						find.snapLaunch(APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT+ "\\" + launch.getParentMenu().getText());
						logger.debug(launch.getParentMenu().getText() + "  lanched....");
					} catch (IOException | AWTException e) {
						
						logger.error("Exception"+e);
					}
				}
			});

			delete.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					try {
						File file = new File(APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT+"\\" + SNAPSHOT_WINDOW_LIST);
						if (file.exists()) {
							BufferedReader br = new BufferedReader(new FileReader(file));
							String line;
							List<String> csv_elements = new ArrayList<String>();
							while ((line = br.readLine()) != null) {
								csv_elements.add(line);
							}
							csv_elements.remove(i);
							writeCSV(csv_elements,APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT+"\\" + SNAPSHOT_WINDOW_LIST);
							br.close();
						}
						
						deleteFile(new File(APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT+ "\\" + delete.getParentMenu().getText()));
						setRecievedSnapShotElements();

					} catch (FileNotFoundException e) {
						logger.error("Exception"+e);
					} catch (IOException e) {
						logger.error("Exception"+e);
					}
				}
			});
			share.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					System.out.println("Menu" + share.getParentMenu().getText() + "Menu item" + share.getText());

					try {
						Stage primaryStage = new Stage();
						Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/GetId.fxml"));
						Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setTitle(WINDOW_TITLE);
						primaryStage.getIcons().add(new Image("app.png"));
						primaryStage.setScene(scene);
						primaryStage.show();
						SingletonAppData.getInstance().setFolderPath(
								new File(APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT+ "\\" + share.getParentMenu().getText()).toString());
						SingletonAppData.getInstance().setFolderName(share.getParentMenu().getText().toString());
					} catch (Exception e) {
						logger.error("Exception occured", e);
					}
				}
			});
			edit.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					singleton.setRecievedSnapName(edit.getParentMenu().getText().toString());
					editRecievedWorkStationPressed();;
				}
			});
			close.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent args0){
					String snapName =close.getParentMenu().getText().toString();
					//File CloseApplicationList = new File(APP_DATA_LOCATION + "\\"+RECEIVED_SNAPSHOT+ "\\" + snapName+"\\"+TO_BE_CLOSED_LIST);
					File CloseFileList = new File(APP_DATA_LOCATION + "\\"+RECEIVED_SNAPSHOT+ "\\" + snapName+"\\"+FILES_TO_BE_CLOSED_LIST);
				//	if(CloseApplicationList.exists() || CloseFileList.exists()){
					if(CloseFileList.exists()){
					singleton.setSnapToBeClosed(RECEIVED_SNAPSHOT+ "\\" +snapName);
					CloseApplicationService service=	new CloseApplicationService(); 
					service.close(APP_DATA_LOCATION + "\\"+RECEIVED_SNAPSHOT+ "\\" + snapName);
					}
				}
			});
			
			schedule.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent args0){
					
					SingletonAppData.getInstance().setScheduleSnap(APP_DATA_LOCATION + "\\"+RECEIVED_SNAPSHOT+ "\\" + schedule.getParentMenu().getText());
					if(null != SingletonAppData.getInstance().getScheduleSnap()){
					try {
						Stage primaryStage = new Stage();
						Parent root = FXMLLoader.load(getClass().getResource("/com/snap/ui/fxml/DateTimePicker.fxml"));
						Scene scene = new Scene(root);
						primaryStage.getIcons().add(new Image("app.png"));
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setTitle(SCHEDULE_WINDOW_TITLE + schedule.getParentMenu().getText());
						primaryStage.setScene(scene);
						primaryStage.show();
						
					} catch (IOException e) {
						logger.error("Exception"+e);
					}
				}}
			});
			
		}
		}
		recievedMenuButton.getItems().addAll(menus);
	
	}
	
	public MainController() throws IOException {
		super();
		logger.debug("inside MainController() ");
	//	enabledPressed();
		setSnapShotElements();
		setRecievedSnapShotElements();
	}

	private void setSnapShotElements() throws IOException {
		List<WorkStationTable> insert = new ArrayList<WorkStationTable>();
		//String splitBy = ",";
		File file = new File(APP_DATA_LOCATION + "\\" + SNAPSHOT_WINDOW_LIST);
		if (file.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				insert.add(new WorkStationTable(line));
			}
			br.close();
			singleton.setInsertIntoTables(insert);
		} else {
			logger.debug("at least one snapshot must be created first to view the table data");
		}
	}
	
	private void setRecievedSnapShotElements() throws IOException {
		List<WorkStationTable> insert = new ArrayList<WorkStationTable>();
		File file = new File(APP_DATA_LOCATION+"\\"+RECEIVED_SNAPSHOT+"\\"+SNAPSHOT_WINDOW_LIST);
		if (file.exists()) {
		CSVReader reader = new CSVReader(new FileReader(file));
		String [] line;
		while ((line = reader.readNext()) != null) {
			insert.add(new WorkStationTable(line[0],null,null));
		}
		reader.close();
		singleton.setRecievedSnapShotTables(insert);;
		} else {
			logger.debug("at least one snapshot must be created first to view the table data");
		}
	}

	public void initialize(URL location, ResourceBundle resources) {
		try {
			setData();
			setRecievedSnapData();
			setSnapShotElements();
			setRecievedSnapShotElements();

		} catch (IOException e) {
			logger.error("Exception"+e);
		}
	}

	public void MouseClick() throws IOException {
		setData();
		logger.debug("menuButton entered...");
	}
	public void recievedMenuClicked() throws IOException{
		setRecievedSnapData();
	}

	private void deleteFile(File element) {
		if (element.isDirectory()) {
			for (File sub : element.listFiles()) {
				deleteFile(sub);
			}
		}
		element.delete();
	}

	private void writeCSV(List<String> elementName,String filePath) {
		logger.debug("Inside write()");
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
					logger.error("Exception"+e);
				}
				logger.debug("Writen Succesful");
	}
}
