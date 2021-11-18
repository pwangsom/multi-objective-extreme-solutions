package com.kmutt.sit.main.old;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class DaDagMain {

	private static Logger logger = Logger.getLogger(DaDagMain.class);
	private static String dateTime;
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	private long startTime;
	private long endTime;
	private String startTimeSt;
	private String endTimeSt;

	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		dateTime = dateFormat.format(new Date());
		System.setProperty("log4j.current.date.time", dateTime);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaDagMain main = new DaDagMain();
		main.run();
	}

	public DaDagMain() {

	}

	public void run() {

		logger.info("DA-DAG Starting...");
		
		recordExecutionTime(true);
		
		
		List<String> folderList = new ArrayList<String>();
		
		folderList.add("D:\\Users\\Peerasak\\git\\daDAG\\da-dag\\run1\\input");
		folderList.add("D:\\Users\\Peerasak\\git\\daDAG\\da-dag\\run2\\input");
		folderList.add("D:\\Users\\Peerasak\\git\\daDAG\\da-dag\\run3\\input");
		
		List<String> outputList = new ArrayList<String>();
		
		for(String folder : folderList) {			
			try {
				Files.list(Paths.get(folder)).forEach(path -> {

					outputList.add(processInputFile(path.toFile()));
					
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
			
		}
		
		String output = "\n";
		
		for(String out : outputList) {
			output += out + "\n";
		}
		
		logger.info(output);

		recordExecutionTime(false);
		
		logger.info("DA-DAG Finished...");
	}
	
	
	private String processInputFile(File fileName) {		
		
		DaDagManager manager = new DaDagManager(fileName);
		return manager.analyseDaxGraph();
		
		
	}
	
	private void recordExecutionTime(boolean mode) {
		if(mode) {
			startTime = System.currentTimeMillis();
			startTimeSt = dateTimeFormat.format(startTime);
			logger.info("Start Time: " + startTimeSt);
		} else {
			endTime = System.currentTimeMillis();
			endTimeSt = dateTimeFormat.format(endTime);
			logger.info("Start Time: " + startTimeSt);
			logger.info("End Time: " + endTimeSt);
			logger.info("Execution Time: " + (endTime - startTime));
		}
	}
}
