package com.kmutt.sit.mop.manager.scheduling;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import com.kmutt.sit.utils.JavaHelper;

public class DagScheduling {
	
	private static String programKey;
	private static final String LOG_DATE_TIME;
	private static final String PROGRAM_NAME = "DAG_SCHEDULING";
	
	static {
		
		Date dateTime = new Date();
		
		programKey = new SimpleDateFormat("yyMMddHHmmss").format(dateTime);
		LOG_DATE_TIME = new SimpleDateFormat("yyyyMMdd_HHmmss").format(dateTime);
		
		System.setProperty("log4j.log4j.filename", "dag-scheduling");
		System.setProperty("log4j.current.date.time", LOG_DATE_TIME);
		System.setProperty("log4j.manager.current.date.time", LOG_DATE_TIME);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length == 7) {				
			
			programKey = args[1].trim() + "_" + Integer.valueOf(args[2]) + "_" + programKey;
			
			setLogProperty(args[0]);
			
			DagScheduling main = new DagScheduling();
			main.opitimize(args[0], args[1].trim(), Integer.valueOf(args[2]),  args[3].trim(), Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]));
			
		} else {
			System.out.println("Please specify working directory, workflow name, workflow size, third objective name, maximum run, maximum iteration, and interval iteration");			
		}
	}
	
	public void opitimize(String workingPath, String workflowName, int workflowSize, String thirdObjName, int maxRun, int maxIt, int interval) {
		System.out.println(PROGRAM_NAME + " Starting...");
		
		DagSchedulingManager manager = new DagSchedulingManager()
											.setProgramKey(programKey)
											.setWorkingPath(workingPath)
											.setWorkflowNameSize(workflowName, workflowSize)
											.setThirdObjective(thirdObjName)
											.setMaxRun(maxRun)
											.setMaxIteration(maxIt)
											.setIntervalIteration(interval);
		manager.manage();

		System.out.println(PROGRAM_NAME + " Finised...");
	}
	
	private static void setLogProperty(String path) {
		path = JavaHelper.appendPathName(path, "log");
		
		System.out.println("Log path: " + path);
		System.setProperty("log4j.log", path);
		System.setProperty("log4j.logkey", programKey);
		System.setProperty("log4j.manager.current.date.time", LOG_DATE_TIME);
		System.setProperty("log4j.manager.filename", PROGRAM_NAME);

		Properties props = new Properties();
		
		try {
			InputStream configStream = DagScheduling.class.getClassLoader().getResourceAsStream("log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			System.out.println("Error: Cannot load configuration file!!");
		}

		props.setProperty("log4j.log", path);
		props.setProperty("log4j.manager.current.date.time", LOG_DATE_TIME);
		props.setProperty("log4j.manager.filename", PROGRAM_NAME);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);		
	}

}
