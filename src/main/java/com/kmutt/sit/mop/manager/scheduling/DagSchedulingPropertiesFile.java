package com.kmutt.sit.mop.manager.scheduling;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import com.kmutt.sit.utils.JavaHelper;

public class DagSchedulingPropertiesFile {

	private static final String LOG_DATE_TIME;
	private static String programKey;
	private static String programName;
	
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
		
		if(args.length == 3) {							
			programKey = args[1].trim() + "_" + Integer.valueOf(args[2]) + "_" + programKey;
			
			loadFileProperty(args[0].trim(), args[1].trim(), Integer.valueOf(args[2]));			
		} else if(args.length == 1 && args[0].trim().equalsIgnoreCase("test")) {
			testAccessNeccessaryFile();
		} else {
			System.out.println("Please specify properties file,and workflow name and size!");			
		}
	}
	
	private static void testAccessNeccessaryFile() {
		try {

			InputStream in = new FileInputStream("MOEAD_Weights" + "/" + "W3D_100.dat");
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			
			String aux = br.readLine();
			
			while (aux != null) {				
				System.out.println(aux);
				
				aux = br.readLine();
			}
			
			br.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	private static void loadFileProperty(String fullPathFileName, String workflowName, int workflowSize) {
		
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(fullPathFileName);

			// load a properties file
			prop.load(input);
			
			// get the property value and print it out
			programName = prop.getProperty("program.name");
			
			String workingPath = prop.getProperty("working.path");
			String thirdObjective = prop.getProperty("third.objective");
			String maxRun = prop.getProperty("max.run");
			String maxIteration = prop.getProperty("max.iteration");
			String interval = prop.getProperty("interval");			
			String[] clusterList = prop.getProperty("list.cluster").split(",");
			String[] algorithmList = prop.getProperty("list.algorithm").split(",");
			String vmModel = prop.getProperty("vm.provider.model");
			String vmBootTime = prop.getProperty("vm.boot.time");
			String vmDegrade = prop.getProperty("performance.degrade.vm");
			String networkDegrade = prop.getProperty("performance.degrade.network");
			boolean skipUnP2p = prop.getProperty("skip.uncomplied.p2p.workflow").trim().equalsIgnoreCase("false") ? false : true;
			
			String filesizeScale = prop.getProperty("magnitude.filesize.scale");
			String runtimeScale = prop.getProperty("magnitude.runtime.scale");
			
			if(JavaHelper.isNull(filesizeScale)) filesizeScale = "1.00";
			if(JavaHelper.isNull(runtimeScale)) runtimeScale = "1.00";
			
			setLogProperty(workingPath);
			
			DagSchedulingPropertiesFile main = new DagSchedulingPropertiesFile();
			main.opitimize(workingPath.trim(), workflowName, workflowSize, thirdObjective.trim(),
						   Integer.valueOf(maxRun), Integer.valueOf(maxIteration), Integer.valueOf(interval),
						   clusterList, algorithmList, vmModel, Double.valueOf(vmBootTime), Double.valueOf(vmDegrade),
						   Double.valueOf(networkDegrade), skipUnP2p, Double.valueOf(filesizeScale), Double.valueOf(runtimeScale));

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
	
	public void opitimize(String workingPath, String workflowName, int workflowSize, String thirdObjName, int maxRun, int maxIt,
						  int interval, String[] clusterList, String[] algorithms, String vmModel, Double vmBootTime,
						  Double vmDegrade, Double networkDegrade, boolean skipUnP2p, Double filesizeScale, Double runtimeScale) {
		System.out.println(programName + " Starting...");
		
		DagSchedulingManager manager = new DagSchedulingManager()
											.setProgramKey(programKey)
											.setWorkingPath(workingPath)
											.setWorkflowNameSize(workflowName, workflowSize)
											.setThirdObjective(thirdObjName)
											.setMaxRun(maxRun)
											.setMaxIteration(maxIt)
											.setIntervalIteration(interval)
											.setClusterList(clusterList)
											.setAlgorithmList(algorithms)
											.setVmModel(vmModel, vmBootTime, vmDegrade, networkDegrade)
											.setSkipUncompliedP2pWorkflow(skipUnP2p)
											.setMagnitudeParameters(filesizeScale, runtimeScale);
		manager.manage();

		System.out.println(programName + " Finised...");
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
