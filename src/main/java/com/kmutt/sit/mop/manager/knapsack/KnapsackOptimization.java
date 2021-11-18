package com.kmutt.sit.mop.manager.knapsack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import com.kmutt.sit.mop.manager.scheduling.DagSchedulingPropertiesFile;
import com.kmutt.sit.utils.JavaHelper;

public class KnapsackOptimization {
	
	private static final String LOG_DATE_TIME;
	private static String programKey;
	private static String programName;
	
	static {
		
		Date dateTime = new Date();
		
		programKey = new SimpleDateFormat("yyMMddHHmmss").format(dateTime);
		LOG_DATE_TIME = new SimpleDateFormat("yyyyMMdd_HHmmss").format(dateTime);
		
		System.setProperty("log4j.log4j.filename", "mop-knapsack");
		System.setProperty("log4j.current.date.time", LOG_DATE_TIME);
		System.setProperty("log4j.manager.current.date.time", LOG_DATE_TIME);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length == 1) {				
			loadFileProperty(args[0].trim());
		} else {
			System.out.println("Please specify the property file!");			
		}
	}
	
	public void optimize(String workingPath, int maxRun, int maxIt, String[] algorithms, String[] problems) {
		System.out.println(programName + " Starting...");
		
		KnapsackManager manager = new KnapsackManager();
		manager.setConfigProgramKey(programKey);
		manager.setConfigWorkingPath(workingPath);
		manager.setConfigMaxRun(maxRun);
		manager.setConfigMaxIteration(maxIt);
		manager.setConfigAlgorithmList(algorithms);
		manager.setConfigProblemList(problems);
		
		manager.manage();

		System.out.println(programName + " Finised...");
	}
	
	private static void loadFileProperty(String fullPathFileName) {
		
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(fullPathFileName);

			// load a properties file
			prop.load(input);
			
			// get the property value and print it out
			programName = prop.getProperty("program.name");
			
			String workingPath = prop.getProperty("working.path");
			String maxRun = prop.getProperty("max.run");
			String maxIteration = prop.getProperty("max.iteration");
			String[] algorithmList = prop.getProperty("list.algorithm").split(",");
			String[] problemList = prop.getProperty("list.problem").split(",");
			
			setLogProperty(workingPath);
			
			KnapsackOptimization optimizer = new KnapsackOptimization();
			optimizer.optimize(workingPath, Integer.valueOf(maxRun), Integer.valueOf(maxIteration), algorithmList, problemList);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void setLogProperty(String path) {
		path = JavaHelper.appendPathName(path, "log");
		
		System.out.println("Log path: " + path);
		System.setProperty("log4j.log", path);
		System.setProperty("log4j.logkey", programKey);
		System.setProperty("log4j.manager.current.date.time", LOG_DATE_TIME);
		System.setProperty("log4j.manager.filename", programName);

		Properties props = new Properties();
		
		try {
			InputStream configStream = DagSchedulingPropertiesFile.class.getClassLoader().getResourceAsStream("log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			System.out.println("Error: Cannot load configuration file!!");
		}

		props.setProperty("log4j.log", path);
		props.setProperty("log4j.manager.current.date.time", LOG_DATE_TIME);
		props.setProperty("log4j.manager.filename", programName);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);		
	}
}
