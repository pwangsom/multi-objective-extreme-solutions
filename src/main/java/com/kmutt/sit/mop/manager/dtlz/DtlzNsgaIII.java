package com.kmutt.sit.mop.manager.dtlz;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import com.kmutt.sit.utils.JavaHelper;

public class DtlzNsgaIII {
	
	private static final String PROGRAM_KEY;
	private static final String LOG_DATE_TIME;
	private static final String PROGRAM_NAME = "DTLZ";
	
	static {
		
		Date dateTime = new Date();
		
		PROGRAM_KEY = new SimpleDateFormat("yyMMddHHmmss").format(dateTime);
		LOG_DATE_TIME = new SimpleDateFormat("yyyyMMdd_HHmmss").format(dateTime);
		
		System.setProperty("log4j.logkey", PROGRAM_KEY);
		System.setProperty("log4j.log4j.filename", "DLTZ-nsga-iii");
		System.setProperty("log4j.current.date.time", LOG_DATE_TIME);
		System.setProperty("log4j.manager.current.date.time", LOG_DATE_TIME);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length == 6) {				
			
			setLogProperty(args[0]);
			
			DtlzNsgaIII main = new DtlzNsgaIII();
			main.run(args[0], args[1].trim(), args[2], Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5]));
			
		} else {
			System.out.println("Please specify working directory, problem class, reference file, maximun run, and maximum iteration");			
		}
	}
	
	public void run(String workingPath, String problemName, String referenceFile, int maxRun, int maxIt, int interval) {
		System.out.println("DTLZ_NSGA_III Starting...");
		
		DtlzNsgaIIIManager manager = new DtlzNsgaIIIManager()
											.setProgramKey(PROGRAM_KEY)
											.setWorkingPath(workingPath)
											.setProblemName(problemName)
											.setReferenceFile(referenceFile)
											.setMaxRun(maxRun)
											.setIteration(maxIt, interval);
		manager.run();
		
		System.out.println("DTLZ_NSGA_III Finished...");
	}
	
	private static void setLogProperty(String path) {
		path = JavaHelper.appendPathName(path, "log");
		
		System.out.println("Log path: " + path);
		System.setProperty("log4j.log", path);
		System.setProperty("log4j.manager.current.date.time", LOG_DATE_TIME);
		System.setProperty("log4j.manager.filename", PROGRAM_NAME);

		Properties props = new Properties();
		
		try {
			InputStream configStream = DtlzNsgaIII.class.getClassLoader().getResourceAsStream("log4j.properties");
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
