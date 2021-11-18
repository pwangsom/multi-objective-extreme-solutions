package com.kmutt.sit.coverageratio;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.kmutt.sit.main.old.nsgaiii.Main;
import com.kmutt.sit.utils.JavaHelper;

public class CoverageMain {

	private static Logger logger = Logger.getLogger(Main.class);
	private static String dateTime;

	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		dateTime = dateFormat.format(new Date());
		System.setProperty("log4j.current.date.time", dateTime);
		System.setProperty("log4j.filename", "a-coverage-da-dag");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length > 0) {
			
			CoverageMain main = new CoverageMain();
			main.setPathProperty(args[0]);
			main.run(args[0]);
			
		} else {
			logger.info("Please specify the working director, max iterations and cluster type!!!");			
		}
		
	}

	public CoverageMain() {

	}

	public void run(String path) {
		
		logger.info("Coverage Index Starting...");
		
		CoverageManager manager = new CoverageManager();
		manager.accessFiles(path);
		manager.assessCoverageIndex();
		manager.printCoverageIndex();
		
		logger.info("Coverage Index Finished...");
	}	
	
	private void setPathProperty(String path) {
		path = JavaHelper.appendPathName(path, "log");
		
		System.out.println("Log path: " + path);
		System.setProperty("log4j.log", path);

		Properties props = new Properties();
		try {
			InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			System.out.println("Error: Cannot load configuration file!!");
		}

		props.setProperty("log4j.log", path);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);
		
	}
	
}
