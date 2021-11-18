package com.kmutt.sit.clustering.ratio;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.kmutt.sit.mop.parameter.Global;
import com.kmutt.sit.utils.JavaHelper;
import com.kmutt.sit.workflow.ClusterType;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class);
	private static String dateTime;

	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		dateTime = dateFormat.format(new Date());
		System.setProperty("log4j.current.date.time", dateTime);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length > 0) {
			
			Main main = new Main();
			main.setPathProperty(args[0]);
			main.setMaxIterationNsgaIII(args[1]);
			main.setClusterType(args[2]);
			main.setThirdObjectiveNsgaIII(args[3]);
			if(!JavaHelper.isNull(args[4])) main.setSuperAgentNsgaIII(args[4]);
			if(!JavaHelper.isNull(args[5])) main.setRunNumber(args[5]);
			main.run(args[0]);
			
		} else {
			logger.info("Please specify the working directory, max iterations, cluster type, third objective, super agent mode, and no of run!!!");			
		}
		
	}

	public Main() {

	}

	public void run(String path) {
		
		logger.info("DA-DAG Starting...");
		
		Manager manager = new Manager(path);
		manager.run();
		
		logger.info("DA-DAG Finished...");
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
	
	private void setThirdObjectiveNsgaIII(String objective) {
		
		switch (objective.toUpperCase()) {
		case "COMMU":
			Global.THIRD_OBJECTIVE = "COMMU";
			break;
		case "VM":
			Global.THIRD_OBJECTIVE = "VM";
			break;
		case "IDLE":
			Global.THIRD_OBJECTIVE = "IDLE";
			break;			
		default:
			Global.THIRD_OBJECTIVE = "VM";
			break;
		}
	}
	
	private void setMaxIterationNsgaIII(String iterations) {		
		Integer it = Integer.valueOf(iterations);
		Global.MAX_ITERATION = it;
		
	}
	
	private void setClusterType(String clusterType) {

		switch (clusterType.toUpperCase()) {
		case "P2P":
			Global.CLUSTER_TYPE = ClusterType.P2P;
			break;
			
		case "LEVEL":
			Global.CLUSTER_TYPE = ClusterType.LEVEL;
			break;

		case "HORI":
			Global.CLUSTER_TYPE = ClusterType.HORIZONTAL;
			break;

		case "BOT":
			Global.CLUSTER_TYPE = ClusterType.BoT;
			break;
			
		case "NONE":
			Global.CLUSTER_TYPE = ClusterType.NONE;
			break;
			
		default:
			break;
		}
	}
	
	private void setSuperAgentNsgaIII(String agent) {
		switch (agent.toUpperCase()) {
		case "TRUE":
			Global.SUPER_AGENT = true;
			break;
		case "FALSE":
			Global.SUPER_AGENT = false;
			break;	
		default:
			Global.SUPER_AGENT = false;
			break;
		}
	}
	
	private void setRunNumber(String run) {
		Global.RUN_NUMBER = run;
	}
}
