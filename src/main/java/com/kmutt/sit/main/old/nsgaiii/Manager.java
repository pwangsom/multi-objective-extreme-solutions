package com.kmutt.sit.main.old.nsgaiii;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.kmutt.sit.mop.parameter.Global;
import com.kmutt.sit.utils.JavaHelper;

public class Manager {
	
	private Logger logger = Logger.getLogger(Manager.class);
	
	private String path;
		
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	private long startTime;
	private long endTime;
	private String startTimeSt;
	private String endTimeSt;
	
	public Manager(String path) {
		this.path = path;
		setGlobalParameter();
	}
	
	public void run() {
		logger.info("Working Path: " + this.path);
		logger.info("Input Path  : " + Global.INPUT_PATH);
		logger.info("Output Path : " + Global.OUTPUT_PATH);
		
		accessInput();
	}
	
	private void accessInput() {
		
		try {
			Files.list(Paths.get(Global.INPUT_PATH)).forEach(file -> {
				
				String fileName = file.getFileName().toString();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String dateTime = dateFormat.format(new Date());
				
				fileName = setLogPropertyEachInputFile(fileName, dateTime);
				
				processInputFile(fileName, dateTime);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
	}
	
	private void processInputFile(String fileName, String dateTime) {
		
		String fullFileName = JavaHelper.appendPathName(Global.INPUT_PATH, fileName);
		
		logger.info("Process Input Starting...");
		logger.info("Input File Name: " + fileName);
		logger.info("Input Full Name: " + fullFileName);
		logger.info("Log File Name  : " + fileName + "_" + dateTime + ".log");
		
		recordExecutionTime(true);
		
		setNsgaIIIOuptputFile(fileName);
		executeNsgaIII(fullFileName);
		
		recordExecutionTime(false);
		
		logger.info("Process Input Finished...");
		
	}
	
	private void executeNsgaIII(String fullPathFileName) {
		
		Executor executor = new Executor(fullPathFileName);
		executor.executeNsgaIII();
		
	}
	
	private void setGlobalParameter() {
		Global.WORKING_SPACE = this.path;
		Global.INPUT_PATH = JavaHelper.appendPathName(Global.WORKING_SPACE, "input");
		Global.OUTPUT_PATH = JavaHelper.appendPathName(Global.WORKING_SPACE, "output");
		Global.REFERENCE_PARETO_FRONT = JavaHelper.appendPathName(Global.WORKING_SPACE, Global.REFERENCE_PARETO_FRONT);
	}
	
	private String setLogPropertyEachInputFile(String fileName, String dateTime) {
		
		System.setProperty("log4j.current.date.time", dateTime);
		System.setProperty("log4j.filename", fileName);

		Properties props = new Properties();
		try {
			InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			logger.error("Error: Cannot load configuration file!!");
		}

		props.setProperty("log4j.filename", fileName);
		System.setProperty("log4j.current.date.time", dateTime);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);

		return fileName;
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
	
	private void setNsgaIIIOuptputFile(String fileName) {
		fileName = fileName.split("\\.")[0];
		
		Global.PARETO_OBJECTIVE_FILE = JavaHelper.appendPathName(Global.OUTPUT_PATH, fileName + "_" + Global.RUN_NUMBER + "_it"
										+ Global.MAX_ITERATION + "_" + Global.CLUSTER_TYPE + "_" + Global.THIRD_OBJECTIVE + "_"
										+ Global.SUPER_AGENT + "_pareto_objective.out");
		Global.PARETO_VARIABLE_FILE = JavaHelper.appendPathName(Global.OUTPUT_PATH, fileName + "_" + Global.RUN_NUMBER + "_it"
										+ Global.MAX_ITERATION + "_" + Global.CLUSTER_TYPE + "_" + Global.THIRD_OBJECTIVE + "_"
										+ Global.SUPER_AGENT + "_pareto_variable.out");
		Global.PARETO_QUALITY_FILE = JavaHelper.appendPathName(Global.OUTPUT_PATH, fileName + "_" + Global.RUN_NUMBER + "_it"
										+ Global.MAX_ITERATION + "_" + Global.CLUSTER_TYPE + "_" + Global.THIRD_OBJECTIVE + "_"
										+ Global.SUPER_AGENT + "_pareto_quality.out");		

		Global.POPULATION_OBJECTIVE_FILE = JavaHelper.appendPathName(Global.OUTPUT_PATH, fileName + "_" + Global.RUN_NUMBER + "_it"
										+ Global.MAX_ITERATION + "_" + Global.CLUSTER_TYPE  + "_" + Global.THIRD_OBJECTIVE + "_"
										+ Global.SUPER_AGENT + "_population_objective.out");
		Global.POPULATION_VARIABLE_FILE = JavaHelper.appendPathName(Global.OUTPUT_PATH, fileName + "_" + Global.RUN_NUMBER + "_it"
										+ Global.MAX_ITERATION + "_" + Global.CLUSTER_TYPE + "_" + Global.THIRD_OBJECTIVE + "_"
										+ Global.SUPER_AGENT + "_population_variable.out");
		Global.POPULATION_QUALITY_FILE = JavaHelper.appendPathName(Global.OUTPUT_PATH, fileName + "_" + Global.RUN_NUMBER + "_it"
										+ Global.MAX_ITERATION + "_" + Global.CLUSTER_TYPE + "_" + Global.THIRD_OBJECTIVE + "_"
										+ Global.SUPER_AGENT + "_population_quality.out");

		logger.debug("");
		logger.info("Pareto Objective: " + Global.PARETO_OBJECTIVE_FILE);
		logger.info("Pareto Variable : " + Global.PARETO_VARIABLE_FILE);
		logger.info("Pareto Quality  : " + Global.PARETO_QUALITY_FILE);
		logger.debug("");
		logger.info("Population Objective: " + Global.POPULATION_OBJECTIVE_FILE);
		logger.info("Population Variable : " + Global.POPULATION_VARIABLE_FILE);
		logger.info("Population Quality  : " + Global.POPULATION_QUALITY_FILE);
	}

}
