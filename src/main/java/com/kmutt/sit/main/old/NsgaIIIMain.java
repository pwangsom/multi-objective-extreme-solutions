package com.kmutt.sit.main.old;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.kmutt.sit.utils.JavaHelper;

public class NsgaIIIMain {

	private static Logger logger = Logger.getLogger(NsgaIIIMain.class);
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
		
		if(args.length > 0) {
			String path = args[0];
			
			NsgaIIIMain main = new NsgaIIIMain();
			main.setPathProperty(path);
			main.run();
			
		}
	}

	public NsgaIIIMain() {

	}

	public void run() {

		String fileName = "src/main/resources/input/pegasus/dataset1/Epigenomics_24.xml";
		String file = setFilenameProperty(fileName);

		logger.info("DA-DAG Starting...");
		logger.info("Input file name: " + file);
		logger.info("Log file name: " + file + "_" + dateTime + ".log");
		
		recordExecutionTime(true);
		
/*		DaDagManager manager = new DaDagManager(fileName);
		manager.extractADagGraphFromFile();
		manager.processADagGraph();*/

		recordExecutionTime(false);
		
		logger.info("DA-DAG Finished...");
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

	private String setFilenameProperty(String fileName) {
		String[] list = fileName.split("/");
		String lastOne = list[list.length - 1];
		System.setProperty("log4j.filename", lastOne);

		Properties props = new Properties();
		try {
			InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			logger.error("Error: Cannot load configuration file!!");
		}

		props.setProperty("log4j.filename", lastOne);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);

		return lastOne;
	}
	
	public void setPathProperty(String path) {		
		path = JavaHelper.appendPathName(path, "log");
		
		logger.info("Log path: " + path);
		System.setProperty("log4j.log", path);

		Properties props = new Properties();
		try {
			InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			logger.error("Error: Cannot load configuration file!!");
		}

		props.setProperty("log4j.log", path);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);
		
	}
}
