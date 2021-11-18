package com.kmutt.sit.mop.manager.dtlz;

import org.apache.log4j.Logger;

import com.kmutt.sit.utils.JavaHelper;

public class DtlzNsgaIIIManager {

	private Logger logger = Logger.getLogger(DtlzNsgaIIIManager.class);
	
	private DtlzNsgaIIIConfiguration config;
	
	public DtlzNsgaIIIManager() {
		this.config = new DtlzNsgaIIIConfiguration();
	}
	
	public void run() {

		logger.debug("");
		logger.info("DTLZ_NSGA_III Starting...");
		
		displayConfigurations();
		
		DtlzNsgaIIIContinueableExperiment experimentor = new DtlzNsgaIIIContinueableExperiment().setConfigurations(this.config);
		experimentor.runExperiment();
		experimentor.findParetoOfEachInterval();
		experimentor.writeResult();
		
		logger.debug("");
		logger.info("DTLZ_NSGA_III Finished...");
		logger.debug("");
	}
	
	public DtlzNsgaIIIManager setProgramKey(String programKey) {
		this.config.setProgramKey(programKey);
		
		return this;
	}
	
	public DtlzNsgaIIIManager setWorkingPath(String path) {
		this.config.setWorkingPath(path);
		this.config.setInputPath(JavaHelper.appendPathName(path, "input"));
		this.config.setOutputPath(JavaHelper.appendPathName(path, "output"));
		
		return this;
	}
	
	public DtlzNsgaIIIManager setProblemName(String problemName) {
		this.config.setProblemClass(problemName);
		
		String[] split = problemName.split("\\.");
		
		this.config.setProblemName(split[split.length - 1]);
		
		return this;
	}
	
	public DtlzNsgaIIIManager setReferenceFile(String referenceFile) {
		this.config.setReferenceParetoFront(referenceFile);
		
		return this;
	}

	public DtlzNsgaIIIManager setMaxRun(int maxRun) {
		this.config.setMaxRun(maxRun);
		
		return this;
	}
	
	public DtlzNsgaIIIManager setIteration(int maxIteration, int intervalIteration) {
		this.config.setMaxIteration(maxIteration);
		this.config.setIntervalIteration(intervalIteration);
		
		return this;
	}
	
	private void displayConfigurations() {
		
		logger.debug("");
		logger.info("=============Configuration=============");
		logger.debug("");

		logger.info("Program Key   : " + this.config.getProgramKey());
		logger.info("Working Path  : " + this.config.getWorkingPath());
		logger.info("Input Path    : " + this.config.getInputPath());
		logger.info("Output Path   : " + JavaHelper.appendPathName(this.config.getOutputPath(), this.config.getProgramKey()));
		logger.info("Problem Class : " + this.config.getProblemClass());
		logger.info("Problem Name  : " + this.config.getProblemName());
		logger.info("Reference File: " + this.config.getReferenceParetoFront());
		logger.info("Max Run       : " + this.config.getMaxRun());
		logger.info("Max Iteration : " + this.config.getMaxIteration());

		logger.debug("");
		
	}
}
