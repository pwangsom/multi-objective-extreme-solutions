package com.kmutt.sit.main.old;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

public class ArgumentMain {
	
	private static Logger logger = Logger.getLogger(ArgumentMain.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		logger.info("ArgumentMain");
		
		String workingPath = args[0];
		

		logger.info("Working Directory: " + workingPath);
		
		try {
			Files.list(Paths.get(workingPath)).forEach(f -> logger.info(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
	}
	

}
