package com.snap.service;

import static com.snap.common.util.SnapConstants.APP_DATA_LOCATION;
import static com.snap.common.util.SnapConstants.APP_DATA_VBS;
import static com.snap.common.util.SnapConstants.CLOSE_DONE;
import static com.snap.common.util.SnapConstants.CLOSE_EXE_FILE_VBS;
import static com.snap.common.util.SnapConstants.FILES_TO_BE_CLOSED_LIST;
import static com.snap.common.util.SnapConstants.SLEEP_TIME_FOR_POPUP;
import static com.snap.common.util.SnapConstants.TO_BE_CLOSED_LIST;
import static com.snap.common.util.SnapConstants.WINDOW_TITLE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import com.snap.common.util.ReadFileUtility;
import com.snap.common.util.WriteFileUtility;
import com.snap.processor.CommandLineProcessor;
import com.snap.ui.controller.CloseLaunchController;

public class CloseApplicationService {
	private static final Logger logger = Logger.getLogger(CloseApplicationService.class.getName());
	private ReadFileUtility fileUtility = new ReadFileUtility();
	
	public void close(String path){
		exeFileExtract(new File(path).getName());
		CommandLineProcessor lineProcessor = new CommandLineProcessor();
		
		File appToBeClosed = new File(path +"\\"+TO_BE_CLOSED_LIST);
		if(appToBeClosed.exists() && appToBeClosed.length()>0){
			final String[] appListCmd = { "cmd", "/c", APP_DATA_VBS + "\\" + CLOSE_EXE_FILE_VBS+" "+"\""+appToBeClosed.getPath()+"\"" };
			lineProcessor.invokeCMD(appListCmd);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		File fileToBeClosed = new File(path +"\\"+FILES_TO_BE_CLOSED_LIST);
		if(fileToBeClosed.exists() && fileToBeClosed.length()>0){
			//List<String> list=check(fileToBeClosed);
			WindowUtil.closeApplication(fileUtility.readFile(fileToBeClosed.getAbsolutePath()));
		}
		
		File CloseApplicationList = new File(path +"\\"+TO_BE_CLOSED_LIST);
		CloseApplicationList.delete();
		fileToBeClosed.delete();
		
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/com/snap/ui/fxml/CloseLaunch.fxml").openStream());
			CloseLaunchController closeLaunch = (CloseLaunchController) loader.getController();
			closeLaunch.setName(CLOSE_DONE);
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("/com/snap/ui/controller/CloseLanchPopup.css").toExternalForm());
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.getIcons().add(new Image("app.png"));
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.setScene(scene);
			primaryStage.setAlwaysOnTop(true);
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
			 try {
		            Thread.sleep(SLEEP_TIME_FOR_POPUP);
		         } catch (Exception e) {
		            logger.error("Exception"+e);
		         }
			 primaryStage.close();
			 logger.debug("snap closed...");
		} catch (Exception e) {
		logger.error("Exception occured", e);
		}
	}

	public static void main(String[] args) {
		CloseApplicationService service = new CloseApplicationService();
		//service.check(new File("C://Scripts//Snap-Session//test12//FilesTobeClosed.txt"));
	}
	
	private void exeFileExtract(String snapShotName){

		Set<String> appList = new HashSet<String>();
		String fileName = null;
		try {
			File file = new File(APP_DATA_LOCATION+"/"+snapShotName+"/"+TO_BE_CLOSED_LIST);
			if(file.exists()){
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((fileName = bufferedReader.readLine()) != null) {
				
					 String patternString = ".*exe.*";
				      Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
				        Matcher matcher = pattern.matcher(fileName);
				        if(matcher.matches()){
						appList.add(fileName.replace("\\", "\\\\"));
					}
			}
			bufferedReader.close();
			WriteFileUtility fileUtility = new WriteFileUtility();
			fileUtility.writeFile(file.getPath(), appList,false);
			logger.debug("Exe's to be killed.."+appList);
			System.out.println(appList);
			}
			}
		catch(Exception e){
			 logger.error("Exception"+e);
		}
	
		
	}
	
	/*public List<String> check(File file){
		List<String> finalList=null;
		try {
			List<String> fileContent =fileUtility.readFile(file.getAbsolutePath());
		     List<String> list=	WindowUtil.getWindowTitle();
		     finalList= new ArrayList<String>();
		     
		     for (String string : fileContent) {
		    	 if(list.contains(string)){
		    		 finalList.add(string); 
		    	 }
			}
		     
		} catch (IOException e) {
			logger.error("Exception occured.."+e);
			return null;
		}
		
		return finalList;
		
	}*/
	
	public static void run(List<String> newList) {
		if (newList != null) {
			for (String w : newList) {
				String cmds[] = { "cmd", "/c", "taskkill /im "+ w };
				try {
					Runtime.getRuntime().exec(cmds);
						Thread.sleep(1000);
				} catch (Exception e1) {
				}
			}
		} else {
		}
	}
	}

